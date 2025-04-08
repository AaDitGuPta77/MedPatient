package com.example.medpatient.backend;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.medpatient.backend.interfaces.FcmCallback;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FcmNotificationSender {

    private final String userFcmToken;
    private final String title;
    private final String body;

    private final Context context;

    private final String postUrl = "https://fcm.googleapis.com/v1/projects/mediquick-f9cad/messages:send";

    public FcmNotificationSender(Context context, String userFcmToken, String title, String body) {
        this.context = context;
        this.userFcmToken = userFcmToken;
        this.title = title;
        this.body =  body;
    }
//    public void SendNotification(String userName, String userGender, String userDob, FcmCallback callback) {
//        RequestQueue requestQueue = Volley.newRequestQueue(context);
//        JSONObject mainObj = new JSONObject();
//        try {
//            JSONObject messageObject = new JSONObject();
//            JSONObject notificationObject = new JSONObject();
//            JSONObject dataObject = new JSONObject();
//
//            // Notification title and body
//            notificationObject.put("title", title);
//            notificationObject.put("body", body);
//
//            // Additional user details
//            dataObject.put("userName", userName);
//            dataObject.put("userGender", userGender);
//            dataObject.put("userDob", userDob);
//
//            messageObject.put("token", userFcmToken);
//            messageObject.put("notification", notificationObject);
//            messageObject.put("data", dataObject); // Sending additional user details in 'data'
//
//            mainObj.put("message", messageObject);
//
//            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, postUrl, mainObj,
//                    response -> {
//                        Log.d("FCM", "Notification sent successfully: " + response.toString());
//                        callback.onSuccess();
//                    },
//                    error -> {
//                        Log.e("FCM", "Notification failed: ", error);
//                        callback.onFailure(error.toString());
//                    }) {
//                @NotNull
//                @Override
//                public Map<String, String> getHeaders() {
//                    AccessToken accessToken = new AccessToken();
//                    String token = accessToken.getAccessToken();
//                    Map<String, String> header = new HashMap<>();
//                    header.put("content-type", "application/json");
//                    header.put("authorization", "Bearer " + token);
//                    return header;
//                }
//            };
//
//            requestQueue.add(request);
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//            callback.onFailure(e.getMessage());
//        }
//    }


    public void sendGeneralNotification(String recipientFcmToken, String title, String message, FcmCallback callback) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JSONObject mainObj = new JSONObject();
        try {
            JSONObject messageObject = new JSONObject();
            JSONObject notificationObject = new JSONObject();

            // Notification title and body
            notificationObject.put("title", title);
            notificationObject.put("body", message);

            messageObject.put("token", recipientFcmToken);
            messageObject.put("notification", notificationObject); // Only title & message for general updates

            mainObj.put("message", messageObject);

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, postUrl, mainObj,
                    response -> {
                        Log.d("FCM", "General notification sent successfully: " + response.toString());
                        callback.onSuccess();
                    },
                    error -> {
                        Log.e("FCM", "General notification failed: ", error);
                        callback.onFailure(error.toString());
                    }) {
                @NotNull
                @Override
                public Map<String, String> getHeaders() {
                    AccessToken accessToken = new AccessToken();
                    String token = accessToken.getAccessToken();
                    Map<String, String> header = new HashMap<>();
                    header.put("content-type", "application/json");
                    header.put("authorization", "Bearer " + token);
                    return header;
                }
            };

            requestQueue.add(request);

        } catch (JSONException e) {
            e.printStackTrace();
            callback.onFailure(e.getMessage());
        }
    }

    public void sendAppointmentNotification(String recipientFcmToken, String title, String message,
                                            String userName, String userGender, String userDob,
                                            String appointmentDate, String appointmentTime,
                                            FcmCallback callback) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JSONObject mainObj = new JSONObject();
        try {
            JSONObject messageObject = new JSONObject();
            JSONObject notificationObject = new JSONObject();
            JSONObject dataObject = new JSONObject();

            // Notification title and body
            notificationObject.put("title", title);
            notificationObject.put("body", message);

            // Additional user details for the doctor
            dataObject.put("userName", userName);
            dataObject.put("userGender", userGender);
            dataObject.put("userDob", userDob);
            dataObject.put("appointmentDate", appointmentDate);  // ✅ Added appointment date
            dataObject.put("appointmentTime", appointmentTime);  // ✅ Added appointment time

            messageObject.put("token", recipientFcmToken);
            messageObject.put("notification", notificationObject);
            messageObject.put("data", dataObject); // Extra details in 'data' field

            mainObj.put("message", messageObject);

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, postUrl, mainObj,
                    response -> {
                        Log.d("FCM", "Appointment notification sent successfully: " + response.toString());
                        callback.onSuccess();
                    },
                    error -> {
                        Log.e("FCM", "Appointment notification failed: ", error);
                        callback.onFailure(error.toString());
                    }) {
                @NotNull
                @Override
                public Map<String, String> getHeaders() {
                    AccessToken accessToken = new AccessToken();
                    String token = accessToken.getAccessToken();
                    Map<String, String> header = new HashMap<>();
                    header.put("content-type", "application/json");
                    header.put("authorization", "Bearer " + token);
                    return header;
                }
            };

            requestQueue.add(request);

        } catch (JSONException e) {
            e.printStackTrace();
            callback.onFailure(e.getMessage());
        }
    }
}

