package com.studentmanagement;

import com.studentmanagement.desktop.DashboardFrame;
import com.studentmanagement.desktop.LoginFrame;
import com.studentmanagement.desktop.StudentFormDialog;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.*;

public class DesktopUITest {

    @Test
    public void testLoginFrameInitialization() throws InterruptedException, InvocationTargetException {
        SwingUtilities.invokeAndWait(() -> {
            LoginFrame frame = new LoginFrame();
            assertNotNull(frame);
            assertEquals("Student Management System – Login", frame.getTitle());
            assertEquals(JFrame.EXIT_ON_CLOSE, frame.getDefaultCloseOperation());
            frame.dispose();
        });
    }

    @Test
    public void testDashboardFrameInitialization() throws InterruptedException, InvocationTargetException {
        SwingUtilities.invokeAndWait(() -> {
            DashboardFrame dashboard = new DashboardFrame("admin");
            assertNotNull(dashboard);
            assertEquals("Student Management System – Dashboard", dashboard.getTitle());
            assertTrue(dashboard.getMinimumSize().width >= 800);
            dashboard.dispose();
        });
    }

    @Test
    public void testStudentFormDialogCreation() throws InterruptedException, InvocationTargetException {
        SwingUtilities.invokeAndWait(() -> {
            JFrame dummyParent = new JFrame();
            StudentFormDialog addDialog = new StudentFormDialog(dummyParent, null);
            assertNotNull(addDialog);
            assertEquals("Add New Student", addDialog.getTitle());
            assertFalse(addDialog.isSaved());
            addDialog.dispose();
            dummyParent.dispose();
        });
    }
}
