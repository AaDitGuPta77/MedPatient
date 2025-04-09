package com.example.medpatient.backend.interfaces;

public interface MedicineOrderStatusCallback {
    void onSuccess(String status);
    void onFailure(String errorMessage);
}

