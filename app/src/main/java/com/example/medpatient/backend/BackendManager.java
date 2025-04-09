package com.example.medpatient.backend;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.medpatient.backend.interfaces.AppointmentDetailsCallback;
import com.example.medpatient.backend.interfaces.AppointmentStatusCallback;
import com.example.medpatient.backend.interfaces.DoctorAppointmentsCallback;
import com.example.medpatient.backend.interfaces.DoctorDetailsCallback;
import com.example.medpatient.backend.interfaces.DoctorListCallback;
import com.example.medpatient.backend.interfaces.FcmCallback;
import com.example.medpatient.backend.interfaces.FcmTokenCallback;
import com.example.medpatient.backend.interfaces.FirebaseCallback;
import com.example.medpatient.backend.interfaces.MedicineOrderCallback;
import com.example.medpatient.backend.interfaces.MedicineOrderDetailsCallback;
import com.example.medpatient.backend.interfaces.MedicineOrderStatusCallback;
import com.example.medpatient.backend.interfaces.OrderStatusUpdateCallback;
import com.example.medpatient.backend.interfaces.PatientHistoryCallback;
import com.example.medpatient.backend.interfaces.PharmacyOrdersCallback;
import com.example.medpatient.backend.interfaces.PrescriptionCallback;
import com.example.medpatient.backend.interfaces.UpdateStatusCallback;
import com.example.medpatient.backend.interfaces.UserHistoryCallback;
import com.example.medpatient.backend.models.Appointment;
import com.example.medpatient.backend.models.Doctor;
import com.example.medpatient.backend.models.Medicine;
import com.example.medpatient.backend.models.Pharmacy;
import com.example.medpatient.backend.models.PharmacyRequest;
import com.example.medpatient.backend.models.Prescription;
import com.example.medpatient.backend.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


/* Overall database Structure

DatabaseRoot
    |--users
        |--userId
            |--fullName (String)
            |--contactNumber (String)
            |--address (String)
            |--gender (String)
            |--DOB (String)
            |--bloodGroup (String)
            |--registered (Boolean)
            |--fcmToken (String)  # FCM token for push notifications
    |--doctors
        |--doctorId
            |--name (String)
            |--specialization (String)
            |--experience (Integer)
            |--ratings (Double)
            |--phone (String)
            |--address (String)
            |--registered (Boolean)
            |--fcmToken (String)  # FCM token for push notifications
    |--pharmacies
        |--pharmacyId
            |--name (String)
            |--phone (String)
            |--address (String)
            |--registered (Boolean)
            |--fcmToken (String)  # FCM token for push notifications
    |--appointments
        |--appointmentId
            |--userId (String)
            |--doctorId (String)
            |--status (String)  # "Pending", "Accepted", "Completed", "Cancelled"
            |--date (String)
            |--diagnosis (String)
            |--prescriptionId (String)  # Links to a prescription (if generated)
            |--created_at (String)
    |--prescriptions
        |--prescriptionId
            |--appointmentId (String)
            |--doctorId (String)
            |--userId (String)
            |--medicines (Array)
                |--{ name: "Paracetamol", dosage: "500mg", frequency: "2 times a day" }
                |--{ name: "Cough Syrup", dosage: "10ml", frequency: "3 times a day" }
            |--instructions (String)
            |--created_at (String)
            |--homeDeliveryStatus (String)  # "Not Requested", "Requested", "Delivered"
    |--pharmacy_requests
        |--requestId
            |--userId (String)
            |--pharmacyId (String)
            |--prescriptionId (String)
            |--status (String)  # "Pending", "Processing", "Delivered", "Cancelled"
            |--created_at (String)
            |--updated_at (String)
    |--pending_notifications  # Buffer for failed notifications
        |--userId  # The user/doctor/pharmacy who should receive the notification
            |--notifId  # Unique notification ID
                |--title (String)
                |--body (String)
                |--timestamp (String)
                |--name (String)  # Optional: Name of the recipient
                |--gender (String)  # Optional: Gender of the recipient
                |--dob (String)  # Optional: Date of birth of the recipient


 */

public class BackendManager {
    private DatabaseReference database;
    private FirebaseAuth mAuth;
    // Write a message to the database

    public BackendManager(){
        database = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
    }

//-----------------------------------------------USER METHODS--------------------------------------------//

    // TODO: Implement addUserDetail(HashMap<String, String> map) function to add user details to the database.
    public void addUserDetail(User user) {
        String userId = mAuth.getCurrentUser() != null ? mAuth.getCurrentUser().getUid() : null;
        if (userId == null) {
            Log.e("Firebase", "User not authenticated. Cannot add user details.");
            return;
        }

        // Fetch FCM Token before saving
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e("FCM", "Fetching FCM registration token failed", task.getException());
                return;
            }

            String fcmToken = task.getResult();
            user.setRegistered(true);
            user.setFcmToken(fcmToken);

            // Store the user data in Firebase
            database.child("users").child(userId).setValue(user)
                    .addOnSuccessListener(aVoid -> Log.d("Firebase", "User details added successfully."))
                    .addOnFailureListener(e -> Log.e("Firebase", "Failed to add user details.", e));
        });
    }

    /*

    User newUser = new User("John Doe", "johndoe@example.com", "1234567890");
    addUserDetail(newUser);

     */



    // TODO: Implement getAllDoctors(final DoctorListCallback callback) function to retrieve a list of all registered doctors.
public void getAllDoctors(final DoctorListCallback callback) {
    DatabaseReference doctorsRef = FirebaseDatabase.getInstance().getReference("doctors");

    doctorsRef.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            List<Doctor> doctorList = new ArrayList<>();
            for (DataSnapshot doctorSnapshot : snapshot.getChildren()) {
                Doctor doctor = doctorSnapshot.getValue(Doctor.class);
                if (doctor != null) {
                    doctorList.add(doctor);
                }
            }
            callback.onSuccess(doctorList);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            Log.e("Firebase", "Failed to fetch doctors", error.toException());
            callback.onFailure("Failed to fetch doctors: " + error.getMessage());
        }
    });
}



    /*
    getAllDoctors(new DoctorListCallback() {
        @Override
        public void onSuccess(List<Doctor> doctors) {
            for (Doctor doctor : doctors) {
                Log.d("Doctor Info", "Name: " + doctor.getName() + ", Specialty: " + doctor.getSpecialty());
            }
        }

        @Override
        public void onFailure(String errorMessage) {
            Log.e("Doctor Fetch", "Error: " + errorMessage);
        }
    });
    */


