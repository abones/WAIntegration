package com.whatsapp.integration.dagger.modules;

import android.app.Service;

import com.rubius.androidshared.abstraction.IServiceContextWrapper;
import com.rubius.androidshared.abstraction.ServiceContextWrapper;
import com.rubius.androidshared.dagger.qualifiers.ServiceContext;

import dagger.Module;
import dagger.Provides;

/**
 *
 */
@Module
public class ServiceModule {
    private final Service service;

    public ServiceModule(Service service) {
        this.service = service;
    }

    @Provides
    @ServiceContext
    public IServiceContextWrapper provideServiceContext() {
        return new ServiceContextWrapper(service);
    }
}
