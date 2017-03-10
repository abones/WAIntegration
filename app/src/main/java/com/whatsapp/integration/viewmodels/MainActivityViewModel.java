package com.whatsapp.integration.viewmodels;

import android.support.annotation.NonNull;

import com.rubius.androidshared.abstraction.IActivityContextWrapper;
import com.rubius.androidshared.dagger.qualifiers.ActivityContext;
import com.rubius.androidshared.exceptions.NotImplementedException;
import com.rubius.androidshared.viewmodels.ActivityViewModel;
import com.whatsapp.integration.activities.MainActivity;

import javax.inject.Inject;

/**
 *
 */
public class MainActivityViewModel extends ActivityViewModel<WhatsappIntegrationApplicationViewModel, MainActivity> implements
    IMainActivityViewModel {

    @Inject
    public MainActivityViewModel(
        @ActivityContext @NonNull IActivityContextWrapper contextWrapper,
        @NonNull WhatsappIntegrationApplicationViewModel applicationViewModel
    ) {
        super(contextWrapper, applicationViewModel);
    }

    @Override
    protected String getPrintPrefix() {
        return "MainActivityViewModel";
    }

    public void sendMessage() {
        throw new NotImplementedException();
    }

    public void setConnection() {
        throw new NotImplementedException();
    }
}
