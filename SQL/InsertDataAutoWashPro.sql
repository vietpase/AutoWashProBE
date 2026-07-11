-- ============================================================
-- 1. LOYALTY TIER 
-- ============================================================
SET IDENTITY_INSERT [dbo].[loyalty_tier] ON;
INSERT INTO [dbo].[loyalty_tier] 
(tier_id, booking_window_days, discount_percent, is_active, min_spending, min_visits, point_multiplier, priority_level, tier_name)
VALUES 
(1, 7,  0,  1, 0.00,        0,   1.0, 1, 'BRONZE'),
(2, 14, 5,  1, 1000000.00,  5,   1.2, 2, 'SILVER'),
(3, 30, 10, 1, 5000000.00,  15,  1.5, 3, 'GOLD'),
(4, 60, 15, 1, 15000000.00, 30,  2.0, 4, 'DIAMOND');
SET IDENTITY_INSERT [dbo].[loyalty_tier] OFF;

-- ============================================================
-- 2. ADMIN ACCOUNT (Mật khẩu mặc định: 123456789)
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
-- 7. CUSTOMER (Mật khẩu mã hóa mặc định: 123456789)
-- ============================================================
SET IDENTITY_INSERT [dbo].[customer] ON;
INSERT INTO [dbo].[customer] (customer_id, create_at, current_points, email, full_name, last_tier_review, password, phone_number, total_spend, total_visits, tier_id) 
VALUES
(1,  '2026-06-22', 10,  'vietpase@gmail.com',       N'Harry Potter',   NULL, '$2a$12$7iQ6mml/x4nuynIzlBcPFeS9uzewtv.rYdwXsf/ZdBfEaR7dwtV3a', '0912345678', 350000.00,   2,  1), 
(2,  '2026-02-15', 150, 'dreamlatern0410@gmail.com', N'Bánh Đậu Xanh',   NULL, '$2a$12$7iQ6mml/x4nuynIzlBcPFeS9uzewtv.rYdwXsf/ZdBfEaR7dwtV3a', '0987654321', 1450000.00,  5,  2), 
(3,  '2026-01-10', 450, 'ron@gmail.com',              N'Ron Weasley',    NULL, '$2a$12$7iQ6mml/x4nuynIzlBcPFeS9uzewtv.rYdwXsf/ZdBfEaR7dwtV3a', '0933333333', 5600000.00,  16, 3), 
(4,  '2026-01-01', 990, 'hermione@gmail.com',         N'Hermione VIP',   NULL, '$2a$12$7iQ6mml/x4nuynIzlBcPFeS9uzewtv.rYdwXsf/ZdBfEaR7dwtV3a', '0944444444', 16200000.00, 31, 4),
-- 10 Khách hàng thêm mới
(5,  '2026-02-01', 50,  'nguyenvanan@gmail.com',   N'Nguyễn Văn An',    NULL, '$2a$12$7iQ6mml/x4nuynIzlBcPFeS9uzewtv.rYdwXsf/ZdBfEaR7dwtV3a', '0901112223', 500000.00,   2,  1),  
(6,  '2026-02-10', 195, 'tranthingocb@gmail.com',  N'Trần Thị Ngọc B',  NULL, '$2a$12$7iQ6mml/x4nuynIzlBcPFeS9uzewtv.rYdwXsf/ZdBfEaR7dwtV3a', '0902223334', 1567500.00,  5,  2),  
(7,  '2026-01-15', 525, 'lehoangcuong@gmail.com',  N'Lê Hoàng Cường',   NULL, '$2a$12$7iQ6mml/x4nuynIzlBcPFeS9uzewtv.rYdwXsf/ZdBfEaR7dwtV3a', '0903334445', 4725000.00,  15, 3),  
(8,  '2026-03-01', 15,  'phamminhdung@gmail.com',  N'Phạm Minh Dũng',   NULL, '$2a$12$7iQ6mml/x4nuynIzlBcPFeS9uzewtv.rYdwXsf/ZdBfEaR7dwtV3a', '0904445556', 150000.00,   1,  1),  
(9,  '2026-02-20', 220, 'hoangphane@gmail.com',    N'Hoàng Phan Em',    NULL, '$2a$12$7iQ6mml/x4nuynIzlBcPFeS9uzewtv.rYdwXsf/ZdBfEaR7dwtV3a', '0905556667', 2100000.00,  6,  2),  
(10, '2026-01-05', 1150,'vuhoangphi@gmail.com',    N'Vũ Hoàng Phi VIP', NULL, '$2a$12$7iQ6mml/x4nuynIzlBcPFeS9uzewtv.rYdwXsf/ZdBfEaR7dwtV3a', '0906667778', 17850000.00, 32, 4),  
(11, '2026-04-10', 35,  'dangthigia@gmail.com',    N'Đặng Thị Gia',     NULL, '$2a$12$7iQ6mml/x4nuynIzlBcPFeS9uzewtv.rYdwXsf/ZdBfEaR7dwtV3a', '0907778889', 350000.00,   1,  1),  
(12, '2026-02-01', 480, 'dovanhung@gmail.com',     N'Đỗ Văn Hùng',      NULL, '$2a$12$7iQ6mml/x4nuynIzlBcPFeS9uzewtv.rYdwXsf/ZdBfEaR7dwtV3a', '0908889990', 5200000.00,  16, 3),  
(13, '2026-03-15', 180, 'buiminhhai@gmail.com',    N'Bùi Minh Hải',     NULL, '$2a$12$7iQ6mml/x4nuynIzlBcPFeS9uzewtv.rYdwXsf/ZdBfEaR7dwtV3a', '0909990001', 1350000.00,  5,  2),  
(14, '2026-05-01', 20,  'ngothanhlong@gmail.com',  N'Ngô Thanh Long',   NULL, '$2a$12$7iQ6mml/x4nuynIzlBcPFeS9uzewtv.rYdwXsf/ZdBfEaR7dwtV3a', '0901231234', 200000.00,   1,  1);  
SET IDENTITY_INSERT [dbo].[customer] OFF;

