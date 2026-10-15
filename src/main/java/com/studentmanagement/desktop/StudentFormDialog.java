package com.studentmanagement.desktop;

import com.studentmanagement.dao.StudentDAO;
import com.studentmanagement.model.Student;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;

/**
 * StudentFormDialog - Modal dialog for Add / Edit student
 *
 * Module 5 Concepts Demonstrated:
 *   • JDialog            – modal dialog window
 *   • JTextField         – text input fields
 *   • JComboBox          – drop-down selector
 *   • JRadioButton       – mutually exclusive option buttons
 *   • ButtonGroup        – groups radio buttons together
 *   • GridBagLayout      – flexible AWT layout manager
 *   • Input Validation   – checking fields before DB write
 */
public class StudentFormDialog extends JDialog {

    // ── Form Fields ───────────────────────────────────────────────────────────
    private JTextField  nameField;
    private JTextField  emailField;
    private JTextField  phoneField;
    private JComboBox<String> deptCombo;
    private JComboBox<Integer> yearCombo;
    private JRadioButton maleRadio;
    private JRadioButton femaleRadio;
    private JRadioButton otherRadio;

    // ── State ──────────────────────────────────────────────────────────────────
    private boolean saved = false;
    private final Student existingStudent; // null → Add mode, non-null → Edit mode
    private final StudentDAO studentDAO = new StudentDAO();

    // ── Colors ────────────────────────────────────────────────────────────────
    private static final Color BG        = new Color(30, 30, 47);
    private static final Color BG_FIELD  = new Color(50, 50, 72);
    private static final Color ACCENT    = new Color(70, 130, 210);
    private static final Color TEXT      = new Color(220, 220, 240);
    private static final Color TEXT_MUTED= new Color(140, 140, 165);

    // ── Constructor ───────────────────────────────────────────────────────────
    public StudentFormDialog(JFrame parent, Student student) {
        super(parent, student == null ? "Add New Student" : "Edit Student", true);
        this.existingStudent = student;
        initComponents();
        if (student != null) populateFields(student);
        pack();
        setResizable(false);
        setLocationRelativeTo(parent);
    }

    // ── UI Initialisation ─────────────────────────────────────────────────────
    private void initComponents() {
        JPanel root = new JPanel(new BorderLayout(0, 0));
        root.setBackground(BG);
        root.setBorder(new EmptyBorder(20, 25, 20, 25));

        // ── Form panel ────────────────────────────────────────────────────────
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(BG);
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(7, 5, 7, 5);
        gc.fill   = GridBagConstraints.HORIZONTAL;
        gc.anchor = GridBagConstraints.WEST;

        int row = 0;

        // Name
        gc.gridx = 0; gc.gridy = row; gc.weightx = 0;
        form.add(label("Full Name *"), gc);
        gc.gridx = 1; gc.weightx = 1;
        nameField = textField(); form.add(nameField, gc); row++;

        // Email
        gc.gridx = 0; gc.gridy = row; gc.weightx = 0;
        form.add(label("Email"), gc);
        gc.gridx = 1; gc.weightx = 1;
        emailField = textField(); form.add(emailField, gc); row++;

        // Phone
        gc.gridx = 0; gc.gridy = row; gc.weightx = 0;
        form.add(label("Phone"), gc);
        gc.gridx = 1; gc.weightx = 1;
        phoneField = textField(); form.add(phoneField, gc); row++;

        // Department
        gc.gridx = 0; gc.gridy = row; gc.weightx = 0;
        form.add(label("Department"), gc);
        gc.gridx = 1; gc.weightx = 1;
        deptCombo = new JComboBox<>(new String[]{
            "Computer Science", "Information Technology", "Electronics",
            "Mechanical", "Civil Engineering", "Electrical", "Other"
        });
        styleCombo(deptCombo); form.add(deptCombo, gc); row++;

        // Year
        gc.gridx = 0; gc.gridy = row; gc.weightx = 0;
        form.add(label("Year"), gc);
        gc.gridx = 1; gc.weightx = 1;
        yearCombo = new JComboBox<>(new Integer[]{1, 2, 3, 4});
        styleCombo(yearCombo); form.add(yearCombo, gc); row++;

        // Gender
        gc.gridx = 0; gc.gridy = row; gc.weightx = 0;
        form.add(label("Gender"), gc);
        gc.gridx = 1; gc.weightx = 1;
        JPanel genderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        genderPanel.setBackground(BG);
        maleRadio   = radio("Male");
        femaleRadio = radio("Female");
        otherRadio  = radio("Other");
        ButtonGroup bg = new ButtonGroup();
        bg.add(maleRadio); bg.add(femaleRadio); bg.add(otherRadio);
        maleRadio.setSelected(true);
        genderPanel.add(maleRadio); genderPanel.add(femaleRadio); genderPanel.add(otherRadio);
        form.add(genderPanel, gc); row++;

        // ── Buttons ───────────────────────────────────────────────────────────
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnPanel.setBackground(BG);
        btnPanel.setBorder(new EmptyBorder(15, 0, 0, 0));

        JButton cancelBtn = makeBtn("Cancel", new Color(90, 90, 115), 100, 34);
        JButton saveBtn   = makeBtn(existingStudent == null ? "Add Student" : "Save Changes",
                                   ACCENT, 130, 34);
        saveBtn.setName("saveBtn");
        cancelBtn.setName("cancelBtn");

        cancelBtn.addActionListener(e -> dispose());
        saveBtn.addActionListener(e -> performSave());

        btnPanel.add(cancelBtn);
        btnPanel.add(saveBtn);

        root.add(form,     BorderLayout.CENTER);
        root.add(btnPanel, BorderLayout.SOUTH);
        setContentPane(root);
        getContentPane().setBackground(BG);
    }

