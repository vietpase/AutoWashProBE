    package com.swp391.autowashpro.entity;

    import jakarta.persistence.*;
    import lombok.AllArgsConstructor;
    import lombok.Getter;
    import lombok.NoArgsConstructor;
    import lombok.Setter;

    import java.time.LocalDate;
    import java.time.LocalDateTime;


    @Entity
    @Table(name = "LoyaltyPoint")
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public class LoyaltyPoint {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "point_id")
        private int pointId;

        @Column(name = "points_change", nullable = false)
        private Integer pointsChange;

        @Column(name = "transaction_type", length = 50, nullable = false)
        private String transactionType;

        @Column(name = "expiry_date")
        private LocalDate expiryDate;

        @Column(name = "created_at", updatable = false)
        private LocalDateTime createdAt;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "customer_id", nullable = false)
        private Customer customer;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "wash_id")
        private WashHistory washHistory;
    }
