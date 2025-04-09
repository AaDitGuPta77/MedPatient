package com.example.medpatient.backend;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import com.example.medpatient.R;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.medpatient.HomeScreen;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class MessageService extends FirebaseMessagingService {
    private NotificationManager notificationManager;
    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);
        Map<String,String> data = message.getData();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "Notification"); // Notification is a channel ID you can give name as you wish

        Intent resultIntent = new Intent(this, HomeScreen.class); // when user clicks on notification give activity that you want user to get redirected to
        PendingIntent pendingIntent;
        pendingIntent = PendingIntent.getActivity(this, 1, resultIntent, PendingIntent.FLAG_IMMUTABLE);
        builder.setContentTitle(message.getNotification().getTitle());
        builder.setContentText(message.getNotification().getBody());
        builder.setSmallIcon(R.drawable.ic_launcher_foreground);
        builder.setStyle(new NotificationCompat.BigTextStyle().bigText(message.getNotification().getBody()));
        builder.setAutoCancel(true);
        builder.setContentIntent(pendingIntent);
        builder.setPriority(0);

        Log.d("MessageService", "Notification Title: " + message.getNotification().getTitle());
        Log.d("MessageService", "Notification Body: " + message.getNotification().getBody());
        Log.d("MessageService", "Data: " + data);
        notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { // check if android version is above O
            String channelID = "Firebase";
            NotificationChannel channel = new NotificationChannel(
                    channelID, "Coding", NotificationManager.IMPORTANCE_HIGH
            );
            channel.enableLights(true);
            channel.enableVibration(true);
            channel.canBypassDnd();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                channel.canBubble();
            }

            notificationManager.createNotificationChannel(channel);
            builder.setChannelId(channelID);
        }

        notificationManager.notify(100, builder.build());

    }
}
