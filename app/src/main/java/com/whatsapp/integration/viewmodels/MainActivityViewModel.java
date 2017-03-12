package com.whatsapp.integration.viewmodels;

import android.content.Intent;
import android.databinding.Bindable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;

import com.rubius.androidshared.abstraction.IActivityContextWrapper;
import com.rubius.androidshared.binding.RecyclerBindingAdapter;
import com.rubius.androidshared.binding.RecyclerConfiguration;
import com.rubius.androidshared.dagger.qualifiers.ActivityContext;
import com.rubius.androidshared.exceptions.NotImplementedException;
import com.rubius.androidshared.viewmodels.ActivityViewModel;
import com.whatsapp.integration.BR;
import com.whatsapp.integration.activities.MainActivity;
import com.whatsapp.integration.service.IMessageServiceManager;

import javax.inject.Inject;

/**
 *
 */
public class MainActivityViewModel
        extends ActivityViewModel<IWhatsappIntegrationApplicationViewModel, MainActivity>
        implements
        IMainActivityViewModel {

    private final IMessageServiceManager messageServiceManager;
    private RecyclerBindingAdapter<String> messagesAdapter;

    @Inject
    public MainActivityViewModel(
            @ActivityContext @NonNull IActivityContextWrapper contextWrapper,
            @NonNull IWhatsappIntegrationApplicationViewModel applicationViewModel,
            @NonNull IMessageServiceManager messageServiceManager
    ) {
        super(contextWrapper, applicationViewModel);
        this.messageServiceManager = messageServiceManager;
    }

    @Override
    public void onCreate(
            @Nullable Bundle savedInstanceState, @Nullable Intent intent
    ) {
        super.onCreate(savedInstanceState, intent);
        messagesAdapter = createMessagesAdapter();
        recyclerConfiguration = new RecyclerConfiguration();
        recyclerConfiguration.setAdapter(messagesAdapter);
        recyclerConfiguration.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void onResume() {
        super.onResume();
        recyclerConfiguration.setLayoutManager(contextWrapper.createLinearLayoutManager());
    }

    @Override
    public void onStart() {
        super.onStart();
        setIsServiceEnabled(messageServiceManager.isServiceEnabled());
        messageServiceManager.startServiceIfEnabled();
    }

    @Override
    protected String getPrintPrefix() {
        return "MainActivityViewModel";
    }

    private RecyclerBindingAdapter<String> createMessagesAdapter() {
        return new RecyclerBindingAdapter<>(
                null,
                0,//R.layout.item_exam_result,
                0,//BR.item,
                null
        );
    }

    // region Properties

    // region isServiceEnabled

    private boolean isServiceEnabled;

    @Bindable
    public boolean getIsServiceEnabled() {
        return isServiceEnabled;
    }

    private void setIsServiceEnabled(boolean isServiceEnabled) {
        if (!set(
                BR.isServiceEnabled,
                this.isServiceEnabled,
                isServiceEnabled,
                () -> this.isServiceEnabled = isServiceEnabled
        ))
            return;
        messageServiceManager.setServiceEnabled(isServiceEnabled);
    }

    // endregion isServiceEnabled

    // region recyclerConfiguration

    private RecyclerConfiguration recyclerConfiguration;

    @Bindable
    public RecyclerConfiguration getRecyclerConfiguration() {
        return recyclerConfiguration;
    }

    // endregion recyclerConfiguration

    // region hasMessages

    private boolean hasMessages;

    @Bindable
    public boolean getHasMessages() {
        return hasMessages;
    }

    private void setHasMessages(boolean hasMessages) {
        set(BR.hasMessages, this.hasMessages, hasMessages, () -> this.hasMessages = hasMessages);
    }

    // endregion hasMessages

    // endregion Properties

    public void toggleService() {
        setIsServiceEnabled(!isServiceEnabled);
    }

    public void sendMessage() {
        throw new NotImplementedException();
    }

    public void setConnection() {
        throw new NotImplementedException();
    }
}
