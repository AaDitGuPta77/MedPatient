package com.example.medpatient.localModels;

public class Patient {
    private String patientId;  // Firestore-generated ID
    private String doctorId;   // ID of the doctor assigned
    private String name;
    private String age;
    private String contact;
    private Boolean isChecked;

    // Default Constructor (Required for Firestore)
    public Patient() {}

    // Constructor
    public Patient(String patientId, String doctorId, String name, String age, String contact) {
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.name = name;
        this.age = age;
        this.contact = contact;
        this.isChecked = false;
    }

    // Getters & Setters
    public String getPatientId() { return patientId; }
    public void setPatientId(String patientId) { this.patientId = patientId; }

    public String getDoctorId() { return doctorId; }
    public void setDoctorId(String doctorId) { this.doctorId = doctorId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getAge() { return age; }
    public void setAge(String age) { this.age = age; }

    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }

    public Boolean getChecked() { return isChecked; }
    public void setChecked(Boolean checked) { isChecked = checked; }
}

