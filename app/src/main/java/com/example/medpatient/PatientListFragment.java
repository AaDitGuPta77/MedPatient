package com.example.medpatient;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

//import com.google.firebase.firestore.DocumentSnapshot;
//import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class PatientListFragment extends Fragment {
    private RecyclerView recyclerView;
    private PatientAdapter adapter;
    private List<Patient> patientList = new ArrayList<>();
//    private FirebaseFirestore db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_patient_list, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

//        db = FirebaseFirestore.getInstance();

        // Example: Replace this with the actual doctor ID (e.g., from authentication)
        String doctorId = "doctor123";

        adapter = new PatientAdapter(patientList, new PatientAdapter.OnPatientClickListener() {
            @Override
            public void onGetDetailsClick(Patient patient) {
                Toast.makeText(getContext(), "Details: " + patient.getName(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCheckedClick(Patient patient) {
                int position = patientList.indexOf(patient);
                ((MainActivity) requireActivity()).addToPreviousPatients(patient);
                adapter.removePatient(position);
            }
        });

        recyclerView.setAdapter(adapter);

        // Fetch patients assigned to this doctor from Firestore
        getAllDoctorAppointments(doctorId);

        return view;
    }
//<-----------------------------------Database----------------------------------------------------->
    private void getAllDoctorAppointments(String doctorId) {
//        db.collection("Patients")
//                .whereEqualTo("doctorId", doctorId) // Get patients assigned to this doctor
//                .get()
//                .addOnSuccessListener(queryDocumentSnapshots -> {
//                    patientList.clear(); // Clear old data before adding new data
//                    for (DocumentSnapshot document : queryDocumentSnapshots) {
//                        Patient patient = document.toObject(Patient.class);
//                        if (patient != null) {
//                            patient.setPatientId(document.getId()); // Assign Firestore-generated ID
//                            patientList.add(patient);
//                        }
//                    }
//                    adapter.notifyDataSetChanged(); // Refresh RecyclerView with new data
//                })
//                .addOnFailureListener(e -> Log.e("Firebase", "Error fetching patients", e));
    }
}