// TODO: Implement getSelectedDoctorToken(String doctorId, FcmTokenCallback callback) function to fetch the FCM token of a selected doctor.

    public void getSelectedDoctorToken(String doctorId, FcmTokenCallback callback) {
        DatabaseReference doctorRef = database.child("doctors").child(doctorId).child("fcmToken");

        doctorRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult().exists()) {
                String fcmToken = task.getResult().getValue(String.class);
                callback.onTokenReceived(fcmToken);
            } else {
                Log.e("Firebase", "Failed to fetch doctor's FCM token", task.getException());
                callback.onFailure("Failed to fetch FCM token.");
            }
        });
    }



    /*
    realtimeDBManager.getSelectedDoctorToken("doctor123", new RealtimeDBManager.FcmTokenCallback() {
        @Override
        public void onSuccess(String fcmToken) {
            Log.d("FCM Token", "Doctor's FCM Token: " + fcmToken);
            // use token
        }

        @Override
        public void onFailure(String errorMessage) {
            Log.e("FCM Token Fetch", errorMessage);
        }
    });
     */


// TODO: Implement getSelectedDoctor(String doctorId, final DoctorDetailsCallback callback) function to retrieve details of a specific doctor based on doctorId.

    public void getSelectedDoctor(String doctorId, final DoctorDetailsCallback callback) {
        DatabaseReference doctorRef = FirebaseDatabase.getInstance().getReference("doctors").child(doctorId);

        doctorRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Doctor doctor = snapshot.getValue(Doctor.class);
                    callback.onDoctorDetailsReceived(doctor);
                } else {
                    callback.onDoctorDetailsReceived(null); // Doctor not found
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onDoctorDetailsReceived(null); // Handle the error scenario
            }
        });
    }

/*
    String doctorId = "DOCTOR123";

    getSelectedDoctor(doctorId, new DoctorDetailsCallback() {
        @Override
        public void onDoctorDetailsReceived(Doctor doctor) {
            if (doctor != null) {
                Log.d("Doctor Info", "Name: " + doctor.getName());
                Log.d("Doctor Info", "Specialization: " + doctor.getSpecialization());
                Log.d("Doctor Info", "Experience: " + doctor.getExperience() + " years");
                Log.d("Doctor Info", "Ratings: " + doctor.getRatings());
                Log.d("Doctor Info", "Phone: " + doctor.getPhone());
                Log.d("Doctor Info", "Address: " + doctor.getAddress());
                Log.d("Doctor Info", "Registered: " + doctor.isRegistered());
                Log.d("Doctor Info", "FCM Token: " + doctor.getFcmToken());
            } else {
                Log.e("Doctor Info", "Doctor not found.");
            }
        }
    });
*/


// TODO: Implement getUserHistory(String userId, final UserHistoryCallback callback) function to fetch a list of past appointments for the user.

    public void getUserHistory(String userId, final UserHistoryCallback callback) {
        DatabaseReference appointmentsRef = FirebaseDatabase.getInstance().getReference("appointments");

        appointmentsRef.orderByChild("userId").equalTo(userId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<Appointment> userAppointments = new ArrayList<>();
                        for (DataSnapshot appointmentSnapshot : snapshot.getChildren()) {
                            Appointment appointment = appointmentSnapshot.getValue(Appointment.class);
                            if (appointment != null) {
                                userAppointments.add(appointment);
                            }
                        }
                        callback.onUserHistoryReceived(userAppointments);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        callback.onUserHistoryReceived(null); // Handle error scenario
                    }
                });
    }

/*
    String userId = "USER123"; // Replace with actual user ID

    getUserHistory(userId, new UserHistoryCallback() {
        @Override
        public void onUserHistoryReceived(List<Appointment> appointments) {
            if (appointments != null && !appointments.isEmpty()) {
                for (Appointment appointment : appointments) {
                    Log.d("UserHistory", "Appointment ID: " + appointment.getAppointmentId());
                    Log.d("UserHistory", "Doctor ID: " + appointment.getDoctorId());
                    Log.d("UserHistory", "Status: " + appointment.getStatus());
                    Log.d("UserHistory", "Date: " + appointment.getDate());
                    Log.d("UserHistory", "Diagnosis: " + appointment.getDiagnosis());
                    Log.d("UserHistory", "Prescription ID: " + appointment.getPrescriptionId());
                    Log.d("UserHistory", "Created At: " + appointment.getCreatedAt());
                }
            } else {
                Log.e("UserHistory", "No past appointments found.");
            }
        }
    });

 */

// TODO: Implement getAllUserPrescriptions(String userId, final PrescriptionCallback callback) function to retrieve all prescriptions linked to the user.

    public void getAllUserPrescriptions(String userId, final PrescriptionCallback callback) {
        DatabaseReference prescriptionsRef = FirebaseDatabase.getInstance().getReference("prescriptions");

        prescriptionsRef.orderByChild("userId").equalTo(userId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<Prescription> userPrescriptions = new ArrayList<>();
                        for (DataSnapshot prescriptionSnapshot : snapshot.getChildren()) {
                            Prescription prescription = prescriptionSnapshot.getValue(Prescription.class);
                            if (prescription != null) {
                                userPrescriptions.add(prescription);
                            }
                        }
                        callback.onPrescriptionsReceived(userPrescriptions);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        callback.onPrescriptionsReceived(null); // Handle error scenario
                    }
                });
    }

