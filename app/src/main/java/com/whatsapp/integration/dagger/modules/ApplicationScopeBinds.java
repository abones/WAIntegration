package com.whatsapp.integration.dagger.modules;

import com.rubius.androidshared.dagger.scopes.ApplicationScope;
import com.whatsapp.integration.viewmodels.IWhatsappIntegrationApplicationViewModel;
import com.whatsapp.integration.viewmodels.WhatsappIntegrationApplicationViewModel;

import dagger.Binds;
import dagger.Module;

/**
 *
 */
@Module
public abstract class ApplicationScopeBinds {
    @Binds
    @ApplicationScope
    public abstract IWhatsappIntegrationApplicationViewModel bindApplicationViewModel(WhatsappIntegrationApplicationViewModel whatsappIntegrationApplication);
}
