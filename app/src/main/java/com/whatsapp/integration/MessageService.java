package com.whatsapp.integration;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;

import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;

/**
 *
 */
public class MessageService extends Service {
    public static final String SETTINGS_CONNECTION = "com.example.accessibility.CONNECTION";
    public static final String SETTINGS_ENABLED = "com.example.accessibility.ENABLED";
    private static final int NOTIFICATION_ID = 1;

    private final IBinder messageServiceBinder = new Binder();
    private final SharedPreferences.OnSharedPreferenceChangeListener onConnectionChangedListener = this::onConnectionChanged;
    private BroadcastReceiver messagesReceiver;

    @Override
    public void onCreate() {
        super.onCreate();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        preferences.registerOnSharedPreferenceChangeListener(onConnectionChangedListener);
        updateConnection(preferences);

        Notification notification = createNotification("Service running");
        startForeground(NOTIFICATION_ID, notification);

        messagesReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                ArrayList<MessageInfo> messages = intent.getParcelableArrayListExtra(MyAccessibilityService.EXTRA_MESSAGES);

                NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                mNotificationManager.notify(NOTIFICATION_ID, createNotification("Reived messages: " + messages.size()));
            }
        };
        IntentFilter intentFilter = new IntentFilter(MyAccessibilityService.ACTION_RECEIVE_MESSAGES);
        registerReceiver(messagesReceiver, intentFilter);
    }

    private Notification createNotification(String title) {
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), FLAG_UPDATE_CURRENT);

        return new NotificationCompat.Builder(this)
            .setSmallIcon(R.drawable.whaticon_holes)
            .setContentText("Service running")
            .setWhen(new Date().getTime())
            .setContentTitle(title)
            .setSubText("Srunning ervice")
            .setContentIntent(pendingIntent)
            .build();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(onConnectionChangedListener);
        unregisterReceiver(messagesReceiver);
        stopForeground(true);
    }

    private void onConnectionChanged(SharedPreferences sharedPreferences, String settings) {
        if (SETTINGS_CONNECTION.equals(settings))
            updateConnection(sharedPreferences);
    }

    private String connection;

    private void setConnection(String connection) {
        this.connection = connection;
        Log.d("FROMSERVICE", "New connection: " + connection);
    }

    private void updateConnection(SharedPreferences preferences) {
        setConnection(preferences.getString(SETTINGS_CONNECTION, null));
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return messageServiceBinder;
    }

    class Binder extends android.os.Binder {
        public void sendMessage(String message) {
            Log.d("FROMSERVICE", message + " to " + connection);
        }
    }
}
