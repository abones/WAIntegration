package com.whatsapp.integration.model;

import com.google.gson.GsonBuilder;

import javax.inject.Inject;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 *
 */

public class RetrofitWrapper
        implements IRetrofitWrapper {

    private final Object whatMessageServiceLock = new Object();
    private Retrofit retrofit;

    @Inject
    public RetrofitWrapper() {
    }

    private Retrofit createRetrofit() {
        GsonBuilder builder = new GsonBuilder().serializeNulls();

        builder.registerTypeAdapter(WhatMessage.class, new WhatMessageSerializer());

        return new Retrofit.Builder()
                .baseUrl(connection)
                .addConverterFactory(GsonConverterFactory.create(builder.create()))
                .build();
    }

    // region connection

    private String connection;

    @Override
    public void setConnection(String connection) {
        this.connection = connection;

        synchronized (whatMessageServiceLock) {
            whatMessageService = null;
        }

        if (connection != null)
            retrofit = createRetrofit();
    }

    // endregion connection

    private IWhatMessageService whatMessageService;

    @Override
    public IWhatMessageService getWhatMessageService() {
        synchronized (whatMessageServiceLock) {
            if (whatMessageService == null) {
                whatMessageService = retrofit.create(IWhatMessageService.class);
            }

            return whatMessageService;
        }
    }
}
