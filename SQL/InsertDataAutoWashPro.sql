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

		






	-- 3. Perks
	--INSERT INTO [dbo].[perk] (perk_name, discount_percent, free_service, add_on_item)
	--VALUES 
	--(N'Giảm giá 5%', 5, NULL, NULL),
	--(N'Sáp miễn phí', 0, N'Sáp bóng', NULL),
	--(N'Tặng vệ sinh nội thất', 0, NULL, N'Vệ sinh nội thất');
	
	-- Gán perks cho tier
	-- Silver: Giảm 5%
	--INSERT INTO [dbo].[tier_perk] (tier_id, perk_id) VALUES (2, 1);
	-- Gold: Giảm 5% + Sáp miễn phí
	--INSERT INTO  [dbo].[tier_perk](tier_id, perk_id) VALUES (3, 1), (3, 2);
	-- Platinum: tất cả perks
	--INSERT INTO [dbo].[tier_perk](tier_id, perk_id) VALUES (4, 1), (4, 2), (4, 3);

	-- 4. Khách hàng
	--INSERT INTO [dbo].[customer](full_name, phone_number, email, tier_id, current_points)
	--VALUES 
	--(N'Nguyễn Văn A', '0901234567', 'vana@gmail.com', 1, 100),
	--(N'Trần Thị B',   '0912345678', 'thib@gmail.com', 2, 500);

	-- 5. Phương tiện
	--INSERT INTO [dbo].[vehicle] (customer_id, license_plate, vehicle_type, brand, color)
	--VALUES 
	--(1, '59A-12345', 'Motorbike', 'Honda', 'Black'),
	--(2, '61B-67890', 'Motorbike', 'Yamaha', 'Red');

	-- 6. Đặt lịch
	--INSERT INTO  [dbo].[booking](vehicle_id, booking_date, booking_time, service_type, status, priority_level, tier_id_at_booking)
	--VALUES 
	--(1, '2026-05-20', '09:00', N'Rửa cao cấp', 'Pending', 1, 1),
	--(2, '2026-05-20', '10:00', N'Rửa toàn diện', 'Confirmed', 2, 2);

	-- 7. Lịch sử rửa
	--INSERT INTO [dbo].[wash_history](booking_id, amount_paid, points_earned, points_used, perk_applied)
	--VALUES 
	--(1, 120000, 12, 0, N'Không'),
	--(2, 200000, 20, 50, N'Giảm 5% (Silver)');

	-- 8. Giao dịch điểm
	--INSERT INTO [dbo].[loyalty_point] (customer_id, wash_id, points_change, transaction_type, expiry_date)
	--VALUES 
	--(1, 1,  12, 'Earn', '2027-05-20'),
	--(2, 2, -50, 'Redeem', NULL);

	-- 9. Khuyến mãi (admin tạo)
	--INSERT INTO [dbo].[promotion] (title, description, min_tier_id, discount_percent, start_date, end_date, status, created_by_admin_id)
	--VALUES (N'Ưu đãi tháng 5', N'Giảm 10% cho khách Silver trở lên', 2, 10, '2026-05-01', '2026-05-31', 'Active', 1);

	-- 10. Danh mục đổi điểm (admin tạo)
	--INSERT INTO [dbo].[reward_catalog](reward_name, points_required, discount_amount, free_wash, created_by_admin_id)
	--VALUES 
	--(N'Giảm 10.000đ', 100, 10000, 0, 1),
	--(N'Rửa xe miễn phí', 300, 0, 1, 1);

	-- 11. Lịch sử đổi điểm
	--INSERT INTO [dbo].[reward_redemption](customer_id, reward_id, points_used)
	--VALUES (2, 1, 100);



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



	--select * from [dbo].[booking]
	
	--select * from [dbo].[customer_monthly_stats]
	--select * from [dbo].[loyalty_point]
	 -- data inserted
	--select * from [dbo].[reward_catalog]
	--select * from [dbo].[reward_redemption]
	
	--select * from [dbo].[wash_history]
	
	-- ============================================================
	-- CREATE DATABASE
	-- ============================================================
	--create database AutoWashPro

	-- ============================================================
	-- DELETE OPERATION
	-- ============================================================
	--DELETE FROM [dbo].[customer]
	--WHERE customer_id IN (3,4,5,6);
	--DELETE FROM [dbo].[vehicle]
	--WHERE vehicle_id IN (1,4,5,6);
	
	-- ============================================================
	-- UPDATE OPERATION
	-- ============================================================
	--UPDATE [dbo].[admin_account] 
	--SET full_name = N'Quản trị viên chính' 
	--WHERE admin_id = 15;	





--{
  --"loginKey": "0123456789",
  --"password": "harrypotter"
--}
--{
  --"loginKey": "0123456888",
  --"password": "tuan9"
--}
--{
  --"phoneNumber": "0123456999",
  --"password": "levana123",
--}

--INSERT INTO [dbo].[admin_account] (full_name, password, role, username)
--VALUES 
--(
  --N'Quản trị viên chính', 
    --'$2a$12$d6cQeET10X95jiZAPxSFVOY/bQrBYQSfysPwshECiwBfWRufTMQfq', -- Mật khẩu gốc là 123456
    --'Manager', 
    --'admin'
--),
--(
    --N'Nhân viên CSKH', 
    --'$2a$12$gm0hSj8wYkeeWto.heqOu.Rdsyippz2Vncc0vG67YKe2FIRxpsO0m', -- Mật khẩu gốc là 123456
    --'Staff', 
    --'staff1'
--);




--username: 0123456999
--password: tester123


--create database AutoWashPro