-- ============================================================
-- 8. VEHICLE 
-- ============================================================
SET IDENTITY_INSERT [dbo].[vehicle] ON;
INSERT INTO [dbo].[vehicle] (vehicle_id, brand, color, is_active, license_plate, vehicle_type, customer_id)
VALUES
(1,  'Toyota Corolla Cross', 'White', 1, '60A-12345', 'medium', 1),
(2,  'Honda HRV',            'Black', 1, '51F-67890', 'medium', 1),
(3,  'VinFast VF9',          'Blue',  1, '50H-11111', 'extra',  1),
(4,  'Hyundai Santafe',      'Red',   1, '61A-22222', 'large',  2),
(5,  'Kia Morning',          'Gray',  1, '72B-33333', 'small',  2),
(6,  'Mazda Cx5',            'White', 0, '43C-44444', 'medium', 2),
(7,  'Mercedes C200',        'Black', 1, '51G-99999', 'medium', 3),
(8,  'Porsche Cayenne',      'Gold',  1, '51K-88888', 'extra',  4),
-- Xe bổ sung cho khách hàng mới
(9,  'Honda Civic',         'Red',   1, '51A-11122', 'medium', 5),
(10, 'Mazda 3',             'White', 1, '60A-55555', 'medium', 6),
(11, 'Ford Ranger',         'Gray',  1, '51C-99988', 'large',  7),
(12, 'Hyundai Accent',      'Silver',1, '61A-77788', 'small',  8),
(13, 'Kia Cerato',          'Black', 1, '72A-44455', 'medium', 9),
(14, 'Mercedes S450',       'Black', 1, '51K-66666', 'medium', 10),
(15, 'VinFast VF8',         'Blue',  1, '50H-88899', 'large',  10),
(16, 'Toyota Vios',         'Brown', 1, '51G-22233', 'small',  11),
(17, 'BMW X5',              'White', 1, '30H-99999', 'large',  12),
(18, 'Mitsubishi Xpander',  'Silver',1, '60A-88888', 'medium', 13),
(19, 'Suzuki Swift',        'Yellow',1, '51E-44433', 'small',  14),
(20, 'Hyundai Tucson',      'Red',   1, '51G-77777', 'medium', 7);
SET IDENTITY_INSERT [dbo].[vehicle] OFF;

-- ============================================================
-- 9. BOOKING 
-- ============================================================
SET IDENTITY_INSERT [dbo].[booking] ON;

