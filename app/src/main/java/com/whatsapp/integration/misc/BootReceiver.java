package com.whatsapp.integration.misc;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.whatsapp.integration.WhatsappIntegrationApplication;
import com.whatsapp.integration.dagger.components.ReceiverComponent;
import com.whatsapp.integration.service.IMessageServiceManager;

import javax.inject.Inject;

/**
 *
 */
public class BootReceiver extends BroadcastReceiver {
    @Inject
    protected IMessageServiceManager serviceManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (!Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction()))
            return;

        WhatsappIntegrationApplication application = (WhatsappIntegrationApplication) context.getApplicationContext();
        ReceiverComponent receiverComponent = application.getReceiverComponent(context);
        receiverComponent.inject(this);

        serviceManager.startServiceIfEnabled();
    }
}
