package com.example.accessibility;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 *
 */
public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (!Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction()))
            return;

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        boolean isEnabled = preferences.getBoolean(MessageService.SETTINGS_ENABLED, false);

        if (isEnabled) {
            Intent serviceIntent = new Intent(context, MessageService.class);
            context.startService(serviceIntent);
        }
    }
}
