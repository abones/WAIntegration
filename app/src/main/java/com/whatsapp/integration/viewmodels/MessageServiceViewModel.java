package com.whatsapp.integration.viewmodels;

import android.support.annotation.NonNull;

import com.rubius.androidshared.abstraction.IContextWrapper;
import com.rubius.androidshared.dagger.qualifiers.ServiceContext;
import com.rubius.androidshared.viewmodels.IView;
import com.rubius.androidshared.viewmodels.ViewModelBase;

import javax.inject.Inject;

/**
 *
 */

public class MessageServiceViewModel
        extends ViewModelBase<IView>
        implements IMessageServiceViewModel {
    @Inject
    public MessageServiceViewModel(@ServiceContext @NonNull IContextWrapper contextWrapper) {
        super(contextWrapper);
    }

    @Override
    protected String getPrintPrefix() {
        return "MessageServiceViewModel";
    }

    @Override
    public void onDestroy() {
        
    }
}
