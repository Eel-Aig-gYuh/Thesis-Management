USE thesisdb;

-- 1. Insert users (parent table)
INSERT INTO users (username, password, lastname, firstname, avatar, email, role, major)
VALUES
('admin', '$2a$10$5X9k5N1sTc1/CjVH5XJoje3QMYijH3ETpgkox00R0MdPaJPPrf7wO', 'Nguyen', 'Admin', 'admin.jpg', 'admin@example.com', 'ROLE_ADMIN', NULL),
('giaovu1', '$2a$10$5X9k5N1sTc1/CjVH5XJoje3QMYijH3ETpgkox00R0MdPaJPPrf7wO', 'Tran', 'Van GiaoVu', 'gv1.jpg', 'giaovu1@example.com', 'ROLE_GIAOVU', NULL),
('gv1', '$2a$10$5X9k5N1sTc1/CjVH5XJoje3QMYijH3ETpgkox00R0MdPaJPPrf7wO', 'Le', 'Thi GiangVien', 'gv1.jpg', 'gv1@example.com', 'ROLE_GIANGVIEN', 'CNTT'),
('gv2', '$2a$10$5X9k5N1sTc1/CjVH5XJoje3QMYijH3ETpgkox00R0MdPaJPPrf7wO', 'Pham', 'Quoc Bao', 'gv2.jpg', 'gv2@example.com', 'ROLE_GIANGVIEN', 'CNTT'),
('sv1', '$2a$10$5X9k5N1sTc1/CjVH5XJoje3QMYijH3ETpgkox00R0MdPaJPPrf7wO', 'Nguyen', 'An SinhVien', 'sv1.jpg', 'sv1@example.com', 'ROLE_SINHVIEN', 'CNTT'),
('sv2', '$2a$10$5X9k5N1sTc1/CjVH5XJoje3QMYijH3ETpgkox00R0MdPaJPPrf7wO', 'Hoang', 'Binh SinhVien', 'sv2.jpg', 'sv2@example.com', 'ROLE_SINHVIEN', 'CNTT');

INSERT INTO users (username, password, lastname, firstname, avatar, email, role, major)
VALUES
('giangvien3', '$2a$10$5X9k5N1sTc1/CjVH5XJoje3QMYijH3ETpgkox00R0MdPaJPPrf7wO', 'Ngo', 'Duc Manh', 'gv3.jpg', 'gv3@example.com', 'ROLE_GIANGVIEN', 'CNTT'),
('giangvien4', '$2a$10$5X9k5N1sTc1/CjVH5XJoje3QMYijH3ETpgkox00R0MdPaJPPrf7wO', 'Pham', 'Thi Lan', 'gv4.jpg', 'gv4@example.com', 'ROLE_GIANGVIEN', 'CNTT'),
('giangvien5', '$2a$10$5X9k5N1sTc1/CjVH5XJoje3QMYijH3ETpgkox00R0MdPaJPPrf7wO', 'Vu', 'Minh Tuan', 'gv5.jpg', 'gv5@example.com', 'ROLE_GIANGVIEN', 'CNTT'),
('giangvien6', '$2a$10$5X9k5N1sTc1/CjVH5XJoje3QMYijH3ETpgkox00R0MdPaJPPrf7wO', 'Ho', 'Thanh Ha', 'gv6.jpg', 'gv6@example.com', 'ROLE_GIANGVIEN', 'CNTT'),
('giangvien7', '$2a$10$5X9k5N1sTc1/CjVH5XJoje3QMYijH3ETpgkox00R0MdPaJPPrf7wO', 'Dang', 'Van Khoa', 'gv7.jpg', 'gv7@example.com', 'ROLE_GIANGVIEN', 'CNTT'),
('giangvien8', '$2a$10$5X9k5N1sTc1/CjVH5XJoje3QMYijH3ETpgkox00R0MdPaJPPrf7wO', 'Nguyen', 'Thi Mai', 'gv8.jpg', 'gv8@example.com', 'ROLE_GIANGVIEN', 'CNTT'),
('giangvien9', '$2a$10$5X9k5N1sTc1/CjVH5XJoje3QMYijH3ETpgkox00R0MdPaJPPrf7wO', 'Trinh', 'Duc Anh', 'gv9.jpg', 'gv9@example.com', 'ROLE_GIANGVIEN', 'CNTT'),
('giangvien10', '$2a$10$5X9k5N1sTc1/CjVH5XJoje3QMYijH3ETpgkox00R0MdPaJPPrf7wO', 'Nguyen', 'Thi Huong', 'gv10.jpg', 'gv10@example.com', 'ROLE_GIANGVIEN', 'HTTT'),
('giangvien11', '$2a$10$5X9k5N1sTc1/CjVH5XJoje3QMYijH3ETpgkox00R0MdPaJPPrf7wO', 'Tran', 'Van Long', 'gv11.jpg', 'gv11@example.com', 'ROLE_GIANGVIEN', 'MMT'),
('giangvien12', '$2a$10$5X9k5N1sTc1/CjVH5XJoje3QMYijH3ETpgkox00R0MdPaJPPrf7wO', 'Le', 'Thi Thuy', 'gv12.jpg', 'gv12@example.com', 'ROLE_GIANGVIEN', 'KTPM'),

