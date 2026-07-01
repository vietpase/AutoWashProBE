-- ============================================================
-- 1. LOYALTY TIER 
-- ============================================================
SET IDENTITY_INSERT [dbo].[loyalty_tier] ON;
INSERT INTO [dbo].[loyalty_tier] 
(tier_id, booking_window_days, discount_percent, is_active, min_spending, min_visits, point_multiplier, priority_level, tier_name)
VALUES 
(1, 7,  0,  1, 0.00,        0,  1.0, 1, 'BRONZE'),
(2, 14, 5,  1, 1000000.00,  5,  1.2, 2, 'SILVER'),
(3, 30, 10, 1, 5000000.00,  15, 1.5, 3, 'GOLD'),
(4, 60, 15, 1, 15000000.00, 30, 2.0, 4, 'DIAMOND');
SET IDENTITY_INSERT [dbo].[loyalty_tier] OFF;

-- ============================================================
-- 2. ADMIN ACCOUNT (Mật khẩu: 123456789)
-- ============================================================
INSERT INTO [dbo].[admin_account] (full_name, password, role, username)
VALUES 
(N'Quản trị viên chính', '$2a$12$7iQ6mml/x4nuynIzlBcPFeS9uzewtv.rYdwXsf/ZdBfEaR7dwtV3a', 'MANAGER', 'admin'),
(N'Nhân viên CSKH', '$2a$12$7iQ6mml/x4nuynIzlBcPFeS9uzewtv.rYdwXsf/ZdBfEaR7dwtV3a', 'STAFF', 'staff1');

-- ============================================================
-- 3. WASH SERVICE
-- ============================================================
SET IDENTITY_INSERT [dbo].[wash_service] ON;
INSERT INTO [dbo].[wash_service] (service_id, description, duration_minutes, is_active, price, service_name) 
VALUES 
(1, N'Bao gồm rửa vỏ chống xước, hút bụi nội thất căn bản, lau kính và xịt bóng dưỡng lốp xe.', 45, 1, 150000.00, N'Rửa Xe Tiêu Chuẩn (Standard Wash)'),
(2, N'Rửa chi tiết sâu từng khe kẽ, tẩy nhựa đường/bụi sắt, phủ lớp wax bóng bảo vệ bề mặt sơn, khử mùi ozone nội thất.', 75, 1, 350000.00, N'Rửa Xe Cao Cấp & Phủ Wax'),
(3, N'Dọn dẹp bụi bẩn và dầu mỡ bám trong khoang động cơ bằng công nghệ hơi nước nóng, dưỡng phục hồi chi tiết nhựa, cao su bảo vệ hệ thống điện.', 90, 1, 650000.00, N'Vệ Sinh Khoang Máy Bằng Hơi Nước Nóng'),
(4, N'Tháo ghế, giặt sạch trần, sàn, toàn bộ ghế da/nỉ bằng máy phun hút áp lực. Dưỡng bề mặt taplo, tapi và diệt khuẩn toàn bộ xe.', 180, 1, 1200000.00, N'Combo Vệ Sinh Nội Thất Toàn Diện'),
(5, N'Tẩy sạch các vết ố mốc, váng mưa trên kính lái và kính sườn, vệ sinh luồng gió điều hòa bằng dung dịch chuyên dụng.', 30, 1, 250000.00, N'Tẩy Ố Kính & Sát Khuẩn Điều Hòa');
SET IDENTITY_INSERT [dbo].[wash_service] OFF;

-- ============================================================
-- 4. PROMOTION
-- ============================================================
SET IDENTITY_INSERT [dbo].[promotion] ON;
INSERT INTO [dbo].[promotion] (promo_id, description, discount_amount, end_date, is_active, promo_name, start_date, min_tier_id)
VALUES 
(1, N'Giảm ngay 20,000đ cho tất cả khách hàng mới trải nghiệm lần đầu.', 20000.00, '2026-12-31', 1, N'WELCOME20', '2026-06-01', 1),
(2, N'Ưu đãi giải nhiệt mùa hè, giảm thẳng 30,000đ áp dụng cho mọi gói.', 30000.00, '2026-07-31', 1, N'SUMMER30', '2026-06-01', 1),
(3, N'Khuyến mãi đặc quyền dành riêng cho thành viên hạng Bạc trở lên.', 50000.00, '2026-09-30', 1, N'SILVERPLUS', '2026-06-15', 2),
(4, N'Tri ân khách hàng VIP hạng Vàng, giảm bùng nổ 80,000đ.', 80000.00, '2026-08-31', 1, N'GOLDBOSS', '2026-06-15', 3);
SET IDENTITY_INSERT [dbo].[promotion] OFF;

