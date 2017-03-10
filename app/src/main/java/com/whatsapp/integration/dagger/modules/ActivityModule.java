package com.whatsapp.integration.dagger.modules;

import android.app.Activity;

import com.rubius.androidshared.abstraction.ActivityContextWrapper;
import com.rubius.androidshared.abstraction.IActivityContextWrapper;
import com.rubius.androidshared.dagger.qualifiers.ActivityContext;
import com.rubius.androidshared.dagger.scopes.ActivityScope;

import dagger.Module;
import dagger.Provides;

/**
 *
 */
@Module
public class ActivityModule {
    private final Activity activity;

    public ActivityModule(Activity activity) {
        this.activity = activity;
    }

    @Provides
    @ActivityScope
    @ActivityContext
    public IActivityContextWrapper provideActivityContextWrapper() {
        return new ActivityContextWrapper(activity);
    }
}
