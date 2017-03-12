package com.whatsapp.integration.model;

import android.support.annotation.Nullable;

import java.util.Date;

/**
 *
 */

public class WhatMessage {
    public WhatMessage(long id, String request, @Nullable String reply, Date createdAt){
        this.id = id;
        this.request = request;
        this.reply = reply;
        this.createdAt = DateHelper.clone(createdAt);
    }

    // region id

    public static final String idName = "id";

    private final long id;

    public long getId() {
        return id;
    }

    // endregion id

    // region request

    public static final String requestName = "request";

    private final String request;

    public String getRequest() {
        return request;
    }

    // endregion request

    // region reply

    public static final String replyName = "reply";

    private final String reply;

    public String getReply() {
        return reply;
    }

    // endregion reply

    // region createdAt

    public static final String createdAtName = "created_at";

    private final Date createdAt;

    public Date getCreatedAt() {
        return DateHelper.clone(createdAt);
    }

    // endregion createdAt
}
