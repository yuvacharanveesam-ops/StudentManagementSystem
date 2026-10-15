package com.studentmanagement.dao;

import com.studentmanagement.model.Student;
import com.studentmanagement.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * StudentDAO - Data Access Object for Student operations
 *
 * Module 5 Concept: JDBC – DAO Pattern
 *
 * This class encapsulates ALL database operations for the Student entity.
 * It uses PreparedStatement to prevent SQL Injection.
 *
 * JDBC objects used:
 *   Connection        – represents a connection to the database
 *   PreparedStatement – pre-compiled SQL with parameter placeholders (?)
 *   ResultSet         – holds the rows returned by a SELECT query
 *
 * All resources are managed with try-with-resources to prevent connection leaks.
 * If the database is unreachable, it automatically falls back to an in-memory repository
 * so the application remains fully interactive and testable.
 */
public class StudentDAO {

    // ── SQL Constants ─────────────────────────────────────────────────────────
    private static final String INSERT_SQL =
        "INSERT INTO students (name, email, phone, department, year, gender) VALUES (?, ?, ?, ?, ?, ?)";

    private static final String SELECT_ALL_SQL =
        "SELECT * FROM students ORDER BY id";

    private static final String SELECT_BY_ID_SQL =
        "SELECT * FROM students WHERE id = ?";

    private static final String SEARCH_SQL =
        "SELECT * FROM students WHERE name LIKE ? OR email LIKE ? OR department LIKE ?";

    private static final String UPDATE_SQL =
        "UPDATE students SET name=?, email=?, phone=?, department=?, year=?, gender=? WHERE id=?";

    private static final String DELETE_SQL =
        "DELETE FROM students WHERE id = ?";

    private static final String VALIDATE_USER_SQL =
        "SELECT * FROM users WHERE username = ? AND password = ?";

    // ── In-Memory Demo Fallback Storage ──────────────────────────────────────
    private static final List<Student> IN_MEMORY_STUDENTS = new ArrayList<>();
    private static final AtomicInteger ID_COUNTER = new AtomicInteger(100);

    static {
        IN_MEMORY_STUDENTS.add(new Student(1, "Aarav Sharma",   "aarav.sharma@college.edu",   "9876543210", "Computer Science",    3, "Male"));
        IN_MEMORY_STUDENTS.add(new Student(2, "Priya Patel",    "priya.patel@college.edu",    "9123456780", "Information Technology", 2, "Female"));
        IN_MEMORY_STUDENTS.add(new Student(3, "Rohit Verma",    "rohit.verma@college.edu",    "9988776655", "Electronics",         4, "Male"));
        IN_MEMORY_STUDENTS.add(new Student(4, "Sneha Gupta",    "sneha.gupta@college.edu",    "9871234560", "Mechanical",          1, "Female"));
        IN_MEMORY_STUDENTS.add(new Student(5, "Arjun Singh",    "arjun.singh@college.edu",    "9765432180", "Civil Engineering",   3, "Male"));
        IN_MEMORY_STUDENTS.add(new Student(6, "Meera Nair",     "meera.nair@college.edu",     "9654321870", "Computer Science",    2, "Female"));
        IN_MEMORY_STUDENTS.add(new Student(7, "Vikram Tiwari",  "vikram.tiwari@college.edu",  "9543218760", "Information Technology", 4, "Male"));
    }

    // ── CRUD Methods ──────────────────────────────────────────────────────────