-- --- THÁNG 2/2026 ---
INSERT INTO [dbo].[booking] (booking_id, booking_date, created_at, vehicle_id, service_id, promotion_id, status, base_price_at_booking, total_price, license_plate_at_booking, tier_id_at_booking, priority_level) VALUES
(51, '2026-02-02', '2026-02-02 08:15:00', 7, 1, NULL, 'COMPLETED', 200000.00, 180000.00, '51G-99999', 3, 3), 
(52, '2026-02-12', '2026-02-12 16:00:00', 8, 4, NULL, 'COMPLETED', 1350000.00, 1147500.00, '51K-88888', 4, 4), 
(53, '2026-02-18', '2026-02-18 10:30:00', 4, 2, NULL, 'COMPLETED', 450000.00, 427500.00, '61A-22222', 2, 2), 
(54, '2026-02-25', '2026-02-25 13:45:00', 7, 2, NULL, 'COMPLETED', 400000.00, 360000.00, '51G-99999', 3, 3),
(203, '2026-02-15', '2026-02-15 09:30:00', 10, 2, NULL, 'COMPLETED', 350000.00, 332500.00, '60A-55555', 2, 2),
(204, '2026-02-28', '2026-02-28 15:00:00', 10, 1, NULL, 'COMPLETED', 150000.00, 142500.00, '60A-55555', 2, 2),
(208, '2026-02-20', '2026-02-20 09:00:00', 11, 4, NULL, 'COMPLETED', 1200000.00, 1080000.00, '51C-99988', 3, 3),
(214, '2026-02-11', '2026-02-11 16:00:00', 14, 4, NULL, 'COMPLETED', 1200000.00, 1020000.00, '51K-66666', 4, 4);

-- --- THÁNG 3/2026 ---
INSERT INTO [dbo].[booking] (booking_id, booking_date, created_at, vehicle_id, service_id, promotion_id, status, base_price_at_booking, total_price, license_plate_at_booking, tier_id_at_booking, priority_level) VALUES
(55, '2026-03-03', '2026-03-03 09:10:00', 4, 2, NULL, 'COMPLETED', 450000.00, 427500.00, '61A-22222', 2, 2), 
(56, '2026-03-08', '2026-03-08 10:00:00', 7, 3, NULL, 'COMPLETED', 700000.00, 630000.00, '51G-99999', 3, 3), 
(57, '2026-03-12', '2026-03-12 15:30:00', 8, 4, NULL, 'COMPLETED', 1350000.00, 1147500.00, '51K-88888', 4, 4), 
(58, '2026-03-20', '2026-03-20 08:05:00', 5, 1, NULL, 'COMPLETED', 150000.00, 142500.00, '72B-33333', 2, 2), 
(59, '2026-03-24', '2026-03-24 14:15:00', 7, 2, NULL, 'COMPLETED', 400000.00, 360000.00, '51G-99999', 3, 3),
(201, '2026-03-10', '2026-03-10 08:00:00', 9,  1, NULL, 'COMPLETED', 150000.00, 150000.00, '51A-11122', 1, 1),
(205, '2026-03-12', '2026-03-12 10:00:00', 10, 2, NULL, 'COMPLETED', 350000.00, 332500.00, '60A-55555', 2, 2),
(206, '2026-03-25', '2026-03-25 11:00:00', 10, 3, NULL, 'COMPLETED', 650000.00, 617500.00, '60A-55555', 2, 2),
(209, '2026-03-15', '2026-03-15 14:15:00', 20, 3, NULL, 'COMPLETED', 650000.00,   585000.00, '51G-77777', 3, 3),
(211, '2026-03-15', '2026-03-15 08:45:00', 12, 1, NULL, 'COMPLETED', 150000.00, 150000.00, '61A-77788', 1, 1),
(212, '2026-03-02', '2026-03-02 13:00:00', 13, 2, NULL, 'COMPLETED', 350000.00, 332500.00, '72A-44455', 2, 2),
(215, '2026-03-20', '2026-03-20 10:30:00', 15, 4, NULL, 'COMPLETED', 1200000.00, 1020000.00, '50H-88899', 4, 4),
(219, '2026-03-05', '2026-03-05 08:15:00', 17, 4, NULL, 'COMPLETED', 1200000.00, 1080000.00, '30H-99999', 3, 3);

