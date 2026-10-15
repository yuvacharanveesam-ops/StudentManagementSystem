package com.studentmanagement.desktop;

import com.studentmanagement.dao.StudentDAO;
import com.studentmanagement.model.Student;
import com.studentmanagement.util.DBConnection;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

/**
 * DashboardFrame - Main Application Window
 *
 * Module 5 Concepts Demonstrated:
 *   • JFrame, JPanel, JTable, JScrollPane
 *   • DefaultTableModel          – mutable TableModel for JTable
 *   • JToolBar                   – floating/fixed toolbar
 *   • JTextField (search bar)    – live search input
 *   • ActionListener, MouseListener – event handling
 *   • BorderLayout, BoxLayout    – AWT layout managers
 *   • JOptionPane                – confirmation/input dialogs
 */
public class DashboardFrame extends JFrame {

    // ── Components ────────────────────────────────────────────────────────────
    private JTable            studentTable;
    private DefaultTableModel tableModel;
    private JTextField        searchField;
    private JLabel            statusLabel;
    private JLabel            welcomeLabel;

    // ── DAO ───────────────────────────────────────────────────────────────────
    private final StudentDAO studentDAO = new StudentDAO();
    private final String     loggedInUser;

    // ── Table column names ────────────────────────────────────────────────────
    private static final String[] COLUMNS = {"ID", "Name", "Email", "Phone", "Department", "Year", "Gender"};

    // ── Colors ────────────────────────────────────────────────────────────────
    private static final Color BG_DARK      = new Color(25, 25, 40);
    private static final Color BG_PANEL     = new Color(35, 35, 55);
    private static final Color BG_TABLE     = new Color(40, 40, 62);
    private static final Color BG_ROW_ALT   = new Color(45, 45, 70);
    private static final Color ACCENT_BLUE  = new Color(70, 130, 210);
    private static final Color ACCENT_GREEN = new Color(60, 180, 100);
    private static final Color ACCENT_RED   = new Color(210, 70, 70);
    private static final Color ACCENT_AMBER = new Color(210, 160, 50);
    private static final Color TEXT_PRIMARY = new Color(220, 220, 240);
    private static final Color TEXT_MUTED   = new Color(140, 140, 165);

    // ── Constructor ───────────────────────────────────────────────────────────
    public DashboardFrame(String username) {
        this.loggedInUser = username;
        initComponents();
        setupEventHandlers();
        loadStudents();
    }

    // ── UI Initialisation ─────────────────────────────────────────────────────
    private void initComponents() {
        setTitle("Student Management System – Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1050, 660);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(800, 500));

        // Root content panel
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BG_DARK);
        setContentPane(root);

        root.add(buildTopBar(),    BorderLayout.NORTH);
        root.add(buildTableArea(), BorderLayout.CENTER);
        root.add(buildStatusBar(), BorderLayout.SOUTH);
    }

