package com.example.medpatient.adapyers;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medpatient.R;
import com.example.medpatient.localModels.MedicalRecords;
import com.example.medpatient.localModels.Medicine;

import java.util.List;

public class PatientHistoryAdapter extends RecyclerView.Adapter<PatientHistoryAdapter.MedicalRecordViewHolder> {

    private List<MedicalRecords> medicalRecords;

    // Constructor
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

        List<Medicine> medicines = record.getMedicines();
        if (medicines != null && !medicines.isEmpty()) {
            StringBuilder medList = new StringBuilder();
            for (Medicine med : medicines) {
                medList.append("• ").append(med.getName())
                        .append(" - ").append(med.getDosage())
                        .append("\n");
            }
            holder.tvMedicineDetails.setText(medList.toString().trim());
            holder.tvMedicineDetails.setVisibility(View.VISIBLE);
        } else {
            holder.tvMedicineDetails.setText("No medicines prescribed.");
            holder.tvMedicineDetails.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return medicalRecords.size();
    }

    public static class MedicalRecordViewHolder extends RecyclerView.ViewHolder {

        TextView tvRecordDate, tvRecordDiagnosis, tvRecordDoctor, tvMedicineDetails;

        public MedicalRecordViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRecordDate = itemView.findViewById(R.id.tvRecordDate);
            tvRecordDiagnosis = itemView.findViewById(R.id.tvRecordDiagnosis);
            tvRecordDoctor = itemView.findViewById(R.id.tvRecordDoctor);
            tvMedicineDetails = itemView.findViewById(R.id.tvMedicineDetails);
        }
    }
}
