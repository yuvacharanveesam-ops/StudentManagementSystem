package com.studentmanagement;

import com.studentmanagement.dao.StudentDAO;
import com.studentmanagement.model.Student;
import com.studentmanagement.desktop.LoginFrame;
import com.studentmanagement.desktop.DashboardFrame;
import com.studentmanagement.desktop.StudentFormDialog;

import javax.swing.*;
import java.util.List;

public class TestSuiteRunner {

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println("     STUDENT MANAGEMENT SYSTEM - TEST SUITE       ");
        System.out.println("=================================================\n");

        runTest("1. Student Model - Getters/Setters", () -> {
            Student s = new Student();
            s.setName("Test Student");
            s.setEmail("test@college.edu");
            if (!"Test Student".equals(s.getName()) || !"test@college.edu".equals(s.getEmail())) {
                throw new RuntimeException("Getter/Setter verification failed");
            }
        });

        runTest("2. Student Model - Parameterized Constructor", () -> {
            Student s = new Student(99, "Jane", "jane@test.com", "12345", "IT", 2, "Female");
            if (s.getId() != 99 || !"IT".equals(s.getDepartment())) {
                throw new RuntimeException("Constructor field assignment failed");
            }
        });

        StudentDAO dao = new StudentDAO();

        runTest("3. Authentication - Valid User (admin/admin123)", () -> {
            if (!dao.validateUser("admin", "admin123")) {
                throw new RuntimeException("Failed to validate valid credentials");
            }
        });

        runTest("4. Authentication - Invalid User Reject", () -> {
            if (dao.validateUser("admin", "wrongpassword")) {
                throw new RuntimeException("Wrong password was erroneously accepted");
            }
            if (dao.validateUser("unknown_user", "admin123")) {
                throw new RuntimeException("Unknown user was erroneously accepted");
            }
        });

        runTest("5. StudentDAO - Read All Students", () -> {
            List<Student> list = dao.getAllStudents();
            if (list == null || list.isEmpty()) {
                throw new RuntimeException("Student list is null or empty");
            }
        });

        runTest("6. StudentDAO - Add New Student", () -> {
            Student s = new Student("Automation Alpha", "alpha@test.com", "9999999999", "Computer Science", 1, "Male");
            if (!dao.addStudent(s)) {
                throw new RuntimeException("Failed to insert student");
            }
        });

        runTest("7. StudentDAO - Search Students", () -> {
            Student s = new Student("Search Query Test", "search@test.com", "8888888888", "Mechanical", 2, "Female");
            dao.addStudent(s);
            List<Student> results = dao.searchStudents("Search Query");
            if (results.isEmpty() || !results.get(0).getName().contains("Search Query")) {
                throw new RuntimeException("Search failed to return matching student");
            }
        });

        runTest("8. StudentDAO - Update Student", () -> {
            Student s = new Student("Original Name", "orig@test.com", "7777777777", "Electronics", 3, "Male");
            dao.addStudent(s);
            List<Student> found = dao.searchStudents("Original Name");
            if (found.isEmpty()) throw new RuntimeException("Could not find inserted student");

            Student toUpdate = found.get(0);
            toUpdate.setName("Modified Name");
            if (!dao.updateStudent(toUpdate)) {
                throw new RuntimeException("Update operation returned false");
            }

            Student verified = dao.getStudentById(toUpdate.getId());
            if (verified == null || !"Modified Name".equals(verified.getName())) {
                throw new RuntimeException("Updated data did not persist");
            }
        });

        runTest("9. StudentDAO - Delete Student", () -> {
            Student s = new Student("Candidate To Delete", "del@test.com", "6666666666", "Civil", 4, "Other");
            dao.addStudent(s);
            List<Student> found = dao.searchStudents("Candidate To Delete");
            if (found.isEmpty()) throw new RuntimeException("Could not find student to delete");

            int id = found.get(0).getId();
            if (!dao.deleteStudent(id)) {
                throw new RuntimeException("Delete operation returned false");
            }
            if (dao.getStudentById(id) != null) {
                throw new RuntimeException("Student still exists after deletion");
            }
        });

        runTest("10. Swing UI - LoginFrame Instantiation", () -> {
            SwingUtilities.invokeAndWait(() -> {
                LoginFrame lf = new LoginFrame();
                if (!lf.getTitle().contains("Login")) {
                    throw new RuntimeException("Incorrect Login title");
                }
                lf.dispose();
            });
        });

        runTest("11. Swing UI - DashboardFrame Instantiation", () -> {
            SwingUtilities.invokeAndWait(() -> {
                DashboardFrame df = new DashboardFrame("admin");
                if (!df.getTitle().contains("Dashboard")) {
                    throw new RuntimeException("Incorrect Dashboard title");
                }
                df.dispose();
            });
        });

        runTest("12. Swing UI - StudentFormDialog Instantiation", () -> {
            SwingUtilities.invokeAndWait(() -> {
                JFrame parent = new JFrame();
                StudentFormDialog dialog = new StudentFormDialog(parent, null);
                if (!dialog.getTitle().contains("Add New Student")) {
                    throw new RuntimeException("Incorrect Dialog title");
                }
                dialog.dispose();
                parent.dispose();
            });
        });

        System.out.println("\n-------------------------------------------------");
        System.out.printf(" TOTAL TESTS: %d | PASSED: %d | FAILED: %d\n", (passed + failed), passed, failed);
        System.out.println("-------------------------------------------------");

        if (failed == 0) {
            System.out.println(" [SUCCESS] ALL 12 TESTS PASSED!");
        } else {
            System.out.println(" [ERROR] SOME TESTS FAILED.");
            System.exit(1);
        }
    }

    private static void runTest(String testName, TestRunnable test) {
        try {
            test.run();
            System.out.printf("  [PASS] %s\n", testName);
            passed++;
        } catch (Throwable t) {
            System.out.printf("  [FAIL] %s - %s\n", testName, t.getMessage());
            failed++;
        }
    }

    @FunctionalInterface
    interface TestRunnable {
        void run() throws Throwable;
    }
}
