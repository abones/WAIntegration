package com.whatsapp.integration.dagger.components;

import com.rubius.androidshared.dagger.scopes.ActivityScope;
import com.whatsapp.integration.activities.MainActivity;
import com.whatsapp.integration.dagger.modules.ActivityModule;
import com.whatsapp.integration.dagger.modules.ActivityScopeBinds;

import dagger.Component;

/**
 *
 */
@ActivityScope
@Component(dependencies = ApplicationComponent.class, modules = {ActivityModule.class, ActivityScopeBinds.class})
public interface ActivityComponent {
    void inject(MainActivity mainActivity);
}
