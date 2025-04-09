package com.example.medpatient.backend.interfaces;

import com.example.medpatient.backend.models.PharmacyRequest;

import java.util.List;

public interface PharmacyOrdersCallback {
    void onOrdersReceived(List<PharmacyRequest> orders);
    void onError(String error);
}

