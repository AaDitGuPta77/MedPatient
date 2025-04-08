package com.example.medpatient.backend.interfaces;

import com.example.medpatient.backend.models.Appointment;

import java.util.List;

public interface PatientHistoryCallback {
    void onHistoryReceived(String userName, List<Appointment> history);
    void onError(String errorMessage);
}

