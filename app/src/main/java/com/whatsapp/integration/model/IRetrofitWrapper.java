package com.whatsapp.integration.model;

/**
 *
 */

public interface IRetrofitWrapper {
    void setConnection(String connection);
    IWhatMessageService getWhatMessageService();
}
