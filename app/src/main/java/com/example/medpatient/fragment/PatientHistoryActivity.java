package com.example.medpatient.fragment;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medpatient.R;
import com.example.medpatient.adapyers.PatientHistoryAdapter;
import com.example.medpatient.backend.BackendManager;
import com.example.medpatient.backend.interfaces.PatientHistoryCallback;
import com.example.medpatient.backend.models.Appointment;
import com.example.medpatient.localModels.MedicalRecords;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.ArrayList;
import java.util.List;

public class PatientHistoryActivity extends AppCompatActivity{

    private RecyclerView historyRecyclerView;
    private List<MedicalRecords> historyList = new ArrayList<>();
    private PatientHistoryAdapter historyAdapter;
    private FloatingActionButton fabAddRecord;
    private ImageView backbutton;


    private DatabaseReference prescriptionsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_history);

        historyRecyclerView = findViewById(R.id.recyclerViewMedicalRecords);
        historyRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        fabAddRecord = findViewById(R.id.fab_add_record);
        backbutton = findViewById(R.id.back_arrow);
        // Initialize the adapter and set it to the RecyclerView
        historyAdapter = new PatientHistoryAdapter(historyList);
        historyRecyclerView.setAdapter(historyAdapter);

        // Back button functionality
        backbutton.setOnClickListener(v -> finish());

        // Add new record
        fabAddRecord.setOnClickListener(v -> openPopup());



        prescriptionsRef = FirebaseDatabase.getInstance().getReference("prescriptions");

        String userId = getIntent().getStringExtra("patientId");

        BackendManager backendManager = new BackendManager();
        backendManager.getPatientHistory(userId, new PatientHistoryCallback() {
            @Override
            public void onHistoryReceived(String userName, List<Appointment> history) {
                Log.d("Patient History", "User: " + userName);
                for (Appointment appointment : history) {
                    // Here you can map the Appointment data to MedicalRecords
                    MedicalRecords medicalRecord = new MedicalRecords();
                    medicalRecord.setDate(appointment.getAppointmentDate());
                    medicalRecord.setDiagnosis(appointment.getDiagnosis());
                    medicalRecord.setDoctorName(appointment.getDoctorId());
                    // Add any other relevant data from Appointment to MedicalRecords

                    // Add the record to the history list
                    historyList.add(medicalRecord);
                }

                // Notify the adapter to update the UI
                historyAdapter.notifyDataSetChanged();
            }


            @Override
            public void onError(String errorMessage) {
                Log.e("Patient History", "Error: " + errorMessage);
            }
        });
    }

    private void openPopup() {
        PopupDialogFragment popup = new PopupDialogFragment();
        Bundle bundle = new Bundle();
//        bundle.putString("appointmentId", appointmentId); // Pass appointmentId

        // Set the Bundle to the DialogFragment
        popup.setArguments(bundle);
        popup.show(getSupportFragmentManager(), "PopupDialog");
    }
}
