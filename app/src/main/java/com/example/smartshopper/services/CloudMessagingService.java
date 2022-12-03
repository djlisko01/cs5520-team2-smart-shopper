package com.example.smartshopper.services;

import android.app.PendingIntent;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.smartshopper.DealDetailsActivity;
import com.example.smartshopper.R;
import com.example.smartshopper.common.PlatformHelpers;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


public class CloudMessagingService extends FirebaseMessagingService {

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        RTDBService rtdbDatabase = new RTDBService();
        rtdbDatabase.writeFCMToken(token);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        // if the notification is for a topic
        if (remoteMessage.getFrom() != null && remoteMessage.getFrom().contains("topic")) {
            if (remoteMessage.getNotification() != null) {
                PlatformHelpers platformHelpers = new PlatformHelpers(this);
                platformHelpers.getDealByKey(remoteMessage.getFrom().substring(8), deal -> {
                    Intent intent = new Intent(this, DealDetailsActivity.class);
                    intent.putExtra("dealItem", deal);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

                    NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "Saved_Deal_Channel")
                            .setSmallIcon(R.drawable.ic_baseline_shopping_basket_24)
                            .setContentTitle(remoteMessage.getNotification().getTitle())
                            .setContentText(remoteMessage.getNotification().getBody())
                            .setPriority(NotificationCompat.PRIORITY_HIGH)
                            .setContentIntent(pIntent)
                            .setAutoCancel(true);
                    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
                    notificationManager.notify(0, builder.build());
                });
            }
        } else {
            super.onMessageReceived(remoteMessage);
        }
    }
}