-- --- THÁNG 4/2026 ---
INSERT INTO [dbo].[booking] (booking_id, booking_date, created_at, vehicle_id, service_id, promotion_id, status, base_price_at_booking, total_price, license_plate_at_booking, tier_id_at_booking, priority_level) VALUES
(60, '2026-04-02', '2026-04-02 13:00:00', 4, 2, NULL, 'COMPLETED', 450000.00, 427500.00, '61A-22222', 2, 2), 
(61, '2026-04-10', '2026-04-10 09:45:00', 7, 4, NULL, 'COMPLETED', 1250000.00, 1125000.00, '51G-99999', 3, 3), 
(62, '2026-04-15', '2026-04-15 16:15:00', 8, 4, NULL, 'COMPLETED', 1350000.00, 1147500.00, '51K-88888', 4, 4), 
(63, '2026-04-22', '2026-04-22 10:20:00', 5, 1, NULL, 'COMPLETED', 150000.00, 142500.00, '72B-33333', 2, 2), 
(64, '2026-04-28', '2026-04-28 15:00:00', 7, 3, NULL, 'COMPLETED', 700000.00, 630000.00, '51G-99999', 3, 3),
(210, '2026-04-18', '2026-04-18 10:00:00', 11, 4, NULL, 'COMPLETED', 1200000.00, 1080000.00, '51C-99988', 3, 3),
(213, '2026-04-10', '2026-04-10 15:30:00', 13, 2, NULL, 'COMPLETED', 350000.00, 332500.00, '72A-44455', 2, 2),
(216, '2026-04-25', '2026-04-25 11:15:00', 14, 4, NULL, 'COMPLETED', 1200000.00, 1020000.00, '51K-66666', 4, 4),
(218, '2026-04-20', '2026-04-20 14:20:00', 16, 2, NULL, 'COMPLETED', 350000.00, 350000.00, '51G-22233', 1, 1),
(220, '2026-04-12', '2026-04-12 13:45:00', 17, 3, NULL, 'COMPLETED', 650000.00,   585000.00, '30H-99999', 3, 3),
(221, '2026-04-05', '2026-04-05 10:00:00', 18, 2, NULL, 'COMPLETED', 350000.00, 332500.00, '60A-88888', 2, 2);

-- --- THÁNG 5/2026 ---
INSERT INTO [dbo].[booking] (booking_id, booking_date, created_at, vehicle_id, service_id, promotion_id, status, base_price_at_booking, total_price, license_plate_at_booking, tier_id_at_booking, priority_level) VALUES
(101, '2026-05-15', '2026-05-15 08:30:00', 1, 1, NULL, 'COMPLETED', 200000.00, 200000.00, '60A-12345', 1, 1), 
(102, '2026-05-20', '2026-05-20 10:15:23', 4, 2, NULL, 'COMPLETED', 450000.00, 450000.00, '61A-22222', 2, 2),
(222, '2026-05-18', '2026-05-18 15:30:00', 19, 1, NULL, 'COMPLETED', 150000.00, 150000.00, '51E-44433', 1, 1);

-- --- THÁNG 6/2026 ---
INSERT INTO [dbo].[booking] (booking_id, booking_date, created_at, vehicle_id, service_id, promotion_id, status, base_price_at_booking, total_price, license_plate_at_booking, tier_id_at_booking, priority_level) VALUES
(103, '2026-06-05', '2026-06-05 07:45:12', 1, 1, NULL, 'COMPLETED', 200000.00, 200000.00, '60A-12345', 1, 1),
(104, '2026-06-02', '2026-06-02 09:00:00', 4, 2, NULL, 'COMPLETED', 450000.00, 427500.00, '61A-22222', 2, 2),
(105, '2026-06-10', '2026-06-10 14:20:55', 4, 2, NULL, 'COMPLETED', 450000.00, 427500.00, '61A-22222', 2, 2),
(106, '2026-06-18', '2026-06-18 11:30:41', 4, 2, NULL, 'COMPLETED', 450000.00, 427500.00, '61A-22222', 2, 2),
(107, '2026-06-25', '2026-06-25 16:05:10', 5, 1, NULL, 'COMPLETED', 150000.00, 142500.00, '72B-33333', 2, 2),
(108, '2026-06-01', '2026-06-01 08:15:00', 7, 2, NULL, 'COMPLETED', 400000.00, 360000.00, '51G-99999', 3, 3),
(109, '2026-06-15', '2026-06-15 13:45:22', 7, 4, NULL, 'COMPLETED', 1250000.00, 1125000.00, '51G-99999', 3, 3),
(110, '2026-06-20', '2026-06-20 10:00:00', 7, 3, NULL, 'COMPLETED', 700000.00, 630000.00, '51G-99999', 3, 3),
(111, '2026-06-10', '2026-06-10 09:12:30', 8, 4, NULL, 'COMPLETED', 1350000.00, 1147500.00, '51K-88888', 4, 4),
(112, '2026-06-24', '2026-06-24 15:50:00', 8, 4, NULL, 'COMPLETED', 1350000.00, 1147500.00, '51K-88888', 4, 4),
(113, '2026-06-12', '2026-06-12 11:22:14', 2, 1, NULL, 'CANCELLED', 200000.00, 200000.00, '51F-67890', 1, 1),
(202, '2026-06-12', '2026-06-12 14:00:00', 9,  2, NULL, 'COMPLETED', 350000.00, 350000.00, '51A-11122', 1, 1),
(207, '2026-06-05', '2026-06-05 08:30:00', 10, 1, NULL, 'COMPLETED', 150000.00, 142500.00, '60A-55555', 2, 2),
(217, '2026-06-14', '2026-06-14 09:00:00', 15, 4, NULL, 'COMPLETED', 1200000.00, 1020000.00, '50H-88899', 4, 4);

