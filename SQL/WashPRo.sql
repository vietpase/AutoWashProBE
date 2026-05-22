-- ============================================================
-- DATABASE: AutoWashPro
-- HỆ THỐNG QUẢN LÝ RỬA XE TỰ ĐỘNG THÔNG MINH
-- TÍCH HỢP LOYALTY, ĐẶT LỊCH, PERK, KHUYẾN MÃI, AUDIT ADMIN
-- ============================================================
CREATE DATABASE AutoWashProSQL;
GO
USE AutoWashProSQL;
GO

-- =========================
-- 1. LOYALTY TIER (Hạng thành viên)
-- =========================
CREATE TABLE LoyaltyTier (
    tier_id INT PRIMARY KEY IDENTITY(1,1),
    tier_name NVARCHAR(50) NOT NULL,           -- Member, Silver, Gold, Platinum
    min_spending DECIMAL(18,2) DEFAULT 0,       -- Tổng chi tiêu tối thiểu (12 tháng)
    min_visits INT DEFAULT 0,                   -- Số lượt rửa tối thiểu (12 tháng)
    booking_window_days INT NOT NULL,           -- Số ngày được đặt trước
    point_multiplier FLOAT DEFAULT 1,           -- Hệ số nhân điểm (VD: Gold = 1.5)
    priority_level INT NOT NULL                 -- Mức ưu tiên xếp hàng (càng cao càng ưu tiên)
);

-- =========================
-- 2. PERK (Đặc quyền cố định của hạng)
-- =========================
CREATE TABLE Perk (
    perk_id INT PRIMARY KEY IDENTITY(1,1),
    perk_name NVARCHAR(100) NOT NULL,
    discount_percent FLOAT DEFAULT 0,           -- % giảm giá (VD: 5.0 = 5%)
    free_service NVARCHAR(100) NULL,            -- Dịch vụ miễn phí (VD: 'Sáp bóng')
    add_on_item NVARCHAR(100) NULL              -- Add‑on tặng kèm (VD: 'Vệ sinh nội thất')
);

-- =========================
-- 3. TIER – PERK (Mapping hạng và đặc quyền)
-- =========================
CREATE TABLE TierPerk (
    tier_id INT NOT NULL,
    perk_id INT NOT NULL,
    PRIMARY KEY (tier_id, perk_id),
    FOREIGN KEY (tier_id) REFERENCES LoyaltyTier(tier_id),
    FOREIGN KEY (perk_id) REFERENCES Perk(perk_id)
);

-- =========================
-- 4. ADMIN ACCOUNT (Quản trị viên)
-- =========================
CREATE TABLE AdminAccount (
    admin_id INT PRIMARY KEY IDENTITY(1,1),
    full_name NVARCHAR(100) NOT NULL,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role NVARCHAR(50)
);

-- =========================
-- 5. CUSTOMER (Khách hàng)
-- =========================
CREATE TABLE Customer (
    customer_id INT PRIMARY KEY IDENTITY(1,1),
    full_name NVARCHAR(100) NOT NULL,
    phone_number VARCHAR(15) UNIQUE NOT NULL,
    email VARCHAR(100),
    tier_id INT NOT NULL DEFAULT 1,
    current_points INT DEFAULT 0,
    total_spend DECIMAL(18,2) DEFAULT 0,
    total_visits INT DEFAULT 0,
    last_tier_review DATE NULL,                   -- Ngày review hạng gần nhất
    tier_updated_by_admin_id INT NULL,            -- Admin can thiệp thủ công nâng/hạ hạng
    created_at DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (tier_id) REFERENCES LoyaltyTier(tier_id),
    FOREIGN KEY (tier_updated_by_admin_id) REFERENCES AdminAccount(admin_id)
);

-- =========================
-- 6. VEHICLE (Phương tiện)
-- =========================
CREATE TABLE Vehicle (
    vehicle_id INT PRIMARY KEY IDENTITY(1,1),
    customer_id INT NOT NULL,
    license_plate VARCHAR(20) UNIQUE NOT NULL,
    vehicle_type NVARCHAR(50),
    brand NVARCHAR(50),
    color NVARCHAR(30),
    FOREIGN KEY (customer_id) REFERENCES Customer(customer_id)
);

