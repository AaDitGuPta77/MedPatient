package com.example.medpatient.backend.models;

public class User {
    private String fullName;
    private String contactNumber;
    private String address;
    private String gender;
    private String DOB;
    private String bloodGroup;
    private boolean registered;
    private String fcmToken;

    // Required empty constructor for Firebase
    public User() {}

    public User(String fullName, String contactNumber, String address, String gender, String DOB, String bloodGroup) {
        this.fullName = fullName;
        this.contactNumber = contactNumber;
        this.address = address;
        this.gender = gender;
        this.DOB = DOB;
        this.bloodGroup = bloodGroup;
        this.registered = false;  // Initially false until registration is complete
        this.fcmToken = "";
    }

    // Getters and Setters
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getContactNumber() { return contactNumber; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getDOB() { return DOB; }
    public void setDOB(String DOB) { this.DOB = DOB; }

    public String getBloodGroup() { return bloodGroup; }
    public void setBloodGroup(String bloodGroup) { this.bloodGroup = bloodGroup; }

    public boolean isRegistered() { return registered; }
    public void setRegistered(boolean registered) { this.registered = registered; }

    public String getFcmToken() { return fcmToken; }
    public void setFcmToken(String fcmToken) { this.fcmToken = fcmToken; }
}
