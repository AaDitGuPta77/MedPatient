package com.example.medpatient.backend.interfaces;

import com.example.medpatient.backend.models.Prescription;

import java.util.List;

public interface PrescriptionCallback {
    void onPrescriptionsReceived(List<Prescription> prescriptions);
}

