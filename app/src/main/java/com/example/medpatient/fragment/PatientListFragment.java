package com.example.medpatient.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

//import com.google.firebase.firestore.DocumentSnapshot;
//import com.google.firebase.firestore.FirebaseFirestore;

import com.example.medpatient.HomeScreen;
import com.example.medpatient.R;
import com.example.medpatient.adapyers.PatientAdapter;
import com.example.medpatient.backend.BackendManager;
import com.example.medpatient.backend.interfaces.UserDataCallback;
import com.example.medpatient.backend.models.Appointment;
import com.example.medpatient.backend.models.User;
import com.example.medpatient.localModels.Patient;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PatientListFragment extends Fragment {
    private RecyclerView recyclerView;
    private PatientAdapter adapter;
    private List<Patient> patientList = new ArrayList<>();
    private String appointmentId;
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
        DatabaseReference appointmentsRef = FirebaseDatabase.getInstance().getReference("appointments");

        appointmentsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                patientList.clear(); // clear the existing list

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Appointment appointment = dataSnapshot.getValue(Appointment.class);
                    if (appointment == null) continue;

                    appointment.setAppointmentId(dataSnapshot.getKey());
                    appointmentId = dataSnapshot.getKey();
                    String doctorId = appointment.getDoctorId();
                    String status = appointment.getStatus();
                    Log.d("DoctorCheck", "Doctor ID: " + appointment.getDoctorId());

                    if ("Accepted".equals(status)) {
                        // Fetch user details and display
                        Log.d("AppointmentStatus", "Status: " + appointment.getStatus());

                        fetchUserAndAddToList(appointment);
                    } else if ("Pending".equals(status)) {
                        showAppointmentPopup(appointmentId, appointment);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Appointments", "Firebase error: " + error.getMessage());
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
                patient.setAppointmentId(appointment.getAppointmentId());
                patient.setChecked(false);

                patientList.add(patient);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(String errorMessage) {
                Log.e("FetchUser", "Error fetching user: " + errorMessage);
            }
        });
    }

    private void showAppointmentPopup(String appointmentId, Appointment appointment) {
        String userId = appointment.getUserId();
        String date = appointment.getAppointmentDate();

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(userId);

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot userSnapshot) {
                if (userSnapshot.exists()) {
                    String name = userSnapshot.child("fullName").getValue(String.class);
                    String DOB = userSnapshot.child("dob").getValue(String.class); // Or "age" if it's stored separately

                    AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                    builder.setTitle("New Appointment Request");

                    builder.setMessage("Name: " + name  + "\nDOB: " + DOB + "\nDo you want to accept it?");

                    builder.setPositiveButton("Accept", (dialog, which) -> {
                        FirebaseDatabase.getInstance().getReference("appointments").child(appointmentId).child("status").setValue("Accepted");
                        Toast.makeText(getContext(), "Appointment Accepted", Toast.LENGTH_SHORT).show();
                    });

                    builder.setNegativeButton("Cancel", (dialog, which) -> {
                        FirebaseDatabase.getInstance().getReference("appointments").child(appointmentId).removeValue();
                        Toast.makeText(getContext(), "Appointment Cancelled", Toast.LENGTH_SHORT).show();
                    });

                    builder.setCancelable(false);
                    builder.show();
                } else {
                    Toast.makeText(getContext(), "User details not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Failed to fetch user: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
















//
//    private void getAllDoctorAppointments() {
//        BackendManager backendManager = new BackendManager();
//        backendManager.getAllDoctorAppointments(new DoctorAppointmentsCallback() {
//            @Override
//            public void onAppointmentsReceived(List<Appointment> appointments) {
//                Log.d("Appointments", "Fetched: " + appointments.size());
//                patientList.clear(); // Clear the old data
//
//                for (Appointment appointment : appointments) {
//                    String patientId = appointment.getUserId();
//                    String doctorId = appointment.getDoctorId(); // or fetch statically if fixed
//
//                    // Now fetch user details for each patient ID
//                    backendManager.fetchUserData(patientId, new UserDataCallback() {
//                        @Override
//                        public void onSuccess(User user) {
//                            Patient patient = new Patient();
//                            patient.setPatientId(patientId);
//                            patient.setDoctorId(doctorId);
//                            patient.setName(user.getFullName());
//                            patient.setAge(user.getDOB());
//                            patient.setContact(user.getContactNumber());
//                            patient.setChecked(false); // or set true if needed
//
//                            patientList.add(patient);
//                            adapter.notifyDataSetChanged(); // Update adapter after each add
//                        }
//
//                        @Override
//                        public void onFailure(String errorMessage) {
//                            Log.e("FetchUser", "Failed to get data for " + patientId + ": " + errorMessage);
//                        }
//                    });
//                }
//            }
//
//            @Override
//            public void onError(String errorMessage) {
//                Log.e("Appointments", "Failed to fetch: " + errorMessage);
//            }
//        });
//    }



}
