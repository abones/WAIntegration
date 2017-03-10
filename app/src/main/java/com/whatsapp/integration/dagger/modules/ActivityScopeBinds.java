package com.whatsapp.integration.dagger.modules;

import com.rubius.androidshared.dagger.scopes.ActivityScope;
import com.whatsapp.integration.viewmodels.IMainActivityViewModel;
import com.whatsapp.integration.viewmodels.MainActivityViewModel;

import dagger.Binds;
import dagger.Module;

/**
 *
 */
@Module
public abstract class ActivityScopeBinds {
    @Binds
    @ActivityScope
    public abstract IMainActivityViewModel bindMainActivityViewModel(MainActivityViewModel mainActivityViewModel);
}
