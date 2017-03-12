package com.whatsapp.integration.misc;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.rubius.androidshared.abstraction.IContextWrapper;
import com.rubius.androidshared.dagger.qualifiers.ApplicationContext;
import com.whatsapp.integration.service.MessageService;

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

    @Override
    public void registerListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
        getSettings().registerOnSharedPreferenceChangeListener(listener);
    }

    @Override
    public void unregisterListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
        getSettings().unregisterOnSharedPreferenceChangeListener(listener);
    }

    @Override
    public String getString(String key, String defValue) {
        return getSettings().getString(key, defValue);
    }
}
