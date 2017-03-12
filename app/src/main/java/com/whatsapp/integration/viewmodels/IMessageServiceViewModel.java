package com.whatsapp.integration.viewmodels;

import com.rubius.androidshared.viewmodels.IServiceViewModel;
import com.rubius.androidshared.viewmodels.IView;

/**
 *
 */

public interface IMessageServiceViewModel
        extends IServiceViewModel<IView> {
    String SETTINGS_CONNECTION = "com.whatsapp.integration.CONNECTION";
    String SETTINGS_ENABLED = "com.whatsapp.integration.ENABLED";
}
