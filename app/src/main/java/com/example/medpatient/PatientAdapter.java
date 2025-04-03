package com.example.medpatient;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
            Intent intent = new Intent(holder.itemView.getContext(), PatientHistory.class);
            intent.putExtra("patientId", patient.getPatientId());  // Pass Firestore ID
            holder.itemView.getContext().startActivity(intent);
        });

        // Handle "Checked" button click
        holder.btnChecked.setOnClickListener(v -> listener.onCheckedClick(patient));
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
}
