# Backend Manager Documentation

## Overview

This document provides an overview of the `BackendManager` class and its functions within the `backend` folder. The `BackendManager` class is responsible for managing various backend operations such as user management, doctor management, appointment scheduling, and more.

## Functions in BackendManager.java

### User Methods

1. **addUserDetail(User user)**
    - Adds user details to the database.
    - Fetches FCM Token before saving.
    - Stores the data in Firebase.
    ```java
    User newUser = new User("John Doe", "johndoe@example.com", "1234567890");
    addUserDetail(newUser);
    ```

2. **getAllDoctors(final DoctorListCallback callback)**
    - Retrieves a list of all registered doctors.
    - Calls the callback with the list of doctors or an error message.
    ```java
    backendManager.getAllDoctors(new DoctorListCallback() {
        @Override
        public void onCallback(List<Doctor> doctorList) {
            // Handle the list of doctors
        }

        @Override
        public void onError(String error) {
            // Handle the error
        }
    });
    ```

3. **getSelectedDoctorToken(String doctorId, FcmTokenCallback callback)**
    - Fetches the FCM token of a selected doctor.
    - Calls the callback with the token or an error message.
    ```java
    backendManager.getSelectedDoctorToken("doctorId123", new FcmTokenCallback() {
        @Override
        public void onCallback(String token) {
            // Handle the token
        }

        @Override
        public void onError(String error) {
            // Handle the error
        }
    });
    ```

4. **getSelectedDoctor(String doctorId, final DoctorDetailsCallback callback)**
    - Retrieves details of a specific doctor based on doctorId.
    - Calls the callback with the doctor details or an error message.
    ```java
    backendManager.getSelectedDoctor("doctorId123", new DoctorDetailsCallback() {
        @Override
        public void onCallback(Doctor doctor) {
            // Handle the doctor details
        }

        @Override
        public void onError(String error) {
            // Handle the error
        }
    });
    ```

5. **getUserHistory(String userId, final UserHistoryCallback callback)**
    - Fetches a list of past appointments for the user.
    - Calls the callback with the list of appointments or an error message.
    ```java
    backendManager.getUserHistory("userId123", new UserHistoryCallback() {
        @Override
        public void onCallback(List<Appointment> appointmentList) {
            // Handle the list of appointments
        }

        @Override
        public void onError(String error) {
            // Handle the error
        }
    });
    ```

6. **getAllUserPrescriptions(String userId, final PrescriptionCallback callback)**
    - Retrieves all prescriptions linked to the user.
    - Calls the callback with the list of prescriptions or an error message.
    ```java
    backendManager.getAllUserPrescriptions("userId123", new PrescriptionCallback() {
        @Override
        public void onCallback(List<Prescription> prescriptionList) {
            // Handle the list of prescriptions
        }

        @Override
        public void onError(String error) {
            // Handle the error
        }
    });
    ```
7. **requestAppointment(Context context, String userId, String doctorId, String appointmentDate, String userName, String userGender, String userDOB, final AppointmentCallback callback)**
    - Allows users to request an appointment with a doctor.
    - Sends a push notification to the doctor.
    - Calls the callback with the success status and message.
    ```java
    backendManager.requestAppointment(context, "userId123", "doctorId123", "2023-10-10", "John Doe", "Male", "1990-01-01", new AppointmentCallback() {
        @Override
        public void onCallback(boolean success, String message) {
            // Handle the appointment request result
        }

        @Override
        public void onError(String error) {
            // Handle the error
        }
    });
    ```

8. **getAppointmentStatus(String appointmentId, final AppointmentStatusCallback callback)**
    - Checks the current status of a user's appointment.
    - Calls the callback with the status or an error message.
    ```java
    backendManager.getAppointmentStatus("appointmentId123", new AppointmentStatusCallback() {
        @Override
        public void onCallback(String status) {
            // Handle the appointment status
        }

        @Override
        public void onError(String error) {
            // Handle the error
        }
    });
    ```

