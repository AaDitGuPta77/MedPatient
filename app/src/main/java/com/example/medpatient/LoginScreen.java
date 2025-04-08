package com.example.medpatient;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.medpatient.backend.BackendManager;
import com.example.medpatient.backend.FirebaseUserAuth;
import com.google.android.material.textfield.TextInputLayout;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoginScreen extends AppCompatActivity {

    private FirebaseUserAuth firebaseUserAuth;
    private BackendManager dbManager = new BackendManager();
    private ExecutorService executorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize Firebase Authentication
        firebaseUserAuth = new FirebaseUserAuth();
        executorService = Executors.newSingleThreadExecutor();

        Button loginButton = findViewById(R.id.loginButton);
        TextView forgotPasswordButton = findViewById(R.id.forgotPasswordButton);
        Button registerButton = findViewById(R.id.registerButton);
        TextInputLayout emailTextView = findViewById(R.id.emailInputLayout);
        TextInputLayout passwordTextView = findViewById(R.id.passwordInputLayout);

        loginButton.setOnClickListener(v -> {
            String email = emailTextView.getEditText().getText().toString();
            String password = passwordTextView.getEditText().getText().toString();

            // Run login in background thread
            executorService.execute(() -> {
                try {
                    boolean isSuccess = firebaseUserAuth.loginUser(email, password).get();
                    runOnUiThread(() -> {
                        if (isSuccess) {

                            dbManager.isUserRegistered(isRegistered -> {
                                if (isRegistered) {
                                    navigateToHomeScreen();
                                    Log.d("MainActivity", "User is registered!");
                                } else {
                                    Intent intent = new Intent(this, RegisterScreen.class);
                                    startActivity(intent);
                                    Log.d("MainActivity", "User is NOT registered.");
                                }
                            });
                        } else {
                            Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(() ->
                        Toast.makeText(this, "An error occurred", Toast.LENGTH_SHORT).show()
                    );
                }
            });
        });

        registerButton.setOnClickListener(v -> {
            Intent intent = new Intent(LoginScreen.this, RegisterScreen.class);
            startActivity(intent);
        });

        forgotPasswordButton.setOnClickListener(v -> {
            firebaseUserAuth.resetPassword();
            Intent intent = new Intent(LoginScreen.this, ForgotPasswordScreen.class);
            startActivity(intent);
        });
    }

    private void navigateToHomeScreen() {
        Intent intent = new Intent(LoginScreen.this, HomeScreen.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }
}

