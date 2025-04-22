package com.example.medpatient.backend.models;

public class Appointment {
    private String appointmentId;
    private String userId;
    private String doctorId;
    private String status;
    private String appointmentDate;
    private String time;
    private String diagnosis;
    private String prescriptionId;
    private Long created_at;

    public Appointment() {
        // Default constructor required for Firebase
    }

    public Appointment(String appointmentId, String userId, String doctorId, String status,
                       String appointmentDate, String diagnosis, String prescriptionId, Long created_at) {
        this.appointmentId = appointmentId;
        this.userId = userId;
        this.doctorId = doctorId;
        this.status = status;
        this.appointmentDate = appointmentDate;
        this.diagnosis = diagnosis;
        this.prescriptionId = prescriptionId;
        this.created_at = created_at;
    }

    public String getAppointmentId() { return appointmentId; }
    public void setAppointmentId(String appointmentId) { this.appointmentId = appointmentId; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getDoctorId() { return doctorId; }
    public void setDoctorId(String doctorId) { this.doctorId = doctorId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getAppointmentDate() { return appointmentDate; }
    public void setAppointmentDate(String appointmentDate) { this.appointmentDate = appointmentDate; }

    public String getDiagnosis() { return diagnosis; }
    public void setDiagnosis(String diagnosis) { this.diagnosis = diagnosis; }

    public String getPrescriptionId() { return prescriptionId; }
    public void setPrescriptionId(String prescriptionId) { this.prescriptionId = prescriptionId; }

    public Long getCreated_at() { return created_at; }
    public void setCreated_at(Long created_at) { this.created_at = created_at; }


    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
