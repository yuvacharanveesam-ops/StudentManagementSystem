package com.studentmanagement.model;

/**
 * Student Model Class (POJO - Plain Old Java Object)
 *
 * This class represents the 'students' table in the database.
 * It follows the JavaBean convention with private fields and public getters/setters.
 *
 * Module 5 Concept: Model layer in MVC architecture used with JDBC.
 */
public class Student {

    // ── Fields matching the 'students' table columns ──────────────────────────
    private int    id;
    private String name;
    private String email;
    private String phone;
    private String department;
    private int    year;
    private String gender;

    // ── Default Constructor ────────────────────────────────────────────────────
    public Student() {}

    // ── Parameterized Constructor (without id – used for INSERT) ──────────────
    public Student(String name, String email, String phone,
                   String department, int year, String gender) {
        this.name       = name;
        this.email      = email;
        this.phone      = phone;
        this.department = department;
        this.year       = year;
        this.gender     = gender;
    }

    // ── Full Constructor (with id – used after SELECT) ─────────────────────────
    public Student(int id, String name, String email, String phone,
                   String department, int year, String gender) {
        this.id         = id;
        this.name       = name;
        this.email      = email;
        this.phone      = phone;
        this.department = department;
        this.year       = year;
        this.gender     = gender;
    }

    // ── Getters and Setters ───────────────────────────────────────────────────

    public int getId()               { return id; }
    public void setId(int id)        { this.id = id; }

    public String getName()          { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail()           { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone()           { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getDepartment()              { return department; }
    public void setDepartment(String dept)     { this.department = dept; }

    public int getYear()             { return year; }
    public void setYear(int year)    { this.year = year; }

    public String getGender()            { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    // ── toString for debugging ─────────────────────────────────────────────────
    @Override
    public String toString() {
        return "Student{id=" + id + ", name='" + name + "', email='" + email +
               "', phone='" + phone + "', department='" + department +
               "', year=" + year + ", gender='" + gender + "'}";
    }
}
