package com.whatsapp.integration.dagger.components;

import com.rubius.androidshared.dagger.scopes.ReceiverScope;
import com.whatsapp.integration.BootReceiver;

import dagger.Component;

/**
 *
 */
@ReceiverScope
@Component(dependencies = ApplicationComponent.class)
public interface ReceiverComponent {
    void inject(BootReceiver bootReceiver);
}