// TODO: Implement requestAppointment(Context context, String userId, String doctorId, String appointmentDate, String userName, String userGender, String userDOB, final AppointmentCallback callback) function to allow users to request an appointment with a doctor.

    public void generateAppointmentAndNotify(Context context, String userId, String doctorId,
                                             String appointmentTime, String appointmentDate) {
        // Generate a unique appointment ID
        String appointmentId = FirebaseDatabase.getInstance().getReference().child("appointments").push().getKey();

        if (appointmentId == null) {
            Log.e("Firebase", "Failed to generate appointment ID.");
            return;
        }

        // Create appointment object
        Map<String, Object> appointmentData = new HashMap<>();
        appointmentData.put("userId", userId);
        appointmentData.put("doctorId", doctorId);
        appointmentData.put("status", "Pending");
        appointmentData.put("appointmentTime", appointmentTime);
        appointmentData.put("appointmentDate", appointmentDate);
        appointmentData.put("created_at", System.currentTimeMillis());

        // Save appointment to Firebase
        DatabaseReference appointmentRef = FirebaseDatabase.getInstance().getReference()
                .child("appointments").child(appointmentId);

        appointmentRef.setValue(appointmentData).addOnSuccessListener(aVoid -> {
            Log.d("Firebase", "Appointment successfully created.");

            // Fetch **User** details (for sending to the doctor)
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference()
                    .child("users").child(userId);

            userRef.get().addOnCompleteListener(userTask -> {
                if (userTask.isSuccessful() && userTask.getResult().exists()) {
                    DataSnapshot snapshot = userTask.getResult();
                    String userName = snapshot.child("fullName").getValue(String.class);
                    String userGender = snapshot.child("gender").getValue(String.class);
                    String userDob = snapshot.child("dob").getValue(String.class);

                    // Calculate **User Age** from DOB
                    int userAge = calculateAge(userDob);

                    // Send notification to the doctor with user details
                    sendAppointmentNotification(context, doctorId,
                            "New Appointment Request",
                            "You have a new appointment request from a " + userName + ".",
                            userName, userGender, userAge,appointmentId,
                            appointmentTime, appointmentDate);
                } else {
                    Log.e("Firebase", "Failed to fetch user details.");
                }
            });

        }).addOnFailureListener(e -> Log.e("Firebase", "Failed to create appointment: " + e.getMessage()));
    }

    /**
     * Helper function to calculate age from DOB
     */
    private int calculateAge(String dob) {
        if (dob == null || dob.isEmpty()) {
            return -1; // Return -1 if DOB is not available
        }

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            Date birthDate = sdf.parse(dob);
            Calendar birthCal = Calendar.getInstance();
            Calendar todayCal = Calendar.getInstance();

            birthCal.setTime(birthDate);

            int age = todayCal.get(Calendar.YEAR) - birthCal.get(Calendar.YEAR);

            if (todayCal.get(Calendar.DAY_OF_YEAR) < birthCal.get(Calendar.DAY_OF_YEAR)) {
                age--; // Adjust if birthday hasn't occurred yet this year
            }

            return age;
        } catch (ParseException e) {
            Log.e("Age Calculation", "Error parsing DOB: " + e.getMessage());
            return -1; // Return -1 if parsing fails
        }
    }



    /* First time call to get the status

    appointmentManager.requestAppointment(context, userId, doctorId, appointmentDate, userName, userGender, userDOB,
    new AppointmentCallback() {
        @Override
        public void onAppointmentRequest(boolean success, String message, String appointmentId) {
            if (success) {
                Log.d("Appointment", "Appointment requested successfully. ID: " + appointmentId);

                // Now you can call getAppointmentStatus() using the returned appointmentId
                appointmentManager.getAppointmentStatus(appointmentId, new AppointmentStatusCallback() {
                    @Override
                    public void onSuccess(String status) {
                        Log.d("Appointment", "Appointment Status: " + status);
                    }

                    @Override
                    public void onFailure(String error) {
                        Log.e("Appointment", "Error: " + error);
                    }
                });
            } else {
                Log.e("Appointment", "Failed to request appointment: " + message);
            }
        }
    });

     */


// TODO: Implement cancelAppointment() function to allow users to cancel an appointment before confirmation.

// TODO: Implement getAppointmentStatus(String appointmentId, final AppointmentStatusCallback callback) function to check the current status of a user's appointment.

    public void getAppointmentStatus(String appointmentId, final AppointmentStatusCallback callback) {
        DatabaseReference appointmentRef = FirebaseDatabase.getInstance().getReference("appointments").child(appointmentId);

        appointmentRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String status = snapshot.child("status").getValue(String.class);
                    if (status != null) {
                        callback.onSuccess(status);
                    } else {
                        callback.onFailure("Status not found.");
                    }
                } else {
                    callback.onFailure("Appointment not found.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onFailure("Error fetching status: " + error.getMessage());
            }
        });
    }

    /*

    String appointmentId = "appointment123"; // Replace with actual appointment ID

    getAppointmentStatus(appointmentId, new AppointmentStatusCallback() {
        @Override
        public void onSuccess(String status) {
            Log.d("AppointmentStatus", "Current Status: " + status);
        }

        @Override
        public void onFailure(String error) {
            Log.e("AppointmentStatus", "Error: " + error);
        }
    });
     */



