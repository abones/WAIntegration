package com.whatsapp.integration.viewmodels;

import android.support.annotation.NonNull;

import com.rubius.androidshared.abstraction.IContextWrapper;
import com.rubius.androidshared.dagger.qualifiers.ApplicationContext;
import com.rubius.androidshared.viewmodels.ApplicationViewModel;

import javax.inject.Inject;

/**
 *
 */
public class WhatsappIntegrationApplicationViewModel extends ApplicationViewModel implements IWhatsappIntegrationApplicationViewModel {
    @Inject
    public WhatsappIntegrationApplicationViewModel(@ApplicationContext @NonNull IContextWrapper contextWrapper) {
        super(contextWrapper);
    }
}
