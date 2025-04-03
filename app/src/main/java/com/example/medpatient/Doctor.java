package com.example.medpatient;

public class Doctor {
    private String name, email, designation, specialties, experience;

    // Empty constructor required for Firestore deserialization
    public Doctor() {}

    public Doctor(String name, String email, String designation, String specialties, String experience) {
        this.name = name;
        this.email = email;
        this.designation = designation;
        this.specialties = specialties;
        this.experience = experience;
    }

    // Getters
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getDesignation() { return designation; }
    public String getSpecialties() { return specialties; }
    public String getExperience() { return experience; }
}

