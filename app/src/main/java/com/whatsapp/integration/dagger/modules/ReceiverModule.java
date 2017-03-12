package com.whatsapp.integration.dagger.modules;

import android.content.Context;

import com.rubius.androidshared.abstraction.CustomContextWrapper;
import com.rubius.androidshared.abstraction.IContextWrapper;
import com.rubius.androidshared.dagger.qualifiers.ReceiverContext;
import com.rubius.androidshared.dagger.scopes.ReceiverScope;

import dagger.Module;
import dagger.Provides;

/**
 *
 */
@Module
public class ReceiverModule {
    private final Context context;

    public ReceiverModule(Context receiver) {
        this.context = receiver;
    }

    @Provides
    @ReceiverScope
    @ReceiverContext
    public IContextWrapper provideReceiverContextWrapper() {
        return new CustomContextWrapper(context);
    }
}
