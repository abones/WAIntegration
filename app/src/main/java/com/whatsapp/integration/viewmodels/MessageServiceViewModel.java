package com.whatsapp.integration.viewmodels;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.rubius.androidshared.abstraction.IServiceContextWrapper;
import com.rubius.androidshared.dagger.qualifiers.ServiceContext;
import com.rubius.androidshared.viewmodels.IView;
import com.rubius.androidshared.viewmodels.ViewModelBase;
import com.whatsapp.integration.R;
import com.whatsapp.integration.activities.MainActivity;
import com.whatsapp.integration.misc.IPreferences;
import com.whatsapp.integration.model.IRetrofitWrapper;
import com.whatsapp.integration.model.MessageInfo;
import com.whatsapp.integration.model.QueuedMessage;
import com.whatsapp.integration.model.WhatMessage;
import com.whatsapp.integration.service.MyAccessibilityService;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;

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
    private final IBinder messageServiceBinder = new Binder();
    private final IRetrofitWrapper retrofitWrapper;
    private final ScheduledExecutorService scheduler;
    private ScheduledFuture<?> pollingTask;

    private BroadcastReceiver messagesReceiver;
    private WhatsappInterfaceConnection whatsappInterfaceConnection;

    private final Queue<QueuedMessage> messages = new ConcurrentLinkedQueue<>();

    @Inject
    public MessageServiceViewModel(
        @ServiceContext @NonNull IServiceContextWrapper contextWrapper,
        IPreferences preferences,
        IRetrofitWrapper retrofitWrapper
    ) {
        super(contextWrapper);
        this.serviceContextWrapper = contextWrapper;
        this.preferences = preferences;
        this.retrofitWrapper = retrofitWrapper;

        scheduler = Executors.newSingleThreadScheduledExecutor();
    }

    // region Connection

    private void setConnection(String connection) {
        retrofitWrapper.setConnection(connection);
    }

    private void onConnectionChanged(SharedPreferences sharedPreferences, String settings) {
        if (SETTINGS_CONNECTION.equals(settings))
            setConnection(preferences.getString(SETTINGS_CONNECTION, null));
    }

    // endregion Connection

    // region isServiceConnected

    private boolean isServiceConnected;

    private void setIsServiceConnected(boolean isServiceConnected) {
        if (this.isServiceConnected == isServiceConnected)
            return;

        this.isServiceConnected = isServiceConnected;

        if (isServiceConnected)
            sendPendingMessages();
    }

    // endregion isServiceConnected

    private Notification createNotification(String title, String subTitle) {
        PendingIntent pendingIntent = contextWrapper.createPendingIntent(
            MainActivity.class,
            FLAG_UPDATE_CURRENT
        );

        return contextWrapper.getNotificationBuilder("MAIN_CHANNEL")
            .setSmallIcon(R.drawable.whaticon_holes)
            .setContentText(subTitle)
            .setWhen(new Date().getTime())
            .setContentTitle(title)
            .setContentIntent(pendingIntent)
            .build();
    }

    private synchronized void sendPendingMessages() {
        while (!messages.isEmpty()) {
            QueuedMessage message = messages.remove();

            // TODO: ?????????????? ?????????????????? ?? contextwrapper
            Intent broadcastIntent = new Intent(MyAccessibilityService.ACTION_SEND_MESSAGE);
            broadcastIntent.putExtra(MyAccessibilityService.EXTRA_MESSAGE, message);
            contextWrapper.sendBroadcast(broadcastIntent);
        }
    }

    // region Overrides of ViewModelBase

    @Override
    protected String getPrintPrefix() {
        return "MessageServiceViewModel";
    }

    @Override
    public void onCreate(
        @Nullable Bundle savedInstanceState, @Nullable Intent intent
    ) {
        super.onCreate(savedInstanceState, intent);
        preferences.registerListener(onConnectionChangedListener);
        setConnection(preferences.getString(SETTINGS_CONNECTION, null));

        serviceContextWrapper.startForeground(
            NOTIFICATION_ID,
            createNotification(contextWrapper.getString(R.string.service_started), "")
        );

        messagesReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                ArrayList<MessageInfo> messages = intent.getParcelableArrayListExtra(
                    MyAccessibilityService.EXTRA_MESSAGES);

                updateNotification(
                    contextWrapper.getString(R.string.service_messages_received),
                    String.format(
                        contextWrapper.getString(R.string.service_messages_received_prefab),
                        messages.size()
                    )
                );
            }
        };
        IntentFilter intentFilter = new IntentFilter(MyAccessibilityService.ACTION_RECEIVE_MESSAGES);
        contextWrapper.registerReceiver(messagesReceiver, intentFilter);

        whatsappInterfaceConnection = new WhatsappInterfaceConnection();
        contextWrapper.bindService(MyAccessibilityService.class, whatsappInterfaceConnection, 0);

        pollingTask = scheduler.scheduleAtFixedRate(this::getMessages, 10L, 10L, TimeUnit.SECONDS);
    }

    private void updateNotification(String title, String subTitle) {
        contextWrapper.notify(
            NOTIFICATION_ID,
            createNotification(title, subTitle)
        );
    }

    @Override
    public void onDestroy() {
        pollingTask.cancel(true);
        scheduler.shutdownNow();
        preferences.unregisterListener(onConnectionChangedListener);
        contextWrapper.unregisterReceiver(messagesReceiver);
        serviceContextWrapper.stopForeground(true);
        contextWrapper.unbindService(whatsappInterfaceConnection);
    }

    @Override
    public IBinder onBind() {
        return messageServiceBinder;
    }

    // endregion Overrides of ViewModelBase

    private List<WhatMessage> receivedMessages = new ArrayList<>();
    private final List<WeakReference<IMessagesChanged>> messageChangedHandlers = new ArrayList<>();

    private void addMessageChangedHandler(IMessagesChanged messagesChanged) {
        messageChangedHandlers.add(new WeakReference<>(messagesChanged));
    }

    private void removeMessageChangedHandler(IMessagesChanged messagesChanged) {
        WeakReference<IMessagesChanged> foundReference = null;
        for (WeakReference<IMessagesChanged> handlerReference : messageChangedHandlers)
            if (handlerReference.get() == messagesChanged) {
                foundReference = handlerReference;
                break;
            }

        if (foundReference != null)
            messageChangedHandlers.remove(foundReference);
    }

    private void getMessages() {
        Call<List<WhatMessage>> call = retrofitWrapper.getWhatMessageService().getMessages(2);

        call.enqueue(new Callback<List<WhatMessage>>() {

            @Override
            public void onResponse(
                Call<List<WhatMessage>> call, Response<List<WhatMessage>> response
            ) {
                receivedMessages = response.body();

                updateNotification(
                    contextWrapper.getString(R.string.service_messages_received),
                    String.format(
                        contextWrapper.getString(R.string.service_messages_received_prefab),
                        receivedMessages.size()
                    )
                );

                for (WeakReference<IMessagesChanged> messageChanged : messageChangedHandlers) {
                    IMessagesChanged actualHandler = messageChanged.get();
                    if (actualHandler != null)
                        actualHandler.onMessagesChanged(receivedMessages);
                }
            }

            @Override
            public void onFailure(Call<List<WhatMessage>> call, Throwable t) {
                String errorMessage = String.format(
                    contextWrapper.getString(R.string.error_retrieving_messages_prefab),
                    t.toString()
                );
                contextWrapper.showShortToast(errorMessage);
                updateNotification(
                    contextWrapper.getString(R.string.error_retrieving_messages),
                    errorMessage
                );
            }
        });
    }

    // region Internal classes

    private class WhatsappInterfaceConnection implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            setIsServiceConnected(true);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            setIsServiceConnected(true);
        }
    }

    public class Binder extends android.os.Binder {
        private IMessagesChanged onMessagesChanged;

        public void sendMessage(QueuedMessage message) {
            messages.add(message);
            if (isServiceConnected)
                sendPendingMessages();
        }

        public void getMessages() {
            MessageServiceViewModel.this.getMessages();
        }

        public void subscribeToMessagesChanged(IMessagesChanged onMessagesChanged) {
            this.onMessagesChanged = onMessagesChanged;
            addMessageChangedHandler(this.onMessagesChanged);
            onMessagesChanged.onMessagesChanged(receivedMessages);
        }

        public void unsubscribeFromMessagesChanged() {
            removeMessageChangedHandler(this.onMessagesChanged);
            this.onMessagesChanged = null;
        }
    }

    public interface IMessagesChanged {
        void onMessagesChanged(List<WhatMessage> messages);
    }

    // endregion Internal classes
}
