package com.example.medpatient.backend.interfaces;


public interface OrderStatusUpdateCallback {
    void onSuccess(String message);
    void onFailure(String error);
}
