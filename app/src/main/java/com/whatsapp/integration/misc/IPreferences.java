package com.whatsapp.integration.misc;

import android.content.SharedPreferences;

/**
 *
 */
public interface IPreferences {
    boolean isMessageServiceEnabled();

    void setServiceEnabled(boolean isServiceEnabled);

    void registerListener(SharedPreferences.OnSharedPreferenceChangeListener onConnectionChangedListener);
    void unregisterListener(SharedPreferences.OnSharedPreferenceChangeListener onConnectionChangedListener);

    String getString(String key, String defValue);
}
