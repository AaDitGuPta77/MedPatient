package com.example.medpatient;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class ForgotPasswordScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password_screen);

        Button proceedButton = findViewById(R.id.proceedButton);

        proceedButton.setOnClickListener(v -> {
            Intent intent = new Intent(ForgotPasswordScreen.this, LoginScreen.class);
            startActivity(intent);
        });
    }
}