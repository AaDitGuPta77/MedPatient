package com.example.medpatient.backend.models;

public class Medicine {
    private String name;
    private String dosage;
    private String frequency;

    public Medicine() {
        // Default constructor required for Firebase
    }

    public Medicine(String name, String dosage, String frequency) {
        this.name = name;
        this.dosage = dosage;
        this.frequency = frequency;
    }

    // Getters
    public String getName() { return name; }
    public String getDosage() { return dosage; }
    public String getFrequency() { return frequency; }
}