-- ============================================================
-- 5. TIME SLOT
-- ============================================================
SET IDENTITY_INSERT [dbo].[time_slot] ON;
INSERT INTO [dbo].[time_slot] (slot_id, end_time, is_active, max_capacity, slot_name, start_time)
VALUES 
(1, '09:00:00', 1, 4, N'Ca Sáng 1', '08:00:00'),
(2, '10:00:00', 1, 4, N'Ca Sáng 2', '09:00:00'),
(3, '11:00:00', 1, 4, N'Ca Sáng 3', '10:00:00'),
(4, '12:00:00', 1, 2, N'Ca Trưa Cao Điểm', '11:00:00'),
(5, '14:00:00', 1, 4, N'Ca Chiều 1', '13:00:00'),
(6, '15:00:00', 1, 4, N'Ca Chiều 2', '14:00:00'),
(7, '16:00:00', 1, 6, N'Ca Chiều Cao Điểm 1', '15:00:00'),
(8, '17:00:00', 1, 6, N'Ca Chiều Cao Điểm 2', '16:00:00'),
(9, '18:00:00', 1, 3, N'Ca Tối Muộn', '17:00:00');
SET IDENTITY_INSERT [dbo].[time_slot] OFF;

-- ============================================================
-- 6. REWARD CATALOG
-- ============================================================
INSERT INTO [dbo].[reward_catalog](description, discount_amount, is_active, points_required, reward_name, stock_quantity)
VALUES 
(N'Áp dụng trực tiếp vào hóa đơn cho mọi dịch vụ.', 50000.00, 1, 50, N'Voucher giảm giá 50.000đ', 9),
(N'Áp dụng trực tiếp vào hóa đơn cho mọi dịch vụ.', 100000.00, 1, 90, N'Voucher giảm giá 100.000đ', 9);

-- ============================================================
-- 7. CUSTOMER (Đã phân bổ trực tiếp ID hạng từ 1 đến 4, Pass thô: 123456789)
-- ============================================================
SET IDENTITY_INSERT [dbo].[customer] ON;
INSERT INTO [dbo].[customer] (customer_id, create_at, current_points, email, full_name, last_tier_review, password, phone_number, total_spend, total_visits, tier_id) 
VALUES
(1, '2026-06-22', 10,  'vietpase@gmail.com',       N'Harry Potter',   NULL, '$2a$12$7iQ6mml/x4nuynIzlBcPFeS9uzewtv.rYdwXsf/ZdBfEaR7dwtV3a', '0912345678', 350000.00,   2,  1), -- Hạng 1: BRONZE
(2, '2026-02-15', 150, 'dreamlatern0410@gmail.com', N'Bánh Đậu Xanh',   NULL, '$2a$12$7iQ6mml/x4nuynIzlBcPFeS9uzewtv.rYdwXsf/ZdBfEaR7dwtV3a', '0987654321', 1450000.00,  5,  2), -- Hạng 2: SILVER
(3, '2026-01-10', 450, 'ron@gmail.com',             N'Ron Weasley',    NULL, '$2a$12$7iQ6mml/x4nuynIzlBcPFeS9uzewtv.rYdwXsf/ZdBfEaR7dwtV3a', '0933333333', 5600000.00,  16, 3), -- Hạng 3: GOLD
(4, '2026-01-01', 990, 'hermione@gmail.com',        N'Hermione VIP',   NULL, '$2a$12$7iQ6mml/x4nuynIzlBcPFeS9uzewtv.rYdwXsf/ZdBfEaR7dwtV3a', '0944444444', 16200000.00, 31, 4); -- Hạng 4: DIAMOND
SET IDENTITY_INSERT [dbo].[customer] OFF;

