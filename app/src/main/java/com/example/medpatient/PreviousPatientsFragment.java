package com.example.medpatient;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class PreviousPatientsFragment extends Fragment {
    private static final String ARG_PATIENTS = "previous_patients";
    private RecyclerView recyclerView;
    private PatientAdapter adapter;

    public static PreviousPatientsFragment newInstance(List<Patient> patients) {
        PreviousPatientsFragment fragment = new PreviousPatientsFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PATIENTS, new ArrayList<>(patients));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_previous_patients, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Get checked patients from arguments
        List<Patient> patients = new ArrayList<>();
        if (getArguments() != null) {
            patients = (List<Patient>) getArguments().getSerializable(ARG_PATIENTS);
        }

        List<Patient> finalPatients = patients;
        adapter = new PatientAdapter(patients, new PatientAdapter.OnPatientClickListener() {
            @Override
            public void onGetDetailsClick(Patient patient) {
                Toast.makeText(getContext(), "Previous Patient: " + patient.getName(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCheckedClick(Patient patient) {
                // Optional: Remove from previous list if needed
                int position = finalPatients.indexOf(patient);
                adapter.removePatient(position);
            }
        });

        recyclerView.setAdapter(adapter);
        return view;
    }
}
