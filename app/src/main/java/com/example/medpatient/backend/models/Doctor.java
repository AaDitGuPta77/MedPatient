package com.example.medpatient.backend.models;
public class Doctor {
    private String name;
    private String specialization;
    private String experience;
    private String  ratings;
    private String phone;
    private String address;
    private boolean registered;
    private String fcmToken;

    // Empty constructor for Firebase
    public Doctor() {}

    public Doctor(String name, String specialization, String experience, String ratings, String phone, String address) {
        this.name = name;
        this.specialization = specialization;
        this.experience = experience;
        this.ratings = ratings;
        this.phone = phone;
        this.address = address;
        this.registered = false;  // Initially false until registration is complete
        this.fcmToken = "";
    }

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }

    public String getExperience() { return experience; }
    public void setExperience(String experience) { this.experience = experience; }

    public String getRatings() { return ratings; }
    public void setRatings(String ratings) { this.ratings = ratings; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public boolean isRegistered() { return registered; }
    public void setRegistered(boolean registered) { this.registered = registered; }

    public String getFcmToken() { return fcmToken; }
    public void setFcmToken(String fcmToken) { this.fcmToken = fcmToken; }
}