('sinhvien3', '$2a$10$5X9k5N1sTc1/CjVH5XJoje3QMYijH3ETpgkox00R0MdPaJPPrf7wO', 'Tran', 'Hoai Nam', 'sv3.jpg', 'sv3@example.com', 'ROLE_SINHVIEN', 'CNTT'),
('sinhvien4', '$2a$10$5X9k5N1sTc1/CjVH5XJoje3QMYijH3ETpgkox00R0MdPaJPPrf7wO', 'Le', 'Ngoc Anh', 'sv4.jpg', 'sv4@example.com', 'ROLE_SINHVIEN', 'CNTT'),
('sinhvien5', '$2a$10$5X9k5N1sTc1/CjVH5XJoje3QMYijH3ETpgkox00R0MdPaJPPrf7wO', 'Nguyen', 'Huu Tuan', 'sv5.jpg', 'sv5@example.com', 'ROLE_SINHVIEN', 'CNTT'),
('sinhvien6', '$2a$10$5X9k5N1sTc1/CjVH5XJoje3QMYijH3ETpgkox00R0MdPaJPPrf7wO', 'Nguyen', 'Thi Hong', 'sv6.jpg', 'sv6@example.com', 'ROLE_SINHVIEN', 'CNTT'),
('sinhvien7', '$2a$10$5X9k5N1sTc1/CjVH5XJoje3QMYijH3ETpgkox00R0MdPaJPPrf7wO', 'Tran', 'Quang Huy', 'sv7.jpg', 'sv7@example.com', 'ROLE_SINHVIEN', 'KTPM'),
('sinhvien8', '$2a$10$5X9k5N1sTc1/CjVH5XJoje3QMYijH3ETpgkox00R0MdPaJPPrf7wO', 'Le', 'Thi Kim', 'sv8.jpg', 'sv8@example.com', 'ROLE_SINHVIEN', 'CNTT'),
('sinhvien9', '$2a$10$5X9k5N1sTc1/CjVH5XJoje3QMYijH3ETpgkox00R0MdPaJPPrf7wO', 'Pham', 'Van Nam', 'sv9.jpg', 'sv9@example.com', 'ROLE_SINHVIEN', 'CNTT'),
('sinhvien10', '$2a$10$5X9k5N1sTc1/CjVH5XJoje3QMYijH3ETpgkox00R0MdPaJPPrf7wO', 'Do', 'Bao Chau', 'sv10.jpg', 'sv10@example.com', 'ROLE_SINHVIEN', 'CNTT'),
('sinhvien11', '$2a$10$5X9k5N1sTc1/CjVH5XJoje3QMYijH3ETpgkox00R0MdPaJPPrf7wO', 'Bui', 'Duc Hieu', 'sv11.jpg', 'sv11@example.com', 'ROLE_SINHVIEN', 'CNTT'),
('sinhvien12', '$2a$10$5X9k5N1sTc1/CjVH5XJoje3QMYijH3ETpgkox00R0MdPaJPPrf7wO', 'Ngo', 'Lan Phuong', 'sv12.jpg', 'sv12@example.com', 'ROLE_SINHVIEN', 'CNTT'),
('sinhvien13', '$2a$10$5X9k5N1sTc1/CjVH5XJoje3QMYijH3ETpgkox00R0MdPaJPPrf7wO', 'Dang', 'Huu Tinh', 'sv13.jpg', 'sv13@example.com', 'ROLE_SINHVIEN', 'CNTT'),
('sinhvien14', '$2a$10$5X9k5N1sTc1/CjVH5XJoje3QMYijH3ETpgkox00R0MdPaJPPrf7wO', 'Vo', 'Minh Hoang', 'sv14.jpg', 'sv14@example.com', 'ROLE_SINHVIEN', 'CNTT'),
('sinhvien15', '$2a$10$5X9k5N1sTc1/CjVH5XJoje3QMYijH3ETpgkox00R0MdPaJPPrf7wO', 'Ho', 'Gia Bao', 'sv15.jpg', 'sv15@example.com', 'ROLE_SINHVIEN', 'CNTT'),
('sinhvien16', '$2a$10$5X9k5N1sTc1/CjVH5XJoje3QMYijH3ETpgkox00R0MdPaJPPrf7wO', 'Phan', 'Van Anh', 'sv16.jpg', 'sv16@example.com', 'ROLE_SINHVIEN', 'CNTT'),
('sinhvien17', '$2a$10$5X9k5N1sTc1/CjVH5XJoje3QMYijH3ETpgkox00R0MdPaJPPrf7wO', 'Cao', 'Thi Duong', 'sv17.jpg', 'sv17@example.com', 'ROLE_SINHVIEN', 'HTTT'),
('sinhvien18', '$2a$10$5X9k5N1sTc1/CjVH5XJoje3QMYijH3ETpgkox00R0MdPaJPPrf7wO', 'Truong', 'Dinh Dat', 'sv18.jpg', 'sv18@example.com', 'ROLE_SINHVIEN', 'MMT'),
('sinhvien19', '$2a$10$5X9k5N1sTc1/CjVH5XJoje3QMYijH3ETpgkox00R0MdPaJPPrf7wO', 'Bui', 'Minh Duc', 'sv19.jpg', 'sv19@example.com', 'ROLE_SINHVIEN', 'KTPM'),
('sinhvien20', '$2a$10$5X9k5N1sTc1/CjVH5XJoje3QMYijH3ETpgkox00R0MdPaJPPrf7wO', 'Dinh', 'Thi Hoa', 'sv20.jpg', 'sv20@example.com', 'ROLE_SINHVIEN', 'CNTT');


