package com.example.studentmanagement.model;

public class Student {
    private String id;
    private String name;
    private int age;
    private boolean isMale;
    private String phoneNumber;
    private String email;
    private double gpa;

    public Student() {
    }

    public Student(String name, int age, boolean isMale, String phoneNumber, String email, double gpa) {
        this.name = name;
        this.age = age;
        this.isMale = isMale;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.gpa = gpa;
    }

    public Student(String id, String name, int age, boolean isMale, String phoneNumber, String email, double gpa) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.isMale = isMale;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.gpa = gpa;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isMale() {
        return isMale;
    }

    public void setMale(boolean male) {
        isMale = male;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public double getGpa() {
        return gpa;
    }

    public void setGpa(double gpa) {
        this.gpa = gpa;
    }
}
