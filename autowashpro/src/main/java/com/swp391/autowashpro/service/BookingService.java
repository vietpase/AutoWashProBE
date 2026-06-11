package com.swp391.autowashpro.service;

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
import java.util.List;

@Service
@RequiredArgsConstructor // Tự động sinh constructor cho tất cả các field final, đỡ viết tay dài dòng
public class BookingService {

    // --- REPOSITORIES CHUNG ---
    private final BookingRepository bookingRepository;
    private final CustomerRepository customerRepository;
    private final VehicleRepository vehicleRepository;
    private final TimeSlotRepository timeSlotRepository;
    private final WashServiceRepository washServiceRepository;
    private final PromotionRepository promotionRepository;
    private final RewardRedemptionRepository rewardRedemptionRepository;
    private final LoyaltyPointRepository loyaltyPointRepository;

    // --- REPOSITORIES BỔ SUNG CHO STAFF ---
    private final WashHistoryRepository washHistoryRepository;
    private final CustomerMonthlyStatsRepository customerMonthlyStatsRepository;

    /* =========================================================================
     * PHẦN 1: CÁC CHỨC NĂNG DÀNH CHO KHÁCH HÀNG (ONLINE CUSTOMER)
     * ========================================================================= */

    /**
     * Khách hàng đặt lịch Online từ Web
     */
    @Transactional
    public BookingResponse createBooking(BookingRequest request) {
        // 1. Kiểm tra thông tin Customer & Rank hiện tại
        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found."));
        LoyaltyTier currentTier = customer.getLoyaltyTier();

        // 2. Validate Booking Window (Hạn ngày đặt lịch tối đa dựa theo Rank khách)
        long daysBetween = ChronoUnit.DAYS.between(LocalDate.now(), request.getBookingDate());
        if (daysBetween < 0 || daysBetween > currentTier.getBookingWindowDays()) {
            throw new RuntimeException("Your membership tier limits bookings within " + currentTier.getBookingWindowDays() + " days ahead.");
        }

        // 3. Kiểm tra xe (Vehicle) có đúng thuộc sở hữu của Customer này không
        Vehicle vehicle = vehicleRepository.findById(request.getVehicleId())
                .orElseThrow(() -> new RuntimeException("Vehicle not found."));
        if (!vehicle.getCustomer().getCustomerId().equals(customer.getCustomerId())) {
            throw new RuntimeException("This vehicle does not belong to the requested customer.");
        }

        // 4. Kiểm tra Slot giờ & Check quá tải công suất (Max Capacity)
        TimeSlot slot = timeSlotRepository.findById(request.getSlotId())
                .orElseThrow(() -> new RuntimeException("Time Slot not found."));
        if (!slot.getIsActive()) {
            throw new RuntimeException("This timeslot is currently closed.");
        }

        long currentBookingsCount = bookingRepository.countActiveBookings(request.getBookingDate(), slot.getSlotId());
        if (currentBookingsCount >= slot.getMaxCapacity()) {
            throw new RuntimeException("This timeslot is full. Maximum capacity is " + slot.getMaxCapacity() + " vehicles.");
        }

        // 5. Kiểm tra dịch vụ rửa xe
        WashService washService = washServiceRepository.findById(request.getWashServiceId())
                .orElseThrow(() -> new RuntimeException("Wash service not found."));
        if (!washService.getIsActive()) {
            throw new RuntimeException("This wash service is deactivated.");
        }

        // --- ENGINE TÍNH TIỀN ---
        BigDecimal basePrice = washService.getPrice();
        BigDecimal discountFromTier = BigDecimal.ZERO;
        BigDecimal discountFromPromo = BigDecimal.ZERO;
        BigDecimal discountFromVouchers = BigDecimal.ZERO;

        // A. Giảm giá theo Rank thành viên (discount_percent)
        if (currentTier.getDiscountPercent() > 0) {
            BigDecimal multiplier = BigDecimal.valueOf(currentTier.getDiscountPercent()).divide(BigDecimal.valueOf(100));
            discountFromTier = basePrice.multiply(multiplier);
        }

        // B. Giảm giá từ Promotion marketing (nếu có)
        Promotion promotion = null;
        if (request.getPromotionId() != null) {
            promotion = promotionRepository.findById(request.getPromotionId()).orElse(null);
            if (promotion != null && promotion.getIsActive()) {
                discountFromPromo = BigDecimal.valueOf(promotion.getDiscountAmount());
            }
        }

        // C. Giảm giá từ đống Voucher đổi điểm (RewardRedemption) khách gom vào đơn
        List<RewardRedemption> redemptionsToApply = new ArrayList<>();
        if (request.getAppliedRedemptionIds() != null && !request.getAppliedRedemptionIds().isEmpty()) {
            for (Integer redemptionId : request.getAppliedRedemptionIds()) {
                RewardRedemption redemption = rewardRedemptionRepository.findById(redemptionId)
                        .orElseThrow(() -> new RuntimeException("Voucher code " + redemptionId + " not found."));

                // Khớp xem voucher có đúng của ông khách này không và đã xài chưa
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

        // D. Tổng hợp lại số tiền cuối cùng (Nếu âm tiền thì đưa về 0)
        BigDecimal totalDiscount = discountFromTier.add(discountFromPromo).add(discountFromVouchers);
        BigDecimal finalPrice = basePrice.subtract(totalDiscount);
        if (finalPrice.compareTo(BigDecimal.ZERO) < 0) {
            finalPrice = BigDecimal.ZERO;
        }

        // 6. Khởi tạo và Lưu thông tin đơn đặt lịch hoàn chỉnh
        Booking booking = new Booking();
        booking.setBookingDate(request.getBookingDate());
        booking.setVehicle(vehicle);
        booking.setTimeSlot(slot);
        booking.setWashService(washService);
        booking.setPromotion(promotion);
        booking.setStatus("PENDING");

        // --- CẬP NHẬT ĐÓNG BĂNG SNAPSHOT CHO LUỒNG ONLINE ---
        booking.setBasePriceAtBooking(basePrice); // Đóng băng giá gốc lúc đặt
        booking.setTotalPrice(finalPrice);       // Đóng băng giá thực thu sau giảm
        booking.setLicensePlateAtBooking(vehicle.getLicensePlate()); // Đóng băng biển số xe
        booking.setTierAtBooking(currentTier);
        booking.setPriorityLevel(currentTier.getPriorityLevel());
        // ----------------------------------------------------

        Booking savedBooking = bookingRepository.save(booking);

        // 7. Thực hiện map ngược ID Booking vào các Voucher để "vô hiệu hóa" chúng
        for (RewardRedemption redemption : redemptionsToApply) {
            redemption.setBooking(savedBooking);
            rewardRedemptionRepository.save(redemption);
        }

        return new BookingResponse(savedBooking);
    }


    /* =========================================================================
     * PHẦN 2: CÁC CHỨC NĂNG DÀNH CHO NHÂN VIÊN (STAFF OPERATIONS)
     * ========================================================================= */

    /**
     * Staff Luồng 1: Xác nhận khách đã đến tiệm (Pending -> Confirmed)
     */
    @Transactional
    public BookingResponse confirmArrival(Integer bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found."));

        if (!booking.getStatus().equalsIgnoreCase("Pending")) {
            throw new RuntimeException("Only 'Pending' bookings can be confirmed. Current status: " + booking.getStatus());
        }

        booking.setStatus("Confirmed");
        return new BookingResponse(bookingRepository.save(booking));
    }

    /**
     * Staff Luồng 2: Rửa xong, Thu tiền, Tích điểm & Cập nhật thống kê tháng (Confirmed -> Completed)
     */
    @Transactional
    public BookingResponse completeBookingAndPay(Integer bookingId) {
        // 1. Kiểm tra đơn đặt lịch
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found."));

        if (!booking.getStatus().equalsIgnoreCase("Confirmed")) {
            throw new RuntimeException("Booking must be 'Confirmed' to complete. Current: " + booking.getStatus());
        }

        // 2. Chuyển trạng thái sang Completed
        booking.setStatus("Completed");
        Booking savedBooking = bookingRepository.save(booking);

        // 3. Tính toán số điểm khách đã tiêu từ voucher áp dụng
        int totalPointsUsed = 0;
        if (savedBooking.getRewardRedemptions() != null && !savedBooking.getRewardRedemptions().isEmpty()) {
            totalPointsUsed = savedBooking.getRewardRedemptions().stream()
                    .mapToInt(RewardRedemption::getPointsUsed)
                    .sum();
        }

        // 4. Tính điểm thưởng được cộng dựa trên giá thực tế và Rank đóng băng lúc đặt đơn
        BigDecimal finalPrice = savedBooking.getTotalPrice();
        int basePoints = finalPrice.divide(BigDecimal.valueOf(1000)).intValue();
        double multiplier = savedBooking.getTierAtBooking().getPointMultiplier();
        int totalPointsEarned = (int) Math.round(basePoints * multiplier);

        // 5. Tạo và lưu lịch sử rửa xe (WashHistory)
        WashHistory washHistory = new WashHistory();
        washHistory.setWashDate(LocalDateTime.now());
        washHistory.setAmountPaid(finalPrice);
        washHistory.setPointsEarned(totalPointsEarned);
        washHistory.setPointsUsed(totalPointsUsed);
        washHistory.setBooking(savedBooking);
        WashHistory savedWashHistory = washHistoryRepository.save(washHistory);

        // 6. Cập nhật hồ sơ khách hàng (Customer) - Tích lũy trọn đời
        Customer customer = savedBooking.getVehicle().getCustomer();
        customer.setTotalSpend(customer.getTotalSpend().add(finalPrice));
        customer.setTotalVisits(customer.getTotalVisits() + 1);
        customer.setCurrentPoints(customer.getCurrentPoints() + totalPointsEarned);
        customerRepository.save(customer);

        // 7. Ghi nhận biến động ví điểm (LoyaltyPoint)
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

        // 8. Cập nhật thống kê tích lũy theo tháng (CustomerMonthlyStats)
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
     * Staff Luồng 3: Đặt lịch trực tiếp tại quầy cho Khách vãng lai
     */
    @Transactional
    public BookingResponse createWalkInBooking(WalkInBookingRequest request) {
        LocalDate today = LocalDate.now();

        // 1. Kiểm tra công suất phục vụ của Khung giờ
        TimeSlot slot = timeSlotRepository.findById(request.getSlotId())
                .orElseThrow(() -> new RuntimeException("Time Slot not found."));

        long occupiedSlots = bookingRepository.countActiveBookings(today, slot.getSlotId());
        if (occupiedSlots >= slot.getMaxCapacity()) {
            throw new RuntimeException("This timeslot is already full for today.");
        }

        // 2. Tìm hoặc tự tạo mới tài khoản Customer theo SĐT
        Customer customer = customerRepository.findByPhoneNumber(request.getWalkInPhoneNumber())
                .orElseGet(() -> {
                    Customer newCust = new Customer();
                    newCust.setFullName(request.getWalkInCustomerName());
                    newCust.setPhoneNumber(request.getWalkInPhoneNumber());
                    newCust.setEmail(request.getEmail());
                    newCust.setPassword("WALK_IN_USER_NO_PASSWORD");
                    newCust.setLoyaltyTier(new LoyaltyTier(1)); // Giả định ID hạng 1 là mặc định
                    newCust.setCurrentPoints(0);
                    newCust.setTotalSpend(BigDecimal.ZERO);
                    newCust.setTotalVisits(0);
                    return customerRepository.save(newCust);
                });

        // 3. Tìm hoặc tự tạo mới thông tin Xe theo Biển số xe
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

        // 4. Lấy thông tin dịch vụ rửa xe
        WashService washService = washServiceRepository.findById(request.getWashServiceId())
                .orElseThrow(() -> new RuntimeException("Wash service not found."));

        // 5. Tính toán chi phí và giảm giá hạng thành viên
        BigDecimal basePrice = washService.getPrice();
        BigDecimal discountFromTier = BigDecimal.ZERO;

        if (customer.getLoyaltyTier().getDiscountPercent() > 0) {
            BigDecimal pct = BigDecimal.valueOf(customer.getLoyaltyTier().getDiscountPercent())
                    .divide(BigDecimal.valueOf(100));
            discountFromTier = basePrice.multiply(pct);
        }

        BigDecimal finalPrice = basePrice.subtract(discountFromTier);
        if (finalPrice.compareTo(BigDecimal.ZERO) < 0) finalPrice = BigDecimal.ZERO;

        // 6. Khởi tạo đơn hàng Booking và đóng băng dữ liệu (Snapshot)
        Booking booking = new Booking();
        booking.setBookingDate(today);
        booking.setVehicle(vehicle);
        booking.setTimeSlot(slot);
        booking.setWashService(washService);

        // Đóng băng trạng thái biến động theo thời gian
        booking.setBasePriceAtBooking(basePrice);
        booking.setTotalPrice(finalPrice);
        booking.setLicensePlateAtBooking(vehicle.getLicensePlate());
        booking.setTierAtBooking(customer.getLoyaltyTier());
        booking.setPriorityLevel(customer.getLoyaltyTier().getPriorityLevel());

        booking.setStatus("Confirmed"); // Vào thẳng trạng thái chờ rửa, không qua bước PENDING nữa

        return new BookingResponse(bookingRepository.save(booking));
    }
}