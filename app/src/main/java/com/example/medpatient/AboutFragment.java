package com.example.medpatient;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

//import com.google.firebase.firestore.FirebaseFirestore;

public class AboutFragment extends Fragment {
    private TextView tvName, tvEmail, tvDesignation, tvSpecialties, tvExperience;
    private EditText etName, etEmail, etDesignation, etSpecialties, etExperience;
    private ImageView editName, editEmail, editDesignation, editSpecialties, editExperience;
    private Button btnSubmit;
//    private FirebaseFirestore db;

    public AboutFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, container, false);
//        db = FirebaseFirestore.getInstance();

        tvName = view.findViewById(R.id.tvName);
        etName = view.findViewById(R.id.etName);
        editName = view.findViewById(R.id.editName);

        tvEmail = view.findViewById(R.id.tvEmail);
        etEmail = view.findViewById(R.id.etEmail);
        editEmail = view.findViewById(R.id.editEmail);

        tvDesignation = view.findViewById(R.id.tvDesignation);
        etDesignation = view.findViewById(R.id.etDesignation);
        editDesignation = view.findViewById(R.id.editDesignation);

        tvSpecialties = view.findViewById(R.id.tvSpecialties);
        etSpecialties = view.findViewById(R.id.etSpecialties);
        editSpecialties = view.findViewById(R.id.editSpecialties);

        tvExperience = view.findViewById(R.id.tvExperience);
        etExperience = view.findViewById(R.id.etExperience);
        editExperience = view.findViewById(R.id.editExperience);

        setEditFunctionality(tvName, etName, editName);
        setEditFunctionality(tvEmail, etEmail, editEmail);
        setEditFunctionality(tvDesignation, etDesignation, editDesignation);
        setEditFunctionality(tvSpecialties, etSpecialties, editSpecialties);
        setEditFunctionality(tvExperience, etExperience, editExperience);

        btnSubmit = view.findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(v -> submitDoctorDetails());

        return view;
    }

    private void setEditFunctionality(TextView textView, EditText editText, ImageView editIcon) {
        editIcon.setOnClickListener(v -> {
            textView.setVisibility(View.GONE);
            editText.setVisibility(View.VISIBLE);
            editText.setText(textView.getText().toString());
            editText.requestFocus();
        });

        // When "Enter" is pressed, save value and switch back to TextView
        editText.setOnKeyListener((v, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                textView.setText(editText.getText().toString());
                textView.setVisibility(View.VISIBLE);
                editText.setVisibility(View.GONE);
                return true;
            }
            return false;
        });

        // When focus is lost, save value and switch back to TextView
        editText.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                textView.setText(editText.getText().toString());
                textView.setVisibility(View.VISIBLE);
                editText.setVisibility(View.GONE);
            }
        });
    }

    private void submitDoctorDetails() {
        String name = tvName.getText().toString().trim();
        String email = tvEmail.getText().toString().trim();
        String designation = tvDesignation.getText().toString().trim();
        String specialties = tvSpecialties.getText().toString().trim();
        String experience = tvExperience.getText().toString().trim();

        if (name.isEmpty() || email.isEmpty() || designation.isEmpty() || specialties.isEmpty() || experience.isEmpty()) {
            Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        Doctor doctor = new Doctor(name, email, designation, specialties, experience);
        addDoctorDetail(doctor);
    }
//<-----------------------------Add Doctor to database-------------------------------------------------->//
    private void addDoctorDetail(Doctor doctor) {
//        db.collection("Doctors")
//                .add(doctor) // Generates a unique document ID
//                .addOnSuccessListener(documentReference -> {
//                    String doctorId = documentReference.getId();
//                    Log.d("Firebase", "Doctor added with ID: " + doctorId);
//                    Toast.makeText(getContext(), "Doctor details added successfully!", Toast.LENGTH_SHORT).show();
//                })
//                .addOnFailureListener(e -> {
//                    Log.e("Firebase", "Error adding doctor details", e);
//                    Toast.makeText(getContext(), "Failed to add doctor details", Toast.LENGTH_SHORT).show();
//                });
    }
}