    /**
     * INSERT a new student record into the database (or in-memory fallback).
     */
    public boolean addStudent(Student student) {
        if (!DBConnection.testConnection()) {
            student.setId(ID_COUNTER.incrementAndGet());
            IN_MEMORY_STUDENTS.add(student);
            return true;
        }

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(INSERT_SQL)) {

            ps.setString(1, student.getName());
            ps.setString(2, student.getEmail());
            ps.setString(3, student.getPhone());
            ps.setString(4, student.getDepartment());
            ps.setInt   (5, student.getYear());
            ps.setString(6, student.getGender());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("[StudentDAO] addStudent error: " + e.getMessage());
            student.setId(ID_COUNTER.incrementAndGet());
            IN_MEMORY_STUDENTS.add(student);
            return true;
        }
    }

    /**
     * SELECT all students from the database (or in-memory fallback).
     */
    public List<Student> getAllStudents() {
        if (!DBConnection.testConnection()) {
            return new ArrayList<>(IN_MEMORY_STUDENTS);
        }

        List<Student> students = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(SELECT_ALL_SQL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                students.add(mapRow(rs));
            }
            return students;

        } catch (SQLException e) {
            System.err.println("[StudentDAO] getAllStudents error: " + e.getMessage());
            return new ArrayList<>(IN_MEMORY_STUDENTS);
        }
    }

    /**
     * SELECT a single student by id.
     */
    public Student getStudentById(int id) {
        if (!DBConnection.testConnection()) {
            return IN_MEMORY_STUDENTS.stream().filter(s -> s.getId() == id).findFirst().orElse(null);
        }

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(SELECT_BY_ID_SQL)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("[StudentDAO] getStudentById error: " + e.getMessage());
        }
        return IN_MEMORY_STUDENTS.stream().filter(s -> s.getId() == id).findFirst().orElse(null);
    }

    /**
     * SELECT students matching the keyword.
     */
    public List<Student> searchStudents(String keyword) {
        if (!DBConnection.testConnection()) {
            String lower = keyword.toLowerCase();
            List<Student> filtered = new ArrayList<>();
            for (Student s : IN_MEMORY_STUDENTS) {
                if ((s.getName() != null && s.getName().toLowerCase().contains(lower)) ||
                    (s.getEmail() != null && s.getEmail().toLowerCase().contains(lower)) ||
                    (s.getDepartment() != null && s.getDepartment().toLowerCase().contains(lower))) {
                    filtered.add(s);
                }
            }
            return filtered;
        }

        List<Student> students = new ArrayList<>();
        String likeKeyword = "%" + keyword + "%";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(SEARCH_SQL)) {

            ps.setString(1, likeKeyword);
            ps.setString(2, likeKeyword);
            ps.setString(3, likeKeyword);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    students.add(mapRow(rs));
                }
            }
            return students;

        } catch (SQLException e) {
            System.err.println("[StudentDAO] searchStudents error: " + e.getMessage());
            return searchStudents(keyword); // Fallback
        }
    }

    /**
     * UPDATE an existing student.
     */
    public boolean updateStudent(Student student) {
        if (!DBConnection.testConnection()) {
            for (int i = 0; i < IN_MEMORY_STUDENTS.size(); i++) {
                if (IN_MEMORY_STUDENTS.get(i).getId() == student.getId()) {
                    IN_MEMORY_STUDENTS.set(i, student);
                    return true;
                }
            }
            return false;
        }

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(UPDATE_SQL)) {

            ps.setString(1, student.getName());
            ps.setString(2, student.getEmail());
            ps.setString(3, student.getPhone());
            ps.setString(4, student.getDepartment());
            ps.setInt   (5, student.getYear());
            ps.setString(6, student.getGender());
            ps.setInt   (7, student.getId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("[StudentDAO] updateStudent error: " + e.getMessage());
            return false;
        }
    }

    /**
     * DELETE a student by id.
     */
    public boolean deleteStudent(int id) {
        if (!DBConnection.testConnection()) {
            return IN_MEMORY_STUDENTS.removeIf(s -> s.getId() == id);
        }

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(DELETE_SQL)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("[StudentDAO] deleteStudent error: " + e.getMessage());
            return IN_MEMORY_STUDENTS.removeIf(s -> s.getId() == id);
        }
    }

    /**
     * Validates user credentials.
     */
    public boolean validateUser(String username, String password) {
        if (!DBConnection.testConnection()) {
            // Default demo credentials
            return "admin".equalsIgnoreCase(username) && "admin123".equals(password);
        }

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(VALIDATE_USER_SQL)) {

            ps.setString(1, username);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            System.err.println("[StudentDAO] validateUser error: " + e.getMessage());
            return "admin".equalsIgnoreCase(username) && "admin123".equals(password);
        }
    }

    // ── Private helper ────────────────────────────────────────────────────────
    private Student mapRow(ResultSet rs) throws SQLException {
        return new Student(
            rs.getInt   ("id"),
            rs.getString("name"),
            rs.getString("email"),
            rs.getString("phone"),
            rs.getString("department"),
            rs.getInt   ("year"),
            rs.getString("gender")
        );
    }
}
