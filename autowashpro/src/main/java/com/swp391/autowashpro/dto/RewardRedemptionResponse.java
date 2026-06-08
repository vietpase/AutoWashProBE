package com.swp391.autowashpro.dto;

import com.swp391.autowashpro.entity.RewardRedemption;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class RewardRedemptionResponse {
    private Integer redemptionId;
    private Integer pointsUsed;
    private LocalDateTime redemptionDate;
    private Integer rewardId;
    private String rewardName;      // Trả về tên quà snapshot
    private BigDecimal discountAmount; // Trả về số tiền snapshot
    private Integer bookingId;
    private String status;

    public RewardRedemptionResponse(RewardRedemption redemption) {
        this.redemptionId = redemption.getRedemptionId();
        this.pointsUsed = redemption.getPointsUsed();
        this.redemptionDate = redemption.getRedemptionDate();
        this.rewardId = redemption.getRewardCatalog().getRewardId();

        // ĐỌC DỮ LIỆU TỪ SNAPSHOT (An toàn tuyệt đối trước mọi cập nhật của Manager)
        this.rewardName = redemption.getRewardNameAtRedemption();
        this.discountAmount = redemption.getDiscountAmountAtRedemption();

        if (redemption.getBooking() != null) {
            this.bookingId = redemption.getBooking().getBookingId();
            this.status = "USED";
        } else {
            this.bookingId = null;
            this.status = "AVAILABLE";
        }
    }
}