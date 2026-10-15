-- ============================================================
--  Student Management System - Database Setup Script
--  Module 5 Assignment - JDBC Demonstration
--  Run this script in MySQL Workbench or MySQL CLI
-- ============================================================

-- Step 1: Create the database
CREATE DATABASE IF NOT EXISTS student_management;

-- Step 2: Select the database
USE student_management;

-- ============================================================
-- Table: users
-- Stores admin/user login credentials
-- ============================================================
CREATE TABLE IF NOT EXISTS users (
    id       INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50)  UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL
);

-- ============================================================
-- Table: students
-- Stores all student records
-- ============================================================
CREATE TABLE IF NOT EXISTS students (
    id         INT PRIMARY KEY AUTO_INCREMENT,
    name       VARCHAR(100) NOT NULL,
    email      VARCHAR(100),
    phone      VARCHAR(20),
    department VARCHAR(100),
    year       INT,
    gender     VARCHAR(20)
);

-- ============================================================
-- Demo user account
-- Username: admin | Password: admin123
-- ============================================================
INSERT INTO users (username, password)
VALUES ('admin', 'admin123')
ON DUPLICATE KEY UPDATE password = 'admin123';

-- ============================================================
-- Sample Student Records (at least 5)
-- ============================================================
INSERT INTO students (name, email, phone, department, year, gender) VALUES
('Aarav Sharma',   'aarav.sharma@college.edu',   '9876543210', 'Computer Science',    3, 'Male'),
('Priya Patel',    'priya.patel@college.edu',    '9123456780', 'Information Technology', 2, 'Female'),
('Rohit Verma',    'rohit.verma@college.edu',    '9988776655', 'Electronics',         4, 'Male'),
('Sneha Gupta',    'sneha.gupta@college.edu',    '9871234560', 'Mechanical',          1, 'Female'),
('Arjun Singh',    'arjun.singh@college.edu',    '9765432180', 'Civil Engineering',   3, 'Male'),
('Meera Nair',     'meera.nair@college.edu',     '9654321870', 'Computer Science',    2, 'Female'),
('Vikram Tiwari',  'vikram.tiwari@college.edu',  '9543218760', 'Information Technology', 4, 'Male');

-- ============================================================
-- Verification Queries (optional - run to verify data)
-- ============================================================
-- SELECT * FROM users;
-- SELECT * FROM students;
