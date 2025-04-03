package com.example.medpatient;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

//import com.google.firebase.firestore.DocumentSnapshot;
//import com.google.firebase.firestore.FirebaseFirestore;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class PatientHistory extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PatientHistoryAdapter adapter;
    private List<MedicalRecords> recordList;
    private TextView tvPatientName;
    private FloatingActionButton fabAddRecord;
    private ImageView backbutton;
//    private FirebaseFirestore db;  // Firebase instance

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_history);

//        db = FirebaseFirestore.getInstance(); // Initialize Firebase

        // Get patient details from Intent
        Intent intent = getIntent();
        String patientName = intent.getStringExtra("patientName");
        String appointmentId = intent.getStringExtra("appointmentId");

        // Initialize views
        tvPatientName = findViewById(R.id.tvPatientName);
        recyclerView = findViewById(R.id.recyclerViewMedicalRecords);
        fabAddRecord = findViewById(R.id.fab_add_record);
        backbutton = findViewById(R.id.back_arrow);

        tvPatientName.setText("Medical Records for " + patientName);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Fetch medical history directly
        getAppointmentDetails(appointmentId);

        // Back button functionality
        backbutton.setOnClickListener(v -> finish());

        // Add new record
        fabAddRecord.setOnClickListener(v -> openPopup());
    }
//<---------------------------------------Database------------------------------------>
    private void getAppointmentDetails(String appointmentId) {
//        db.collection("Appointments")
//                .document(appointmentId)
//                .get()
//                .addOnSuccessListener(documentSnapshot -> {
//                    if (documentSnapshot.exists()) {
//                        MedicalRecords record = documentSnapshot.toObject(MedicalRecords.class);
//                        if (record != null) {
//                            recordList = new ArrayList<>();
//                            recordList.add(record);
//                            adapter = new PatientHistoryAdapter(recordList);
//                            recyclerView.setAdapter(adapter);
//                        }
//                    } else {
//                        Toast.makeText(PatientHistory.this, "No appointment found.", Toast.LENGTH_SHORT).show();
//                    }
//                })
//                .addOnFailureListener(e -> Toast.makeText(PatientHistory.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void openPopup() {
        PopupDialogFragment popup = new PopupDialogFragment();
        popup.show(getSupportFragmentManager(), "PopupDialog");
    }
}
