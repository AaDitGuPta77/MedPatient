package com.example.medpatient.adapyers;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medpatient.backend.models.Appointment;
import com.example.medpatient.fragment.PatientHistoryActivity;
import com.example.medpatient.localModels.Patient;
import com.example.medpatient.PatientHistoryScreen;
import com.example.medpatient.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PatientAdapter extends RecyclerView.Adapter<PatientAdapter.PatientViewHolder> {
    private List<Patient> patients;
    private OnPatientClickListener listener;

    public interface OnPatientClickListener {
        void onGetDetailsClick(Patient patient);
        void onCheckedClick(Patient patient);
    }

    public PatientAdapter(List<Patient> patients, OnPatientClickListener listener) {
        this.patients = patients;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PatientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_patient, parent, false);
        return new PatientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PatientViewHolder holder, int position) {
        Patient patient = patients.get(position);
        holder.tvName.setText(patient.getName());
        holder.tvAge.setText("Age: " + patient.getAge());
        holder.tvContact.setText("Contact: " + patient.getContact());

        // Handle "Get Details" button click
        holder.btnGetDetails.setOnClickListener(v -> {
            listener.onGetDetailsClick(patient);

            // Open PatientHistory activity and pass patientId
            Intent intent = new Intent(holder.itemView.getContext(), PatientHistoryActivity.class);
            intent.putExtra("patientId", patient.getPatientId());  // Pass Firestore ID
            holder.itemView.getContext().startActivity(intent);
        });

        // Handle "Checked" button click
        holder.btnChecked.setOnClickListener(v -> {
            listener.onCheckedClick(patient);

            // Update the status of the patient's appointment in Firebase (for example)
            updatePatientStatusToCompleted(patient);
        });
    }
    
    @Override
    public int getItemCount() {
        return patients.size();
    }

    // Update the patient list dynamically
    public void updatePatientList(List<Patient> newPatients) {
        this.patients = newPatients;
        notifyDataSetChanged(); // Refresh RecyclerView with new data
    }

    public void removePatient(int position) {
        patients.remove(position);
        notifyItemRemoved(position);
    }

    public static class PatientViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvAge, tvContact;
        Button btnGetDetails, btnChecked;

        public PatientViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvAge = itemView.findViewById(R.id.tvAge);
            tvContact = itemView.findViewById(R.id.tvContact);
            btnGetDetails = itemView.findViewById(R.id.btnGetDetails);
            btnChecked = itemView.findViewById(R.id.btnChecked);
        }
    }

    private void updatePatientStatusToCompleted(Patient patient) {
        // Get the patient's ID or appointment ID
        String patientId = patient.getPatientId();

        // Update the status in Firebase (replace with actual Firebase reference)
        DatabaseReference appointmentRef = FirebaseDatabase.getInstance().getReference("appointments");
        appointmentRef.orderByChild("userId").equalTo(patientId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            // Assuming your data model has a field "status"
                            Appointment appointment = dataSnapshot.getValue(Appointment.class);
                            if (appointment != null) {
                                String appointmentId = dataSnapshot.getKey();
                                // Update status to "Completed"
                                appointmentRef.child(appointmentId).child("status").setValue("Completed")
                                        .addOnSuccessListener(aVoid -> {
                                            // Optionally show confirmation (e.g., Toast)
                                            // Refresh the patient list after updating status
                                            Log.d("Status","Completed");
                                            refreshPatientList();  // Call method to refresh the list
                                        })
                                        .addOnFailureListener(e -> {
                                            });
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("PatientAdapter", "Failed to fetch appointment: " + error.getMessage());
                    }
                });
    }
    private void refreshPatientList() {
        // Fetch updated patient list and call updatePatientList to refresh RecyclerView
        // Assuming that the data is coming from Firebase, you can use the appropriate Firebase method to load data
        DatabaseReference patientsRef = FirebaseDatabase.getInstance().getReference("patients");
        patientsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Patient> updatedPatients = new ArrayList<Patient>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Patient patient = dataSnapshot.getValue(Patient.class);
                    if (patient != null) {
                        updatedPatients.add(patient);
                    }
                }
                updatePatientList(updatedPatients);  // Update the RecyclerView with the new patient list
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("PatientAdapter", "Failed to load patients: " + error.getMessage());
            }
        });
    }

}

