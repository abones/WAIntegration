package com.whatsapp.integration.service;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.whatsapp.integration.WhatsappIntegrationApplication;
import com.whatsapp.integration.dagger.components.ServiceComponent;
import com.whatsapp.integration.model.QueuedMessage;
import com.whatsapp.integration.viewmodels.IMessageServiceViewModel;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.inject.Inject;

/**
 *
 */
public class MessageService extends Service {
    public static final String SETTINGS_CONNECTION = "com.whatsapp.integration.CONNECTION";
    public static final String SETTINGS_ENABLED = "com.whatsapp.integration.ENABLED";

    private final IBinder messageServiceBinder = new Binder();

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

        whatsappInterfaceConnection = new WhatsappInterfaceConnection();
        bindService(new Intent(this, MyAccessibilityService.class), whatsappInterfaceConnection, 0);
    }

    @Override
    public void onDestroy() {
        viewModel.onDestroy();
        super.onDestroy();
        unbindService(whatsappInterfaceConnection);
    }

    // endregion Overrides of Service

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
