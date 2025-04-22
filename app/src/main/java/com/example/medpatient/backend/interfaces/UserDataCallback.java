package com.example.medpatient.backend.interfaces;

import com.example.medpatient.backend.models.User;

public interface UserDataCallback {
    void onSuccess(User user);
    void onFailure(String error);
}


