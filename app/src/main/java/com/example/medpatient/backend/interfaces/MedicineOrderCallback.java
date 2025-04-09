package com.example.medpatient.backend.interfaces;

public interface MedicineOrderCallback {
    void onSuccess(String message);
    void onFailure(String error);
}