9. **requestMedicineOrder(Context context, String userId, String pharmacyId, String prescriptionId, final MedicineOrderCallback callback)**
    - Requests medicine from a pharmacy based on a prescription.
    - Sends a push notification to the pharmacy.
    - Calls the callback with the success status and message.
    ```java
    backendManager.requestMedicineOrder(context, "userId123", "pharmacyId123", "prescriptionId123", new MedicineOrderCallback() {
        @Override
        public void onCallback(boolean success, String message) {
            // Handle the medicine order request result
        }

        @Override
        public void onError(String error) {
            // Handle the error
        }
    });
    ```

     ## NOTE getallpharmacy() method to write

10. **getMedicineOrderStatus(String requestId, final MedicineOrderStatusCallback callback)**
     - Tracks the status of a requested medicine order.
     - Calls the callback with the status or an error message.
    ```java
    backendManager.getMedicineOrderStatus("requestId123", new MedicineOrderStatusCallback() {
        @Override
        public void onCallback(String status) {
            // Handle the medicine order status
        }

        @Override
        public void onError(String error) {
            // Handle the error
        }
    });
    ```

### Doctor Methods

1. **addDoctorDetail(Doctor doctor)**
    - Adds Doctor details to the database.
    - Fetches FCM Token before saving.
    - Stores the data in Firebase.
    ```java
    User newDoctor = new Doctor(name, specialty, experience, ratings, phone, address);
    addDoctorDetail(newDoctor);
    ```

1. **getAllDoctorAppointments(String doctorId)**
    - Retrieves a list of all pending and completed appointments for the doctor.
    - Returns the list of appointments.
    ```java
    List<Appointment> appointments = backendManager.getAllDoctorAppointments("doctorId123");
    ```

2. **getAppointmentDetails(String appointmentId, final AppointmentDetailsCallback callback)**
    - Retrieves detailed information about a specific appointment.
    - Calls the callback with the appointment details or an error message.
    ```java
    backendManager.getAppointmentDetails("appointmentId123", new AppointmentDetailsCallback() {
        @Override
        public void onCallback(Appointment appointment) {
            // Handle the appointment details
        }

        @Override
        public void onError(String error) {
            // Handle the error
        }
    });
    ```

3. **updateAppointmentStatus(String doctorId, String appointmentId, String newStatus, final UpdateStatusCallback callback)**
    - Allows doctors to accept, complete, or cancel an appointment.
    - Calls the callback with the success status and message.
    ```java
    backendManager.updateAppointmentStatus("doctorId123", "appointmentId123", "Completed", new UpdateStatusCallback() {
        @Override
        public void onCallback(boolean success, String message) {
            // Handle the status update result
        }

        @Override
        public void onError(String error) {
            // Handle the error
        }
    });
    ```

4. **getPatientHistory(String userId, final PatientHistoryCallback callback)**
    - Views the medical history of a patient before accepting an appointment.
    - Calls the callback with the patient's history or an error message.
    ```java
    backendManager.getPatientHistory("userId123", new PatientHistoryCallback() {
        @Override
        public void onCallback(List<Appointment> history) {
            // Handle the patient history
        }

        @Override
        public void onError(String error) {
            // Handle the error
        }
    });
    ```

5. **sendPrescription(String appointmentId, String doctorId, String userId, List<Medicine> medicines, String instructions, final PrescriptionCallback callback)**
    - Allows doctors to upload prescription object to patient's appointment.
    - Calls the callback with the list of prescriptions or an error message.
    ```java
    List<Medicine> medicines = new ArrayList<>();
    medicines.add(new Medicine("MedicineName", "Dosage", "Frequency"));
    backendManager.sendPrescription("appointmentId123", "doctorId123", "userId123", medicines, "Take after meals", new PrescriptionCallback() {
        @Override
        public void onCallback(List<Prescription> prescriptions) {
            // Handle the prescription result
        }

        @Override
        public void onError(String error) {
            // Handle the error
        }
    });
    ```

### Pharmacy Methods

1. **addPharmacyDetail(Doctor doctor)**
    - Adds Pharmacy details to the database.
    - Fetches FCM Token before saving.
    - Stores the data in Firebase.

```java
Pharmacy newPharmacy = new Pharmacy(pharmacyName, pharmacyPhone, pharmacyAddress);
    addPharmacyDetail(newPharmacy);
```