-- 2. Insert departments (parent table)
INSERT INTO departments (name, created_by)
VALUES
('Khoa Công nghệ thông tin', 1),
('Khoa Hệ thống thông tin', 1),
('Khoa Kỹ thuật phần mềm', 1);

-- 4. Insert theses (parent table for thesis_students, thesis_advisors, etc.)
--    Make sure this statement successfully runs before proceeding.
INSERT INTO theses (created_by, semester, title, status, department_id, average_score)
VALUES
(3, 'Spring 2025', 'Xây dựng hệ thống quản lý khóa luận tốt nghiệp', 'REGISTERED', 1, NULL),
(4, 'Spring 2025', 'Ứng dụng AI trong phân tích dữ liệu học tập', 'DRAFT', 1, NULL),
(3, 'Spring 2025', 'Nghiên cứu và phát triển ứng dụng di động cho quản lý dự án', 'APPROVED', 1, 8.75),
(4, 'Spring 2025', 'Phân tích hiệu suất thuật toán học máy trên dữ liệu lớn', 'APPROVED', 1, 9.00),
(7, 'Fall 2024', 'Thiết kế hệ thống quản lý thư viện số', 'APPROVED', 2, 8.25),
(8, 'Fall 2024', 'Xây dựng website thương mại điện tử với React và Spring Boot', 'REJECTED', 3, NULL),
(9, 'Spring 2025', 'Nghiên cứu blockchain trong quản lý chuỗi cung ứng', 'REGISTERED', 1, NULL),
(10, 'Spring 2025', 'Tối ưu hóa cơ sở dữ liệu cho ứng dụng web quy mô lớn', 'DRAFT', 1, NULL),
(11, 'Fall 2024', 'Ứng dụng IoT trong giám sát môi trường thông minh', 'APPROVED', 1, 8.50),
(12, 'Spring 2025', 'Phát triển game 2D với Unity', 'REGISTERED', 3, NULL);


