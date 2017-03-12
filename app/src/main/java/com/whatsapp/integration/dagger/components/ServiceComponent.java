package com.whatsapp.integration.dagger.components;

import com.rubius.androidshared.dagger.scopes.ServiceScope;
import com.whatsapp.integration.dagger.modules.ServiceModule;
import com.whatsapp.integration.dagger.modules.ServiceScopeBinds;
import com.whatsapp.integration.service.MessageService;

import dagger.Component;

/**
 *
 */
@ServiceScope
@Component(dependencies = ApplicationComponent.class, modules = {ServiceModule.class, ServiceScopeBinds.class})
public interface ServiceComponent {
    void inject(MessageService messageService);
}
