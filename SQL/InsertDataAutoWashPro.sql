	-- ============================================================
	-- SAMPLE DATA (Dữ liệu mẫu)
	-- ============================================================

	-- 1. Admin
	--INSERT INTO [dbo].[admin_account] (full_name, password, role,username)
	--VALUES 
	--(N'Quản trị viên chính', '123456', 'Manager', 'admin'),
	--(N'Nhân viên CSKH', '123456', 'Staff', 'staff1');

	-- 2. Tiers
	--INSERT INTO [dbo].[loyalty_tier](tier_name, min_spending, min_visits, booking_window_days, point_multiplier, priority_level)
	--VALUES 
	--('Member',   0,          0,  7,  1.0, 1),
	--('Silver',   2000000,   10, 10, 1.2, 2),
	--('Gold',     5000000,   20, 12, 1.5, 3),
	--('Platinum', 10000000,  40, 14, 2.0, 4);

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
	-- SAMPLE DATA (Dữ liệu mẫu)
	-- ============================================================
	--select * from [dbo].[admin_account] --data inserted
	--select * from [dbo].[booking]
	--select * from [dbo].[customer]
	--select * from [dbo].[customer_monthly_stats]
	--select * from [dbo].[loyalty_point]
	--select * from [dbo].[loyalty_tier] -- data inserted
	--select * from [dbo].[perk] --error data
	--select * from [dbo].[promotion]
	--select * from [dbo].[reward_catalog]
	--select * from [dbo].[reward_redemption]
	--select * from [dbo].[tier_perk] -- error data
	--select * from [dbo].[vehicle]
	--select * from [dbo].[wash_history]

	--create database AutoWashPro

	--DELETE FROM [dbo].[customer]
	--WHERE customer_id IN (3,4,5,6);
	--DELETE FROM [dbo].[vehicle]
	--WHERE vehicle_id IN (1,4,5,6);
	

		

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

--UPDATE [dbo].[admin_account] 
--SET full_name = N'Quản trị viên chính' 
--WHERE admin_id = 15;


--username: 0123456999
--password: tester123