-- 5. Now insert into thesis_students (child table)
--    The thesis_ids should now exist.
INSERT INTO thesis_students (thesis_id, student_id, registered_at)
VALUES
(1, 5, NOW()),
(2, 6, NOW());

-- 6. Continue with other child tables that depend on 'theses'
INSERT INTO thesis_advisors (thesis_id, advisor_id, registered_at)
VALUES
(1, 3, NOW()),
(2, 4, NOW());

INSERT INTO thesis_reviewers (thesis_id, reviewer_id, assigned_at)
VALUES
(1, 4, NOW()),
(2, 3, NOW());

INSERT INTO thesis_files (thesis_id, student_id, file_url, file_name, submitted_at)
VALUES
(1, 5, '/uploads/thesis1.pdf', 'khoaluan1.pdf', NOW()),
(2, 6, '/uploads/thesis2.pdf', 'khoaluan2.pdf', NOW());

-- 7. Insert councils (parent table for council_members, council_theses)
INSERT INTO councils (created_by, name, defense_date, defense_location, status)
VALUES
(2, 'Hội đồng bảo vệ đợt 2', '2025-06-20', 'Phòng 205', 'SCHEDULED'),
(2, 'Hội đồng bảo vệ đợt 3 (Fall 2024)', '2024-12-10', 'Phòng Lab A', 'COMPLETED');

-- 8. Insert council_members (child of users and councils)
INSERT INTO council_members (council_id, member_id, role)
VALUES
(1, 3, 'CHAIRMAN'),
(1, 4, 'SECRETARY'),
(1, 2, 'MEMBER'),
(2, 7, 'CHAIRMAN'),
(2, 8, 'SECRETARY'),
(2, 9, 'MEMBER');

-- 9. Insert council_theses (child of councils and theses)
INSERT INTO council_theses (council_id, thesis_id)
VALUES
(1, 1),
(1, 2),
(2, 3),
(2, 4),
(2, 9);

-- 10. Insert criteria (parent table for scores, thesis_criteria)
INSERT INTO criteria (created_by, name, max_score)
VALUES
(2, 'Nội dung', 5),
(2, 'Hình thức trình bày', 3),
(2, 'Thuyết trình', 2),
(2, 'Khả năng ứng dụng', 3),
(2, 'Tính sáng tạo', 3),
(2, 'Phản biện', 2),
(2, 'Đóng góp nghiên cứu', 2);

-- 11. Insert scores (child of theses, users (via council_member_id), and criteria)
INSERT INTO scores (thesis_id, council_member_id, criteria_id, score)
VALUES
-- Scores for thesis 3 (Council 2)
(1, 7, 1, 9.5),
(1, 7, 2, 8.0),
(1, 7, 3, 4.0),
(1, 8, 1, 9.0),
(1, 8, 2, 7.5),
(1, 8, 3, 4.5),
(1, 9, 1, 9.0),
(1, 9, 2, 8.0),
(1, 9, 3, 4.0),
-- Scores for thesis 4 (Council 2)
(2, 7, 1, 9.0),
(2, 7, 2, 8.5),
(2, 7, 3, 4.5),
(2, 8, 1, 9.5),
(2, 8, 2, 9.0),
(2, 8, 3, 5.0);

-- 12. Insert notifications (child of users)
INSERT INTO notifications (user_id, type, content, status)
VALUES
(5, 'EMAIL', 'Bạn đã được phân công khóa luận.', 'SENT'),
(6, 'EMAIL', 'Hãy nộp bản thuyết trình.', 'PENDING'),
(7, 'EMAIL', 'Khóa luận của bạn đã được duyệt.', 'SENT'),
(9, 'EMAIL', 'Nhắc nhở nộp báo cáo tiến độ.', 'PENDING'),
(10, 'SMS', 'Bạn có lịch bảo vệ khóa luận vào ngày 2025-06-20.', 'SENT'),
(13, 'EMAIL', 'Khóa luận của bạn đã bị từ chối. Vui lòng liên hệ giáo vụ để biết thêm chi tiết.', 'SENT');

-- 13. Insert thesis_criteria (child of theses and criteria)
INSERT INTO thesis_criteria (thesis_id, criteria_id)
VALUES
(1, 1), (1, 2), (1, 3), (1, 4),
(2, 1), (2, 2), (2, 3), (2, 4);