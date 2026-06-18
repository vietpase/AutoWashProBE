package com.swp391.autowashpro.service;

import com.swp391.autowashpro.dto.AvailableSlotResponse;
import com.swp391.autowashpro.dto.BookingRequest;
import com.swp391.autowashpro.dto.BookingResponse;
import com.swp391.autowashpro.dto.WalkInBookingRequest;
import com.swp391.autowashpro.entity.*;
import com.swp391.autowashpro.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {

    // --- REPOSITORIES CORE ---
    private final BookingRepository bookingRepository;
    private final BookingSlotRepository bookingSlotRepository;
    private final CustomerRepository customerRepository;
    private final VehicleRepository vehicleRepository;
    private final TimeSlotRepository timeSlotRepository;
    private final WashServiceRepository washServiceRepository;
    private final PromotionRepository promotionRepository;
    private final RewardRedemptionRepository rewardRedemptionRepository;
    private final LoyaltyPointRepository loyaltyPointRepository;
    private final WashHistoryRepository washHistoryRepository;
    private final CustomerMonthlyStatsRepository customerMonthlyStatsRepository;
    private final LoyaltyTierRepository loyaltyTierRepository;

    /* =========================================================================
     * PHẦN 1: API DÀNH CHO GIAO DIỆN KHÁCH HÀNG (DISPLAY & UI)
     * ========================================================================= */

    /**
     * API tính toán động để hiển thị các nút bấm Khung Giờ lên màn hình.
     * Nút nào không đủ chuỗi ca trống hoặc bị full tải sẽ trả về isAvailable = false để UI xám nút (Disable).
     */
    @Transactional(readOnly = true)
    public List<AvailableSlotResponse> getAvailableSlotsForCustomer(LocalDate date, Integer washServiceId) {
        WashService washService = washServiceRepository.findById(washServiceId)
                .orElseThrow(() -> new RuntimeException("Wash service not found."));

        // Tính toán số lượng slot cần thiết (Ví dụ: 180 phút / 60 phút = 3 slots liên tiếp)
        int slotsNeeded = (int) Math.ceil((double) washService.getDurationMinutes() / 60);

        // Lấy tất cả các ca đang hoạt động và sắp xếp theo thứ tự thời gian bắt đầu tăng dần
        List<TimeSlot> allActiveSlots = timeSlotRepository.findByIsActiveTrue();
        allActiveSlots.sort(Comparator.comparing(TimeSlot::getStartTime));

        List<AvailableSlotResponse> responseList = new ArrayList<>();

        for (int i = 0; i < allActiveSlots.size(); i++) {
            TimeSlot startSlot = allActiveSlots.get(i);

            AvailableSlotResponse dto = new AvailableSlotResponse();
            dto.setSlotId(startSlot.getSlotId());
            dto.setSlotName(startSlot.getSlotName());
            dto.setStartTime(startSlot.getStartTime());
            dto.setEndTime(startSlot.getEndTime());
            dto.setMaxCapacity(startSlot.getMaxCapacity());

            // TH 1: Nếu từ ca này trở đi không còn đủ số lượng ca tiếp theo để hoàn thành dịch vụ (lố giờ đóng cửa)
            if (i + slotsNeeded > allActiveSlots.size()) {
                dto.setAvailable(false);
                responseList.add(dto);
                continue;
            }

            boolean isChainAvailable = true;

            // TH 2: Quét qua chuỗi các mắt xích ca con tiếp theo xem có ca nào bị full chỗ không
            for (int j = 0; j < slotsNeeded; j++) {
                TimeSlot intermediateSlot = allActiveSlots.get(i + j);

                // Gọi câu lệnh COUNT từ SQL xuống bảng trung gian
                long occupiedCount = bookingSlotRepository.countOccupiedVehicles(date, intermediateSlot.getSlotId());

                if (occupiedCount >= intermediateSlot.getMaxCapacity()) {
                    isChainAvailable = false;
                    break; // Bị nghẽn ở giữa chừng -> Huỷ chuỗi ngay lập tức
                }
            }

            dto.setAvailable(isChainAvailable);
            responseList.add(dto);
        }

        return responseList;
    }


    /* =========================================================================
     * PHẦN 2: CÁC CHỨC NĂNG DÀNH CHO KHÁCH HÀNG (ONLINE CUSTOMER)
     * ========================================================================= */

    /**
     * Khách hàng đặt lịch Online từ Web (Chỉ nhận vào 1 slotId bắt đầu dạng Single-Select)
     */
    @Transactional
    public BookingResponse createBooking(BookingRequest request) {
        // 1. Kiểm tra thông tin Customer & Rank hiện tại
        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found."));
        LoyaltyTier currentTier = customer.getLoyaltyTier();

        // 2. Validate Booking Window
        long daysBetween = ChronoUnit.DAYS.between(LocalDate.now(), request.getBookingDate());
        if (daysBetween < 0 || daysBetween > currentTier.getBookingWindowDays()) {
            throw new RuntimeException("Your membership tier limits bookings within " + currentTier.getBookingWindowDays() + " days ahead.");
        }

        // 3. Kiểm tra xe (Vehicle)
        Vehicle vehicle = vehicleRepository.findById(request.getVehicleId())
                .orElseThrow(() -> new RuntimeException("Vehicle not found."));
        if (!vehicle.getCustomer().getCustomerId().equals(customer.getCustomerId())) {
            throw new RuntimeException("This vehicle does not belong to the requested customer.");
        }

        // 4. Kiểm tra Slot giờ bắt đầu khách bấm chọn
        TimeSlot startSlot = timeSlotRepository.findById(request.getSlotId())
                .orElseThrow(() -> new RuntimeException("Time Slot not found."));
        if (!startSlot.getIsActive()) {
            throw new RuntimeException("This timeslot is currently closed.");
        }

        // 5. Kiểm tra dịch vụ rửa xe
        WashService washService = washServiceRepository.findById(request.getWashServiceId())
                .orElseThrow(() -> new RuntimeException("Wash service not found."));
        if (!washService.getIsActive()) {
            throw new RuntimeException("This wash service is deactivated.");
        }

        // 6. THUẬT TOÁN ĐẾM CHỖ CHUỖI MẮT XÍCH QUA BẢNG TRUNG GIAN (Bảo vệ Backend)
        List<TimeSlot> slotsToReserve = validateAndGetChainSlots(request.getBookingDate(), startSlot, washService);

        // --- ENGINE TÍNH TIỀN ---
        BigDecimal basePrice = washService.getPrice();
        BigDecimal discountFromTier = BigDecimal.ZERO;
        BigDecimal discountFromPromo = BigDecimal.ZERO;
        BigDecimal discountFromVouchers = BigDecimal.ZERO;

        if (currentTier.getDiscountPercent() > 0) {
            BigDecimal multiplier = BigDecimal.valueOf(currentTier.getDiscountPercent()).divide(BigDecimal.valueOf(100));
            discountFromTier = basePrice.multiply(multiplier);
        }

        Promotion promotion = null;
        if (request.getPromotionId() != null) {
            promotion = promotionRepository.findById(request.getPromotionId()).orElse(null);
            if (promotion != null && promotion.getIsActive()) {
                discountFromPromo = BigDecimal.valueOf(promotion.getDiscountAmount());
            }
        }

        List<RewardRedemption> redemptionsToApply = new ArrayList<>();
        if (request.getAppliedRedemptionIds() != null && !request.getAppliedRedemptionIds().isEmpty()) {
            for (Integer redemptionId : request.getAppliedRedemptionIds()) {
                if (redemptionId == null || redemptionId == 0) {
                    continue;
                }

                RewardRedemption redemption = rewardRedemptionRepository.findById(redemptionId)
                        .orElseThrow(() -> new RuntimeException("Voucher code " + redemptionId + " not found."));

                if (!redemption.getCustomer().getCustomerId().equals(customer.getCustomerId())) {
                    throw new RuntimeException("Voucher does not belong to this customer.");
                }
                if (redemption.getBooking() != null) {
                    throw new RuntimeException("Voucher has already been used in another booking.");
                }

                discountFromVouchers = discountFromVouchers.add(redemption.getDiscountAmountAtRedemption());
                redemptionsToApply.add(redemption);
            }
        }

        BigDecimal totalDiscount = discountFromTier.add(discountFromPromo).add(discountFromVouchers);
        BigDecimal finalPrice = basePrice.subtract(totalDiscount);
        if (finalPrice.compareTo(BigDecimal.ZERO) < 0) finalPrice = BigDecimal.ZERO;

        // 7. Khởi tạo và Lưu thông tin đơn đặt lịch chính
        Booking booking = new Booking();
        booking.setBookingDate(request.getBookingDate());
        booking.setVehicle(vehicle);
        booking.setWashService(washService);
        booking.setPromotion(promotion);
        booking.setStatus("PENDING");

        // Đóng băng dữ liệu Snapshot
        booking.setBasePriceAtBooking(basePrice);
        booking.setTotalPrice(finalPrice);
        booking.setLicensePlateAtBooking(vehicle.getLicensePlate());
        booking.setTierAtBooking(currentTier);
        booking.setPriorityLevel(currentTier.getPriorityLevel());

        Booking savedBooking = bookingRepository.save(booking);

        // 8. TIẾN HÀNH GIỮ CHỖ THỰC TẾ: Đẻ dữ liệu vào bảng trung gian BookingSlot
        for (TimeSlot targetSlot : slotsToReserve) {
            BookingSlot bookingSlot = new BookingSlot();
            bookingSlot.setBooking(savedBooking);
            bookingSlot.setTimeSlot(targetSlot);
            bookingSlotRepository.save(bookingSlot);
        }

        // 9. Vô hiệu hóa Voucher hình thức
        for (RewardRedemption redemption : redemptionsToApply) {
            redemption.setBooking(savedBooking);
            rewardRedemptionRepository.save(redemption);
        }

        return new BookingResponse(savedBooking);
    }


    /* =========================================================================
     * PHẦN 3: CÁC CHỨC NĂNG DÀNH CHO NHÂN VIÊN (STAFF OPERATIONS)
     * ========================================================================= */

    /**
     * Staff Luồng 1: Xác nhận khách đã đến tiệm (Pending -> Confirmed)
     */
    @Transactional
    public BookingResponse confirmArrival(Integer bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found."));

        if (!booking.getStatus().equalsIgnoreCase("Pending")) {
            throw new RuntimeException("Only 'Pending' bookings can be confirmed.");
        }

        booking.setStatus("Confirmed");
        return new BookingResponse(bookingRepository.save(booking));
    }

    /**
     * Staff Luồng 2: Rửa xong, Thu tiền, Tích điểm & Cập nhật thống kê tháng (Confirmed -> Completed)
     */
    @Transactional
    public BookingResponse completeBookingAndPay(Integer bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found."));

        if (!booking.getStatus().equalsIgnoreCase("Confirmed")) {
            throw new RuntimeException("Booking must be 'Confirmed' to complete.");
        }

        booking.setStatus("Completed");
        Booking savedBooking = bookingRepository.save(booking);

        int totalPointsUsed = 0;
        if (savedBooking.getRewardRedemptions() != null && !savedBooking.getRewardRedemptions().isEmpty()) {
            totalPointsUsed = savedBooking.getRewardRedemptions().stream()
                    .mapToInt(RewardRedemption::getPointsUsed)
                    .sum();
        }

        BigDecimal finalPrice = savedBooking.getTotalPrice();
        int basePoints = finalPrice.divide(BigDecimal.valueOf(1000)).intValue();
        double multiplier = savedBooking.getTierAtBooking().getPointMultiplier();
        int totalPointsEarned = (int) Math.round(basePoints * multiplier);

        WashHistory washHistory = new WashHistory();
        washHistory.setWashDate(LocalDateTime.now());
        washHistory.setAmountPaid(finalPrice);
        washHistory.setPointsEarned(totalPointsEarned);
        washHistory.setPointsUsed(totalPointsUsed);
        washHistory.setBooking(savedBooking);
        WashHistory savedWashHistory = washHistoryRepository.save(washHistory);

        Customer customer = savedBooking.getVehicle().getCustomer();
        customer.setTotalSpend(customer.getTotalSpend().add(finalPrice));
        customer.setTotalVisits(customer.getTotalVisits() + 1);
        customer.setCurrentPoints(customer.getCurrentPoints() + totalPointsEarned);
        customerRepository.save(customer);

        if (totalPointsEarned > 0) {
            LoyaltyPoint pointLog = new LoyaltyPoint();
            pointLog.setCustomer(customer);
            pointLog.setPointsChange(totalPointsEarned);
            pointLog.setTransactionType("EARN_BOOKING");
            pointLog.setCreatedAt(LocalDateTime.now());
            pointLog.setExpiryDate(LocalDate.now().plusYears(1));
            pointLog.setWashHistory(savedWashHistory);
            loyaltyPointRepository.save(pointLog);
        }

        String currentYearMonth = YearMonth.now().format(DateTimeFormatter.ofPattern("yyyyMM"));
        CustomerMonthlyStats monthlyStats = customerMonthlyStatsRepository
                .findByCustomerCustomerIdAndYearMonth(customer.getCustomerId(), currentYearMonth)
                .orElseGet(() -> {
                    CustomerMonthlyStats newStats = new CustomerMonthlyStats();
                    newStats.setCustomer(customer);
                    newStats.setYearMonth(currentYearMonth);
                    newStats.setMonthlySpend(BigDecimal.ZERO);
                    newStats.setMonthlyVisits(0);
                    return newStats;
                });

        monthlyStats.setMonthlySpend(monthlyStats.getMonthlySpend().add(finalPrice));
        monthlyStats.setMonthlyVisits(monthlyStats.getMonthlyVisits() + 1);
        customerMonthlyStatsRepository.save(monthlyStats);

        return new BookingResponse(savedBooking);
    }

    /**
     * Staff Luồng 3: Đặt lịch trực tiếp tại quầy cho Khách vãng lai Walk-in
     */
    @Transactional
    public BookingResponse createWalkInBooking(WalkInBookingRequest request) {
        LocalDate today = LocalDate.now();

        TimeSlot startSlot = timeSlotRepository.findById(request.getSlotId())
                .orElseThrow(() -> new RuntimeException("Time Slot not found."));
        if (!startSlot.getIsActive()) {
            throw new RuntimeException("This timeslot is currently closed.");
        }

        WashService washService = washServiceRepository.findById(request.getWashServiceId())
                .orElseThrow(() -> new RuntimeException("Wash service not found."));
        if (!washService.getIsActive()) {
            throw new RuntimeException("This wash service is deactivated.");
        }

        // CHẶN TRÙNG LỊCH CHO LUỒNG TẠI QUẦY
        List<TimeSlot> slotsToReserve = validateAndGetChainSlots(today, startSlot, washService);

        Customer customer = customerRepository.findByPhoneNumber(request.getWalkInPhoneNumber())
                .orElseGet(() -> {
                    Customer newCust = new Customer();
                    newCust.setFullName(request.getWalkInCustomerName());
                    newCust.setPhoneNumber(request.getWalkInPhoneNumber());
                    newCust.setEmail(request.getEmail());
                    newCust.setPassword("WALK_IN_USER_NO_PASSWORD");
                    LoyaltyTier defaultTier = loyaltyTierRepository.findById(1)
                            .orElseThrow(() -> new RuntimeException("Default Loyalty Tier (ID=1) not found in DB."));
                    newCust.setLoyaltyTier(defaultTier);
                    newCust.setCurrentPoints(0);
                    newCust.setTotalSpend(BigDecimal.ZERO);
                    newCust.setTotalVisits(0);
                    return customerRepository.save(newCust);
                });

        Vehicle vehicle = vehicleRepository.findByLicensePlate(request.getLicensePlate())
                .orElseGet(() -> {
                    Vehicle newVehicle = new Vehicle();
                    newVehicle.setLicensePlate(request.getLicensePlate());
                    newVehicle.setVehicleType(request.getVehicleType());
                    newVehicle.setBrand(request.getBrand());
                    newVehicle.setColor(request.getColor());
                    newVehicle.setCustomer(customer);
                    return vehicleRepository.save(newVehicle);
                });

        BigDecimal basePrice = washService.getPrice();
        BigDecimal discountFromTier = BigDecimal.ZERO;

        if (customer.getLoyaltyTier().getDiscountPercent() > 0) {
            BigDecimal pct = BigDecimal.valueOf(customer.getLoyaltyTier().getDiscountPercent()).divide(BigDecimal.valueOf(100));
            discountFromTier = basePrice.multiply(pct);
        }

        BigDecimal finalPrice = basePrice.subtract(discountFromTier);
        if (finalPrice.compareTo(BigDecimal.ZERO) < 0) finalPrice = BigDecimal.ZERO;

        Booking booking = new Booking();
        booking.setBookingDate(today);
        booking.setVehicle(vehicle);
        booking.setWashService(washService);
        booking.setStatus("Confirmed"); // Khách vãng lai vào thẳng trạng thái chờ rửa luôn

        booking.setBasePriceAtBooking(basePrice);
        booking.setTotalPrice(finalPrice);
        booking.setLicensePlateAtBooking(vehicle.getLicensePlate());
        booking.setTierAtBooking(customer.getLoyaltyTier());
        booking.setPriorityLevel(customer.getLoyaltyTier().getPriorityLevel());

        Booking savedBooking = bookingRepository.save(booking);

        // GIỮ CHỖ CHO KHÁCH TẠI QUẦY
        for (TimeSlot targetSlot : slotsToReserve) {
            BookingSlot bookingSlot = new BookingSlot();
            bookingSlot.setBooking(savedBooking);
            bookingSlot.setTimeSlot(targetSlot);
            bookingSlotRepository.save(bookingSlot);
        }

        return new BookingResponse(savedBooking);
    }

    /* =========================================================================
     * PHẦN 4: PHƯƠNG THỨC BỔ TRỢ RIÊNG TƯ (PRIVATE HELPER)
     * ========================================================================= */

    /**
     * Hàm dùng chung để quét chuỗi ca con và ném lỗi quá tải bằng dữ liệu của BookingSlot
     */
    private List<TimeSlot> validateAndGetChainSlots(LocalDate date, TimeSlot startSlot, WashService washService) {
        int slotsNeeded = (int) Math.ceil((double) washService.getDurationMinutes() / 60);

        List<TimeSlot> allActiveSlots = timeSlotRepository.findByIsActiveTrue();
        allActiveSlots.sort(Comparator.comparing(TimeSlot::getStartTime));

        int startIndex = allActiveSlots.indexOf(startSlot);

        // Nếu không tìm thấy vị trí ca hoặc chuỗi ca kéo dài vượt quá giờ đóng cửa của tiệm
        if (startIndex == -1 || startIndex + slotsNeeded > allActiveSlots.size()) {
            throw new RuntimeException("Not enough operating hours left in the day for this service duration (" + washService.getDurationMinutes() + " mins).");
        }

        List<TimeSlot> slotsToReserve = new ArrayList<>();

        // Quét và gom các ca con cần giữ chỗ
        for (int j = 0; j < slotsNeeded; j++) {
            TimeSlot intermediateSlot = allActiveSlots.get(startIndex + j);

            // Tận dụng câu lệnh đếm COUNT siêu tốc từ database
            long occupiedCount = bookingSlotRepository.countOccupiedVehicles(date, intermediateSlot.getSlotId());

            if (occupiedCount >= intermediateSlot.getMaxCapacity()) {
                throw new RuntimeException("The shop will be overloaded during the period: "
                        + intermediateSlot.getSlotName() + " (" + occupiedCount + "/" + intermediateSlot.getMaxCapacity() + " occupied). "
                        + "Please select a different starting timeslot.");
            }
            slotsToReserve.add(intermediateSlot);
        }

        return slotsToReserve;
    }
}