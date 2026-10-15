package com.studentmanagement.desktop;

import com.studentmanagement.dao.StudentDAO;
import com.studentmanagement.model.Student;
import com.studentmanagement.util.DBConnection;

import java.awt.*;
import java.awt.event.*;
import java.util.List;

/**
 * AwtStudentApp - Pure Java AWT Implementation of Student Management System
 *
 * Demonstrates ALL Module-5 AWT Concepts:
 *   1. Containers: Frame, Panel, Dialog (Modal/Non-Modal), ScrollPane
 *   2. Components: Label, Button, TextField, TextArea, Choice, Checkbox, CheckboxGroup, List
 *   3. Menus: MenuBar, Menu, MenuItem
 *   4. Layout Managers: BorderLayout, FlowLayout, GridLayout
 *   5. Event Handling: ActionListener, WindowAdapter, windowClosing, ItemListener
 *   6. JDBC Integration: StudentDAO with PreparedStatement
 */
public class AwtStudentApp extends Frame implements ActionListener {

    // ── Form Input Components (AWT) ───────────────────────────────────────────
    private TextField txtId;
    private TextField txtName;
    private TextField txtEmail;
    private TextField txtPhone;
    private Choice    choiceDept;
    private Choice    choiceYear;
    private CheckboxGroup cgGender;
    private Checkbox  chkMale;
    private Checkbox  chkFemale;

    // ── Search & Output Components (AWT) ──────────────────────────────────────
    private TextField txtSearch;
    private Button    btnSearch;
    private Button    btnShowAll;
    private TextArea  txtAreaDisplay;
    private Label     lblStatus;

    // ── Action Buttons (AWT) ──────────────────────────────────────────────────
    private Button btnAdd;
    private Button btnUpdate;
    private Button btnDelete;
    private Button btnClear;

    // ── Menu Components (AWT) ─────────────────────────────────────────────────
    private MenuBar  menuBar;
    private Menu     menuFile, menuHelp;
    private MenuItem itemRefresh, itemExit, itemAbout;

    // ── Backend DAO ───────────────────────────────────────────────────────────
    private final StudentDAO studentDAO = new StudentDAO();

    public AwtStudentApp() {
        super("Student Management System – Pure Java AWT");
        initAwtUI();
        loadStudentData();
    }

