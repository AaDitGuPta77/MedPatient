package com.example.medpatient;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.medpatient.backend.FirebaseUserAuth;
import com.google.android.material.textfield.TextInputLayout;

public class RegisterScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        FirebaseUserAuth firebaseUserAuth = new FirebaseUserAuth();

        TextInputLayout email = findViewById(R.id.registerScreenEmailInputLayout);
        TextInputLayout password = findViewById(R.id.registerScreenPasswordInputLayout);
        TextInputLayout confirmPassword = findViewById(R.id.registerScreenPasswordConfirmationInputLayout);
        Button registerUser = findViewById(R.id.registerScreenRegisterButton);

        registerUser.setOnClickListener(v -> {
            if (password.getEditText().getText().toString().equals(confirmPassword.getEditText().getText().toString())) {
                firebaseUserAuth.registerUser(
                        email.getEditText()
                                .getText()
                                .toString()
                        , password
                                .getEditText()
                                .getText()
                                .toString()
                );
                Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT).show();
                navigateToOnBoardingScreen();
            } else {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void navigateToOnBoardingScreen() {
        Intent intent = new Intent(this, OnBoardingScreen.class);
        startActivity(intent);
        finish();
    }
}