package com.example.medpatient.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.medpatient.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.medpatient.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PopupDialogFragment extends DialogFragment {

    private LinearLayout editTextContainer;
    private EditText dateField, diseaseField;
    private String appointmentId;
    private DatabaseReference appointmentsRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_medicine, container, false);

        // Initialize Firebase database reference
        appointmentsRef = FirebaseDatabase.getInstance().getReference("appointments");

        // Initialize views
        editTextContainer = view.findViewById(R.id.editTextContainer);
        diseaseField = view.findViewById(R.id.diseaseField);
        dateField = view.findViewById(R.id.dateField);
        Button addButton = view.findViewById(R.id.addButton);
        Button submitButton = view.findViewById(R.id.submitButton);

        // Set the current date in the dateField
        dateField.setText(getCurrentDate());

        // Retrieve the appointmentId passed through the Bundle
        if (getArguments() != null) {
            appointmentId = getArguments().getString("appointmentId");
        }

        // Add more fields dynamically for medicines and dosages
        addButton.setOnClickListener(v -> addNewEditTexts());

        // Submit the data to Firebase and close the dialog
        submitButton.setOnClickListener(v -> submitAppointmentData());

        return view;
    }

    // Dynamically add new fields for medicine and dosage
    private void addNewEditTexts() {
        LinearLayout newLayout = new LinearLayout(getContext());
        newLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        newLayout.setOrientation(LinearLayout.HORIZONTAL);

        EditText medicineField = new EditText(getContext());
        medicineField.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
        medicineField.setHint("Medicine Name");

        EditText dosageField = new EditText(getContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
        params.setMargins(8, 0, 0, 0);
        dosageField.setLayoutParams(params);
        dosageField.setHint("Dosage");

        newLayout.addView(medicineField);
        newLayout.addView(dosageField);

        editTextContainer.addView(newLayout);
    }

    // Get the current date in the format dd/MM/yyyy
    private String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return sdf.format(new Date());
    }

    // Submit the diagnosis and medicines data to Firebase
    private void submitAppointmentData() {
        // Collect the diagnosis from the diseaseField
        String diagnosis = diseaseField.getText().toString();

        // Collect all medicines and dosages from the dynamic fields
        List<String> medicinesWithDosage = new ArrayList<>();
        for (int i = 0; i < editTextContainer.getChildCount(); i++) {
            LinearLayout medicineLayout = (LinearLayout) editTextContainer.getChildAt(i);
            EditText medicineField = (EditText) medicineLayout.getChildAt(0);
            EditText dosageField = (EditText) medicineLayout.getChildAt(1);

            String medicine = medicineField.getText().toString();
            String dosage = dosageField.getText().toString();

            if (!medicine.isEmpty() && !dosage.isEmpty()) {
                medicinesWithDosage.add(medicine + " - " + dosage); // Format: Medicine - Dosage
            }
        }

        // Update the appointment data in Firebase
        if (appointmentId != null && !appointmentId.isEmpty()) {
            // Prepare the data to update
            appointmentsRef.child(appointmentId).child("diagnosis").setValue(diagnosis);
            appointmentsRef.child(appointmentId).child("medicinesWithDosage").setValue(medicinesWithDosage)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(), "Appointment updated successfully!", Toast.LENGTH_SHORT).show();
                            dismiss();  // Close the dialog after submitting the data
                        } else {
                            Toast.makeText(getContext(), "Failed to update appointment", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(getContext(), "Invalid Appointment ID", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }
    }
}
