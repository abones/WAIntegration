package com.whatsapp.integration.viewmodels;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.rubius.androidshared.abstraction.IServiceContextWrapper;
import com.rubius.androidshared.dagger.qualifiers.ServiceContext;
import com.rubius.androidshared.viewmodels.IView;
import com.rubius.androidshared.viewmodels.ViewModelBase;
import com.whatsapp.integration.R;
import com.whatsapp.integration.activities.MainActivity;
import com.whatsapp.integration.misc.IPreferences;
import com.whatsapp.integration.model.MessageInfo;
import com.whatsapp.integration.service.MessageService;
import com.whatsapp.integration.service.MyAccessibilityService;

import java.util.ArrayList;
import java.util.Date;

import javax.inject.Inject;

import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;
import static com.whatsapp.integration.service.MessageService.SETTINGS_CONNECTION;

/**
 *
 */

public class MessageServiceViewModel
        extends ViewModelBase<IView>
        implements IMessageServiceViewModel {
    private static final int NOTIFICATION_ID = 1;

    private final IServiceContextWrapper serviceContextWrapper;
    private final IPreferences preferences;
    private final SharedPreferences.OnSharedPreferenceChangeListener onConnectionChangedListener = this::onConnectionChanged;
    private BroadcastReceiver messagesReceiver;

    @Inject
    public MessageServiceViewModel(
            @ServiceContext @NonNull IServiceContextWrapper contextWrapper,
            IPreferences preferences) {
        super(contextWrapper);
        this.serviceContextWrapper = contextWrapper;
        this.preferences = preferences;
    }

    // region Connection

    private String connection;

    private void setConnection(String connection) {
        this.connection = connection;
    }

    private void onConnectionChanged(SharedPreferences sharedPreferences, String settings) {
        if (SETTINGS_CONNECTION.equals(settings))
            setConnection(preferences.getString(SETTINGS_CONNECTION, null));
    }

    // endregion Connection

    private Notification createNotification(String title) {
        PendingIntent pendingIntent = contextWrapper.createPendingIntent(
                MainActivity.class,
                FLAG_UPDATE_CURRENT
        );

        return contextWrapper.getNotificationBuilder()
                             .setSmallIcon(R.drawable.whaticon_holes)
                             .setContentText("Service running")
                             .setWhen(new Date().getTime())
                             .setContentTitle(title)
                             .setSubText("Srunning ervice")
                             .setContentIntent(pendingIntent)
                             .build();
    }

    @Override
    protected String getPrintPrefix() {
        return "MessageServiceViewModel";
    }

    @Override
    public void onCreate(
            @Nullable Bundle savedInstanceState, @Nullable Intent intent) {
        super.onCreate(savedInstanceState, intent);
        preferences.registerListener(onConnectionChangedListener);
        setConnection(preferences.getString(MessageService.SETTINGS_CONNECTION, null));

        serviceContextWrapper.startForeground(
                NOTIFICATION_ID,
                createNotification("Service running")
        );

        messagesReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                ArrayList<MessageInfo> messages = intent.getParcelableArrayListExtra(
                        MyAccessibilityService.EXTRA_MESSAGES);

                contextWrapper.notify(
                        NOTIFICATION_ID,
                        createNotification("Received messages: " + messages.size())
                );
            }
        };
        IntentFilter intentFilter = new IntentFilter(MyAccessibilityService.ACTION_RECEIVE_MESSAGES);
        contextWrapper.registerReceiver(messagesReceiver, intentFilter);
    }

    @Override
    public void onDestroy() {
        preferences.unregisterListener(onConnectionChangedListener);
        contextWrapper.unregisterReceiver(messagesReceiver);
        serviceContextWrapper.stopForeground(true);
    }
}
