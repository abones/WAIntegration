package com.example.accessibility;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 *
 */
public class MessageService extends Service {
    private final IBinder messageServiceBinder = new Binder();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return messageServiceBinder;
    }

    class Binder extends android.os.Binder {
        public void sendMessage(String message) {
            Log.d("FROMSERVICE", message);
        }
    }
}
