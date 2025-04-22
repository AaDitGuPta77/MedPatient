package com.example.medpatient.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medpatient.R;
import com.example.medpatient.adapyers.PatientAdapter;
import com.example.medpatient.localModels.Patient;
import com.example.medpatient.backend.models.User;
import com.example.medpatient.backend.models.Appointment;
import com.example.medpatient.backend.BackendManager;
import com.example.medpatient.backend.interfaces.UserDataCallback;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PreviousPatientsFragment extends Fragment {
    private RecyclerView recyclerView;
    private PatientAdapter adapter;
    private List<Patient> completedPatients = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_previous_patients, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new PatientAdapter(completedPatients, new PatientAdapter.OnPatientClickListener() {
            @Override
            public void onGetDetailsClick(Patient patient) {
                Toast.makeText(getContext(), "Previous Patient: " + patient.getName(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCheckedClick(Patient patient) {
                // You may optionally allow re-marking as completed or do nothing
                Toast.makeText(getContext(), "Already marked as Completed", Toast.LENGTH_SHORT).show();
            }
        });

        recyclerView.setAdapter(adapter);

        fetchCompletedAppointments();

        return view;
    }

    private void fetchCompletedAppointments() {
        DatabaseReference appointmentsRef = FirebaseDatabase.getInstance().getReference("appointments");

        appointmentsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                completedPatients.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Appointment appointment = dataSnapshot.getValue(Appointment.class);
                    if (appointment == null) continue;

                    if ("Completed".equalsIgnoreCase(appointment.getStatus())) {
                        fetchUserAndAddToList(appointment);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Failed to load appointments", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchUserAndAddToList(Appointment appointment) {
        BackendManager backendManager = new BackendManager();
        backendManager.fetchUserData(appointment.getUserId(), new UserDataCallback() {
            @Override
            public void onSuccess(User user) {
                Patient patient = new Patient();
                patient.setPatientId(appointment.getUserId());
                patient.setDoctorId(appointment.getDoctorId());
                patient.setName(user.getFullName());
                patient.setAge(user.getDOB());
                patient.setContact(user.getContactNumber());
                patient.setChecked(true); // already completed

                completedPatients.add(patient);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(getContext(), "Failed to load patient info", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