// TODO: Implement requestMedicineOrder(Context context,String userId, String pharmacyId, String prescriptionId, final MedicineOrderCallback callback) function to request medicine from a pharmacy based on a prescription.

    public void requestMedicineOrder(Context context,String userId, String pharmacyId, String prescriptionId, final MedicineOrderCallback callback) {
        DatabaseReference pharmacyRequestsRef = FirebaseDatabase.getInstance().getReference("pharmacy_requests");
        String requestId = pharmacyRequestsRef.push().getKey(); // Generate unique request ID
        String createdAt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

        if (requestId == null) {
            callback.onFailure("Failed to generate request ID.");
            return;
        }

        // Create pharmacy request data
        Map<String, Object> requestData = new HashMap<>();
        requestData.put("userId", userId);
        requestData.put("pharmacyId", pharmacyId);
        requestData.put("prescriptionId", prescriptionId);
        requestData.put("status", "Pending");
        requestData.put("created_at", createdAt);
        requestData.put("updated_at", createdAt);

        // Store the medicine request in the database
        pharmacyRequestsRef.child(requestId).setValue(requestData).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Fetch pharmacy's FCM token and send a notification
                getSelectedUserToken(pharmacyId, new FcmTokenCallback() {
                    @Override
                    public void onTokenReceived(String pharmacyFcmToken) {
                        if (pharmacyFcmToken != null && !pharmacyFcmToken.isEmpty()) {
                            sendGeneralNotification(context, pharmacyId, "You have received a new medicine request.", "Message");
                        }
                    }

                    @Override
                    public void onFailure(String error) {
                        Log.e("FCM", "Failed to fetch pharmacy's FCM token: " + error);
                    }
                });

                callback.onSuccess("Medicine order requested successfully.");
            } else {
                callback.onFailure("Failed to request medicine order.");
            }
        });
    }

    /*
    String userId = "user123"; // Replace with actual user ID
    String pharmacyId = "pharmacy456"; // Replace with actual pharmacy ID
    String prescriptionId = "prescription789"; // Replace with actual prescription ID

    requestMedicineOrder(userId, pharmacyId, prescriptionId, new MedicineOrderCallback() {
        @Override
        public void onSuccess(String message) {
            Log.d("MedicineOrder", message);
        }

        @Override
        public void onFailure(String error) {
            Log.e("MedicineOrder", "Error: " + error);
        }
    });

     */

// TODO: Implement getMedicineOrderStatus(String requestId, final MedicineOrderStatusCallback callback) function to track the status of a requested medicine order.

    public void getMedicineOrderStatus(String requestId, final MedicineOrderStatusCallback callback) {
        if (requestId == null || requestId.isEmpty()) {
            callback.onFailure("Invalid request ID.");
            return;
        }

        DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference("pharmacy_requests").child(requestId);

        orderRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String status = snapshot.child("status").getValue(String.class);
                    if (status != null) {
                        callback.onSuccess(status);
                    } else {
                        callback.onFailure("Status not found.");
                    }
                } else {
                    callback.onFailure("Medicine order not found.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onFailure("Database error: " + error.getMessage());
            }
        });
    }

    /*
    getMedicineOrderStatus("request123", new MedicineOrderStatusCallback() {
        @Override
        public void onSuccess(String status) {
            Log.d("OrderStatus", "Current status: " + status);
        }

        @Override
        public void onFailure(String errorMessage) {
            Log.e("OrderStatus", "Error: " + errorMessage);
        }
    });

     */



// -----------------------------------------------DOCTOR METHODS--------------------------------------------//

// TODO: Implement register addDoctorDetail(Doctor doctor) function to register a new doctor.

    public void addDoctorDetail(Doctor doctor) {
        String doctorId = mAuth.getCurrentUser() != null ? mAuth.getCurrentUser().getUid() : null;
        if (doctorId == null) {
            Log.e("Firebase", "Doctor not authenticated. Cannot register doctor.");
            return;
        }

        // Fetch FCM Token before saving
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e("FCM", "Fetching FCM registration token failed", task.getException());
                return;
            }

            String fcmToken = task.getResult();
            doctor.setRegistered(true);
            doctor.setFcmToken(fcmToken);

            // Store the doctor data in Firebase
            database.child("doctors").child(doctorId).setValue(doctor)
                    .addOnSuccessListener(aVoid -> Log.d("Firebase", "Doctor registered successfully."))
                    .addOnFailureListener(e -> Log.e("Firebase", "Failed to register doctor.", e));
        });
    }

    /*
        User newDoctor = new Doctor(name, specialty, experience, ratings, phone, address);
        addDoctorDetail(newDoctor);
     */



// TODO: Implement getAllDoctorAppointments(String doctorId) function to retrieve a list of all pending and completed appointments for the doctor.

    public void getAllDoctorAppointments(final DoctorAppointmentsCallback callback) {
        String doctorId = ""; // Replace with actual doctor ID
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            doctorId = currentUser.getUid();  // 🔥 This is the current user's UID
            Log.d("FirebaseAuth", "Current user ID: " + doctorId);
        } else {
            Log.w("FirebaseAuth", "No user is currently logged in.");
        }
        List<Appointment> doctorAppointments = new ArrayList<>();
        DatabaseReference appointmentsRef = FirebaseDatabase.getInstance().getReference("appointments");

        appointmentsRef.orderByChild("doctorId").equalTo(doctorId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot appointmentSnapshot : snapshot.getChildren()) {
                            if (appointmentSnapshot.exists()) {
                                Appointment appointment = new Appointment();

                                // Set Appointment ID from snapshot key
                                appointment.setAppointmentId(appointmentSnapshot.getKey());

                                // Manually retrieve and set each field
                                appointment.setDoctorId(appointmentSnapshot.child("doctorId").getValue(String.class));
                                appointment.setUserId(appointmentSnapshot.child("userId").getValue(String.class));
                                appointment.setStatus(appointmentSnapshot.child("status").getValue(String.class));
                                appointment.setDate(appointmentSnapshot.child("appointmentDate").getValue(String.class));
                                appointment.setTime(appointmentSnapshot.child("appointmentTime").getValue(String.class));
                                appointment.setCreatedAt(String.valueOf(appointmentSnapshot.child("created_at").getValue(Long.class)));
                                appointment.setAppointmentId(appointmentSnapshot.getKey());

                                // Only add if status is Pending or Completed
                                if (appointment.getStatus() != null &&
                                        (appointment.getStatus().equals("Pending") || appointment.getStatus().equals("Completed"))) {
                                    doctorAppointments.add(appointment);
                                }
                            }
                        }
                        callback.onAppointmentsReceived(doctorAppointments);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("Firebase", "Error fetching doctor appointments: " + error.getMessage());
                        callback.onError(error.getMessage());
                    }
                });
    }


    
/*
    getAllDoctorAppointments(doctorId, new DoctorAppointmentsCallback() {
        @Override
        public void onAppointmentsReceived(List<Appointment> appointments) {
            for (Appointment appointment : appointments) {
                Log.d("DoctorAppointments", "Appointment ID: " + appointment.getAppointmentId() +
                                            ", Status: " + appointment.getStatus());
            }
        }

        @Override
        public void onError(String errorMessage) {
            Log.e("DoctorAppointments", "Error fetching appointments: " + errorMessage);
        }
    });
*/