-- --- THÁNG 7/2026 ---
INSERT INTO [dbo].[booking] (booking_id, booking_date, created_at, vehicle_id, service_id, promotion_id, status, base_price_at_booking, total_price, license_plate_at_booking, tier_id_at_booking, priority_level) VALUES
(114, '2026-07-01', '2026-07-01 07:30:00', 1, 1, NULL, 'PENDING', 200000.00, 200000.00, '60A-12345', 1, 1),
(115, '2026-07-02', '2026-07-02 14:05:19', 4, 3, NULL, 'CONFIRMED', 750000.00, 712500.00, '61A-22222', 2, 2);

SET IDENTITY_INSERT [dbo].[booking] OFF;

-- ============================================================
-- 10. BOOKING SLOT
-- ============================================================
INSERT INTO [dbo].[booking_slot] (booking_id, slot_id) VALUES
(51, 1), (52, 8), (53, 3), (54, 5),
(55, 2), (56, 3), (57, 7), (58, 1), (59, 6),
(60, 5), (61, 2), (62, 8), (63, 3), (64, 7),
(103, 1), (104, 2), (105, 3), (106, 5), (107, 6), (108, 1), (109, 2), (110, 3), (111, 7), (112, 8), (115, 2),
(201, 1), (202, 6), (203, 2), (204, 7), (205, 2), (206, 3), (207, 1), 
(208, 2), (209, 5), (210, 2), (211, 1), (212, 4), (213, 7), (214, 8), 
(215, 3), (216, 3), (217, 1), (218, 6), (219, 1), (220, 5), (221, 2), (222, 7);

-- ============================================================
-- 11. WASH HISTORY
-- ============================================================
SET IDENTITY_INSERT [dbo].[wash_history] ON;

-- --- THÁNG 2, 3, 4 ---
INSERT INTO [dbo].[wash_history] (wash_id, wash_date, amount_paid, points_earned, points_used, booking_id) VALUES
(51, '2026-02-02 09:00:00', 180000.00, 20, 0, 51),
(52, '2026-02-12 17:00:00', 1147500.00, 135, 0, 52),
(53, '2026-02-18 11:30:00', 427500.00, 45, 0, 53),
(54, '2026-02-25 14:45:00', 360000.00, 40, 0, 54),
(55, '2026-03-03 10:15:00', 427500.00, 45, 0, 55),
(56, '2026-03-08 11:15:00', 630000.00, 70, 0, 56),
(57, '2026-03-12 16:45:00', 1147500.00, 135, 0, 57),
(58, '2026-03-20 09:00:00', 142500.00, 15, 0, 58),
(59, '2026-03-24 15:15:00', 360000.00, 40, 0, 59),
(60, '2026-04-02 14:00:00', 427500.00, 45, 0, 60),
(61, '2026-04-10 11:00:00', 1125000.00, 125, 0, 61),
(62, '2026-04-15 17:30:00', 1147500.00, 135, 0, 62),
(63, '2026-04-22 11:15:00', 142500.00, 15, 0, 63),
(64, '2026-04-28 16:00:00', 630000.00, 70, 0, 64);

