package com.whatsapp.integration.dagger.modules;

import com.rubius.androidshared.dagger.scopes.ApplicationScope;
import com.whatsapp.integration.model.IRetrofitWrapper;
import com.whatsapp.integration.model.RetrofitWrapper;

import dagger.Binds;
import dagger.Module;

/**
 *
 */
@Module
public abstract class DatabaseModule {
    @Binds
    @ApplicationScope
    public abstract IRetrofitWrapper bindRetrofitWrapper(RetrofitWrapper retrofitWrapper);
}
