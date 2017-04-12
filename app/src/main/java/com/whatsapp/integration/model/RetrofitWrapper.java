package com.whatsapp.integration.model;

import com.google.gson.GsonBuilder;
import com.rubius.androidshared.helpers.StringHelper;

import javax.inject.Inject;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 *
 */

public class RetrofitWrapper
    implements IRetrofitWrapper {

    @Inject
    public RetrofitWrapper() {
    }

    // region retrofit

    private Retrofit retrofit;

    private synchronized void setRetrofit(Retrofit retrofit) {
        this.retrofit = retrofit;
        if (retrofit == null)
            setWhatMessageService(null);
    }

    private Retrofit createRetrofit() {
        GsonBuilder builder = new GsonBuilder().serializeNulls();

        builder.registerTypeAdapter(WhatMessage.class, new WhatMessageSerializer());

        return new Retrofit.Builder()
            .baseUrl(connection)
            .addConverterFactory(GsonConverterFactory.create(builder.create()))
            .build();
    }

    // endregion retrofit

    // region connection

    private String connection;

    @Override
    public void setConnection(String connection) {
        if (this.connection == null ? connection == null : this.connection.equals(connection))
            return;

        this.connection = connection;

        setRetrofit(StringHelper.isNullOrEmpty(connection) ? null : createRetrofit());
    }

    // endregion connection

    // region WhatMessageService

    private IWhatMessageService whatMessageService;

    private synchronized void setWhatMessageService(IWhatMessageService whatMessageService) {
        this.whatMessageService = whatMessageService;
    }

    @Override
    public synchronized IWhatMessageService getWhatMessageService() {
        if (whatMessageService == null) {
            if (retrofit == null)
                setWhatMessageService(new WhatMessageServiceDummy());
            else
                setWhatMessageService(retrofit.create(IWhatMessageService.class));
        }

        return whatMessageService;
    }

    // endregion WhatMessageService
}
