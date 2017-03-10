package com.whatsapp.integration.dagger.components;

import com.rubius.androidshared.abstraction.IContextWrapper;
import com.rubius.androidshared.dagger.qualifiers.ApplicationContext;
import com.rubius.androidshared.dagger.scopes.ApplicationScope;
import com.rubius.androidshared.viewmodels.IApplicationViewModel;
import com.whatsapp.integration.WhatsappIntegrationApplication;
import com.whatsapp.integration.dagger.modules.ApplicationModule;
import com.whatsapp.integration.dagger.modules.ApplicationScopeBinds;
import com.whatsapp.integration.dagger.modules.DatabaseModule;

import dagger.Component;

/**
 *
 */
@ApplicationScope
@Component(modules = {ApplicationModule.class, ApplicationScopeBinds.class, DatabaseModule.class})
public interface ApplicationComponent {
    void inject(WhatsappIntegrationApplication application);

    IApplicationViewModel getApplicationViewModel();

    @ApplicationContext
    IContextWrapper getApplicationContextWrapper();
}
