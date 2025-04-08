package com.example.medpatient.backend.interfaces;

import com.example.medpatient.backend.models.Doctor;

import java.util.List;

public interface DoctorListCallback {
    void onSuccess(List<Doctor> doctors);

    void onFailure(String errorMessage);
}
