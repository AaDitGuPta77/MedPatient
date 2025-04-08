package com.example.medpatient.backend.interfaces;

import com.example.medpatient.backend.models.Appointment;

import java.util.List;

public interface DoctorAppointmentsCallback {
    void onAppointmentsReceived(List<Appointment> appointments);
    void onError(String errorMessage);
}
