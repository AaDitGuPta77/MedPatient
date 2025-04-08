package com.example.medpatient.backend.interfaces;

import com.example.medpatient.backend.models.Doctor;

public interface DoctorDetailsCallback {
    void onDoctorDetailsReceived(Doctor doctor);
}