-- --- THÁNG 5, 6 (Định danh cố định ID từ 1-12) ---
INSERT INTO [dbo].[wash_history] (wash_id, wash_date, amount_paid, points_earned, points_used, booking_id) VALUES
(1,  '2026-05-15 09:00:00', 200000.00, 20, 0, 101),
(2,  '2026-05-20 10:15:00', 450000.00, 45, 0, 102),
(3,  '2026-06-05 08:45:00', 200000.00, 20, 0, 103), 
(4,  '2026-06-02 09:40:00', 427500.00, 45, 0, 104),
(5,  '2026-06-10 10:50:00', 427500.00, 45, 0, 105),
(6,  '2026-06-18 14:30:00', 427500.00, 45, 0, 106),
(7,  '2026-06-25 14:55:00', 142500.00, 15, 0, 107), 
(8,  '2026-06-01 09:00:00', 360000.00, 40, 0, 108),
(9,  '2026-06-15 11:20:00', 1125000.00, 125, 0, 109),
(10, '2026-06-20 11:00:00', 630000.00, 70, 0, 110),
(11, '2026-06-10 16:30:00', 1147500.00, 135, 0, 111),
(12, '2026-06-24 17:15:00', 1147500.00, 135, 0, 112);

-- --- LỊCH SỬ RỬA XE CỦA 10 KHÁCH HÀNG MỚI (Dùng dãy ID riêng 201->222) ---
INSERT INTO [dbo].[wash_history] (wash_id, wash_date, amount_paid, points_earned, points_used, booking_id) VALUES
(201, '2026-03-10 08:45:00', 150000.00, 15, 0, 201),
(202, '2026-06-12 14:45:00', 350000.00, 35, 0, 202),
(203, '2026-02-15 10:45:00', 332500.00, 35, 0, 203),
(204, '2026-02-28 15:45:00', 142500.00, 15, 0, 204),
(205, '2026-03-12 11:15:00', 332500.00, 35, 0, 205),
(206, '2026-03-25 12:30:00', 617500.00, 65, 0, 206),
(207, '2026-06-05 09:15:00', 142500.00, 15, 0, 207),
(208, '2026-02-20 12:00:00', 1080000.00, 120,0, 208),
(209, '2026-03-15 15:45:00', 585000.00,  65, 0, 209),
(210, '2026-04-18 13:00:00', 1080000.00, 120,0, 210),
(211, '2026-03-15 09:30:00', 150000.00, 15, 0, 211),
(212, '2026-03-02 14:15:00', 332500.00, 35, 0, 212),
(213, '2026-04-10 16:45:00', 332500.00, 35, 0, 213),
(214, '2026-02-11 19:00:00', 1020000.00, 120,0, 214),
(215, '2026-03-20 13:30:00', 1020000.00, 120,0, 215),
(216, '2026-04-25 14:15:00', 1020000.00, 120,0, 216),
(217, '2026-06-14 12:00:00', 1020000.00, 120,0, 217),
(218, '2026-04-20 15:35:00', 350000.00, 35, 0, 218),
(219, '2026-03-05 11:15:00', 1080000.00, 120,0, 219),
(220, '2026-04-12 15:15:00', 585000.00,  65, 0, 220),
(221, '2026-04-05 11:15:00', 332500.00, 35, 0, 221),
(222, '2026-05-18 16:15:00', 150000.00, 15, 0, 222);

SET IDENTITY_INSERT [dbo].[wash_history] OFF;

-- ============================================================
-- 12. LOYALTY POINT
-- ============================================================
INSERT INTO [dbo].[loyalty_point] (created_at, expiry_date, points_change, transaction_type, customer_id, wash_id) VALUES
('2026-06-05 08:45:00', '2026-07-05', 20,  'EARN_BOOKING', 1, 3),
('2026-06-25 14:55:00', '2026-07-25', 150, 'EARN_BOOKING', 2, 7),
-- Giao dịch tích điểm tiêu biểu của khách hàng mới
('2026-03-10 08:45:00', '2026-04-10', 15,  'EARN_BOOKING', 5,  201),
('2026-02-15 10:45:00', '2026-03-15', 35,  'EARN_BOOKING', 6,  203),
('2026-02-20 12:00:00', '2026-03-20', 120, 'EARN_BOOKING', 7,  208),
('2026-02-11 19:00:00', '2026-03-11', 120, 'EARN_BOOKING', 10, 214);

