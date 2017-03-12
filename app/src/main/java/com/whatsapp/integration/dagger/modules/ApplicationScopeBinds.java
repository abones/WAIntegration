package com.whatsapp.integration.dagger.modules;

import com.rubius.androidshared.dagger.scopes.ApplicationScope;
import com.whatsapp.integration.IPreferences;
import com.whatsapp.integration.Preferences;
import com.whatsapp.integration.service.IMessageServiceManager;
import com.whatsapp.integration.service.MessageServiceManager;
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

    @Binds
    @ApplicationScope
    public abstract IMessageServiceManager bindMessageSeviceManager(MessageServiceManager messageServiceManager);

    @Binds
    @ApplicationScope
    public abstract IPreferences bindSettings(Preferences preferences);
}
