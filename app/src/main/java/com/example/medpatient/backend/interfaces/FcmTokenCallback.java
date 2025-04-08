package com.example.medpatient.backend.interfaces;

// Callback interface
public interface FcmTokenCallback {
    void onTokenReceived(String fcmToken);
    void onFailure(String errorMessage);
}
