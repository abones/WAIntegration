package com.whatsapp.integration.service;

import android.support.annotation.NonNull;

import com.rubius.androidshared.abstraction.IContextWrapper;
import com.rubius.androidshared.dagger.qualifiers.ApplicationContext;
import com.whatsapp.integration.ISettings;
import com.whatsapp.integration.MessageService;

import javax.inject.Inject;

/**
 *
 */

public class MessageServiceManager
        implements IMessageServiceManager {

    private final IContextWrapper applicationContext;
    private final ISettings settings;

    @Inject
    public MessageServiceManager(@ApplicationContext @NonNull IContextWrapper applicationContext, @NonNull ISettings settings) {
        this.applicationContext = applicationContext;
        this.settings = settings;
    }

    @Override
    public void startServiceIfEnabled() {
        if (!settings.isMessageServiceEnabled())
            return;

        applicationContext.startService(MessageService.class);
    }
}
