package com.asha.asha.health.system.model;

public class Patient {

    private int patientId;
    private String name;
    private int age;
    private String address;
    private String phone;
    private int ashaWorkerId;

    // Constructor
    public Patient(String name, int age, String address, String phone, int ashaWorkerId) {
        this.name = name;
        this.age = age;
        this.address = address;
        this.phone = phone;
        this.ashaWorkerId = ashaWorkerId;
    }

    // Getters
    public String getName() { return name; }
    public int getAge() { return age; }
    public String getAddress() { return address; }
    public String getPhone() { return phone; }
    public int getAshaWorkerId() { return ashaWorkerId; }
}