package com.example.medpatient.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.medpatient.OnBoardingScreen;
import com.example.medpatient.R;
import com.example.medpatient.backend.BackendManager;
import com.example.medpatient.backend.models.Doctor;
import com.google.android.material.textfield.TextInputLayout;

public class OnBoardingFragmentPart2 extends Fragment {

    private BackendManager dbManager;

    public OnBoardingFragmentPart2() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_on_boarding_part2, container, false);

        TextInputLayout rating = view.findViewById(R.id.onBoardingScreenRatingInputLayout);
        TextInputLayout phone = view.findViewById(R.id.onBoardingScreenPhoneNumberInputLayout);
        TextInputLayout address = view.findViewById(R.id.onBoardingScreenAddressInputLayout);
        Button backBtn = view.findViewById(R.id.onBoardingScreenPart2BackButton);
        Button nextBtn = view.findViewById(R.id.onBoardingScreenPart2NextButton);

        backBtn.setOnClickListener( v-> {
            getActivity().getOnBackPressedDispatcher().onBackPressed();
        });

        nextBtn.setOnClickListener(v -> {
            String doctorRating = rating.getEditText().getText().toString().trim();
            String doctorPhone = phone.getEditText().getText().toString().trim();
            String doctorAddress = address.getEditText().getText().toString().trim();

            if (doctorRating.isEmpty() || doctorPhone.isEmpty() || doctorAddress.isEmpty()) {
                Toast.makeText(getContext(), "Please fill all the fields", Toast.LENGTH_SHORT).show();
            } else {
                // Create a map to store data
                Doctor doc = new Doctor();
                doc.setRatings(doctorRating);
                doc.setPhone(doctorPhone);
                doc.setAddress(doctorAddress);

                ((OnBoardingScreen) requireActivity()).receiveUserData(doc);

                Toast.makeText(getContext(), "Onboarding Complete!", Toast.LENGTH_SHORT).show();

            }
        });
        return view;
    }
}