1. **getAllMedicineOrders(String pharmacyId, final PharmacyOrdersCallback callback)**
    - Retrieves all pending and completed pharmacy requests.
    - Calls the callback with the list of orders or an error message.
    ```java
    backendManager.getAllMedicineOrders("pharmacyId123", new PharmacyOrdersCallback() {
        @Override
        public void onCallback(List<MedicineOrder> orders) {
            // Handle the list of orders
        }

        @Override
        public void onError(String error) {
            // Handle the error
        }
    });
    ```

2. **getMedicineOrderDetails(String requestId, final MedicineOrderDetailsCallback callback)**
    - Retrieves detailed information about a specific medicine request.
    - Calls the callback with the order details or an error message.
    ```java
    backendManager.getMedicineOrderDetails("requestId123", new MedicineOrderDetailsCallback() {
        @Override
        public void onCallback(MedicineOrder order) {
            // Handle the order details
        }

        @Override
        public void onError(String error) {
            // Handle the error
        }
    });
    ```

3. **updateMedicineOrderStatus(String requestId, String newStatus, final OrderStatusUpdateCallback callback)**
    - Allows pharmacies to update order status (Processing, Delivered, Cancelled).
    - Calls the callback with the success status and message.
    ```java
    backendManager.updateMedicineOrderStatus("requestId123", "Delivered", new OrderStatusUpdateCallback() {
        @Override
        public void onCallback(boolean success, String message) {
            // Handle the status update result
        }

        @Override
        public void onError(String error) {
            // Handle the error
        }
    });
    ```

### General Methods

1. **sendPushNotification(Context context, String recipientId, String title, String message, String senderName, String senderGender, String senderDOB)**
    - Sends real-time notifications to users, doctors, and pharmacies using FCM tokens.
    - Stores failed notifications for retry.
    ```java
    backendManager.sendPushNotification(context, "recipientId123", "Notification Title", "Notification Message", "John Doe", "Male", "1990-01-01");
    ```

2. **storeFailedNotifications(String recipientId, String title, String body, String senderName, String senderGender, String senderDOB)**
    - Stores failed push notifications in the "pending_notifications" node for retrying.
    ```java
    backendManager.storeFailedNotifications("recipientId123", "Notification Title", "Notification Body", "John Doe", "Male", "1990-01-01");
    ```

3. **retryFailedNotifications(Context context)**
    - Periodically checks and resends failed notifications.
    ```java
    backendManager.retryFailedNotifications(context);
    ```

4. **isUserRegistered(FirebaseCallback callback)**
    - Checks if the current authenticated user is registered.
    - Calls the callback with the registration status.
    ```java
    backendManager.isUserRegistered(new FirebaseCallback() {
        @Override
        public void onCallback(boolean isRegistered) {
            // Handle the registration status
        }

        @Override
        public void onError(String error) {
            // Handle the error
        }
    });
    ```

## Database Structure

The database structure for the MDCare application is as follows:

```
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
            |--fcmToken (String)  
    |--doctors
        |--doctorId
            |--name (String)
            |--specialization (String)
            |--experience (Integer)
            |--ratings (Double)
            |--phone (String)
            |--address (String)
            |--registered (Boolean)
            |--fcmToken (String)  
            |--upiID(String)
    |--pharmacies
        |--pharmacyId
            |--name (String)
            |--phone (String)
            |--address (String)
            |--registered (Boolean)
            |--fcmToken (String)  
    |--appointments
        |--appointmentId
            |--userId (String)
            |--doctorId (String)
            |--status (String)  # "Pending", "Accepted", "Completed", "Cancelled"
            |--date (String)
            // paymentStatus
            // fees
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
            // paymentStatus
            // amount
    |--pending_notifications  # Buffer for failed notifications
        |--userId  # Reciver ID
            |--notifId 
                |--title (String)
                |--body (String)
                |--timestamp (String)
                |--name (String)  
                |--gender (String)  
                |--dob (String)  

```

## Conclusion
The `BackendManager` class provides a comprehensive set of methods to manage various backend operations for the MDCare application. Each method is designed to handle specific tasks related to user management, doctor management, appointment scheduling, and more, ensuring a seamless and efficient backend workflow.



Mange payments by user

status update push notification

update Appointment 

