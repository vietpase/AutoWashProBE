	-- ============================================================
	-- SAMPLE DATA (Dữ liệu mẫu)
	-- ============================================================

	-- 1. Tiers
	INSERT INTO [dbo].[loyalty_tier] 
	(booking_window_days, discount_percent, is_active, min_spending, min_visits, point_multiplier, priority_level, tier_name)
	VALUES 
	(7,  0,  1, 0.00,        0,  1.0, 1, 'BRONZE'),
	(14, 5,  1, 1000000.00,  5,  1.2, 2, 'SILVER'),
	(30, 10, 1, 5000000.00,  15, 1.5, 3, 'GOLD'),
	(60, 15, 1, 15000000.00, 30, 2.0, 4, 'DIAMOND');


	-- 2. Admin
	INSERT INTO [dbo].[admin_account] (full_name, password, role,username)
	VALUES 
	(N'Quản trị viên chính', '$2a$12$7iQ6mml/x4nuynIzlBcPFeS9uzewtv.rYdwXsf/ZdBfEaR7dwtV3a', 'MANAGER', 'admin'),
	(N'Nhân viên CSKH', '$2a$12$7iQ6mml/x4nuynIzlBcPFeS9uzewtv.rYdwXsf/ZdBfEaR7dwtV3a', 'STAFF', 'staff1');

	--loginKey: admin 
	--password: 123456789
	--loginKey: staff1
	--password: 123456789

	--3. WashService
	INSERT INTO wash_service (description, duration_minutes, is_active, price, service_name) 
	VALUES 
	(
		N'Bao gồm rửa vỏ chống xước, hút bụi nội thất căn bản, lau kính và xịt bóng dưỡng lốp xe.', 
		45, 
		1, 
		150000.00, 
		N'Rửa Xe Tiêu Chuẩn (Standard Wash)'
	),
	(
		N'Rửa chi tiết sâu từng khe kẽ, tẩy nhựa đường/bụi sắt, phủ lớp wax bóng bảo vệ bề mặt sơn, khử mùi ozone nội thất.', 
		75, 
		1, 
		350000.00, 
		N'Rửa Xe Cao Cấp & Phủ Wax'
	),
	(
		N'Dọn dẹp bụi bẩn và dầu mỡ bám trong khoang động cơ bằng công nghệ hơi nước nóng, dưỡng phục hồi chi tiết nhựa, cao su bảo vệ hệ thống điện.', 
		90, 
		1, 
		650000.00, 
		N'Vệ Sinh Khoang Máy Bằng Hơi Nước Nóng'
	),
	(
		N'Tháo ghế, giặt sạch trần, sàn, toàn bộ ghế da/nỉ bằng máy phun hút áp lực. Dưỡng bề mặt taplo, tapi và diệt khuẩn toàn bộ xe.', 
		180, 
		1, 
		1200000.00, 
		N'Combo Vệ Sinh Nội Thất Toàn Diện'
	),
	(
		N'Tẩy sạch các vết ố mốc, váng mưa trên kính lái và kính sườn, vệ sinh luồng gió điều hòa bằng dung dịch chuyên dụng.', 
		30, 
		1, 
		250000.00, 
		N'Tẩy Ố Kính & Sát Khuẩn Điều Hòa'
	);

	--4. Promotion
	INSERT INTO promotion (description, discount_amount, end_date, is_active, promo_name, start_date, min_tier_id)
	VALUES 
	(
		N'Giảm ngay 20,000đ cho tất cả khách hàng mới đăng ký trải nghiệm dịch vụ lần đầu.', 
		20000.00, 
		'2026-12-31', 
		1, 
		N'WELCOME20', 
		'2026-06-01 ', 
		1
	),
	(
		N'Ưu đãi giải nhiệt mùa hè, giảm thẳng 30,000đ áp dụng cho mọi gói dịch vụ làm sạch xe.', 
		30000.00, 
		'2026-07-31', 
		1, 
		N'SUMMER30', 
		'2026-06-01', 
		1
	),
	(
		N'Khuyến mãi đặc quyền dành riêng cho thành viên hạng Bạc trở lên, giảm ngay 50,000đ.', 
		50000.00, 
		'2026-09-30 ', 
		1, 
		N'SILVERPLUS', 
		'2026-06-15 ', 
		2
	),
	(
		N'Tri ân khách hàng VIP hạng Vàng, giảm bùng nổ 80,000đ cho các combo vệ sinh chuyên sâu.', 
		80000.00, 
		'2026-08-31 ', 
		1, 
		N'GOLDBOSS', 
		'2026-06-15', 
		3
	);
	--TimeSlot
	INSERT INTO time_slot (end_time, is_active, max_capacity, slot_name, start_time)
	VALUES 
	('09:00:00', 1, 4, N'Ca Sáng 1', '08:00:00'),
	('10:00:00', 1, 4, N'Ca Sáng 2', '09:00:00'),
	('11:00:00', 1, 4, N'Ca Sáng 3', '10:00:00'),
	('12:00:00', 1, 2, N'Ca Trưa Cao Điểm', '11:00:00'),
	('14:00:00', 1, 4, N'Ca Chiều 1', '13:00:00'),
	('15:00:00', 1, 4, N'Ca Chiều 2', '14:00:00'),
	('16:00:00', 1, 6, N'Ca Chiều Cao Điểm 1', '15:00:00'),
	('17:00:00', 1, 6, N'Ca Chiều Cao Điểm 2', '16:00:00'),
	('18:00:00', 1, 3, N'Ca Tối Muộn', '17:00:00');
	--RewardCatalog
	INSERT INTO [dbo].[reward_catalog](description, discount_amount, is_active, points_required, reward_name, stock_quantity)
	VALUES 
	(N'Áp dụng trực tiếp vào hóa đơn cho mọi dịch vụ rửa xe/chăm sóc xe.', 50000.00, 1, 50, N'Voucher giảm giá 50.000đ', 9),
	(N'Áp dụng trực tiếp vào hóa đơn cho mọi dịch vụ rửa xe/chăm sóc xe.', 100000.00, 1, 90, N'Voucher giảm giá 100.000đ', 9),
	(N'Nhận 1 chiếc khăn lau xe siêu thấm hút, không xước sơn tại quầy.', 0.00, 1, 30, N'Khăn lau xe Microfiber cao cấp', 5),
	(N'Nhận 1 lọ tinh dầu thiên nhiên cao cấp treo xe giúp khử mùi, chống say xe.', 0.00, 1, 60, N'Tinh dầu treo xe hương sả chanh', 10),
	(N'Voucher miễn phí 100% gói rửa vỏ và hút bụi nhanh trị giá 120k.', 120000.00, 1, 100, N'Miễn phí Rửa xe bọt tuyết cơ bản', 9),
	(N'Voucher miễn phí gói rửa xe cao cấp, tẩy ố lazang và quét bóng lốp trị giá 200k.', 200000.00, 1, 180, N'Miễn phí Rửa xe chuyên sâu & Dưỡng lốp', 9),
	(N'Giảm ngay 300.000đ khi sử dụng dịch vụ dọn dẹp nội thất chuyên sâu.', 300000.00, 1, 250, N'Voucher 300k Vệ sinh nội thất', 2),
	(N'Voucher giảm 100% dịch vụ phủ nano hiệu ứng lá sen chống bám nước kính lái trị giá 500k.', 500000.00, 1, 450, N'Combo Phủ Ceramic kính lái', 9);
		
	--Customer
	INSERT INTO	[dbo].[customer](
		create_at, 
		current_points, 
		email, 
		full_name, 
		last_tier_review, 
		password, 
		phone_number, 
		total_spend, 
		total_visits, 
		tier_id
	) VALUES
	(
		'2026-06-22', NULL, 'vietpase@gmail.com', N'Harry Potter', 
		NULL, '$2a$12$CghKjljciIK2y.ZU4RdKOu2Ck0oyE6dcV23uALEcrg7I41DVhw04u', 
		'0912345678', NULL, NULL, 1
	),
	(
		'2026-02-15', NULL, 'dreamlatern0410@gmail.com', N'Bánh Đậu Xanh', 
		NULL, '$2a$12$PSfDb1TUcqTzq5wF/SDgqefRtg8ayd1lpfWqx8jXXVB8itqk8Vw/y', 
		'0987654321', NULL, NULL, 1
	);
	--Vehicle
	INSERT INTO vehicle
	(brand, color, is_active, license_plate, vehicle_type, customer_id)
	VALUES
	('Toyota Corolla Cross',  'White', 1, '60A-12345', 'Sedan',     1),
	('Honda HRV',   'Black', 1, '51F-67890', 'SUV',       1),
	('VinFast VF9', 'Blue',  1, '50H-11111', 'Electric',  1),

	('Hyundai Santafe', 'Red',   1, '61A-22222', 'Hatchback', 2),
	('Kia Morning',     'Gray',  1, '72B-33333', 'SUV',       2),
	('Mazda Cx5',   'White', 0, '43C-44444', 'Sedan',     2);



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
	-- CREATE DATABASE
	-- ============================================================
	--create database AutoWashPro
	-- ============================================================
	-- DELETE DATABASE
	-- ============================================================
	--delete database AutoWashPro

	-- ============================================================
	-- DELETE OPERATION
	-- ============================================================
	--DELETE FROM [dbo].[customer]WHERE customer_id IN (3,4,5,6);
	--DELETE FROM [dbo].[vehicle]WHERE vehicle_id IN (8);
	--DELETE FROM [dbo].[promotion] WHERE [promo_id] IN (5);
	
	-- ============================================================
	-- UPDATE OPERATION
	-- ============================================================
	
	--vehicle
	--UPDATE [dbo].[vehicle]
	--SET brand = 'Toyota',
	--	color = 'Black',
	--	is_active = 1,
	--	license_plate = '60A-99999',
	--	vehicle_type = 'SUV'
	--WHERE vehicle_id = 1;


	--UPDATE [dbo].[admin_account] 
	--SET full_name = N'Quản trị viên chính' 
	--WHERE admin_id = 15;	



