use gheethesisdb;

INSERT INTO users (username, password, lastname, firstname, avatar, email, role, major)
VALUES
('admin', '$2a$10$5X9k5N1sTc1/CjVH5XJoje3QMYijH3ETpgkox00R0MdPaJPPrf7wO', 'Nguyen', 'Admin', 'admin.jpg', 'admin@example.com', 'ROLE_ADMIN', NULL),
('giaovu1', '$2a$10$5X9k5N1sTc1/CjVH5XJoje3QMYijH3ETpgkox00R0MdPaJPPrf7wO', 'Tran', 'Van GiaoVu', 'gv1.jpg', 'giaovu1@example.com', 'ROLE_GIAOVU', NULL),
('gv1', '$2a$10$5X9k5N1sTc1/CjVH5XJoje3QMYijH3ETpgkox00R0MdPaJPPrf7wO', 'Le', 'Thi GiangVien', 'gv1.jpg', 'gv1@example.com', 'ROLE_GIANGVIEN', 'CNTT'),
('gv2', '$2a$10$5X9k5N1sTc1/CjVH5XJoje3QMYijH3ETpgkox00R0MdPaJPPrf7wO', 'Pham', 'Quoc Bao', 'gv2.jpg', 'gv2@example.com', 'ROLE_GIANGVIEN', 'CNTT'),
('sv1', '$2a$10$5X9k5N1sTc1/CjVH5XJoje3QMYijH3ETpgkox00R0MdPaJPPrf7wO', 'Nguyen', 'An SinhVien', 'sv1.jpg', 'sv1@example.com', 'ROLE_SINHVIEN', 'CNTT'),
('sv2', '$2a$10$5X9k5N1sTc1/CjVH5XJoje3QMYijH3ETpgkox00R0MdPaJPPrf7wO', 'Hoang', 'Binh SinhVien', 'sv2.jpg', 'sv2@example.com', 'ROLE_SINHVIEN', 'CNTT');

INSERT INTO theses (created_by, title, status, semester)
VALUES
(3, 'Xây dựng hệ thống quản lý khóa luận tốt nghiệp', 'REGISTERED', 'Spring 2025'),
(4, 'Ứng dụng AI trong phân tích dữ liệu học tập', 'DRAFT', 'Spring 2025');

INSERT INTO thesis_students (thesis_id, student_id, registered_at)
VALUES
(1, 5, NOW()),
(2, 6, NOW());

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

-- ✅ Sửa chỗ này: thêm 'name' và 'created_by'
INSERT INTO councils (created_by, name, defense_date, defense_location)
VALUES
(2, 'Hội đồng bảo vệ đợt 1', '2025-06-15', 'Phòng 101');

INSERT INTO council_members (council_id, member_id, role)
VALUES
(1, 3, 'CHAIRMAN'),
(1, 4, 'SECRETARY'),
(1, 2, 'MEMBER');

INSERT INTO council_theses (council_id, thesis_id)
VALUES
(1, 1),
(1, 2);

INSERT INTO criteria (created_by, name, max_score)
VALUES
(2, 'Nội dung', 10.0),
(2, 'Hình thức trình bày', 5.0),
(2, 'Thuyết trình', 5.0);

INSERT INTO scores (thesis_id, council_member_id, criteria_id, score)
VALUES
(1, 3, 1, 9.0),
(1, 3, 2, 4.5),
(1, 3, 3, 5.0),
(1, 4, 1, 8.5),
(1, 4, 2, 4.0),
(1, 4, 3, 4.5);

INSERT INTO notifications (user_id, type, content, status)
VALUES
(5, 'EMAIL', 'Bạn đã được phân công khóa luận.', 'SENT'),
(6, 'EMAIL', 'Hãy nộp bản thuyết trình.', 'PENDING');