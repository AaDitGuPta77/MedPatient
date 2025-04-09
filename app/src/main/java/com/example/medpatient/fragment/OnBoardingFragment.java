package com.example.medpatient.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.medpatient.OnBoardingScreen;
import com.example.medpatient.R;
import com.example.medpatient.RegisterScreen;
import com.example.medpatient.backend.models.Doctor;
import com.google.android.material.textfield.TextInputLayout;

public class OnBoardingFragment extends Fragment {


    public OnBoardingFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_on_boarding, container, false);

        TextInputLayout fullName = view.findViewById(R.id.onBoardingScreenFullNameInputLayout);
        TextInputLayout specialization = view.findViewById(R.id.onBoardingScreenSpecializationInputLayout);
        TextInputLayout experience = view.findViewById(R.id.onBoardingScreenExperienceInputLayout);
        Button backBtn = view.findViewById(R.id.onBoardingScreenBackButton);
        Button nextBtn = view.findViewById(R.id.onBoardingScreenNextButton);

        backBtn.setOnClickListener(v-> {
            Intent intent = new Intent(getActivity(), RegisterScreen.class);
            startActivity(intent);
        });

        nextBtn.setOnClickListener(v-> {
            if (fullName.getEditText().getText().toString().isEmpty()
                || specialization.getEditText().getText().toString().isEmpty()
                || experience.getEditText().getText().toString().isEmpty()) {
                Toast.makeText(getActivity(), "All fields are required", Toast.LENGTH_SHORT).show();
            } else {
                Doctor doctor = new Doctor(
                        fullName.getEditText().getText().toString(),
                        specialization.getEditText().getText().toString(),
                        experience.getEditText().getText().toString(),
                        "", // experience will be set in the second onboarding step
                        "", // rating will be set in the second onboarding step
                        ""  // address will be set in the second onboarding step
                );

                ((OnBoardingScreen) requireActivity()).receiveUserData(doctor);

                getParentFragmentManager().beginTransaction()
                        .replace(R.id.onBoardingScreenFragmentContainer, new OnBoardingFragmentPart2())
                        .addToBackStack("Part2")
                        .commit();
            }
        });
        return view;
    }
}