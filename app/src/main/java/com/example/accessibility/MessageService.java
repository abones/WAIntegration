package com.example.accessibility;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import java.util.Date;

import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;

/**
 *
 */
public class MessageService extends Service {
    public static final String SETTINGS_CONNECTION = "com.example.accessibility.CONNECTION";

    private final IBinder messageServiceBinder = new Binder();
    private final SharedPreferences.OnSharedPreferenceChangeListener onConnectionChangedListener = this::onConnectionChanged;
    private NotificationManagerCompat notificationManager;

    @Override
    public void onCreate() {
        super.onCreate();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        preferences.registerOnSharedPreferenceChangeListener(onConnectionChangedListener);
        updateConnection(preferences);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext())
            .setSmallIcon(R.drawable.whaticon)
            .setOngoing(true)
            .setContentText("Service running")
            .setWhen(new Date().getTime())
            .setContentTitle("Running service")
            .setSubText("Srunning ervice")
            .setContentIntent(pendingIntent);

        notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(0, builder.build());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(onConnectionChangedListener);
        notificationManager.cancel(0);
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
