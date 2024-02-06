package com.example.receiver;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

public class MyReceiver extends BroadcastReceiver {
    static final String CHANNEL_ID = "ReceiverMessageChannel";

    @Override
    public void onReceive(Context context, Intent intent) {
        final String actionSendMsg = context.getString(R.string.action_send_msg);
        if (intent.getAction().equals(actionSendMsg)) {
            String msg = intent.getStringExtra("msg");
            // channel like a category of notification
            // example: Shopee may have many channels like: Order, Promotion, ...
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Receiver message", importance);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID).setSmallIcon(R.drawable.ic_launcher_foreground).setContentTitle("Message received").setContentText(msg).setPriority(NotificationCompat.PRIORITY_DEFAULT);
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
            notificationManager.notify(0, builder.build());
        }
    }
}