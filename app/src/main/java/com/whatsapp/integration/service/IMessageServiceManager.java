package com.whatsapp.integration.service;

/**
 *
 */

public interface IMessageServiceManager {
    void startServiceIfEnabled();
    boolean isServiceEnabled();

    void setServiceEnabled(boolean isServiceEnabled);
}
