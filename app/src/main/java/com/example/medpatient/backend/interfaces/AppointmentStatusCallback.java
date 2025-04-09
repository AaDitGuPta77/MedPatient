package com.example.medpatient.backend.interfaces;

public interface AppointmentStatusCallback {
    void onSuccess(String status);
    void onFailure(String error);
}