-- ============================================================
-- 13. CUSTOMER MONTHLY STATS
-- ============================================================
INSERT INTO [dbo].[customer_monthly_stats] (year_month, monthly_spend, monthly_visits, customer_id) VALUES
-- --- THÁNG 2/2026 ---
('202602', 427500.00,  1, 2),   
('202602', 540000.00,  2, 3),   
('202602', 1147500.00, 1, 4),  
('202602', 475000.00,  2, 6),   
('202602', 1080000.00, 1, 7),   
('202602', 1020000.00, 1, 10),  

-- --- THÁNG 3/2026 ---
('202603', 570000.00,  2, 2),   
('202603', 990000.00,  2, 3),   
('202603', 1147500.00, 1, 4),  
('202603', 150000.00,  1, 5),   
('202603', 950000.00,  2, 6),   
('202603', 585000.00,  1, 7),   
('202603', 150000.00,  1, 8),   
('202603', 332500.00,  1, 9),   
('202603', 1020000.00, 1, 10),  
('202603', 1080000.00, 1, 12),  

-- --- THÁNG 4/2026 ---
('202604', 570000.00,  2, 2),   
('202604', 1755000.00, 2, 3),  
('202604', 1147500.00, 1, 4),  
('202604', 1080000.00, 1, 7),   
('202604', 332500.00,  1, 9),   
('202604', 1020000.00, 1, 10),  
('202604', 350000.00,  1, 11),  
('202604', 585000.00,  1, 12),  
('202604', 332500.00,  1, 13),  

-- --- THÁNG 5/2026 ---
('202605', 150000.00,  1, 14),  

-- --- THÁNG 6/2026 ---
('202606', 200000.00,  1, 1),
('202606', 1500000.00, 5, 2),
('202606', 5600000.00, 16, 3),
('202606', 16200000.00, 31, 4),
('202606', 350000.00,  1, 5),   
('202606', 142500.00,  1, 6),   
('202606', 1020000.00, 1, 10);


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


	-- ============================================================
	-- TABLE OPERATIONS
	-- ============================================================
	-- Lấy danh sách staff accounts
	--SELECT * 
	--FROM [dbo].[admin_account] a
	--WHERE a.role NOT LIKE '%MANAGER%';

	-- Lấy lịch sử Booking theo id Customer
	--select
	--	p.created_at,
	--	p.points_change,
	--	r.reward_name_at_redemption
	--from [dbo].[customer] c
	--join [dbo].[loyalty_point] p on c.customer_id = p.customer_id
	--join [dbo].[reward_redemption] r on c.customer_id = r.customer_id


	--select count(*) as numberOfBooking from [dbo].[booking]
	--select sum(b.total_price) as totalPrice from [dbo].[booking] b
	--select count(*) as oldCustomer from [dbo].[customer] c where c.total_visits >2
	--select count(*) as newCustomer from [dbo].[customer] c where c.total_visits =1
	--select count(*) as activePromotion from [dbo].[promotion] p where p.is_active = 'true' 

	--select [monthly_spend],[year_month] from [dbo].[customer_monthly_stats]
	--------------------------------------------------------------
	----ServiceName - NumberOfCustomer
	--------------------------------------------------------------
	--SELECT 
	--	w.service_name AS serviceName, 
	--	COUNT(*) AS number
	--FROM [dbo].[booking] b
	--JOIN [dbo].[wash_service] w ON b.service_id = w.service_id
	--GROUP BY w.service_name;
	--------------------------------------------------------------
	----TierName - NumberOfCustomer
	--------------------------------------------------------------
	--select l.tier_name as tierName, 
	--count(*) as number
	--from [dbo].[customer] c
	--join [dbo].[loyalty_tier] l on c.tier_id = l.tier_id
	--group by l.tier_name

	-- ============================================================
	-- DATABASE
	-- ============================================================
	--create database AutoWashPro


	-- ============================================================
	-- AUTO UPDATE LOYALTYTIER
	-- ============================================================
	-- Ép tất cả về hạng 1 (Bronze) và xóa ngày review cũ để test tính năng up-tier
	--UPDATE [dbo].[customer] 
	--SET tier_id = 1, last_tier_review = NULL;


	--delete from [dbo].[promotion] where promo_id = 5
