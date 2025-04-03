package com.example.medpatient;

public class Medicine {
    private String name;
    private String dosage;

    public Medicine() {} // Required for Firebase

    public Medicine(String name, String dosage) {
        this.name = name;
        this.dosage = dosage;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDosage() { return dosage; }
    public void setDosage(String dosage) { this.dosage = dosage; }
}