// TODO: Implement getAppointmentDetails() function to retrieve detailed information about a specific appointment.

    public void getAppointmentDetails(String appointmentId, final AppointmentDetailsCallback callback) {
        DatabaseReference appointmentRef = FirebaseDatabase.getInstance().getReference("appointments").child(appointmentId);

        appointmentRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Appointment appointment = snapshot.getValue(Appointment.class);
                    if (appointment != null) {
                        callback.onSuccess(appointment);
                    } else {
                        callback.onFailure("Failed to retrieve appointment details.");
                    }
                } else {
                    callback.onFailure("Appointment not found.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onFailure("Database error: " + error.getMessage());
            }
        });
    }

    /*
    getAppointmentDetails("appointment123", new AppointmentDetailsCallback() {
        @Override
        public void onSuccess(Appointment appointment) {
            Log.d("Appointment", "Details: " + appointment.toString());
            // Display details in UI
        }

        @Override
        public void onFailure(String errorMessage) {
            Log.e("Appointment", "Error: " + errorMessage);
        }
    });
     */



// TODO: Implement updateAppointmentStatus() function to allow doctors to accept, complete, or cancel an appointment.

    public void updateAppointmentStatus(String doctorId, String appointmentId, String newStatus, final UpdateStatusCallback callback) {
        DatabaseReference appointmentRef = FirebaseDatabase.getInstance().getReference("appointments").child(appointmentId);

        appointmentRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Appointment appointment = snapshot.getValue(Appointment.class);
                    if (appointment != null && appointment.getDoctorId().equals(doctorId)) {
                        // Only update if the doctor ID matches
                        Map<String, Object> updates = new HashMap<>();
                        updates.put("status", newStatus);

                        appointmentRef.updateChildren(updates)
                                .addOnSuccessListener(aVoid -> callback.onSuccess("Appointment status updated successfully."))
                                .addOnFailureListener(e -> callback.onFailure("Failed to update appointment status: " + e.getMessage()));

                    } else {
                        callback.onFailure("Doctor is not authorized to update this appointment.");
                    }
                } else {
                    callback.onFailure("Appointment not found.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onFailure("Database error: " + error.getMessage());
            }
        });
    }

    /*
    updateAppointmentStatus("doctor123", "appointment456", "Accepted", new UpdateStatusCallback() {
        @Override
        public void onSuccess(String message) {
            Log.d("Appointment", message);
        }

        @Override
        public void onFailure(String errorMessage) {
            Log.e("Appointment", "Error: " + errorMessage);
        }
    });
     */



// TODO: Implement getPatientHistory() function to view the medical history of a patient before accepting an appointment.

    public void getPatientHistory(String userId, final PatientHistoryCallback callback) {
        DatabaseReference appointmentsRef = FirebaseDatabase.getInstance().getReference("appointments");
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users").child(userId);

        // Fetch user name first
        usersRef.child("name").get().addOnCompleteListener(userTask -> {
            if (userTask.isSuccessful() && userTask.getResult().exists()) {
                String userName = userTask.getResult().getValue(String.class);

                // Now fetch the patient's past appointments
                appointmentsRef.orderByChild("userId").equalTo(userId)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                List<Appointment> patientHistory = new ArrayList<>();
                                for (DataSnapshot appointmentSnapshot : snapshot.getChildren()) {
                                    Appointment appointment = appointmentSnapshot.getValue(Appointment.class);
                                    if (appointment != null && (appointment.getStatus().equals("Completed") || appointment.getStatus().equals("Accepted"))) {
                                        patientHistory.add(appointment);
                                    }
                                }
                                callback.onHistoryReceived(userName, patientHistory);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                callback.onError("Failed to fetch appointment history: " + error.getMessage());
                            }
                        });
            } else {
                callback.onError("User not found.");
            }
        });
    }

    /*
    getPatientHistory("user123", new PatientHistoryCallback() {
        @Override
        public void onHistoryReceived(String userName, List<Appointment> history) {
            Log.d("Patient History", "User: " + userName);
            for (Appointment appointment : history) {
                Log.d("Appointment", "Date: " + appointment.getDate() + ", Diagnosis: " + appointment.getDiagnosis());
            }
        }

        @Override
        public void onError(String errorMessage) {
            Log.e("Patient History", "Error: " + errorMessage);
        }
    });
     */
// TODO: Implement sendPrescription() function to allow doctors to upload prescription object to patient's appointment and reflect them in their history.

    public void sendPrescription(String appointmentId, String doctorId, String userId, List<Medicine> medicines, String instructions, final PrescriptionCallback callback) {
        DatabaseReference prescriptionsRef = FirebaseDatabase.getInstance().getReference("prescriptions");
        DatabaseReference appointmentRef = FirebaseDatabase.getInstance().getReference("appointments").child(appointmentId);

        String prescriptionId = prescriptionsRef.push().getKey(); // Generate a unique prescription ID
        String createdAt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

        if (prescriptionId == null) {
            callback.onPrescriptionsReceived(new ArrayList<>()); // Return an empty list on failure
            return;
        }

        // Create prescription object
        Prescription newPrescription = new Prescription(
                prescriptionId, appointmentId, doctorId, userId, medicines, instructions, createdAt, "Not Requested"
        );

        // Store the prescription in the database
        prescriptionsRef.child(prescriptionId).setValue(newPrescription).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Update the appointment with the prescription ID and mark it as completed
                Map<String, Object> updateData = new HashMap<>();
                updateData.put("prescriptionId", prescriptionId);
                updateData.put("status", "Completed");

                appointmentRef.updateChildren(updateData).addOnCompleteListener(appointmentTask -> {
                    if (appointmentTask.isSuccessful()) {
                        // Return the new prescription inside a list
                        List<Prescription> prescriptionList = new ArrayList<>();
                        prescriptionList.add(newPrescription);
                        callback.onPrescriptionsReceived(prescriptionList);
                    } else {
                        callback.onPrescriptionsReceived(new ArrayList<>());
                    }
                });
            } else {
                callback.onPrescriptionsReceived(new ArrayList<>());
            }
        });
    }

    /*

    List<Medicine> medicines = new ArrayList<>();
    medicines.add(new Medicine("Paracetamol", "500mg", "2 times a day"));
    medicines.add(new Medicine("Cough Syrup", "10ml", "3 times a day"));

    sendPrescription("appointment123", "doctor456", "user789", medicines, "Take with food.", new PrescriptionCallback() {
        @Override
        public void onPrescriptionsReceived(List<Prescription> prescriptions) {
            if (!prescriptions.isEmpty()) {
                Log.d("Prescription", "Prescription uploaded: " + prescriptions.get(0).getPrescriptionId());
            } else {
                Log.e("Prescription", "Failed to upload prescription.");
            }
        }
    });

     */


