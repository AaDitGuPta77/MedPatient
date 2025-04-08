package com.example.medpatient.backend.interfaces;

public interface AppointmentCallback {
    void onAppointmentRequest(boolean success, String message, String appointmentId);
}


