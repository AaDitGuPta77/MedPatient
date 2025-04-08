package com.example.medpatient.backend.interfaces;

import com.example.medpatient.backend.models.Appointment;

public interface AppointmentDetailsCallback {
    void onSuccess(Appointment appointment);
    void onFailure(String errorMessage);
}
