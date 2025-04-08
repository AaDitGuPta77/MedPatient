package com.example.medpatient.backend.interfaces;

import com.example.medpatient.backend.models.Appointment;

import java.util.List;

public interface UserHistoryCallback {
    void onUserHistoryReceived(List<Appointment> appointments);
}

