package com.whatsapp.integration.dagger.modules;

import com.rubius.androidshared.dagger.scopes.ServiceScope;
import com.whatsapp.integration.viewmodels.IMessageServiceViewModel;
import com.whatsapp.integration.viewmodels.MessageServiceViewModel;

import dagger.Binds;
import dagger.Module;

/**
 *
 */
@Module
public abstract class ServiceScopeBinds {
    @Binds
    @ServiceScope
    public abstract IMessageServiceViewModel bindMessageServiceViewModel(MessageServiceViewModel messageServiceViewModel);
}
