package com.example.medpatient.backend.interfaces;

public interface UpdateStatusCallback {
    void onSuccess(String message);
    void onFailure(String errorMessage);
}

