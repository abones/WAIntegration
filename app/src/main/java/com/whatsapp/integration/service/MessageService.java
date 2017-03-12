package com.whatsapp.integration.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.whatsapp.integration.WhatsappIntegrationApplication;
import com.whatsapp.integration.dagger.components.ServiceComponent;
import com.whatsapp.integration.viewmodels.IMessageServiceViewModel;

import javax.inject.Inject;

/**
 *
 */
public class MessageService extends Service {
    @Inject
    protected IMessageServiceViewModel viewModel;

    // region Overrides of Service

    @Override
    public void onCreate() {
        super.onCreate();
        WhatsappIntegrationApplication application = (WhatsappIntegrationApplication) getApplicationContext();
        ServiceComponent serviceComponent = application.getServiceComponent(this);
        serviceComponent.inject(this);

        viewModel.onCreate(null, null);
    }

    @Override
    public void onDestroy() {
        viewModel.onDestroy();
        super.onDestroy();
    }

    // endregion Overrides of Service

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return viewModel.onBind();
    }
}
