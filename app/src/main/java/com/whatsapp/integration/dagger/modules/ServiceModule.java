package com.whatsapp.integration.dagger.modules;

import android.app.Service;
import android.content.Context;

import com.rubius.androidshared.abstraction.CustomContextWrapper;
import com.rubius.androidshared.abstraction.IContextWrapper;
import com.rubius.androidshared.dagger.qualifiers.ServiceContext;

import dagger.Module;
import dagger.Provides;

/**
 *
 */
@Module
public class ServiceModule {
    private final Context context;

    public ServiceModule(Service context) {
        this.context = context;
    }

    @Provides
    @ServiceContext
    public IContextWrapper provideServiceContext() {
        return new CustomContextWrapper(context);
    }
}
