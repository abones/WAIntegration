package com.whatsapp.integration.activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

import com.whatsapp.integration.MessageService;
import com.whatsapp.integration.QueuedMessage;
import com.whatsapp.integration.R;
import com.whatsapp.integration.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private final MyServiceConnection serviceConnection;
    private Intent serviceIntent;

    public MainActivity() {
        serviceConnection = new MyServiceConnection();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setActivity(this);
        serviceIntent = new Intent(this, MessageService.class);
        startService(serviceIntent);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        preferences.edit().putBoolean(MessageService.SETTINGS_ENABLED, true).apply();
    }

    @Override
    public void onStart() {
        super.onStart();
        bindService(
            serviceIntent,
            serviceConnection,
            Context.BIND_ABOVE_CLIENT | Context.BIND_IMPORTANT
        );
    }

    @Override
    public void onStop() {
        unbindService(serviceConnection);
        super.onStop();
    }

    public void sendMessage() {
        serviceConnection.getService().sendMessage(new QueuedMessage("Ololo", "+79138539660"));
    }

    public void setConnection() {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
        editor.putString(MessageService.SETTINGS_CONNECTION, "localhost");
        editor.apply();
    }

    private static class MyServiceConnection implements ServiceConnection {
        private MessageService.Binder service;

        public MessageService.Binder getService() {
            return service;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            this.service = (MessageService.Binder) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            this.service = null;
        }
    }
}
