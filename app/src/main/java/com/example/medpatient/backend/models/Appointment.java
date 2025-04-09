package com.example.medpatient.backend.models;

public class Appointment {
    private String appointmentId;
    private String userId;
    private String doctorId;
    private String status;
    private String date;
    private String time;
    private String diagnosis;
    private String prescriptionId;
    private String createdAt;

    public Appointment() {
        // Default constructor required for Firebase
    }

    public Appointment(String appointmentId, String userId, String doctorId, String status,
                       String date, String diagnosis, String prescriptionId, String createdAt) {
        this.appointmentId = appointmentId;
        this.userId = userId;
        this.doctorId = doctorId;
        this.status = status;
        this.date = date;
        this.diagnosis = diagnosis;
        this.prescriptionId = prescriptionId;
        this.createdAt = createdAt;
    }

    public String getAppointmentId() { return appointmentId; }
    public void setAppointmentId(String appointmentId) { this.appointmentId = appointmentId; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getDoctorId() { return doctorId; }
    public void setDoctorId(String doctorId) { this.doctorId = doctorId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getDiagnosis() { return diagnosis; }
    public void setDiagnosis(String diagnosis) { this.diagnosis = diagnosis; }

    public String getPrescriptionId() { return prescriptionId; }
    public void setPrescriptionId(String prescriptionId) { this.prescriptionId = prescriptionId; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }


    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
