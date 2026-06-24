package com.swp391.autowashpro.service;

import com.swp391.autowashpro.dto.*;
import com.swp391.autowashpro.entity.*;
import com.swp391.autowashpro.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.StringJoiner;

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

    private static final java.time.format.DateTimeFormatter YYYYMM_FORMATTER = java.time.format.DateTimeFormatter.ofPattern("yyyyMM");
    private static final java.time.ZoneId VIETNAM_ZONE = java.time.ZoneId.of("Asia/Ho_Chi_Minh");

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
    public BookingDetailPriceResponse createBooking(BookingRequest request) {
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


        //AddOn
        StringJoiner addOnJoiner = new StringJoiner(", ");

        // --- ENGINE TÍNH TIỀN ---
        BigDecimal basePrice = washService.getPrice();
        BigDecimal discountFromTier = BigDecimal.ZERO;
        BigDecimal discountFromPromo = BigDecimal.ZERO;
        BigDecimal discountFromVouchers = BigDecimal.ZERO;

        // Tính giảm giá theo Tier
        if (currentTier.getDiscountPercent() > 0) {
            BigDecimal multiplier = BigDecimal.valueOf(currentTier.getDiscountPercent())
                    .divide(BigDecimal.valueOf(100), 4, java.math.RoundingMode.HALF_UP);
            discountFromTier = basePrice.multiply(multiplier);
        }

        // Tính giảm giá theo Campaign công khai (Promotion)
        Promotion promotion = null;
        if (request.getPromotionId() != null) {
            promotion = promotionRepository.findById(request.getPromotionId()).orElse(null);
            if(promotion.getLoyaltyTier() != null &&
                    !promotion.getLoyaltyTier().getTierId().equals(currentTier.getTierId()) ){
                throw new RuntimeException("This promotion is only available for " + promotion.getLoyaltyTier().getTierName() + " members.");
            }

            if (promotion != null && promotion.getIsActive()) {
                if (promotion.getDiscountAmount() <= 100) {
                    BigDecimal promoMultiplier = BigDecimal.valueOf(promotion.getDiscountAmount())
                            .divide(BigDecimal.valueOf(100), 4, java.math.RoundingMode.HALF_UP);
                    discountFromPromo = basePrice.multiply(promoMultiplier);
                } else {
                    discountFromPromo = BigDecimal.valueOf(promotion.getDiscountAmount());
                }

                // Nếu không có giảm giá thì mới thêm mô tả vào hóa đơn chi tiết
                if (discountFromPromo.compareTo(BigDecimal.ZERO) == 0) {
                    addOnJoiner.add(promotion.getDescription());
                }
            }
        }

        // Tính giảm giá theo đống Voucher cá nhân của khách (RewardRedemption)
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

                BigDecimal currentVoucherDiscount = BigDecimal.ZERO;
                if (redemption.getDiscountAmountAtRedemption().doubleValue() <= 100) {
                    BigDecimal voucherMultiplier = redemption.getDiscountAmountAtRedemption()
                            .divide(BigDecimal.valueOf(100), 4, java.math.RoundingMode.HALF_UP);
                    currentVoucherDiscount = basePrice.multiply(voucherMultiplier);
                } else {
                    currentVoucherDiscount = redemption.getDiscountAmountAtRedemption();
                }

                // Cộng dồn tiền giảm giá thay vì gán đè
                discountFromVouchers = discountFromVouchers.add(currentVoucherDiscount);

                if (currentVoucherDiscount.compareTo(BigDecimal.ZERO) == 0) {
                    addOnJoiner.add(redemption.getRewardNameAtRedemption());
                }
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


        // 10. TÍNH ĐIỂM TÍCH LŨY DỰ KIẾN (Bảo vệ an toàn phép chia)
        BigDecimal basePoints = finalPrice.divide(BigDecimal.valueOf(1000), 2, java.math.RoundingMode.HALF_UP);
        BigDecimal multiplier = BigDecimal.valueOf(customer.getLoyaltyTier().getPointMultiplier());

        Integer totalPointEarned = basePoints.multiply(multiplier)
                .setScale(0, java.math.RoundingMode.HALF_UP)
                .intValue();

        // Chuyển kết quả StringJoiner thành chuỗi String hoàn chỉnh, nếu trống thì trả về "NONE"
        String finalAddOn = addOnJoiner.length() > 0 ? addOnJoiner.toString() : "NONE";

        // 11. Trả về Response chi tiết
        BookingDetailPriceResponse response = new BookingDetailPriceResponse();
        response.setBasePrice(basePrice);
        response.setDiscountFromTier(discountFromTier);
        response.setDiscountFromPromo(discountFromPromo);
        response.setDiscountFromReward(discountFromVouchers);
        response.setAddOn(finalAddOn);
        response.setTotalPointEarned(totalPointEarned);
        response.setFinalPrice(finalPrice);

        return response;
    }


    /* =========================================================================
     * PHẦN 3: CÁC CHỨC NĂNG DÀNH CHO NHÂN VIÊN (STAFF OPERATIONS)
     * ========================================================================= */

    /**
     * Staff Luồng 1: Xác nhận khách đã đến tiệm (PENDING -> CONFIRMED)
     */
    @Transactional
    public BookingResponse confirmArrival(Integer bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found."));

        if (!"PENDING".equalsIgnoreCase(booking.getStatus())) {
            throw new RuntimeException("Only 'PENDING' bookings can be CONFIRMED.");
        }

        booking.setStatus("CONFIRMED");
        return new BookingResponse(bookingRepository.save(booking));
    }

    /**
     * Staff Luồng 2: Rửa xong, Thu tiền, Tích điểm & Cập nhật thống kê tháng (CONFIRMED -> COMPLETED)
     */
    @Transactional
    public BookingResponse completeBookingAndPay(Integer bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found."));

        if (!"CONFIRMED".equalsIgnoreCase(booking.getStatus())) {
            throw new RuntimeException("Booking must be 'CONFIRMED' to complete.");
        }

        booking.setStatus("COMPLETED");
        Booking savedBooking = bookingRepository.save(booking);

        int totalPointsUsed = 0;
        if (savedBooking.getRewardRedemptions() != null && !savedBooking.getRewardRedemptions().isEmpty()) {
            totalPointsUsed = savedBooking.getRewardRedemptions().stream()
                    .mapToInt(r -> r.getPointsUsed() != null ? r.getPointsUsed() : 0)
                    .sum();
        }

        BigDecimal finalPrice = savedBooking.getTotalPrice();
        BigDecimal basePoints = finalPrice.divide(BigDecimal.valueOf(1000), 2, java.math.RoundingMode.HALF_UP);
        BigDecimal multiplier = BigDecimal.valueOf(savedBooking.getTierAtBooking().getPointMultiplier());

        int totalPointsEarned = basePoints.multiply(multiplier)
                .setScale(0, java.math.RoundingMode.HALF_UP)
                .intValue();

        // Lưu lịch sử rửa xe
        WashHistory washHistory = new WashHistory();
        washHistory.setWashDate(LocalDateTime.now(VIETNAM_ZONE));
        washHistory.setAmountPaid(finalPrice);
        washHistory.setPointsEarned(totalPointsEarned);
        washHistory.setPointsUsed(totalPointsUsed);
        washHistory.setBooking(savedBooking);
        WashHistory savedWashHistory = washHistoryRepository.save(washHistory);

        // Cập nhật thông tin khách hàng
        Customer customer = savedBooking.getVehicle().getCustomer();

        //Nếu trường số tiền hoặc điểm tích lũy cũ bị NULL trong DB thì tự gán bằng 0 trước khi cộng
        if (customer.getTotalSpend() == null) customer.setTotalSpend(BigDecimal.ZERO);
        if (customer.getCurrentPoints() == null) customer.setCurrentPoints(0);
        if (customer.getTotalVisits() == null) customer.setTotalVisits(0);

        customer.setTotalSpend(customer.getTotalSpend().add(finalPrice));
        customer.setTotalVisits(customer.getTotalVisits() + 1);
        // Cộng điểm tích lũy mới
        customer.setCurrentPoints(customer.getCurrentPoints() + totalPointsEarned);
        customerRepository.save(customer);

        // Ghi log biến động điểm nếu có điểm thưởng phát sinh
        if (totalPointsEarned > 0) {
            LoyaltyPoint pointLog = new LoyaltyPoint();
            pointLog.setCustomer(customer);
            pointLog.setPointsChange(totalPointsEarned);
            pointLog.setTransactionType("EARN_BOOKING");
            pointLog.setCreatedAt(LocalDateTime.now(VIETNAM_ZONE));
            pointLog.setExpiryDate(LocalDate.now(VIETNAM_ZONE).plusMonths(1));
            pointLog.setWashHistory(savedWashHistory);
            loyaltyPointRepository.save(pointLog);
        }


        String currentYearMonth = YearMonth.now(VIETNAM_ZONE).format(YYYYMM_FORMATTER);
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

        if (monthlyStats.getMonthlySpend() == null) monthlyStats.setMonthlySpend(BigDecimal.ZERO);
        if (monthlyStats.getMonthlyVisits() == null) monthlyStats.setMonthlyVisits(0);

        monthlyStats.setMonthlySpend(monthlyStats.getMonthlySpend().add(finalPrice));
        monthlyStats.setMonthlyVisits(monthlyStats.getMonthlyVisits() + 1);
        customerMonthlyStatsRepository.save(monthlyStats);

        return new BookingResponse(savedBooking);
    }

    /**
     * Staff Luồng 3: Đặt lịch trực tiếp tại quầy cho Khách vãng lai Walk-in
     */
    @Transactional
    public BookingDetailPriceResponse createWalkInBooking(WalkInBookingRequest request) {
        LocalDate today = LocalDate.now();

        // 1. KIỂM TRA KHUNG GIỜ (TIMESLOT)
        TimeSlot startSlot = timeSlotRepository.findById(request.getSlotId())
                .orElseThrow(() -> new RuntimeException("Time Slot not found."));
        if (!startSlot.getIsActive()) {
            throw new RuntimeException("This timeslot is currently closed.");
        }

        // 2. KIỂM TRA GÓI DỊCH VỤ (WASH SERVICE)
        WashService washService = washServiceRepository.findById(request.getWashServiceId())
                .orElseThrow(() -> new RuntimeException("Wash service not found."));
        if (!washService.getIsActive()) {
            throw new RuntimeException("This wash service is deactivated.");
        }

        // CHẶN TRÙNG LỊCH CHO LUỒNG TẠI QUẦY (Lấy chuỗi slot cần giữ)
        List<TimeSlot> slotsToReserve = validateAndGetChainSlots(today, startSlot, washService);

        // 3. ĐỊNH DANH KHÁCH HÀNG QUA EMAIL
        Customer customer = customerRepository.findByEmail(request.getEmail())
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

        // 4. ĐỊNH DANH XE
        Vehicle vehicle = vehicleRepository.findByLicensePlate(request.getLicensePlate())
                .orElseGet(() -> {
                    Vehicle newVehicle = new Vehicle();
                    newVehicle.setLicensePlate(request.getLicensePlate());
                    newVehicle.setVehicleType(request.getVehicleType());
                    newVehicle.setBrand(request.getBrand());
                    newVehicle.setColor(request.getColor());
                    newVehicle.setCustomer(customer);
                    newVehicle.setIsActive(true);
                    return vehicleRepository.save(newVehicle);
                });

        boolean needUpdateVehicle = false;

        // Tình huống A: Xe đổi chủ hoặc gán sai chủ
        if (vehicle.getCustomer() == null || !vehicle.getCustomer().getCustomerId().equals(customer.getCustomerId())) {
            vehicle.setCustomer(customer);
            needUpdateVehicle = true;
        }

        // Tình huống B: Xe từng bị xóa mềm -> Kích hoạt lại
        if (Boolean.FALSE.equals(vehicle.getIsActive())) {
            vehicle.setIsActive(true);
            needUpdateVehicle = true;
        }

        // Tình huống C: Đồng bộ thông tin xe (Sử dụng Objects.equals để an toàn chống Null)
        if (!java.util.Objects.equals(vehicle.getVehicleType(), request.getVehicleType()) ||
                !java.util.Objects.equals(vehicle.getBrand(), request.getBrand()) ||
                !java.util.Objects.equals(vehicle.getColor(), request.getColor())) {

            vehicle.setVehicleType(request.getVehicleType());
            vehicle.setBrand(request.getBrand());
            vehicle.setColor(request.getColor());
            needUpdateVehicle = true;
        }

        if (needUpdateVehicle) {
            vehicle = vehicleRepository.save(vehicle);
        }

        // 5. TÍNH TOÁN TÀI CHÍNH (PRICING LOGIC)
        BigDecimal basePrice = washService.getPrice();
        BigDecimal discountFromTier = BigDecimal.ZERO;

        if (customer.getLoyaltyTier().getDiscountPercent() > 0) {
            BigDecimal pct = BigDecimal.valueOf(customer.getLoyaltyTier().getDiscountPercent())
                    .divide(BigDecimal.valueOf(100), 4, java.math.RoundingMode.HALF_UP);
            discountFromTier = basePrice.multiply(pct);
        }

        BigDecimal finalPrice = basePrice.subtract(discountFromTier);
        if (finalPrice.compareTo(BigDecimal.ZERO) < 0) {
            finalPrice = BigDecimal.ZERO;
        }

        // 6. KHỞI TẠO ĐƠN ĐẶT LỊCH (BOOKING)
        Booking booking = new Booking();
        booking.setBookingDate(today);
        booking.setVehicle(vehicle);
        booking.setWashService(washService);
        booking.setStatus("CONFIRMED");

        // Snapshot lịch sử hóa đơn
        booking.setBasePriceAtBooking(basePrice);
        booking.setTotalPrice(finalPrice);
        booking.setLicensePlateAtBooking(vehicle.getLicensePlate());
        booking.setTierAtBooking(customer.getLoyaltyTier());
        booking.setPriorityLevel(customer.getLoyaltyTier().getPriorityLevel());

        Booking savedBooking = bookingRepository.save(booking);

        // 7. KHÓA CỨNG Ô GIỜ
        for (TimeSlot targetSlot : slotsToReserve) {
            BookingSlot bookingSlot = new BookingSlot();
            bookingSlot.setBooking(savedBooking);
            bookingSlot.setTimeSlot(targetSlot);
            bookingSlotRepository.save(bookingSlot);
        }

        // 8. TÍNH ĐIỂM THƯỞNG CHI TIẾT (Đã vá lỗi Arithmetic và làm tròn toán học)
        BigDecimal discountFromPromo = BigDecimal.ZERO;
        BigDecimal discountFromReward = BigDecimal.ZERO;
        String addOn = "NONE";

        // Thực hiện chia có kèm scale và chế độ làm tròn HALF_UP (Làm tròn lên từ .5)
        BigDecimal basePoints = finalPrice.divide(BigDecimal.valueOf(1000), 2, java.math.RoundingMode.HALF_UP);
        BigDecimal multiplier = BigDecimal.valueOf(customer.getLoyaltyTier().getPointMultiplier());

        // Nhân hệ số và lấy phần nguyên sau khi làm tròn chuẩn toán học
        Integer totalPointEarned = basePoints.multiply(multiplier)
                .setScale(0, java.math.RoundingMode.HALF_UP)
                .intValue();

        // 9. TRẢ VỀ DTO HÓA ĐƠN CHI TIẾT
        BookingDetailPriceResponse response = new BookingDetailPriceResponse();
        response.setBasePrice(basePrice);
        response.setDiscountFromTier(discountFromTier);
        response.setDiscountFromPromo(discountFromPromo);
        response.setDiscountFromReward(discountFromReward);
        response.setAddOn(addOn);
        response.setFinalPrice(finalPrice);
        response.setTotalPointEarned(totalPointEarned);

        return response;
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

    /* =========================================================================
     * PHẦN 5: LẤY DANH SÁCH BOOKING (MANAGER OPERATIONS)
     * ========================================================================= */
    public List<BookingListResponse> getBookingList(){
        List<BookingListResponse> bookingList = bookingRepository.findAll().stream().map(BookingListResponse::new).toList();
        return bookingList;
    }

    /**
     * Hủy lịch đặt xe và hoàn trả tài nguyên (Voucher) về ví cho khách hàng
     */
    @Transactional
    public void cancelBooking(Integer bookingId) {
        // 1. Kiểm tra xem đơn đặt lịch có tồn tại không
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found with ID: " + bookingId));

        // 2. Kiểm tra trạng thái đơn hàng hiện tại
        // Đơn đã HOÀN THÀNH thì không được phép hủy nữa
        if ("COMPLETED".equalsIgnoreCase(booking.getStatus())) {
            throw new RuntimeException("Cannot cancel a booking that is already COMPLETED.");
        }
        // Đơn đã HỦY TRƯỚC ĐÓ rồi thì chặn luôn, tránh gửi request trùng lặp
        if ("CANCELLED".equalsIgnoreCase(booking.getStatus())) {
            throw new RuntimeException("This booking has already been cancelled.");
        }

        // 3. Cập nhật trạng thái đơn hàng thành CANCELLED
        booking.setStatus("CANCELLED");
        bookingRepository.save(booking);

        // 4. Kiểm tra xem đơn này có đang trói buộc cái Voucher nào không
        // (Tìm bản ghi đổi thưởng dựa theo đối tượng booking hiện tại)
        RewardRedemption redemption = rewardRedemptionRepository.findByBooking(booking);

        if (redemption != null) {
            // Đặt lại booking bằng null để đưa Voucher quay về trạng thái "Chưa sử dụng"
            redemption.setBooking(null);
            rewardRedemptionRepository.save(redemption);
        }
    }
}