-- ============================================================
-- 8. VEHICLE (Gán đúng kiểu chữ thường phân loại: small/medium/large/extra)
-- ============================================================
SET IDENTITY_INSERT [dbo].[vehicle] ON;
INSERT INTO [dbo].[vehicle] (vehicle_id, brand, color, is_active, license_plate, vehicle_type, customer_id)
VALUES
(1, 'Toyota Corolla Cross', 'White', 1, '60A-12345', 'medium', 1),
(2, 'Honda HRV',            'Black', 1, '51F-67890', 'medium', 1),
(3, 'VinFast VF9',          'Blue',  1, '50H-11111', 'extra',  1),
(4, 'Hyundai Santafe',      'Red',   1, '61A-22222', 'large',  2),
(5, 'Kia Morning',          'Gray',  1, '72B-33333', 'small',  2),
(6, 'Mazda Cx5',            'White', 0, '43C-44444', 'medium', 2),
(7, 'Mercedes C200',        'Black', 1, '51G-99999', 'medium', 3),
(8, 'Porsche Cayenne',      'Gold',  1, '51K-88888', 'extra',  4);
SET IDENTITY_INSERT [dbo].[vehicle] OFF;

-- ============================================================
-- 9. BOOKING (Đã sửa đổi promotion_id thành promo_id)
-- ============================================================
SET IDENTITY_INSERT [dbo].[booking] ON;

-- --- THÁNG 5/2026 ---
INSERT INTO [dbo].[booking] (booking_id, booking_date, vehicle_id, service_id, promotion_id, status, base_price_at_booking, total_price, license_plate_at_booking, tier_id_at_booking, priority_level) VALUES
(101, '2026-05-15', 1, 1, NULL, 'COMPLETED', 200000.00, 200000.00, '60A-12345', 1, 1), -- Harry (Bronze)
(102, '2026-05-20', 4, 2, NULL, 'COMPLETED', 450000.00, 450000.00, '61A-22222', 2, 2); -- Bánh Đậu Xanh (Silver)

-- --- THÁNG 6/2026 ---
-- Khách 1 (Harry - Hạng 1)
INSERT INTO [dbo].[booking] (booking_id, booking_date, vehicle_id, service_id, promotion_id, status, base_price_at_booking, total_price, license_plate_at_booking, tier_id_at_booking, priority_level) VALUES
(103, '2026-06-05', 1, 1, NULL, 'COMPLETED', 200000.00, 200000.00, '60A-12345', 1, 1);

-- Khách 2 (Bánh Đậu Xanh - Hạng 2)
INSERT INTO [dbo].[booking] (booking_id, booking_date, vehicle_id, service_id, promotion_id, status, base_price_at_booking, total_price, license_plate_at_booking, tier_id_at_booking, priority_level) VALUES
(104, '2026-06-02', 4, 2, NULL, 'COMPLETED', 450000.00, 427500.00, '61A-22222', 2, 2),
(105, '2026-06-10', 4, 2, NULL, 'COMPLETED', 450000.00, 427500.00, '61A-22222', 2, 2),
(106, '2026-06-18', 4, 2, NULL, 'COMPLETED', 450000.00, 427500.00, '61A-22222', 2, 2),
(107, '2026-06-25', 5, 1, NULL, 'COMPLETED', 150000.00, 142500.00, '72B-33333', 2, 2);

-- Khách 3 (Ron Weasley - Hạng 3)
INSERT INTO [dbo].[booking] (booking_id, booking_date, vehicle_id, service_id, promotion_id, status, base_price_at_booking, total_price, license_plate_at_booking, tier_id_at_booking, priority_level) VALUES
(108, '2026-06-01', 7, 2, NULL, 'COMPLETED', 400000.00, 360000.00, '51G-99999', 3, 3),
(109, '2026-06-15', 7, 4, NULL, 'COMPLETED', 1250000.00, 1125000.00, '51G-99999', 3, 3),
(110, '2026-06-20', 7, 3, NULL, 'COMPLETED', 700000.00, 630000.00, '51G-99999', 3, 3);

-- Khách 4 (Hermione - Hạng 4)
INSERT INTO [dbo].[booking] (booking_id, booking_date, vehicle_id, service_id, promotion_id, status, base_price_at_booking, total_price, license_plate_at_booking, tier_id_at_booking, priority_level) VALUES
(111, '2026-06-10', 8, 4, NULL, 'COMPLETED', 1350000.00, 1147500.00, '51K-88888', 4, 4),
(112, '2026-06-24', 8, 4, NULL, 'COMPLETED', 1350000.00, 1147500.00, '51K-88888', 4, 4);

