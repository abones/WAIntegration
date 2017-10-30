package com.whatsapp.integration.activities;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.android.databinding.library.baseAdapters.BR;
import com.rubius.androidshared.activities.AppCompatActivityBase;
import com.rubius.androidshared.viewmodels.IActivityView;
import com.rubius.androidshared.viewmodels.IActivityViewModel;
import com.whatsapp.integration.R;
import com.whatsapp.integration.WhatsappIntegrationApplication;
import com.whatsapp.integration.dagger.components.ActivityComponent;
import com.whatsapp.integration.databinding.ActivityMainBinding;
import com.whatsapp.integration.viewmodels.IMainActivityViewModel;

public class MainActivity extends AppCompatActivityBase<WhatsappIntegrationApplication, IMainActivityViewModel, ActivityMainBinding> implements
    IActivityView {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMainBinding binding = getBinding();

        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
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
}
