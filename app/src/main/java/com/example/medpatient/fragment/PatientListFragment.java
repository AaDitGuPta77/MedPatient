package com.example.medpatient.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

//import com.google.firebase.firestore.DocumentSnapshot;
//import com.google.firebase.firestore.FirebaseFirestore;

import com.example.medpatient.HomeScreen;
import com.example.medpatient.R;
import com.example.medpatient.adapyers.PatientAdapter;
import com.example.medpatient.backend.BackendManager;
import com.example.medpatient.backend.interfaces.DoctorAppointmentsCallback;
import com.example.medpatient.backend.models.Appointment;
import com.example.medpatient.localModels.Patient;

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
                ((HomeScreen) requireActivity()).addToPreviousPatients(patient);
                adapter.removePatient(position);
            }
        });

        recyclerView.setAdapter(adapter);

        // Fetch patients assigned to this doctor from Firestore
        getAllDoctorAppointments();

        return view;
    }
//<-----------------------------------Database----------------------------------------------------->
    private void getAllDoctorAppointments() {
        BackendManager backendManager = new BackendManager();
        backendManager.getAllDoctorAppointments(new DoctorAppointmentsCallback() {
            @Override
            public void onAppointmentsReceived(List<Appointment> appointments) {
                // abhi shirf size log karva raha hu ise adapter me set kar dena
                Log.d("Appointments", "Fetched: " + appointments.size());

                // Do something with the appointment list (e.g., populate a RecyclerView)
            }

            @Override
            public void onError(String errorMessage) {
                Log.e("Appointments", "Failed to fetch: " + errorMessage);
            }
        });
    }
}
