package com.whatsapp.integration;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.rubius.androidshared.abstraction.IContextWrapper;
import com.rubius.androidshared.dagger.qualifiers.ApplicationContext;

import javax.inject.Inject;

/**
 *
 */

public class Settings
        implements ISettings {

    private final IContextWrapper applicationContext;

    @Inject
    public Settings(@ApplicationContext @NonNull IContextWrapper applicationContext) {
        this.applicationContext = applicationContext;
    }

    private SharedPreferences getSettings() {
        return applicationContext.getDefaultSharedPreferences();
    }

    @Override
    public boolean isMessageServiceEnabled() {
        return getSettings().getBoolean(MessageService.SETTINGS_ENABLED, false);
    }
}