-- =========================
-- 7. BOOKING (Đặt lịch)
-- =========================
CREATE TABLE Booking (
    booking_id INT PRIMARY KEY IDENTITY(1,1),
    vehicle_id INT NOT NULL,
    booking_date DATE NOT NULL,
    booking_time TIME NOT NULL,
    service_type NVARCHAR(100),
    status NVARCHAR(30) DEFAULT 'Pending',
    priority_level INT DEFAULT 1,                 -- Lấy từ hạng lúc đặt
    tier_id_at_booking INT NOT NULL,              -- Hạng lúc đặt (để truy vết)
    cancelled_by_admin_id INT NULL,               -- Admin huỷ lịch (nếu có)
    created_at DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (vehicle_id) REFERENCES Vehicle(vehicle_id),
    FOREIGN KEY (tier_id_at_booking) REFERENCES LoyaltyTier(tier_id),
    FOREIGN KEY (cancelled_by_admin_id) REFERENCES AdminAccount(admin_id)
);

-- =========================
-- 8. WASH HISTORY (Lịch sử rửa thực tế)
-- =========================
CREATE TABLE WashHistory (
    wash_id INT PRIMARY KEY IDENTITY(1,1),
    booking_id INT UNIQUE NOT NULL,
    wash_date DATETIME DEFAULT GETDATE(),
    amount_paid DECIMAL(18,2) NOT NULL,
    points_earned INT DEFAULT 0,
    points_used INT DEFAULT 0,
    perk_applied NVARCHAR(200),                   -- Mô tả perk đã áp dụng
    FOREIGN KEY (booking_id) REFERENCES Booking(booking_id)
);

-- =========================
-- 9. LOYALTY POINT (Giao dịch điểm)
-- =========================
CREATE TABLE LoyaltyPoint (
    point_id INT PRIMARY KEY IDENTITY(1,1),
    customer_id INT NOT NULL,
    wash_id INT NULL,                             -- Liên kết nếu điểm đến từ 1 lần rửa
    points_change INT NOT NULL,                   -- >0: Earn, <0: Redeem/Expire
    transaction_type NVARCHAR(50) NOT NULL,       -- 'Earn', 'Redeem', 'Expire'
    expiry_date DATE NULL,                        -- Ngày hết hạn (chỉ cho Earn)
    created_at DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (customer_id) REFERENCES Customer(customer_id),
    FOREIGN KEY (wash_id) REFERENCES WashHistory(wash_id)
);

-- =========================
-- 10. PROMOTION (Khuyến mãi)
-- =========================
CREATE TABLE Promotion (
    promotion_id INT PRIMARY KEY IDENTITY(1,1),
    title NVARCHAR(100) NOT NULL,
    description NVARCHAR(255),
    min_tier_id INT NULL,                         -- NULL = tất cả, ngược lại áp dụng cho hạng >= min_tier_id
    discount_percent FLOAT DEFAULT 0,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    status NVARCHAR(30) DEFAULT 'Active',
    created_by_admin_id INT NULL,                 -- Admin tạo khuyến mãi
    FOREIGN KEY (min_tier_id) REFERENCES LoyaltyTier(tier_id),
    FOREIGN KEY (created_by_admin_id) REFERENCES AdminAccount(admin_id)
);

-- =========================
-- 11. REWARD CATALOG (Danh mục đổi điểm)
-- =========================
CREATE TABLE RewardCatalog (
    reward_id INT PRIMARY KEY IDENTITY(1,1),
    reward_name NVARCHAR(100) NOT NULL,
    points_required INT NOT NULL,
    discount_amount DECIMAL(18,2) DEFAULT 0,
    free_wash BIT DEFAULT 0,
    is_active BIT DEFAULT 1,
    created_by_admin_id INT NULL,                 -- Admin tạo phần thưởng
    FOREIGN KEY (created_by_admin_id) REFERENCES AdminAccount(admin_id)
);

-- =========================
-- 12. REWARD REDEMPTION (Lịch sử đổi điểm)
-- =========================
CREATE TABLE RewardRedemption (
    redemption_id INT PRIMARY KEY IDENTITY(1,1),
    customer_id INT NOT NULL,
    reward_id INT NOT NULL,
    points_used INT NOT NULL,
    redemption_date DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (customer_id) REFERENCES Customer(customer_id),
    FOREIGN KEY (reward_id) REFERENCES RewardCatalog(reward_id)
);

