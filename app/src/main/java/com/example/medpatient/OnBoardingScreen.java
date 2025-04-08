package com.example.medpatient;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;

import com.example.medpatient.backend.BackendManager;
import com.example.medpatient.backend.models.Doctor;
import com.example.medpatient.fragment.OnBoardingFragment;

import java.util.HashMap;

public class OnBoardingScreen extends AppCompatActivity {

    private HashMap<String, String> onBoardingData;
    private Doctor onBoardingDoctor;
    private final BackendManager dbManager = new BackendManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_on_boarding_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.onBoardingScreenFragmentContainer, new OnBoardingFragment())
                .addToBackStack("Part1")
                .commit();
    }

    public void receiveUserData(Doctor doctor) {
        if (onBoardingDoctor == null) {
            onBoardingDoctor = new Doctor();
        }

        // Correctly set values
        if (doctor.getName() != null) onBoardingDoctor.setName(doctor.getName());
        if (doctor.getSpecialization() != null) onBoardingDoctor.setSpecialization(doctor.getSpecialization());
        if (doctor.getAddress() != null) onBoardingDoctor.setAddress(doctor.getAddress());
        if (doctor.getPhone() != null) onBoardingDoctor.setPhone(doctor.getPhone());
        if (doctor.getRatings() != null) onBoardingDoctor.setRatings(doctor.getRatings());
        if (doctor.getExperience() != null) onBoardingDoctor.setExperience(doctor.getExperience());

        // Check if all required fields are non-empty
        if (!onBoardingDoctor.getName().isEmpty() &&
                !onBoardingDoctor.getSpecialization().isEmpty() &&
                !onBoardingDoctor.getAddress().isEmpty() &&
                !onBoardingDoctor.getPhone().isEmpty() &&
                !onBoardingDoctor.getRatings().isEmpty() &&
                !onBoardingDoctor.getExperience().isEmpty()) {

            // Call the database method only when all fields are filled
            dbManager.addDoctorDetail(onBoardingDoctor);

            // Navigate to HomeScreen
            finish();
            Intent intent = new Intent(this, HomeScreen.class);
            startActivity(intent);
        }
    }


}