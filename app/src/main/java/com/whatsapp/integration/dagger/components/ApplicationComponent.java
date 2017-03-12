package com.whatsapp.integration.dagger.components;

import com.rubius.androidshared.dagger.scopes.ApplicationScope;
import com.whatsapp.integration.WhatsappIntegrationApplication;
import com.whatsapp.integration.dagger.modules.ApplicationModule;
import com.whatsapp.integration.dagger.modules.ApplicationScopeBinds;
import com.whatsapp.integration.dagger.modules.DatabaseModule;
import com.whatsapp.integration.misc.IPreferences;
import com.whatsapp.integration.service.IMessageServiceManager;
import com.whatsapp.integration.viewmodels.IWhatsappIntegrationApplicationViewModel;

import dagger.Component;

/**
 *
 */
@ApplicationScope
@Component(modules = {ApplicationModule.class, ApplicationScopeBinds.class, DatabaseModule.class})
public interface ApplicationComponent {
    void inject(WhatsappIntegrationApplication application);

    IWhatsappIntegrationApplicationViewModel getApplicationViewModel();
    IMessageServiceManager getMessageServiceManager();
    IPreferences getPreferences();
}