    private void initAwtUI() {
        // ── 1. Window Configuration ──────────────────────────────────────────
        setSize(850, 680);
        setLayout(new BorderLayout(10, 10));
        setLocationRelativeTo(null);
        setBackground(new Color(240, 243, 246));

        // ── 2. MenuBar, Menu, MenuItem ────────────────────────────────────────
        menuBar = new MenuBar();
        menuFile = new Menu("File");
        menuHelp = new Menu("Help");

        itemRefresh = new MenuItem("Refresh List");
        itemExit = new MenuItem("Exit");
        itemAbout = new MenuItem("About SMS");

        itemRefresh.addActionListener(this);
        itemExit.addActionListener(this);
        itemAbout.addActionListener(this);

        menuFile.add(itemRefresh);
        menuFile.addSeparator();
        menuFile.add(itemExit);
        menuHelp.add(itemAbout);

        menuBar.add(menuFile);
        menuBar.add(menuHelp);
        setMenuBar(menuBar);

        // ── 3. NORTH REGION: Header & Search Panel (Panel + FlowLayout) ──────
        Panel northPanel = new Panel(new BorderLayout(5, 5));
        northPanel.setBackground(new Color(41, 128, 185));

        Label lblTitle = new Label("STUDENT MANAGEMENT SYSTEM (JAVA AWT)", Label.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitle.setForeground(Color.WHITE);
        northPanel.add(lblTitle, BorderLayout.NORTH);

        Panel searchPanel = new Panel(new FlowLayout(FlowLayout.CENTER, 10, 8));
        Label lblSearch = new Label("Search (Name/Dept/Email):");
        lblSearch.setForeground(Color.WHITE);
        txtSearch = new TextField(22);
        btnSearch = new Button("Search");
        btnShowAll = new Button("Show All");

        btnSearch.addActionListener(this);
        btnShowAll.addActionListener(this);

        searchPanel.add(lblSearch);
        searchPanel.add(txtSearch);
        searchPanel.add(btnSearch);
        searchPanel.add(btnShowAll);
        northPanel.add(searchPanel, BorderLayout.SOUTH);

        add(northPanel, BorderLayout.NORTH);

        // ── 4. WEST REGION: Student Form Input (Panel + GridLayout) ───────────
        Panel formContainer = new Panel(new BorderLayout());
        formContainer.setBackground(new Color(250, 250, 252));

        Label lblFormHeader = new Label("── Student Details Form ──", Label.CENTER);
        lblFormHeader.setFont(new Font("Arial", Font.BOLD, 13));
        formContainer.add(lblFormHeader, BorderLayout.NORTH);

        Panel formGrid = new Panel(new GridLayout(7, 2, 8, 12));
        formGrid.setPreferredSize(new Dimension(340, 280));

        // ID (Read-only)
        formGrid.add(new Label("Student ID (for Edit/Delete):"));
        txtId = new TextField();
        formGrid.add(txtId);

        // Name
        formGrid.add(new Label("Full Name:"));
        txtName = new TextField();
        formGrid.add(txtName);

        // Email
        formGrid.add(new Label("Email:"));
        txtEmail = new TextField();
        formGrid.add(txtEmail);

        // Phone
        formGrid.add(new Label("Phone:"));
        txtPhone = new TextField();
        formGrid.add(txtPhone);

        // Department (Choice dropdown)
        formGrid.add(new Label("Department:"));
        choiceDept = new Choice();
        choiceDept.add("Computer Science");
        choiceDept.add("Information Technology");
        choiceDept.add("Electronics & Comm.");
        choiceDept.add("Electrical & Electronics");
        choiceDept.add("Mechanical");
        choiceDept.add("Civil");
        formGrid.add(choiceDept);

        // Academic Year (Choice dropdown)
        formGrid.add(new Label("Academic Year:"));
        choiceYear = new Choice();
        choiceYear.add("1");
        choiceYear.add("2");
        choiceYear.add("3");
        choiceYear.add("4");
        formGrid.add(choiceYear);

        // Gender (CheckboxGroup - Radio button behavior)
        formGrid.add(new Label("Gender:"));
        Panel genderPanel = new Panel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        cgGender = new CheckboxGroup();
        chkMale = new Checkbox("Male", cgGender, true);
        chkFemale = new Checkbox("Female", cgGender, false);
        genderPanel.add(chkMale);
        genderPanel.add(chkFemale);
        formGrid.add(genderPanel);

        formContainer.add(formGrid, BorderLayout.CENTER);

        // Button Bar under Form (Panel + FlowLayout)
        Panel buttonBar = new Panel(new FlowLayout(FlowLayout.CENTER, 8, 10));
        btnAdd = new Button("Add Student");
        btnUpdate = new Button("Update");
        btnDelete = new Button("Delete");
        btnClear = new Button("Clear Form");

        btnAdd.addActionListener(this);
        btnUpdate.addActionListener(this);
        btnDelete.addActionListener(this);
        btnClear.addActionListener(this);

        buttonBar.add(btnAdd);
        buttonBar.add(btnUpdate);
        buttonBar.add(btnDelete);
        buttonBar.add(btnClear);
        formContainer.add(buttonBar, BorderLayout.SOUTH);

        add(formContainer, BorderLayout.WEST);

        // ── 5. CENTER REGION: Output Display (TextArea inside ScrollPane) ────
        Panel centerPanel = new Panel(new BorderLayout(5, 5));
        Label lblListHeader = new Label("Registered Student Records (Display Area):", Label.LEFT);
        lblListHeader.setFont(new Font("Arial", Font.BOLD, 13));
        centerPanel.add(lblListHeader, BorderLayout.NORTH);

        txtAreaDisplay = new TextArea(20, 50);
        txtAreaDisplay.setFont(new Font("Monospaced", Font.PLAIN, 12));
        txtAreaDisplay.setEditable(false);
        centerPanel.add(txtAreaDisplay, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);

        // ── 6. SOUTH REGION: Status Bar (Panel + Label) ───────────────────────
        Panel southPanel = new Panel(new BorderLayout());
        southPanel.setBackground(new Color(220, 225, 230));
        lblStatus = new Label(" Status: System ready. Running on Java AWT + JDBC (MySQL/In-Memory).");
        lblStatus.setFont(new Font("Arial", Font.PLAIN, 12));
        southPanel.add(lblStatus, BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);

        // ── 7. Window Event Listener (WindowAdapter) ──────────────────────────
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
                System.exit(0);
            }
        });
    }

    // ── Event Handling (ActionListener implementation) ────────────────────────
    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();

        if (src == btnAdd) {
            handleAddStudent();
        } else if (src == btnUpdate) {
            handleUpdateStudent();
        } else if (src == btnDelete) {
            handleDeleteStudent();
        } else if (src == btnClear) {
            clearForm();
        } else if (src == btnSearch) {
            handleSearch();
        } else if (src == btnShowAll || src == itemRefresh) {
            loadStudentData();
        } else if (src == itemExit) {
            dispose();
            System.exit(0);
        } else if (src == itemAbout) {
            showAboutDialog();
        }
    }

    // ── CRUD Handlers ─────────────────────────────────────────────────────────

    private void handleAddStudent() {
        String name = txtName.getText().trim();
        String email = txtEmail.getText().trim();
        String phone = txtPhone.getText().trim();
        String dept = choiceDept.getSelectedItem();
        int year = Integer.parseInt(choiceYear.getSelectedItem());
        String gender = cgGender.getSelectedCheckbox().getLabel();

        if (name.isEmpty() || email.isEmpty()) {
            showModalMessage("Validation Error", "Name and Email are mandatory fields!");
            return;
        }

        Student s = new Student(name, email, phone, dept, year, gender);
        boolean success = studentDAO.addStudent(s);

        if (success) {
            showModalMessage("Success", "Student added successfully! ID: " + s.getId());
            clearForm();
            loadStudentData();
        } else {
            showModalMessage("Error", "Failed to add student to database.");
        }
    }

    private void handleUpdateStudent() {
        String idStr = txtId.getText().trim();
        if (idStr.isEmpty()) {
            showModalMessage("Required ID", "Please enter the Student ID to update.");
            return;
        }

        try {
            int id = Integer.parseInt(idStr);
            String name = txtName.getText().trim();
            String email = txtEmail.getText().trim();
            String phone = txtPhone.getText().trim();
            String dept = choiceDept.getSelectedItem();
            int year = Integer.parseInt(choiceYear.getSelectedItem());
            String gender = cgGender.getSelectedCheckbox().getLabel();

            Student s = new Student(id, name, email, phone, dept, year, gender);
            boolean success = studentDAO.updateStudent(s);

            if (success) {
                showModalMessage("Success", "Student ID " + id + " updated successfully!");
                clearForm();
                loadStudentData();
            } else {
                showModalMessage("Update Failed", "No student found with ID: " + id);
            }
        } catch (NumberFormatException ex) {
            showModalMessage("Invalid ID", "Student ID must be a numeric value.");
        }
    }

    private void handleDeleteStudent() {
        String idStr = txtId.getText().trim();
        if (idStr.isEmpty()) {
            showModalMessage("Required ID", "Please enter the Student ID to delete.");
            return;
        }

        try {
            int id = Integer.parseInt(idStr);
            boolean success = studentDAO.deleteStudent(id);
            if (success) {
                showModalMessage("Deleted", "Student ID " + id + " removed successfully.");
                clearForm();
                loadStudentData();
            } else {
                showModalMessage("Delete Failed", "No student found with ID: " + id);
            }
        } catch (NumberFormatException ex) {
            showModalMessage("Invalid ID", "Student ID must be a numeric value.");
        }
    }

    private void handleSearch() {
        String keyword = txtSearch.getText().trim();
        if (keyword.isEmpty()) {
            loadStudentData();
            return;
        }

        List<Student> list = studentDAO.searchStudents(keyword);
        renderTable(list, "Search results for: '" + keyword + "' (" + list.size() + " found)");
    }

    private void loadStudentData() {
        List<Student> list = studentDAO.getAllStudents();
        renderTable(list, "All Students (" + list.size() + " registered)");
    }

    private void renderTable(List<Student> list, String headerTitle) {
        StringBuilder sb = new StringBuilder();
        sb.append("=========================================================================================\n");
        sb.append(String.format(" %-5s | %-20s | %-24s | %-12s | %-16s | %-4s | %-6s\n",
                "ID", "NAME", "EMAIL", "PHONE", "DEPARTMENT", "YEAR", "GENDER"));
        sb.append("=========================================================================================\n");

        if (list.isEmpty()) {
            sb.append("                       No student records found.\n");
        } else {
            for (Student s : list) {
                sb.append(String.format(" %-5d | %-20s | %-24s | %-12s | %-16s | %-4d | %-6s\n",
                        s.getId(),
                        truncate(s.getName(), 20),
                        truncate(s.getEmail(), 24),
                        truncate(s.getPhone(), 12),
                        truncate(s.getDepartment(), 16),
                        s.getYear(),
                        s.getGender()));
            }
        }
        sb.append("=========================================================================================\n");
        txtAreaDisplay.setText(sb.toString());
        lblStatus.setText(" Status: " + headerTitle);
    }

    private String truncate(String val, int max) {
        if (val == null) return "";
        return val.length() <= max ? val : val.substring(0, max - 3) + "...";
    }

    private void clearForm() {
        txtId.setText("");
        txtName.setText("");
        txtEmail.setText("");
        txtPhone.setText("");
        txtSearch.setText("");
        choiceDept.select(0);
        choiceYear.select(0);
        chkMale.setState(true);
    }

    // ── Modal Dialog Example (Pure AWT Dialog) ────────────────────────────────
    private void showModalMessage(String title, String message) {
        Dialog d = new Dialog(this, title, true); // true = modal dialog
        d.setSize(360, 160);
        d.setLayout(new BorderLayout(10, 10));
        d.setLocationRelativeTo(this);

        Label lbl = new Label(message, Label.CENTER);
        lbl.setFont(new Font("Arial", Font.PLAIN, 12));
        d.add(lbl, BorderLayout.CENTER);

        Panel pBtn = new Panel(new FlowLayout());
        Button btnOk = new Button("  OK  ");
        btnOk.addActionListener(e -> d.dispose());
        pBtn.add(btnOk);
        d.add(pBtn, BorderLayout.SOUTH);

        d.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                d.dispose();
            }
        });

        d.setVisible(true);
    }

    private void showAboutDialog() {
        Dialog d = new Dialog(this, "About Student Management System", true);
        d.setSize(400, 220);
        d.setLayout(new BorderLayout(10, 10));
        d.setLocationRelativeTo(this);

        TextArea ta = new TextArea(
            "Java Assessment-4 (Module-5)\n" +
            "Topic: GUI Programming with Java – AWT & JDBC Architecture\n\n" +
            "Features Demonstrated:\n" +
            "• AWT Containers: Frame, Panel, Dialog, ScrollPane\n" +
            "• AWT Components: Button, Label, TextField, TextArea, Choice, CheckboxGroup\n" +
            "• AWT Menus: MenuBar, Menu, MenuItem\n" +
            "• Layout Managers: BorderLayout, GridLayout, FlowLayout\n" +
            "• Event Handling: ActionListener, WindowAdapter\n" +
            "• Database: MySQL with JDBC PreparedStatement & DAO Pattern\n",
            8, 40, TextArea.SCROLLBARS_VERTICAL_ONLY
        );
        ta.setEditable(false);
        d.add(ta, BorderLayout.CENTER);

        Panel p = new Panel(new FlowLayout());
        Button b = new Button("Close");
        b.addActionListener(e -> d.dispose());
        p.add(b);
        d.add(p, BorderLayout.SOUTH);

        d.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                d.dispose();
            }
        });

        d.setVisible(true);
    }

    // ── Application Entry Point ───────────────────────────────────────────────
    public static void main(String[] args) {
        boolean dbConnected = DBConnection.testConnection();
        if (!dbConnected) {
            System.out.println("[AwtStudentApp] MySQL not connected. Using In-Memory Fallback Demo.");
        }
        AwtStudentApp app = new AwtStudentApp();
        app.setVisible(true);
    }
}