-- =========================
-- 13. CUSTOMER MONTHLY STATS (Hỗ trợ monthly tier review)
-- =========================
CREATE TABLE CustomerMonthlyStats (
    stats_id INT PRIMARY KEY IDENTITY(1,1),
    customer_id INT NOT NULL,
    year_month CHAR(6) NOT NULL,                  -- Định dạng YYYYMM
    monthly_spend DECIMAL(18,2) DEFAULT 0,
    monthly_visits INT DEFAULT 0,
    FOREIGN KEY (customer_id) REFERENCES Customer(customer_id),
    UNIQUE (customer_id, year_month)
);

-- ============================================================
-- SAMPLE DATA (Dữ liệu mẫu)
-- ============================================================

-- 1. Admin
INSERT INTO AdminAccount (full_name, username, password, role)
VALUES 
(N'Quản trị viên chính', 'admin', '123456', 'Manager'),
(N'Nhân viên CSKH', 'staff1', '123456', 'Staff');

-- 2. Tiers
INSERT INTO LoyaltyTier (tier_name, min_spending, min_visits, booking_window_days, point_multiplier, priority_level)
VALUES 
('Member',   0,          0,  7,  1.0, 1),
('Silver',   2000000,   10, 10, 1.2, 2),
('Gold',     5000000,   20, 12, 1.5, 3),
('Platinum', 10000000,  40, 14, 2.0, 4);

-- 3. Perks
INSERT INTO Perk (perk_name, discount_percent, free_service, add_on_item)
VALUES 
(N'Giảm giá 5%', 5, NULL, NULL),
(N'Sáp miễn phí', 0, N'Sáp bóng', NULL),
(N'Tặng vệ sinh nội thất', 0, NULL, N'Vệ sinh nội thất');

-- Gán perks cho tier
-- Silver: Giảm 5%
INSERT INTO TierPerk (tier_id, perk_id) VALUES (2, 1);
-- Gold: Giảm 5% + Sáp miễn phí
INSERT INTO TierPerk (tier_id, perk_id) VALUES (3, 1), (3, 2);
-- Platinum: tất cả perks
INSERT INTO TierPerk (tier_id, perk_id) VALUES (4, 1), (4, 2), (4, 3);

-- 4. Khách hàng
INSERT INTO Customer (full_name, phone_number, email, tier_id, current_points)
VALUES 
(N'Nguyễn Văn A', '0901234567', 'vana@gmail.com', 1, 100),
(N'Trần Thị B',   '0912345678', 'thib@gmail.com', 2, 500);

-- 5. Phương tiện
INSERT INTO Vehicle (customer_id, license_plate, vehicle_type, brand, color)
VALUES 
(1, '59A-12345', 'Motorbike', 'Honda', 'Black'),
(2, '61B-67890', 'Motorbike', 'Yamaha', 'Red');

-- 6. Đặt lịch
INSERT INTO Booking (vehicle_id, booking_date, booking_time, service_type, status, priority_level, tier_id_at_booking)
VALUES 
(1, '2026-05-20', '09:00', N'Rửa cao cấp', 'Pending', 1, 1),
(2, '2026-05-20', '10:00', N'Rửa toàn diện', 'Confirmed', 2, 2);

-- 7. Lịch sử rửa
INSERT INTO WashHistory (booking_id, amount_paid, points_earned, points_used, perk_applied)
VALUES 
(1, 120000, 12, 0, N'Không'),
(2, 200000, 20, 50, N'Giảm 5% (Silver)');

-- 8. Giao dịch điểm
INSERT INTO LoyaltyPoint (customer_id, wash_id, points_change, transaction_type, expiry_date)
VALUES 
(1, 1,  12, 'Earn', '2027-05-20'),
(2, 2, -50, 'Redeem', NULL);

-- 9. Khuyến mãi (admin tạo)
INSERT INTO Promotion (title, description, min_tier_id, discount_percent, start_date, end_date, status, created_by_admin_id)
VALUES (N'Ưu đãi tháng 5', N'Giảm 10% cho khách Silver trở lên', 2, 10, '2026-05-01', '2026-05-31', 'Active', 1);

-- 10. Danh mục đổi điểm (admin tạo)
INSERT INTO RewardCatalog (reward_name, points_required, discount_amount, free_wash, created_by_admin_id)
VALUES 
(N'Giảm 10.000đ', 100, 10000, 0, 1),
(N'Rửa xe miễn phí', 300, 0, 1, 1);

-- 11. Lịch sử đổi điểm
INSERT INTO RewardRedemption (customer_id, reward_id, points_used)
VALUES (2, 1, 100);