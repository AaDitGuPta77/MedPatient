package com.example.medpatient;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class PatientHistoryAdapter extends RecyclerView.Adapter<PatientHistoryAdapter.MedicalRecordViewHolder> {

    private List<MedicalRecords> medicalRecords;

    public PatientHistoryAdapter(List<MedicalRecords> medicalRecords) {
        this.medicalRecords = medicalRecords;
    }

    @NonNull
    @Override
    public MedicalRecordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_record_list, parent, false);
        return new MedicalRecordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MedicalRecordViewHolder holder, int position) {
        MedicalRecords record = medicalRecords.get(position);
        holder.tvRecordDate.setText("Date: " + record.getDate());
        holder.tvRecordDiagnosis.setText("Diagnosis: " + record.getDiagnosis());
        holder.tvRecordDoctor.setText("Doctor: " + record.getDoctorName());

        if (!record.getMedicines().isEmpty()) {
            holder.medicineContainer.setVisibility(View.VISIBLE);
            holder.tvMedicinesHeader.setVisibility(View.VISIBLE);

            StringBuilder medicineDetails = new StringBuilder();
            for (Medicine med : record.getMedicines()) {
                medicineDetails.append(med.getName()).append(" - ").append(med.getDosage()).append("\n");
            }
            holder.tvMedicineDetails.setText(medicineDetails.toString().trim());
        } else {
            holder.medicineContainer.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return medicalRecords.size();
    }

    public static class MedicalRecordViewHolder extends RecyclerView.ViewHolder {
        TextView tvRecordDate, tvRecordDiagnosis, tvRecordDoctor, tvMedicinesHeader, tvMedicineDetails;
        View medicineContainer;

        public MedicalRecordViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRecordDate = itemView.findViewById(R.id.tvRecordDate);
            tvRecordDiagnosis = itemView.findViewById(R.id.tvRecordDiagnosis);
            tvRecordDoctor = itemView.findViewById(R.id.tvRecordDoctor);
            tvMedicinesHeader = itemView.findViewById(R.id.tvMedicinesHeader);
            tvMedicineDetails = itemView.findViewById(R.id.tvMedicineDetails);
            medicineContainer = itemView.findViewById(R.id.medicineContainer);
        }
    }

    private void displayMedicines(MedicalRecordViewHolder holder, List<Medicine> medicineList) {
        StringBuilder medicineDetails = new StringBuilder();
        for (Medicine medicine : medicineList) {
            medicineDetails.append("• ").append(medicine.getName()).append(" - ").append(medicine.getDosage()).append("\n");
        }
        holder.tvMedicineDetails.setText(medicineDetails.toString());
    }
}
