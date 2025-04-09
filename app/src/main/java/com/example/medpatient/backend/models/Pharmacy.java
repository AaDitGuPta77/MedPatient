package com.example.medpatient.backend.models;
public class Pharmacy {
    private String name;
    private String phone;
    private String address;
    private boolean registered;
    private String fcmToken;

    // Empty constructor for Firebase
    public Pharmacy() {}

    public Pharmacy(String name, String phone, String address) {
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.registered = false;  // Initially false until registration is complete
        this.fcmToken = "";
    }

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public boolean isRegistered() { return registered; }
    public void setRegistered(boolean registered) { this.registered = registered; }

    public String getFcmToken() { return fcmToken; }
    public void setFcmToken(String fcmToken) { this.fcmToken = fcmToken; }
}
