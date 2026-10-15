package com.studentmanagement.desktop;

import com.studentmanagement.dao.StudentDAO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * LoginFrame - Desktop Login Window
 *
 * Module 5 Concepts Demonstrated:
 *   • JFrame                   – top-level Swing window
 *   • JPanel                   – container for grouping components
 *   • JLabel                   – text label (AWT component base)
 *   • JTextField               – single-line text input
 *   • JPasswordField           – masked password input
 *   • JButton                  – clickable button
 *   • BorderLayout             – AWT layout manager for outer frame
 *   • GridLayout               – AWT layout manager for form fields
 *   • ActionListener           – event handling interface
 *   • JOptionPane              – standard dialog boxes
 */
public class LoginFrame extends JFrame {

    // ── Swing Components ──────────────────────────────────────────────────────
    private JTextField     usernameField;
    private JPasswordField passwordField;
    private JButton        loginButton;
    private JButton        clearButton;

    // ── DAO for database validation ────────────────────────────────────────────
    private final StudentDAO studentDAO = new StudentDAO();

    // ── Constructor ───────────────────────────────────────────────────────────
    public LoginFrame() {
        initComponents();
        setupEventHandlers();
    }

    // ── UI Initialisation ─────────────────────────────────────────────────────
    private void initComponents() {
        // JFrame configuration
        setTitle("Student Management System – Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 380);
        setLocationRelativeTo(null);   // center on screen
        setResizable(false);

        // ── Outer panel: BorderLayout ────────────────────────────────────────
        // BorderLayout divides the container into 5 regions: N, S, E, W, CENTER
        JPanel outerPanel = new JPanel(new BorderLayout());
        outerPanel.setBackground(new Color(30, 30, 47));
        outerPanel.setBorder(new EmptyBorder(30, 40, 30, 40));

        // ── NORTH: Title header ──────────────────────────────────────────────
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(30, 30, 47));
        headerPanel.setBorder(new EmptyBorder(0, 0, 20, 0));

        JLabel titleLabel = new JLabel("STUDENT MANAGEMENT SYSTEM", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(new Color(100, 200, 255));

        JLabel subtitleLabel = new JLabel("Please login to continue", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtitleLabel.setForeground(new Color(160, 160, 180));

        headerPanel.add(titleLabel,    BorderLayout.NORTH);
        headerPanel.add(subtitleLabel, BorderLayout.SOUTH);

        // ── CENTER: Form panel with GridLayout ───────────────────────────────
        // GridLayout arranges components in a regular grid of rows and columns
        JPanel formPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        formPanel.setBackground(new Color(30, 30, 47));

        // Username row
        JPanel userRow = new JPanel(new BorderLayout(10, 0));
        userRow.setBackground(new Color(30, 30, 47));
        JLabel userLabel = createLabel("Username:");
        usernameField    = new JTextField();
        styleTextField(usernameField);
        userRow.add(userLabel,     BorderLayout.WEST);
        userRow.add(usernameField, BorderLayout.CENTER);

        // Password row
        JPanel passRow = new JPanel(new BorderLayout(10, 0));
        passRow.setBackground(new Color(30, 30, 47));
        JLabel passLabel = createLabel("Password:");
        passwordField    = new JPasswordField();
        styleTextField(passwordField);
        passRow.add(passLabel,     BorderLayout.WEST);
        passRow.add(passwordField, BorderLayout.CENTER);

        // Button row using FlowLayout
        // FlowLayout arranges components left-to-right, wrapping as needed
        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonRow.setBackground(new Color(30, 30, 47));

        loginButton = createButton("LOGIN",  new Color(70, 130, 200));
        clearButton = createButton("CLEAR",  new Color(90, 90, 110));

        buttonRow.add(loginButton);
        buttonRow.add(clearButton);

        // Hint label row
        JLabel hintLabel = new JLabel("Default: admin / admin123", SwingConstants.CENTER);
        hintLabel.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        hintLabel.setForeground(new Color(120, 120, 140));

        formPanel.add(userRow);
        formPanel.add(passRow);
        formPanel.add(buttonRow);
        formPanel.add(hintLabel);

        // ── Assemble outer panel ─────────────────────────────────────────────
        outerPanel.add(headerPanel, BorderLayout.NORTH);
        outerPanel.add(formPanel,   BorderLayout.CENTER);

        setContentPane(outerPanel);
    }

    // ── Event Handling ────────────────────────────────────────────────────────
    /**
     * Sets up ActionListeners for all buttons.
     *
     * Module 5 Concept: Event Handling
     *   ActionListener is a functional interface with one method: actionPerformed(ActionEvent).
     *   When the user clicks a button, the AWT Event Queue dispatches an ActionEvent to
     *   all registered ActionListeners for that button.
     */
    private void setupEventHandlers() {

        // LOGIN button ActionListener – validates credentials and opens Dashboard
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performLogin();
            }
        });

        // CLEAR button ActionListener – clears input fields
        clearButton.addActionListener(e -> {
            usernameField.setText("");
            passwordField.setText("");
            usernameField.requestFocus();
        });

        // Allow pressing ENTER in the password field to trigger login
        passwordField.addActionListener(e -> performLogin());
    }

    /**
     * Reads form fields, validates them, and either opens the Dashboard
     * or shows an error dialog.
     */
    private void performLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        // ── Validation: empty fields ─────────────────────────────────────────
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(
                this,
                "Username and Password cannot be empty.",
                "Validation Error",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        // ── Database validation via DAO ──────────────────────────────────────
        boolean valid = studentDAO.validateUser(username, password);

        if (valid) {
            // Successful login: open dashboard, close login window
            DashboardFrame dashboard = new DashboardFrame(username);
            dashboard.setVisible(true);
            dispose(); // close this LoginFrame
        } else {
            JOptionPane.showMessageDialog(
                this,
                "Invalid username or password.\nPlease try again.",
                "Login Failed",
                JOptionPane.ERROR_MESSAGE
            );
            passwordField.setText("");
            passwordField.requestFocus();
        }
    }

    // ── Styling Helpers ───────────────────────────────────────────────────────

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        label.setForeground(new Color(200, 200, 220));
        label.setPreferredSize(new Dimension(90, 30));
        return label;
    }

    private void styleTextField(JTextField field) {
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setBackground(new Color(50, 50, 70));
        field.setForeground(Color.WHITE);
        field.setCaretColor(Color.WHITE);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(80, 100, 150)),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
    }

    private JButton createButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setPreferredSize(new Dimension(110, 36));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }
}
