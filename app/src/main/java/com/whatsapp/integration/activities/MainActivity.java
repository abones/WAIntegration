package com.whatsapp.integration.activities;

import com.android.databinding.library.baseAdapters.BR;
import com.rubius.androidshared.activities.AppCompatActivityBase;
import com.rubius.androidshared.viewmodels.IActivityView;
import com.rubius.androidshared.viewmodels.IActivityViewModel;
import com.whatsapp.integration.R;
import com.whatsapp.integration.WhatsappIntegrationApplication;
import com.whatsapp.integration.dagger.components.ActivityComponent;
import com.whatsapp.integration.databinding.ActivityMainBinding;
import com.whatsapp.integration.viewmodels.MainActivityViewModel;

public class MainActivity extends AppCompatActivityBase<WhatsappIntegrationApplication, MainActivityViewModel, ActivityMainBinding> implements
    IActivityView {
    @Override
    public String getPrintPrefix() {
        return "MainActivity";
    }

    @Override
    public void releaseViewModel() {
        getTypedApplication().releaseActivityComponent(this.getClass());
    }

    @Override
    public IActivityViewModel getViewModel() {
        ActivityComponent activityComponent = getTypedApplication().getActivityComponent(this);
        activityComponent.inject(this);
        return viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public int getViewModelVariableId() {
        return BR.viewModel;
    }

//    private final MyServiceConnection serviceConnection;
//    private Intent serviceIntent;
//
//    public MainActivity() {
//        serviceConnection = new MyServiceConnection();
//    }

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
//        binding.setActivity(this);
//        serviceIntent = new Intent(this, MessageService.class);
//        startService(serviceIntent);
//
//        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
//        preferences.edit().putBoolean(MessageService.SETTINGS_ENABLED, true).apply();
//    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        bindService(
//            serviceIntent,
//            serviceConnection,
//            Context.BIND_ABOVE_CLIENT | Context.BIND_IMPORTANT
//        );
//    }

//    @Override
//    public void onStop() {
//        unbindService(serviceConnection);
//        super.onStop();
//    }

//    public void sendMessage() {
//        serviceConnection.getService().sendMessage(new QueuedMessage("Ololo", "+79138539660"));
//    }

//    public void setConnection() {
//        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
//        editor.putString(MessageService.SETTINGS_CONNECTION, "localhost");
//        editor.apply();
//    }

//    private static class MyServiceConnection implements ServiceConnection {
//        private MessageService.Binder service;
//
//        public MessageService.Binder getService() {
//            return service;
//        }
//
//        @Override
//        public void onServiceConnected(ComponentName name, IBinder service) {
//            this.service = (MessageService.Binder) service;
//        }
//
//        @Override
//        public void onServiceDisconnected(ComponentName name) {
//            this.service = null;
//        }
//    }
}
