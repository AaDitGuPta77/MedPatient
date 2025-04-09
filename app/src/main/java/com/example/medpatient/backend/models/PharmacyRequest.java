package com.example.medpatient.backend.models;

public class PharmacyRequest {
    private String userId;
    private String pharmacyId;
    private String prescriptionId;
    private String status;
    private String created_at;
    private String updated_at;

    public PharmacyRequest() {
        // Default constructor required for Firebase
    }

    public PharmacyRequest(String userId, String pharmacyId, String prescriptionId, String status, String created_at, String updated_at) {
        this.userId = userId;
        this.pharmacyId = pharmacyId;
        this.prescriptionId = prescriptionId;
        this.status = status;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public String getUserId() { return userId; }
    public String getPharmacyId() { return pharmacyId; }
    public String getPrescriptionId() { return prescriptionId; }
    public String getStatus() { return status; }
    public String getCreatedAt() { return created_at; }
    public String getUpdatedAt() { return updated_at; }

    public void setStatus(String status) { this.status = status; }
}

