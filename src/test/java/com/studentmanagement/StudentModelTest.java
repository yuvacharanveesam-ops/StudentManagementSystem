package com.studentmanagement;

import com.studentmanagement.model.Student;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class StudentModelTest {

    @Test
    public void testStudentDefaultConstructorAndSetters() {
        Student s = new Student();
        s.setId(10);
        s.setName("Test Student");
        s.setEmail("test@college.edu");
        s.setPhone("1234567890");
        s.setDepartment("Computer Science");
        s.setYear(2);
        s.setGender("Male");

        assertEquals(10, s.getId());
        assertEquals("Test Student", s.getName());
        assertEquals("test@college.edu", s.getEmail());
        assertEquals("1234567890", s.getPhone());
        assertEquals("Computer Science", s.getDepartment());
        assertEquals(2, s.getYear());
        assertEquals("Male", s.getGender());
    }

    @Test
    public void testStudentParameterizedConstructor() {
        Student s = new Student(20, "Jane Doe", "jane@example.com", "9876543210", "Electronics", 3, "Female");
        assertEquals(20, s.getId());
        assertEquals("Jane Doe", s.getName());
        assertEquals("jane@example.com", s.getEmail());
        assertEquals("9876543210", s.getPhone());
        assertEquals("Electronics", s.getDepartment());
        assertEquals(3, s.getYear());
        assertEquals("Female", s.getGender());
        assertTrue(s.toString().contains("Jane Doe"));
    }
}
