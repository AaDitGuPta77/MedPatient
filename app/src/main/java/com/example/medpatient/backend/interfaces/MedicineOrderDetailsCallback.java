package com.example.medpatient.backend.interfaces;

import com.example.medpatient.backend.models.PharmacyRequest;

public interface MedicineOrderDetailsCallback {
    void onOrderDetailsReceived(PharmacyRequest order);
    void onError(String error);
}