// TODO: Implement getSelectedUserToken() function to fetch the FCM token of a selected user to send notifications.

    public void getSelectedUserToken(String userId, final FcmTokenCallback callback) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(userId).child("fcmToken");

        userRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult().exists()) {
                String fcmToken = task.getResult().getValue(String.class);
                if (fcmToken != null && !fcmToken.isEmpty()) {
                    callback.onTokenReceived(fcmToken);
                } else {
                    callback.onFailure("FCM token is null or empty.");
                }
            } else {
                callback.onFailure("Failed to fetch FCM token: " + task.getException().getMessage());
            }
        });
    }

    /*
        String userId = "user123"; // Replace with actual user ID
        getSelectedUserToken(userId, new FcmTokenCallback() {
            @Override
            public void onTokenReceived(String token) {
                Log.d("FCM", "User's FCM Token: " + token);
                // Now you can use this token to send notifications
            }

            @Override
            public void onFailure(String error) {
                Log.e("FCM", "Error fetching FCM token: " + error);
            }
        });

     */


// -----------------------------------------------PHARMACY METHODS--------------------------------------------//

// TODO: Implement addPharmacyDetail(Pharmacy pharmacy) function to register a new pharmacy.

    public void addPharmacyDetail(Pharmacy pharmacy) {
        String pharmacyId = mAuth.getCurrentUser() != null ? mAuth.getCurrentUser().getUid() : null;
        if (pharmacyId == null) {
            Log.e("Firebase", "Pharmacy not authenticated. Cannot register pharmacy.");
            return;
        }

        // Fetch FCM Token before saving
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e("FCM", "Fetching FCM registration token failed", task.getException());
                return;
            }

            String fcmToken = task.getResult();
            pharmacy.setRegistered(true);
            pharmacy.setFcmToken(fcmToken);

            // Store the pharmacy data in Firebase
            database.child("pharmacies").child(pharmacyId).setValue(pharmacy)
                    .addOnSuccessListener(aVoid -> Log.d("Firebase", "Pharmacy registered successfully."))
                    .addOnFailureListener(e -> Log.e("Firebase", "Failed to register pharmacy.", e));
        });
    }

    /*
    Pharmacy newPharmacy = new Pharmacy(pharmacyName, pharmacyPhone, pharmacyAddress);
    addPharmacyDetail(newPharmacy);
     */


// TODO: Implement getAllMedicineOrders() function to retrieve all pending and completed pharmacy requests.

    public void getAllMedicineOrders(String pharmacyId, final PharmacyOrdersCallback callback) {
        DatabaseReference pharmacyRequestsRef = FirebaseDatabase.getInstance().getReference("pharmacy_requests");

        pharmacyRequestsRef.orderByChild("pharmacyId").equalTo(pharmacyId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<PharmacyRequest> pharmacyOrders = new ArrayList<>();
                        for (DataSnapshot requestSnapshot : snapshot.getChildren()) {
                            PharmacyRequest request = requestSnapshot.getValue(PharmacyRequest.class);
                            if (request != null && (request.getStatus().equals("Pending") || request.getStatus().equals("Delivered"))) {
                                pharmacyOrders.add(request);
                            }
                        }
                        callback.onOrdersReceived(pharmacyOrders);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        callback.onError(error.getMessage());
                    }
                });
    }

    /*

    String pharmacyId = "pharmacy123"; // Replace with actual pharmacy ID

    getAllMedicineOrders(pharmacyId, new PharmacyOrdersCallback() {
        @Override
        public void onOrdersReceived(List<PharmacyRequest> orders) {
            for (PharmacyRequest order : orders) {
                Log.d("PharmacyOrder", "Request ID: " + order.getPrescriptionId() + ", Status: " + order.getStatus());
            }
        }

        @Override
        public void onError(String error) {
            Log.e("PharmacyOrder", "Error fetching orders: " + error);
        }
    });
     */


// TODO: Implement getMedicineOrderDetails() function to retrieve detailed information about a specific medicine request.

    public void getMedicineOrderDetails(String requestId, final MedicineOrderDetailsCallback callback) {
        DatabaseReference pharmacyRequestsRef = FirebaseDatabase.getInstance().getReference("pharmacy_requests").child(requestId);

        pharmacyRequestsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    PharmacyRequest request = snapshot.getValue(PharmacyRequest.class);
                    callback.onOrderDetailsReceived(request);
                } else {
                    callback.onError("No order found with the given request ID.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onError(error.getMessage());
            }
        });
    }

    /*
    String requestId = "request123"; // Replace with actual request ID

    getMedicineOrderDetails(requestId, new MedicineOrderDetailsCallback() {
        @Override
        public void onOrderDetailsReceived(PharmacyRequest order) {
            Log.d("OrderDetails", "User ID: " + order.getUserId() +
                                  ", Prescription ID: " + order.getPrescriptionId() +
                                  ", Status: " + order.getStatus());
        }

        @Override
        public void onError(String error) {
            Log.e("OrderDetails", "Error fetching order details: " + error);
        }
    });

     */


