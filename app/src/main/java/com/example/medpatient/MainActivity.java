package com.example.medpatient;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private final List<Patient> previousPatients = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
        bottomNav.setOnNavigationItemSelectedListener(this::onNavigationItemSelected);

        // Load the default fragment
        loadFragment(new PatientListFragment());
    }

    private boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment selectedFragment = null;

        if (item.getItemId() == R.id.nav_patient_list) {
            selectedFragment = new PatientListFragment();
        } else if (item.getItemId() == R.id.nav_previous_patients) {
            selectedFragment = PreviousPatientsFragment.newInstance(previousPatients);
        } else if (item.getItemId() == R.id.nav_about) {
            selectedFragment = new AboutFragment();
        }

        if (selectedFragment != null) {
            loadFragment(selectedFragment);
            return true;
        }
        return false;
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    public void addToPreviousPatients(Patient patient) {
        previousPatients.add(patient);
        refreshPreviousPatients();
    }

    private void refreshPreviousPatients() {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (currentFragment instanceof PreviousPatientsFragment) {
            loadFragment(PreviousPatientsFragment.newInstance(previousPatients));
        }
    }
}
