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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PopupDialogFragment extends DialogFragment {

    private LinearLayout editTextContainer;
    private EditText dateField;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_medicine, container, false);

        editTextContainer = view.findViewById(R.id.editTextContainer);
        dateField = view.findViewById(R.id.dateField);
        Button addButton = view.findViewById(R.id.addButton);
        Button submitButton = view.findViewById(R.id.submitButton);

        // Set Current Date
        dateField.setText(getCurrentDate());

        // Add more fields dynamically
        addButton.setOnClickListener(v -> addNewEditTexts());

        // Close dialog on submit
        submitButton.setOnClickListener(v -> dismiss());

        return view;
    }

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

    private String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return sdf.format(new Date());
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
