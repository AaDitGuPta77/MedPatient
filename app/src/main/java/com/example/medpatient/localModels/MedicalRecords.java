package com.example.medpatient.localModels;

import java.util.List;

public class MedicalRecords {
    private String date;
    private String diagnosis;
    private String doctorName;
    private List<Medicine> medicines;  // New field

    public MedicalRecords() {} // Required for Firebase

    public MedicalRecords(String date, String diagnosis, String doctorName, List<Medicine> medicines) {
        this.date = date;
        this.diagnosis = diagnosis;
        this.doctorName = doctorName;
        this.medicines = medicines;
    }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getDiagnosis() { return diagnosis; }
    public void setDiagnosis(String diagnosis) { this.diagnosis = diagnosis; }

    public String getDoctorName() { return doctorName; }
    public void setDoctorName(String doctorName) { this.doctorName = doctorName; }

    public List<Medicine> getMedicines() { return medicines; }
    public void setMedicines(List<Medicine> medicines) { this.medicines = medicines; }
}


