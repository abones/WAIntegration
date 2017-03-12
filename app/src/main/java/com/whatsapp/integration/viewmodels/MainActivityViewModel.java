package com.whatsapp.integration.viewmodels;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.databinding.Bindable;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.widget.EditText;

import com.rubius.androidshared.abstraction.IActivityContextWrapper;
import com.rubius.androidshared.binding.RecyclerBindingAdapter;
import com.rubius.androidshared.binding.RecyclerConfiguration;
import com.rubius.androidshared.dagger.qualifiers.ActivityContext;
import com.rubius.androidshared.viewmodels.ActivityViewModel;
import com.whatsapp.integration.BR;
import com.whatsapp.integration.R;
import com.whatsapp.integration.activities.MainActivity;
import com.whatsapp.integration.misc.IPreferences;
import com.whatsapp.integration.service.IMessageServiceManager;
import com.whatsapp.integration.service.MessageService;

import javax.inject.Inject;

/**
 *
 */
public class MainActivityViewModel
        extends ActivityViewModel<IWhatsappIntegrationApplicationViewModel, MainActivity>
        implements
        IMainActivityViewModel {

    private final IMessageServiceManager messageServiceManager;
    private final IPreferences preferences;
    private RecyclerBindingAdapter<String> messagesAdapter;

    private final MessageServiceConnection messageServiceConnection = new MessageServiceConnection();

    @Inject
    public MainActivityViewModel(
            @ActivityContext @NonNull IActivityContextWrapper contextWrapper,
            @NonNull IWhatsappIntegrationApplicationViewModel applicationViewModel,
            @NonNull IMessageServiceManager messageServiceManager,
            IPreferences preferences
    ) {
        super(contextWrapper, applicationViewModel);
        this.messageServiceManager = messageServiceManager;
        this.preferences = preferences;
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
        contextWrapper.bindService(MessageService.class, messageServiceConnection, 0);
    }

    @Override
    public void onPause() {
        super.onPause();
        contextWrapper.unbindService(messageServiceConnection);
    }

    @Override
    public void onStart() {
        super.onStart();
        setIsServiceEnabled(messageServiceManager.isServiceEnabled());
        connection = preferences.getString(IMessageServiceViewModel.SETTINGS_CONNECTION, null);
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

    // region connection

    private String connection;

    @Bindable
    public String getConnection() {
        return connection;
    }

    private void setConnection(String connection) {
        if (!set(BR.connection, this.connection, connection, () -> this.connection = connection))
            return;

        preferences.setConnection(connection);
    }

    // endregion connection

    // endregion Properties

    public void toggleService() {
        setIsServiceEnabled(!isServiceEnabled);
    }

    public void changeConnection() {
        AlertDialog.Builder dialogBuilder = contextWrapper.createAlertDialog();

        EditText editText = contextWrapper.createEditText();
        editText.setText(connection);
        dialogBuilder.setView(editText);

        dialogBuilder.setTitle(contextWrapper.getString(R.string.message_enter_connection));
        dialogBuilder.setPositiveButton(
                contextWrapper.getString(R.string.connection_ok),
                (dialog, whichButton) -> setConnection(editText.getText().toString())
        );
        dialogBuilder.setNegativeButton(contextWrapper.getString(R.string.connection_cancel), null);
        dialogBuilder.show();
    }

    public void getMessages() {
        MessageServiceViewModel.Binder service = messageServiceConnection.getService();

        if (service == null)
            return;

        service.getMessages();
    }

    private static class MessageServiceConnection implements ServiceConnection {
        private MessageServiceViewModel.Binder service;

        public MessageServiceViewModel.Binder getService() {
            return service;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            this.service = (MessageServiceViewModel.Binder) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            this.service = null;
        }
    }
}