// TODO: Implement updateMedicineOrderStatus() function to allow pharmacies to update order status (Processing, Delivered, Cancelled).

    public void updateMedicineOrderStatus(String requestId, String newStatus, final OrderStatusUpdateCallback callback) {
        DatabaseReference pharmacyRequestsRef = FirebaseDatabase.getInstance().getReference("pharmacy_requests").child(requestId);

        Map<String, Object> updates = new HashMap<>();
        updates.put("status", newStatus);
        updates.put("updated_at", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date()));

        pharmacyRequestsRef.updateChildren(updates).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                callback.onSuccess("Order status updated successfully.");
            } else {
                callback.onFailure("Failed to update order status.");
            }
        }).addOnFailureListener(e -> callback.onFailure(e.getMessage()));
    }

    /*

    String requestId = "request123"; // Replace with actual request ID
    String newStatus = "Processing"; // "Processing", "Delivered", "Cancelled"

    updateMedicineOrderStatus(requestId, newStatus, new OrderStatusUpdateCallback() {
        @Override
        public void onSuccess(String message) {
            Log.d("OrderUpdate", message);
        }

        @Override
        public void onFailure(String error) {
            Log.e("OrderUpdate", "Error updating order: " + error);
        }
    });

     */


// TODO: Implement verifyUserPayment() function to confirm payment before processing a medicine order.


// TODO: Implement getSelectedUserToken() function to fetch the FCM token of a selected user for notifications.


// -----------------------------------------------GENERAL METHODS--------------------------------------------//

