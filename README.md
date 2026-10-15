# Student Management System (SMS) - Java Desktop Application

> **Java Assessment-4 (Module-5 Implementation)**  
> Developed using **Java Swing, AWT, JDBC, MySQL, and Maven**.

---

## 📌 1. Project Overview & Introduction
The **Student Management System (SMS)** is an enterprise-grade desktop application designed for educational institutions to efficiently manage student records. It provides administrative personnel with an intuitive graphical interface to perform complete CRUD (Create, Read, Update, Delete) operations, dynamic multi-field search, data filtering, and secure user authentication.

The application strictly adheres to clean architectural design principles, implementing the **Data Access Object (DAO)** pattern, separation of concerns (UI, Business Model, Data Access), and built-in offline resiliency.

---

## 🎯 2. Objectives
- Streamline student data administration and eliminate paper-based record-keeping.
- Implement **Module-5 core concepts**: Java GUI (AWT/Swing), Event-Driven Programming, and JDBC database architecture.
- Prevent SQL injection through pre-compiled, parameterized queries (`PreparedStatement`).
- Ensure high availability with an automated in-memory demo fallback mode whenever MySQL database connectivity is unavailable.
- Provide comprehensive automated test coverage via JUnit 5.

---

## ⚙️ 3. Module-5 Java Concepts & Technologies Used

### A. Java GUI (Swing & AWT)
- **Top-Level Containers**: `JFrame` (`LoginFrame`, `DashboardFrame`), `JDialog` (`StudentFormDialog`).
- **Intermediate Containers**: `JPanel`, `JScrollPane` (for scrollable student data table).
- **Atomic Components**: `JLabel`, `JTextField`, `JPasswordField`, `JButton`, `JComboBox`, `JTable`.
- **Layout Managers**:
  - `BorderLayout`: Main frames and structured regions (Header, Center content, Footer buttons).
  - `GridLayout`: Aligned form fields and label grids.
  - `FlowLayout`: Responsive toolbar actions and button alignments.
- **Event Handling**:
  - `ActionListener` and Java Lambda expressions for responsive button clicks and keyboard shortcuts (e.g. Enter to login/search).
  - Window listener lifecycle management.
- **Concurrency & Thread Safety**: Execution of GUI creation via `SwingUtilities.invokeLater()` on the **Event Dispatch Thread (EDT)** to prevent race conditions.

### B. JDBC Architecture & Database Operations
- **Driver**: MySQL Connector/J (`com.mysql.cj.jdbc.Driver`).
- **Driver Manager**: `DriverManager.getConnection()` configured via centralized [`DBConnection`](src/main/java/com/studentmanagement/util/DBConnection.java).
- **DAO Pattern**: Encapsulation of all database operations inside [`StudentDAO`](src/main/java/com/studentmanagement/dao/StudentDAO.java).
- **PreparedStatement**: Parameterized SQL execution preventing SQL Injection attacks:
  - `INSERT INTO students ...`
  - `SELECT * FROM students ORDER BY id`
  - `SELECT * FROM students WHERE name LIKE ? OR email LIKE ? OR department LIKE ?`
  - `UPDATE students SET ... WHERE id = ?`
  - `DELETE FROM students WHERE id = ?`
- **Resource Management**: Safe handling using `try-with-resources` to ensure `Connection`, `PreparedStatement`, and `ResultSet` are closed properly without leaks.

### C. Testing & Build Tool
- **Maven**: Dependency management and build lifecycle.
- **JUnit 5**: Unit testing suites covering Model validation, DAO persistence, and GUI state transitions.

---

## 🚀 4. Key Application Features

1. **Secure Login Authentication**:
   - Masked password input (`JPasswordField`).
   - Credential verification against database user accounts with default demo credentials (`admin` / `admin123`).
2. **Interactive Student Dashboard**:
   - Modern styled `JTable` rendering student ID, Name, Email, Phone, Department, Year, and Gender.
   - Quick statistics and record counters.
3. **Student Record Management (CRUD)**:
   - **Add**: Modal dialog with input validation (email format, required fields).
   - **Edit**: Pre-fills selected student details for instant updates.
   - **Delete**: Prompts confirmation dialog (`JOptionPane`) before deletion.
4. **Dynamic Real-Time Search & Filter**:
   - Instant filtering by student name, email, or department.
5. **Dual Persistence Support (Fault-Tolerant)**:
   - Seamlessly uses MySQL when connected.
   - Automatically switches to in-memory demo data with zero downtime if MySQL is offline.

---

## 📂 5. Project Directory Structure

```text
StudentManagementSystem/
├── database/
│   └── student_management.sql           # MySQL Database setup script & sample data
├── src/
│   ├── main/java/com/studentmanagement/
│   │   ├── dao/
│   │   │   └── StudentDAO.java           # JDBC Data Access Object (CRUD & Fallback)
│   │   ├── desktop/
│   │   │   ├── CapturePreviews.java     # Preview and UI screenshot utility
│   │   │   ├── DashboardFrame.java      # Main Swing Dashboard Window
│   │   │   ├── LoginFrame.java          # Swing Login Interface
│   │   │   ├── Main.java                # Application startup & Look-and-Feel entry
│   │   │   └── StudentFormDialog.java   # Add/Edit Student Modal Form
│   │   ├── model/
│   │   │   └── Student.java             # Student Entity POJO
│   │   └── util/
│   │       └── DBConnection.java        # JDBC Connection Manager
│   └── test/java/com/studentmanagement/
│       ├── DesktopUITest.java           # Swing UI validation tests
│       ├── StudentDAOTest.java          # DAO CRUD operation tests
│       ├── StudentModelTest.java        # Entity validation tests
│       └── TestSuiteRunner.java         # Test suite runner
├── .gitignore                           # Git ignore configurations
├── pom.xml                              # Maven build file
├── run.bat                              # One-click desktop launcher
└── test.bat                             # One-click test runner
```

---

## 💻 6. How to Run the Application

### Prerequisites
- **Java JDK 17+** (JDK 21 or 26 recommended)
- **Apache Maven 3.8+**
- *(Optional)* MySQL Server 8.0+

### Option A: Using the Launcher Script (Windows)
Double-click [`run.bat`](run.bat) or run in terminal:
```cmd
run.bat
```

### Option B: Using Maven
```bash
mvn clean compile
mvn exec:java -Dexec.mainClass="com.studentmanagement.desktop.Main"
```

### Option C: Database Setup (Optional)
To use a persistent MySQL database:
1. Open your MySQL client (Command Line, Workbench, or phpMyAdmin).
2. Execute the script:
   ```sql
   source database/student_management.sql;
   ```
3. Verify or adjust database password in [`DBConnection.java`](src/main/java/com/studentmanagement/util/DBConnection.java).

---

## 🧪 7. Running Automated Tests
Run the comprehensive test suite using [`test.bat`](test.bat) or:
```bash
mvn test
```
All **11 tests** validate UI initialization, Model getters/setters, and DAO CRUD functions.
