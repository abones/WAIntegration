package com.whatsapp.integration.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.whatsapp.integration.R;
import com.whatsapp.integration.WhatsappIntegrationApplication;
import com.whatsapp.integration.activities.MainActivity;
import com.whatsapp.integration.dagger.components.ServiceComponent;
import com.whatsapp.integration.model.MessageInfo;
import com.whatsapp.integration.model.QueuedMessage;
import com.whatsapp.integration.viewmodels.IMessageServiceViewModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.inject.Inject;

import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;

/**
 *
 */
public class MessageService extends Service {
    public static final String SETTINGS_CONNECTION = "com.whatsapp.integration.CONNECTION";
    public static final String SETTINGS_ENABLED = "com.whatsapp.integration.ENABLED";
    private static final int NOTIFICATION_ID = 1;

    private final IBinder messageServiceBinder = new Binder();
    private final SharedPreferences.OnSharedPreferenceChangeListener onConnectionChangedListener = this::onConnectionChanged;
    private BroadcastReceiver messagesReceiver;
    private WhatsappInterfaceConnection whatsappInterfaceConnection;

    @Inject
    protected IMessageServiceViewModel viewModel;

    private class WhatsappInterfaceConnection implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            setServiceConnected(true);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            setServiceConnected(true);
        }
    }

    // region isConnected

    private boolean isConnected;

    private void setServiceConnected(boolean isConnected) {
        if (this.isConnected == isConnected)
            return;

        this.isConnected = isConnected;

        if (isConnected)
            sendPendingMessages();
    }

    // endregion isConnected

    // region Overrides of Service

    @Override
    public void onCreate() {
        super.onCreate();
        WhatsappIntegrationApplication application = (WhatsappIntegrationApplication) getApplicationContext();
        ServiceComponent serviceComponent = application.getServiceComponent(this);
        serviceComponent.inject(this);

        viewModel.onCreate(null, null);

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
                mNotificationManager.notify(NOTIFICATION_ID, createNotification("Received messages: " + messages.size()));
            }
        };
        IntentFilter intentFilter = new IntentFilter(MyAccessibilityService.ACTION_RECEIVE_MESSAGES);
        registerReceiver(messagesReceiver, intentFilter);

        whatsappInterfaceConnection = new WhatsappInterfaceConnection();
        bindService(new Intent(this, MyAccessibilityService.class), whatsappInterfaceConnection, 0);
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
        viewModel.onDestroy();

        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(onConnectionChangedListener);
        unregisterReceiver(messagesReceiver);
        unbindService(whatsappInterfaceConnection);
        stopForeground(true);
    }

    // endregion Overrides of Service

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

    private final Queue<QueuedMessage> messages = new ConcurrentLinkedQueue<>();

    private synchronized void sendPendingMessages() {
        while (!messages.isEmpty()) {
            QueuedMessage message = messages.remove();

            Intent broadcastIntent = new Intent(MyAccessibilityService.ACTION_SEND_MESSAGE);
            broadcastIntent.putExtra(MyAccessibilityService.EXTRA_MESSAGE, message);
            sendBroadcast(broadcastIntent);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return messageServiceBinder;
    }

    public class Binder extends android.os.Binder {
        public void sendMessage(QueuedMessage message) {
            messages.add(message);
            if (isConnected)
                sendPendingMessages();
        }
    }
}