-- ĐƠN KHÁC (Hủy & Chờ Xử Lý)
INSERT INTO [dbo].[booking] (booking_id, booking_date, vehicle_id, service_id, promotion_id, status, base_price_at_booking, total_price, license_plate_at_booking, tier_id_at_booking, priority_level) VALUES
(113, '2026-06-12', 2, 1, NULL, 'CANCELLED', 200000.00, 200000.00, '51F-67890', 1, 1),
(114, '2026-07-01', 1, 1, NULL, 'PENDING', 200000.00, 200000.00, '60A-12345', 1, 1),
(115, '2026-07-02', 4, 3, NULL, 'CONFIRMED', 750000.00, 712500.00, '61A-22222', 2, 2);

SET IDENTITY_INSERT [dbo].[booking] OFF;

-- ============================================================
-- 10. BOOKING SLOT
-- ============================================================
INSERT INTO [dbo].[booking_slot] (booking_id, slot_id) VALUES
(103, 1), (104, 2), (105, 3), (106, 5), (107, 6), (108, 1), (109, 2), (110, 3), (111, 7), (112, 8), (115, 2);

-- ============================================================
-- 11. WASH HISTORY
-- ============================================================
INSERT INTO [dbo].[wash_history] (wash_date, amount_paid, points_earned, points_used, booking_id) VALUES
('2026-05-15 09:00:00', 200000.00, 20, 0, 101),
('2026-05-20 10:15:00', 450000.00, 45, 0, 102),
('2026-06-05 08:45:00', 200000.00, 20, 0, 103),
('2026-06-02 09:40:00', 427500.00, 45, 0, 104),
('2026-06-10 10:50:00', 427500.00, 45, 0, 105),
('2026-06-18 14:30:00', 427500.00, 45, 0, 106),
('2026-06-25 14:55:00', 142500.00, 15, 0, 107),
('2026-06-01 09:00:00', 360000.00, 40, 0, 108),
('2026-06-15 11:20:00', 1125000.00, 125, 0, 109),
('2026-06-20 11:00:00', 630000.00, 70, 0, 110),
('2026-06-10 16:30:00', 1147500.00, 135, 0, 111),
('2026-06-24 17:15:00', 1147500.00, 135, 0, 112);

-- ============================================================
-- 12. LOYALTY POINT
-- ============================================================
INSERT INTO [dbo].[loyalty_point] (created_at, expiry_date, points_change, transaction_type, customer_id, wash_id) VALUES
('2026-06-05 08:45:00', '2026-07-05', 20,  'EARN_BOOKING', 1, 3),
('2026-06-25 14:55:00', '2026-07-25', 150, 'EARN_BOOKING', 2, 7);

-- ============================================================
-- 13. CUSTOMER MONTHLY STATS
-- ============================================================
INSERT INTO [dbo].[customer_monthly_stats] (year_month, monthly_spend, monthly_visits, customer_id) VALUES
('202606', 200000.00, 1, 1),
('202606', 1500000.00, 5, 2),
('202606', 5600000.00, 16, 3),
('202606', 16200000.00, 31, 4);



	-- ============================================================
	-- TABLE OPERATION
	-- ============================================================
	--select * from [dbo].[admin_account]	--data inserted
	--select * from [dbo].[customer]
	--select * from [dbo].[loyalty_tier]	--data inserted
	--select * from [dbo].[wash_service]	--data inserted
	--select * from [dbo].[promotion]		--data inserted
	--select * from [dbo].[time_slot]		--data inserted
	--select * from [dbo].[vehicle]
	--select * from [dbo].[reward_catalog]
	--select * from [dbo].[booking_slot]
	--select * from [dbo].[booking]
	--select * from [dbo].[reward_redemption]
		
	--select * from [dbo].[customer_monthly_stats]
	--select * from [dbo].[loyalty_point]	
	--select * from [dbo].[wash_history]


	-- Ép tất cả về hạng 1 (Bronze) và xóa ngày review cũ để test tính năng up-tier
	--UPDATE [dbo].[customer] 
	--SET tier_id = 1, last_tier_review = NULL;