// TODO: Implement sendPushNotification() function to send real-time notifications to users, doctors, and pharmacies using FCM tokens.
    public void sendAppointmentNotification(Context context, String recipientId,
                                            String title, String message, String senderName,
                                            String senderGender, int senderAge,
                                            String appointmentId,
                                            String appointmentTime, String appointmentDate) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users") ////
                .child(recipientId).child("fcmToken");

        userRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult().exists()) {
                String recipientFcmToken = task.getResult().getValue(String.class);
                if (recipientFcmToken == null || recipientFcmToken.isEmpty()) {
                    Log.e("FCM", "FCM token is null or empty. Storing notification for retry.");
                    storeFailedNotifications(recipientId, title, message, senderName, senderGender, String.valueOf(senderAge),
                            "appointment",appointmentId, appointmentTime, appointmentDate);
                    return;
                }

                // Call helper function to send appointment notification
                sendAppointmentFCM(context, recipientFcmToken, title, message, senderName, senderGender, String.valueOf(senderAge),
                        appointmentTime, appointmentDate, new FcmCallback() {
                            @Override
                            public void onSuccess() {
                                Log.d("FCM", "Appointment notification sent successfully.");
                            }

                            @Override
                            public void onFailure(String error) {
                                Log.e("FCM", "Appointment notification failed. Storing for retry. Error: " + error);
                                storeFailedNotifications(recipientId, title, message, senderName, senderGender, String.valueOf(senderAge),
                                        "appointment",appointmentId, appointmentTime, appointmentDate);
                            }
                        });
            } else {
                Log.e("FCM", "Failed to fetch FCM token. Storing notification for retry.");
                storeFailedNotifications(recipientId, title, message, senderName, senderGender, String.valueOf(senderAge),
                        "appointment", appointmentId, appointmentTime, appointmentDate);
            }
        });
    }

    private void sendAppointmentFCM(Context context, String recipientFcmToken, String title, String message, String name,String gender,String dob, String appointmentTime, String appointmentDate, FcmCallback callback) {
        FcmNotificationSender sender = new FcmNotificationSender(context, recipientFcmToken, title, message);
        sender.sendAppointmentNotification(recipientFcmToken, title, message, name, gender, dob,appointmentDate,appointmentTime, callback);
    }



    public void sendGeneralNotification(Context context, String recipientId,
                                        String title, String message) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users")
                .child(recipientId).child("fcmToken");

        userRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult().exists()) {
                String recipientFcmToken = task.getResult().getValue(String.class);
                if (recipientFcmToken == null || recipientFcmToken.isEmpty()) {
                    Log.e("FCM", "FCM token is null or empty. Storing notification for retry.");
                    retryGeneralNotifications(context);
                    return;
                }

                // Call helper function to send general notification
                sendGeneralFCM(context, recipientFcmToken, title, message, new FcmCallback() {
                    @Override
                    public void onSuccess() {
                        Log.d("FCM", "General notification sent successfully.");
                    }

                    @Override
                    public void onFailure(String error) {
                        Log.e("FCM", "General notification failed. Storing for retry. Error: " + error);
                        retryGeneralNotifications(context);
                    }
                });

            } else {
                Log.e("FCM", "Failed to fetch FCM token. Storing notification for retry.");
                retryGeneralNotifications(context);
            }
        });
    }

    // Helper function to send general FCM notification
    private void sendGeneralFCM(Context context, String recipientFcmToken, String title, String message, FcmCallback callback) {
        FcmNotificationSender sender = new FcmNotificationSender(context, recipientFcmToken, title, message);
        sender.sendGeneralNotification(recipientFcmToken, title, message, callback);
    }





    // TODO: Implement storeFailedNotifications() function to store failed push notifications in the "pending_notifications" node for retrying.
    public void storeFailedNotifications(String recipientId, String title, String body,
                                         String senderName, String senderGender, String senderDOB,
                                         String type, String appointmentId, String appointmentTime, String appointmentDate) {
        if (recipientId == null || recipientId.isEmpty()) {
            Log.e("Firebase", "Recipient ID is null or empty. Cannot store failed notification.");
            return;
        }

        DatabaseReference notifRef = database.child("pending_notifications").child(recipientId).push();

        Map<String, Object> notifData = new HashMap<>();
        notifData.put("title", title);
        notifData.put("body", body);
        notifData.put("timestamp", System.currentTimeMillis());
        notifData.put("type", type);  // "appointment" or "general"

        // Include sender details if available
        if (senderName != null) notifData.put("name", senderName);
        if (senderGender != null) notifData.put("gender", senderGender);
        if (senderDOB != null) notifData.put("dob", senderDOB);

        // If it's an appointment notification, include appointment details
        if ("appointment".equals(type)) {
            if (appointmentId != null) notifData.put("appointmentId", appointmentId);
            if (appointmentTime != null) notifData.put("appointmentTime", appointmentTime);
            if (appointmentDate != null) notifData.put("appointmentDate", appointmentDate);
        }

        notifRef.setValue(notifData)
                .addOnSuccessListener(aVoid -> Log.d("Firebase", "Failed notification stored successfully."))
                .addOnFailureListener(e -> Log.e("Firebase", "Failed to store notification.", e));
    }



    // TODO: Implement retryFailedNotifications() function to periodically check and resend failed notifications.
    public void retryGeneralNotifications(Context context) {
        DatabaseReference notifRef = database.child("pending_notifications");

        notifRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult().exists()) {
                for (DataSnapshot userSnapshot : task.getResult().getChildren()) {
                    String recipientId = userSnapshot.getKey();

                    for (DataSnapshot notifSnapshot : userSnapshot.getChildren()) {
                        String notifId = notifSnapshot.getKey();
                        String title = notifSnapshot.child("title").getValue(String.class);
                        String body = notifSnapshot.child("body").getValue(String.class);
                        String type = notifSnapshot.child("type").getValue(String.class);

                        if (!"general".equals(type)) {
                            continue; // Skip if it's not a general notification
                        }

                        // Retrieve the recipient's FCM token
                        database.child("users").child(recipientId).child("fcmToken").get()
                                .addOnCompleteListener(tokenTask -> {
                                    if (tokenTask.isSuccessful() && tokenTask.getResult().exists()) {
                                        String recipientFcmToken = tokenTask.getResult().getValue(String.class);

                                        if (recipientFcmToken != null) {
                                            FcmNotificationSender sender = new FcmNotificationSender(context, recipientFcmToken, title, body);
                                            sender.sendGeneralNotification(recipientFcmToken, title, body, new FcmCallback() {
                                                @Override
                                                public void onSuccess() {
                                                    Log.d("FCM", "General notification resent successfully.");
                                                    // Remove the notification from pending_notifications
                                                    notifSnapshot.getRef().removeValue()
                                                            .addOnSuccessListener(aVoid -> Log.d("Firebase", "Resent general notification removed."))
                                                            .addOnFailureListener(e -> Log.e("Firebase", "Failed to remove resent general notification.", e));
                                                }

                                                @Override
                                                public void onFailure(String error) {
                                                    Log.e("FCM", "General notification resend failed. Keeping it for retry.");
                                                }
                                            });
                                        }
                                    }
                                });
                    }
                }
            } else {
                Log.d("Firebase", "No pending general notifications to retry.");
            }
        });
    }

    public void retryAppointmentNotifications(Context context) {
        DatabaseReference notifRef = database.child("pending_notifications");

        notifRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult().exists()) {
                for (DataSnapshot userSnapshot : task.getResult().getChildren()) {
                    String recipientId = userSnapshot.getKey();

                    for (DataSnapshot notifSnapshot : userSnapshot.getChildren()) {
                        String notifId = notifSnapshot.getKey();
                        String title = notifSnapshot.child("title").getValue(String.class);
                        String body = notifSnapshot.child("body").getValue(String.class);
                        String senderName = notifSnapshot.child("name").getValue(String.class);
                        String senderGender = notifSnapshot.child("gender").getValue(String.class);
                        String senderDOB = notifSnapshot.child("dob").getValue(String.class);
                        String appointmentId = notifSnapshot.child("appointmentId").getValue(String.class);
                        String appointmentTime = notifSnapshot.child("appointmentTime").getValue(String.class);
                        String appointmentDate = notifSnapshot.child("appointmentDate").getValue(String.class);
                        String type = notifSnapshot.child("type").getValue(String.class);

                        if (!"appointment".equals(type)) {
                            continue; // Skip if it's not an appointment notification
                        }

                        // Retrieve the recipient's FCM token
                        database.child("doctors").child(recipientId).child("fcmToken").get()
                                .addOnCompleteListener(tokenTask -> {
                                    if (tokenTask.isSuccessful() && tokenTask.getResult().exists()) {
                                        String recipientFcmToken = tokenTask.getResult().getValue(String.class);

                                        if (recipientFcmToken != null) {
                                            FcmNotificationSender sender = new FcmNotificationSender(context, recipientFcmToken, title, body);
                                            sender.sendAppointmentNotification(recipientFcmToken, title, body, senderName, senderGender, senderDOB,
                                                    appointmentDate, appointmentTime, new FcmCallback() {
                                                        @Override
                                                        public void onSuccess() {
                                                            Log.d("FCM", "Appointment notification resent successfully.");
                                                            // Remove the notification from pending_notifications
                                                            notifSnapshot.getRef().removeValue()
                                                                    .addOnSuccessListener(aVoid -> Log.d("Firebase", "Resent appointment notification removed."))
                                                                    .addOnFailureListener(e -> Log.e("Firebase", "Failed to remove resent appointment notification.", e));
                                                        }

                                                        @Override
                                                        public void onFailure(String error) {
                                                            Log.e("FCM", "Appointment notification resend failed. Keeping it for retry.");
                                                        }
                                                    });
                                        }
                                    }
                                });
                    }
                }
            } else {
                Log.d("Firebase", "No pending appointment notifications to retry.");
            }
        });
    }





// TODO: Implement getUserType() function to determine whether the current authenticated user is a user, doctor, or pharmacy.

// TODO: Implement logoutUser() function to handle user logout and remove the FCM token from the database.

    public void isUserRegistered(FirebaseCallback callback) {
        String user = mAuth.getUid();
        if (user == null) {
            callback.onCallback(false);
            return;
        }

        DatabaseReference userRef = database.child("doctors").child(user).child("registered");

        userRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Boolean isRegistered = task.getResult().getValue(Boolean.class);
                if (Boolean.TRUE.equals(isRegistered)) {
                    Log.d("Firebase", "User is registered.");
                    callback.onCallback(true);
                } else {
                    Log.d("Firebase", "User is NOT registered.");
                    callback.onCallback(false);
                }
            } else {
                Log.e("Firebase", "Error fetching data", task.getException());
                callback.onCallback(false);
            }
        });
    }
}
