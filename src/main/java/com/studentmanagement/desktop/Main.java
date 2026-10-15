package com.studentmanagement.desktop;

import com.studentmanagement.util.DBConnection;

import javax.swing.*;

/**
 * Main - Entry point for the Swing Desktop Application
 *
 * Module 5 Concept: Swing Application startup
 *
 * This class:
 *  1. Tests the database connection on startup.
 *  2. Sets the system look-and-feel for a native appearance.
 *  3. Launches LoginFrame on the Event Dispatch Thread (EDT).
 *
 * SwingUtilities.invokeLater() is required because all Swing GUI updates
 * must happen on the EDT to avoid thread-safety issues.
 */
public class Main {

    public static void main(String[] args) {

        // ── Set System Look-and-Feel ─────────────────────────────────────────
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("[Main] Could not set system L&F: " + e.getMessage());
        }

        // ── Check Database Connectivity ──────────────────────────────────────
        boolean dbConnected = DBConnection.testConnection();
        if (!dbConnected) {
            System.out.println("[Main] MySQL not connected. Running in Demo / Offline Mode with sample data.");
        }

        // ── Launch the Login window on the Event Dispatch Thread ─────────────
        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
    }
}
