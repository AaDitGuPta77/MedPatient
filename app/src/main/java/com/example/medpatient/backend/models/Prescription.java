package com.example.medpatient.backend.models;

import java.util.List;

public class Prescription {
    private String prescriptionId;
    private String appointmentId;
    private String doctorId;
    private String userId;
    private List<Medicine> medicines;
    private String instructions;
    private String createdAt;
    private String homeDeliveryStatus;

    public Prescription() {
        // Default constructor required for Firebase
    }

    public Prescription(String prescriptionId, String appointmentId, String doctorId, String userId,
                        List<Medicine> medicines, String instructions, String createdAt, String homeDeliveryStatus) {
        this.prescriptionId = prescriptionId;
        this.appointmentId = appointmentId;
        this.doctorId = doctorId;
        this.userId = userId;
        this.medicines = medicines;
        this.instructions = instructions;
        this.createdAt = createdAt;
        this.homeDeliveryStatus = homeDeliveryStatus;
    }

    // Getters
    public String getPrescriptionId() { return prescriptionId; }
    public String getAppointmentId() { return appointmentId; }
    public String getDoctorId() { return doctorId; }
    public String getUserId() { return userId; }
    public List<Medicine> getMedicines() { return medicines; }
    public String getInstructions() { return instructions; }
    public String getCreatedAt() { return createdAt; }
    public String getHomeDeliveryStatus() { return homeDeliveryStatus; }
}