    private void populateFields(Student s) {
        nameField.setText(s.getName());
        emailField.setText(s.getEmail());
        phoneField.setText(s.getPhone());
        deptCombo.setSelectedItem(s.getDepartment());
        yearCombo.setSelectedItem(s.getYear());
        switch (s.getGender()) {
            case "Female" -> femaleRadio.setSelected(true);
            case "Other"  -> otherRadio.setSelected(true);
            default       -> maleRadio.setSelected(true);
        }
    }

    // ── Save Logic ────────────────────────────────────────────────────────────
    private void performSave() {
        String name = nameField.getText().trim();
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name is required.", "Validation", JOptionPane.WARNING_MESSAGE);
            nameField.requestFocus();
            return;
        }

        String gender = maleRadio.isSelected() ? "Male" : femaleRadio.isSelected() ? "Female" : "Other";

        Student s = new Student(
            name,
            emailField.getText().trim(),
            phoneField.getText().trim(),
            (String)  deptCombo.getSelectedItem(),
            (Integer) yearCombo.getSelectedItem(),
            gender
        );

        boolean ok;
        if (existingStudent == null) {
            ok = studentDAO.addStudent(s);
        } else {
            s.setId(existingStudent.getId());
            ok = studentDAO.updateStudent(s);
        }

        if (ok) {
            saved = true;
            dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                "Database operation failed. Please check the connection.",
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean isSaved() { return saved; }

    // ── Styling Helpers ───────────────────────────────────────────────────────
    private JLabel label(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        l.setForeground(TEXT);
        l.setPreferredSize(new Dimension(110, 28));
        return l;
    }

    private JTextField textField() {
        JTextField f = new JTextField(22);
        f.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        f.setBackground(BG_FIELD);
        f.setForeground(Color.WHITE);
        f.setCaretColor(Color.WHITE);
        f.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(80, 100, 160)),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        return f;
    }

    private <T> void styleCombo(JComboBox<T> combo) {
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        combo.setBackground(BG_FIELD);
        combo.setForeground(Color.WHITE);
        combo.setPreferredSize(new Dimension(220, 32));
    }

    private JRadioButton radio(String text) {
        JRadioButton r = new JRadioButton(text);
        r.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        r.setBackground(BG);
        r.setForeground(TEXT);
        return r;
    }

    private JButton makeBtn(String text, Color bg, int w, int h) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setPreferredSize(new Dimension(w, h));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
}
