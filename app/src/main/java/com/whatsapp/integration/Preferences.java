package com.whatsapp.integration;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.rubius.androidshared.abstraction.IContextWrapper;
import com.rubius.androidshared.dagger.qualifiers.ApplicationContext;

import javax.inject.Inject;

/**
 *
 */

public class Preferences
        implements IPreferences {

    private final IContextWrapper applicationContext;

    @Inject
    public Preferences(@ApplicationContext @NonNull IContextWrapper applicationContext) {
        this.applicationContext = applicationContext;
    }

    private SharedPreferences getSettings() {
        return applicationContext.getDefaultSharedPreferences();
    }

    @Override
    public boolean isMessageServiceEnabled() {
        return getSettings().getBoolean(MessageService.SETTINGS_ENABLED, false);
    }

    @Override
    public void setServiceEnabled(boolean isServiceEnabled) {
        getSettings().edit().putBoolean(MessageService.SETTINGS_ENABLED, isServiceEnabled).apply();
    }
}
