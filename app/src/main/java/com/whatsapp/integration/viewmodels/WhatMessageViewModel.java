package com.whatsapp.integration.viewmodels;

import android.databinding.Bindable;
import android.support.annotation.NonNull;
import android.text.format.DateUtils;

import com.rubius.androidshared.abstraction.IContextWrapper;
import com.rubius.androidshared.helpers.StringHelper;
import com.rubius.androidshared.viewmodels.IView;
import com.rubius.androidshared.viewmodels.ViewModelBase;
import com.whatsapp.integration.BR;
import com.whatsapp.integration.R;

import java.text.DateFormat;
import java.util.Date;

/**
 *
 */
public class WhatMessageViewModel extends ViewModelBase<IView> {
    public WhatMessageViewModel(@NonNull IContextWrapper contextWrapper) {
        super(contextWrapper);
    }

    @Override
    protected String getPrintPrefix() {
        return "WhatMessageViewModel";
    }

    // region request

    private String request;

    @Bindable
    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        String actualRequest = StringHelper.isNullOrEmpty(request) ? contextWrapper.getString(R.string.item_request_none) : request;

        set(BR.request, this.request, actualRequest, () -> this.request = actualRequest);
    }

    // endregion request

    // region reply

    private String reply;

    @Bindable
    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        String actualReply = StringHelper.isNullOrEmpty(reply) ? contextWrapper.getString(R.string.item_reply_none) : reply;

        set(BR.reply, this.reply, actualReply, () -> this.reply = actualReply);
    }

    // endregion request

    // region createdAt

    private Date createdAt;

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;

        String dateString;
        if (createdAt == null)
            dateString = StringHelper.EMPTY;
        else {
            boolean isToday = DateUtils.isToday(createdAt.getTime());

            DateFormat format = isToday
                ? DateFormat.getTimeInstance(DateFormat.SHORT)
                : DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);

            dateString = format.format(createdAt);
        }

        setCreatedAtString(dateString);
    }

    // endregion createdAt

    // region createdAtString

    private String createdAtString;

    @Bindable
    public String getCreatedAtString() {
        return createdAtString;
    }

    private void setCreatedAtString(String createdAtString) {
        set(
            BR.createdAtString,
            this.createdAtString,
            createdAtString,
            () -> this.createdAtString = createdAtString
        );
    }

    // endregion createdAtString
}
