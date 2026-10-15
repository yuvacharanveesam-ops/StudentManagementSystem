package com.studentmanagement;

import com.studentmanagement.dao.StudentDAO;
import com.studentmanagement.model.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class StudentDAOTest {

    private StudentDAO dao;

    @BeforeEach
    public void setUp() {
        dao = new StudentDAO();
    }

    @Test
    public void testAuthentication() {
        // Valid login
        assertTrue(dao.validateUser("admin", "admin123"), "Default admin credentials should pass");

        // Invalid password
        assertFalse(dao.validateUser("admin", "wrongpass"), "Wrong password should fail");

        // Invalid username
        assertFalse(dao.validateUser("nonexistent", "admin123"), "Unknown user should fail");
    }

    @Test
    public void testGetAllStudents() {
        List<Student> list = dao.getAllStudents();
        assertNotNull(list);
        assertFalse(list.isEmpty(), "Student list should not be empty");
    }

    @Test
    public void testAddAndRetrieveStudent() {
        Student newStudent = new Student("Test Candidate", "test.candidate@test.com", "9000000000", "Mechanical", 1, "Male");
        boolean added = dao.addStudent(newStudent);
        assertTrue(added, "Student should be added successfully");

        // Verify retrieval via search
        List<Student> found = dao.searchStudents("Test Candidate");
        assertFalse(found.isEmpty(), "Newly added student should be searchable");
        Student retrieved = found.get(0);
        assertEquals("test.candidate@test.com", retrieved.getEmail());
    }

    @Test
    public void testUpdateStudent() {
        Student newStudent = new Student("Update Target", "update@test.com", "9111111111", "Electrical", 2, "Female");
        dao.addStudent(newStudent);

        List<Student> found = dao.searchStudents("Update Target");
        assertFalse(found.isEmpty());
        Student target = found.get(0);

        // Update details
        target.setName("Updated Name");
        target.setYear(4);
        boolean updated = dao.updateStudent(target);
        assertTrue(updated, "Student update should succeed");

        Student verified = dao.getStudentById(target.getId());
        assertNotNull(verified);
        assertEquals("Updated Name", verified.getName());
        assertEquals(4, verified.getYear());
    }

    @Test
    public void testDeleteStudent() {
        Student newStudent = new Student("Delete Target", "delete@test.com", "9222222222", "Civil", 3, "Other");
        dao.addStudent(newStudent);

        List<Student> found = dao.searchStudents("Delete Target");
        assertFalse(found.isEmpty());
        int targetId = found.get(0).getId();

        boolean deleted = dao.deleteStudent(targetId);
        assertTrue(deleted, "Student deletion should succeed");

        Student shouldBeNull = dao.getStudentById(targetId);
        assertNull(shouldBeNull, "Deleted student should no longer exist");
    }

    @Test
    public void testSearchStudents() {
        List<Student> csStudents = dao.searchStudents("Computer Science");
        assertNotNull(csStudents);
        for (Student s : csStudents) {
            boolean matches = s.getDepartment().toLowerCase().contains("computer") ||
                              s.getName().toLowerCase().contains("computer") ||
                              s.getEmail().toLowerCase().contains("computer");
            assertTrue(matches, "Search result should match search query");
        }
    }
}
