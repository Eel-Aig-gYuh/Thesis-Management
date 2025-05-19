USE gheethesisdb;

-- Bảng users
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    lastname VARCHAR(255),
    firstname VARCHAR(50),
    avatar VARCHAR(255) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    role ENUM('ROLE_ADMIN', 'ROLE_GIAOVU', 'ROLE_GIANGVIEN', 'ROLE_SINHVIEN') NOT NULL,
    major VARCHAR(100),
    is_active BOOLEAN DEFAULT TRUE
);

-- Bảng theses: Khóa luận.
CREATE TABLE theses (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    
    created_by BIGINT,
    
    semester VARCHAR(50),
    title VARCHAR(255) NOT NULL,
    average_score DECIMAL(4,2),
    status ENUM('DRAFT', 'REGISTERED', 'APPROVED', 'REJECTED', 'CANCELLED') DEFAULT 'DRAFT',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    FOREIGN KEY (created_by) REFERENCES users(id)
);

-- Bảng thesis_students: sinh viên thực hiện khóa luận.
CREATE TABLE thesis_students (
	id BIGINT PRIMARY KEY AUTO_INCREMENT,
    
    thesis_id BIGINT,
    student_id BIGINT,
    
    registered_at TIMESTAMP,
    
    FOREIGN KEY (thesis_id) REFERENCES theses(id),
    FOREIGN KEY (student_id) REFERENCES users(id)
);

-- Bảng thesis_advisors: giáo viên hướng dẫn.
CREATE TABLE thesis_advisors (
	id BIGINT PRIMARY KEY AUTO_INCREMENT,
    
    thesis_id BIGINT,
    advisor_id BIGINT,
    
    registered_at TIMESTAMP,
    
    FOREIGN KEY (thesis_id) REFERENCES theses(id),
    FOREIGN KEY (advisor_id) REFERENCES users(id)
);

-- Bảng thesis_reviewers: giáo viên phản biện.
CREATE TABLE thesis_reviewers (
	id BIGINT PRIMARY KEY AUTO_INCREMENT,
    
    thesis_id BIGINT,
    reviewer_id BIGINT,
    
    assigned_at TIMESTAMP,
    
    FOREIGN KEY (thesis_id) REFERENCES theses(id),
    FOREIGN KEY (reviewer_id) REFERENCES users(id)
);

-- Bảng thesis_files: Lưu file của sinh viên
CREATE TABLE thesis_files (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    
    thesis_id BIGINT,
    student_id BIGINT,
    
    file_url VARCHAR(255) NOT NULL,
    file_name VARCHAR(255) NOT NULL,
    submitted_at TIMESTAMP,
    
    FOREIGN KEY (thesis_id) REFERENCES theses(id),
    FOREIGN KEY (student_id) REFERENCES users(id)
);

-- Bảng councils: Hội đồng bảo vệ.
CREATE TABLE councils (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    
    created_by BIGINT,
    
    name VARCHAR(255),
    defense_date DATE,
    defense_location VARCHAR(255),
    status ENUM('SCHEDULED', 'COMPLETED', 'LOCKED', 'CANCELED') DEFAULT 'SCHEDULED',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    FOREIGN KEY (created_by) REFERENCES users(id)
);

-- Bảng council_members: Thành viên hội đồng.
CREATE TABLE council_members (
	id BIGINT PRIMARY KEY AUTO_INCREMENT,
    
    council_id BIGINT,
    member_id BIGINT,
    
    role ENUM('CHAIRMAN', 'SECRETARY', 'REVIEWER', 'MEMBER') NOT NULL,
   
    FOREIGN KEY (council_id) REFERENCES councils(id),
    FOREIGN KEY (member_id) REFERENCES users(id)
);

-- Bảng council_theses: Khóa luận cần chấm.
CREATE TABLE council_theses (
	id BIGINT PRIMARY KEY AUTO_INCREMENT,
    
    council_id BIGINT,
    thesis_id BIGINT,
    
    FOREIGN KEY (council_id) REFERENCES councils(id),
    FOREIGN KEY (thesis_id) REFERENCES theses(id)
);

-- Bảng criteria: Tiêu chí chấm điểm.
CREATE TABLE criteria (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    created_by BIGINT,
    
    name VARCHAR(100) NOT NULL,
    max_score DECIMAL(4,2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    FOREIGN KEY (created_by) REFERENCES users(id)
);

-- Bảng scores: Bảng điểm.
CREATE TABLE scores (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    
    thesis_id BIGINT,
    council_member_id BIGINT,
    criteria_id BIGINT,
    
    score DECIMAL(4,2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    FOREIGN KEY (thesis_id) REFERENCES theses(id),
    FOREIGN KEY (council_member_id) REFERENCES users(id),
    FOREIGN KEY (criteria_id) REFERENCES criteria(id)
);

-- Bảng notifications
CREATE TABLE notifications (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    
    user_id BIGINT,
    
    type ENUM('EMAIL', 'SMS') NOT NULL,
    content TEXT NOT NULL,
    status ENUM('PENDING', 'SENT', 'FAILED') DEFAULT 'PENDING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    FOREIGN KEY (user_id) REFERENCES users(id)
);