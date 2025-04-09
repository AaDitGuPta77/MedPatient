package com.example.medpatient.backend.interfaces;

public interface FcmCallback {
    void onSuccess();
    void onFailure(String error);
}
