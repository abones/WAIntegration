package com.whatsapp.integration.dagger.modules;

import android.app.Application;

import com.rubius.androidshared.abstraction.CustomContextWrapper;
import com.rubius.androidshared.abstraction.IContextWrapper;
import com.rubius.androidshared.dagger.qualifiers.ApplicationContext;
import com.rubius.androidshared.dagger.scopes.ApplicationScope;

import dagger.Module;
import dagger.Provides;

/**
 *
 */
@Module
public class ApplicationModule {
    private final Application application;

    public ApplicationModule(Application application) {
        this.application = application;
    }

    @Provides
    @ApplicationScope
    @ApplicationContext
    public IContextWrapper provideApplicationContextWrapper() {
        return new CustomContextWrapper(application);
    }

    //@Provides
    //@ApplicationScope
    //public IMessageServiceManager provideMessageServiceManager() {
    //    return new MessageServiceManager();
    //}
}
