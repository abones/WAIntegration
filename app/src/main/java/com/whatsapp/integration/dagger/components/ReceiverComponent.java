package com.whatsapp.integration.dagger.components;

import com.rubius.androidshared.dagger.scopes.ReceiverScope;
import com.whatsapp.integration.dagger.modules.ReceiverModule;
import com.whatsapp.integration.misc.BootReceiver;

import dagger.Component;

/**
 *
 */
@ReceiverScope
@Component(dependencies = ApplicationComponent.class, modules = ReceiverModule.class)
public interface ReceiverComponent {
    void inject(BootReceiver bootReceiver);
}