    // ── Top Bar (title + search + logout) ─────────────────────────────────────
    private JPanel buildTopBar() {
        JPanel topBar = new JPanel(new BorderLayout(15, 0));
        topBar.setBackground(new Color(20, 20, 35));
        topBar.setBorder(new EmptyBorder(12, 20, 12, 20));

        // Left: app title
        welcomeLabel = new JLabel("👤  Welcome, " + loggedInUser + "   |   Student Records");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        welcomeLabel.setForeground(new Color(100, 200, 255));

        // Center: search bar
        JPanel searchPanel = new JPanel(new BorderLayout(8, 0));
        searchPanel.setOpaque(false);

        JLabel searchIcon = new JLabel("🔍");
        searchIcon.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(280, 32));
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        searchField.setBackground(new Color(50, 50, 75));
        searchField.setForeground(Color.WHITE);
        searchField.setCaretColor(Color.WHITE);
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(80, 100, 160)),
            BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));
        searchField.putClientProperty("JTextField.placeholderText", "Search name, email, department…");

        JButton searchBtn = makeButton("Search", ACCENT_BLUE, 85, 32);
        searchBtn.setName("searchBtn");

        searchPanel.add(searchIcon, BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchBtn,  BorderLayout.EAST);

        // Right: logout
        JButton logoutBtn = makeButton("Logout", ACCENT_RED, 85, 32);
        logoutBtn.setName("logoutBtn");
        logoutBtn.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });

        // Toolbar with CRUD buttons (between top bar and table)
        JPanel toolRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        toolRow.setOpaque(false);

        JButton addBtn    = makeButton("➕  Add",    ACCENT_GREEN, 105, 34);
        JButton editBtn   = makeButton("✏️  Edit",   ACCENT_BLUE,  105, 34);
        JButton deleteBtn = makeButton("🗑️  Delete", ACCENT_RED,   105, 34);
        JButton refreshBtn= makeButton("🔄  Refresh",new Color(80,80,110), 105, 34);

        addBtn.setName("addBtn");
        editBtn.setName("editBtn");
        deleteBtn.setName("deleteBtn");
        refreshBtn.setName("refreshBtn");

        addBtn.addActionListener(e -> openStudentForm(null));
        editBtn.addActionListener(e -> editSelected());
        deleteBtn.addActionListener(e -> deleteSelected());
        refreshBtn.addActionListener(e -> { searchField.setText(""); loadStudents(); });

        toolRow.add(addBtn);
        toolRow.add(editBtn);
        toolRow.add(deleteBtn);
        toolRow.add(refreshBtn);

        searchBtn.addActionListener(e -> searchStudents());

        // Assemble
        JPanel rightGroup = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        rightGroup.setOpaque(false);
        rightGroup.add(searchPanel);
        rightGroup.add(logoutBtn);

        JPanel topFull = new JPanel(new BorderLayout());
        topFull.setBackground(new Color(20, 20, 35));

        topBar.add(welcomeLabel, BorderLayout.WEST);
        topBar.add(rightGroup,   BorderLayout.EAST);

        JPanel toolBarRow = new JPanel(new BorderLayout());
        toolBarRow.setBackground(new Color(28, 28, 45));
        toolBarRow.setBorder(new EmptyBorder(8, 20, 8, 20));
        toolBarRow.add(toolRow, BorderLayout.WEST);

        topFull.add(topBar,     BorderLayout.NORTH);
        topFull.add(toolBarRow, BorderLayout.CENTER);
        return topFull;
    }

    // ── Table Area ─────────────────────────────────────────────────────────────
    private JScrollPane buildTableArea() {
        // Non-editable table model
        tableModel = new DefaultTableModel(COLUMNS, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };

        studentTable = new JTable(tableModel);
        studentTable.setName("studentTable");
        studentTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        studentTable.setForeground(TEXT_PRIMARY);
        studentTable.setBackground(BG_TABLE);
        studentTable.setSelectionBackground(new Color(70, 130, 210, 180));
        studentTable.setSelectionForeground(Color.WHITE);
        studentTable.setRowHeight(30);
        studentTable.setShowGrid(false);
        studentTable.setIntercellSpacing(new Dimension(0, 0));
        studentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        studentTable.setFillsViewportHeight(true);

        // Alternating row colours via custom renderer
        studentTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int col) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
                if (!isSelected) {
                    setBackground(row % 2 == 0 ? BG_TABLE : BG_ROW_ALT);
                    setForeground(TEXT_PRIMARY);
                }
                setBorder(new EmptyBorder(0, 10, 0, 10));
                return this;
            }
        });

        // Header styling
        studentTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        studentTable.getTableHeader().setBackground(new Color(50, 50, 80));
        studentTable.getTableHeader().setForeground(new Color(150, 200, 255));
        studentTable.getTableHeader().setReorderingAllowed(false);

        // Column widths
        int[] widths = {50, 160, 200, 120, 160, 55, 80};
        for (int i = 0; i < widths.length; i++) {
            studentTable.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
        }

        JScrollPane scroll = new JScrollPane(studentTable);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(BG_TABLE);
        scroll.setBackground(BG_DARK);
        return scroll;
    }

    // ── Status Bar ─────────────────────────────────────────────────────────────
    private JPanel buildStatusBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(new Color(20, 20, 35));
        bar.setBorder(new EmptyBorder(5, 20, 5, 20));

        statusLabel = new JLabel("Ready");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        statusLabel.setForeground(TEXT_MUTED);

        boolean isConnected = DBConnection.testConnection();
        JLabel dbLabel = new JLabel(isConnected ? "🟢 MySQL Connected" : "🟡 Demo Mode (In-Memory)");
        dbLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        dbLabel.setForeground(isConnected ? new Color(80, 200, 120) : new Color(255, 190, 70));

        bar.add(statusLabel, BorderLayout.WEST);
        bar.add(dbLabel,     BorderLayout.EAST);
        return bar;
    }

    // ── Event Handlers ────────────────────────────────────────────────────────
    private void setupEventHandlers() {
        // Double-click row → edit
        studentTable.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) editSelected();
            }
        });

        // Live search on Enter key
        searchField.addActionListener(e -> searchStudents());
    }

    // ── Data Operations ───────────────────────────────────────────────────────
    private void loadStudents() {
        List<Student> list = studentDAO.getAllStudents();
        populateTable(list);
        setStatus("Loaded " + list.size() + " student(s).");
    }

    private void searchStudents() {
        String kw = searchField.getText().trim();
        if (kw.isEmpty()) { loadStudents(); return; }
        List<Student> list = studentDAO.searchStudents(kw);
        populateTable(list);
        setStatus("Found " + list.size() + " result(s) for \"" + kw + "\".");
    }

    private void populateTable(List<Student> students) {
        tableModel.setRowCount(0);
        for (Student s : students) {
            tableModel.addRow(new Object[]{
                s.getId(), s.getName(), s.getEmail(),
                s.getPhone(), s.getDepartment(), s.getYear(), s.getGender()
            });
        }
    }

    private void editSelected() {
        int row = studentTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select a student to edit.",
                "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int id = (int) tableModel.getValueAt(row, 0);
        Student s = studentDAO.getStudentById(id);
        if (s != null) openStudentForm(s);
    }

    private void deleteSelected() {
        int row = studentTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select a student to delete.",
                "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String name = (String) tableModel.getValueAt(row, 1);
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Delete student \"" + name + "\"?\nThis action cannot be undone.",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );
        if (confirm == JOptionPane.YES_OPTION) {
            int id = (int) tableModel.getValueAt(row, 0);
            boolean ok = studentDAO.deleteStudent(id);
            if (ok) {
                setStatus("Deleted student: " + name);
                loadStudents();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete student.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void openStudentForm(Student student) {
        StudentFormDialog dialog = new StudentFormDialog(this, student);
        dialog.setVisible(true);
        if (dialog.isSaved()) {
            loadStudents();
            setStatus(student == null ? "New student added." : "Student updated.");
        }
    }

    private void setStatus(String msg) {
        statusLabel.setText(msg);
    }

    // ── Button Factory ────────────────────────────────────────────────────────
    private JButton makeButton(String text, Color bg, int w, int h) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setPreferredSize(new Dimension(w, h));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new MouseAdapter() {
            final Color original = bg;
            @Override public void mouseEntered(MouseEvent e) {
                btn.setBackground(original.brighter());
            }
            @Override public void mouseExited(MouseEvent e) {
                btn.setBackground(original);
            }
        });
        return btn;
    }
}
