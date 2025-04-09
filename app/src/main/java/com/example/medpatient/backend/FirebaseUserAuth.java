package com.example.medpatient.backend;

import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class FirebaseUserAuth {
    private static final String TAG = "FirebaseUserAuth";
    private final FirebaseAuth mAuth;
    private final ExecutorService executorService;

    public FirebaseUserAuth() {
        this.mAuth = FirebaseAuth.getInstance();
        this.executorService = Executors.newFixedThreadPool(2); // Using a thread pool for async execution
    }

    public boolean isLoggedIn() {
        if (mAuth.getCurrentUser() != null) {
            Log.d(TAG, "Already Logged In");
            return true;
        }
        return false;
    }

    public Future<Boolean> registerUser(String email, String password) {
        return executorService.submit(() -> {
            try {
                Log.d(TAG, "Starting user registration on a separate thread...");
                Task<AuthResult> task = mAuth.createUserWithEmailAndPassword(email, password);
                AuthResult result = Tasks.await(task);

                if (result.getUser() != null) {
                    Log.d(TAG, "User registration successful. Logging in...");
                    return loginUser(email, password).get(); // Log in after registration
                } else {
                    Log.d(TAG, "User registration failed.");
                }
            } catch (Exception e) {
                Log.e(TAG, "Register Failed: " + e.getMessage());
            }
            return false;
        });
    }

    public Future<Boolean> loginUser(String email, String password) {
        return executorService.submit(() -> {
            try {
                Log.d(TAG, "Starting login process on a separate thread...");
                Task<AuthResult> task = mAuth.signInWithEmailAndPassword(email, password);
                AuthResult result = Tasks.await(task);

                if (result.getUser() != null) {
                    Log.d(TAG, "Login successful.");
                    return true;
                } else {
                    Log.d(TAG, "Login failed.");
                }
            } catch (Exception e) {
                Log.e(TAG, "Login Failed: " + e.getMessage());
            }
            return false;
        });
    }

    public Future<Boolean> resetPassword() {
        return executorService.submit(() -> {
            try {
                Log.d(TAG, "Starting password reset process on a separate thread...");
                mAuth.sendPasswordResetEmail(mAuth.getCurrentUser().getEmail());
                Log.d(TAG, "Password reset email sent successfully.");
                return true;
            } catch (Exception e) {
                Log.e(TAG, "Password Reset Failed: " + e.getMessage());
            }
            return false;
        });
    }

    public void signOut() {
        executorService.execute(() -> {
            Log.d(TAG, "Signing out on a separate thread...");
            mAuth.signOut();
            Log.d(TAG, "User signed out successfully.");
        });
    }

    // Shut down the executor when the app is closing
    public void shutdownExecutor() {
        executorService.shutdown();
    }

}

