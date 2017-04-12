package com.whatsapp.integration.model;

import com.whatsapp.integration.misc.CallDummy;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Query;

/**
 *
 */

public class WhatMessageServiceDummy implements IWhatMessageService {
    @Override
    public Call<List<WhatMessage>> getMessages(@Query("awaits_reply") int awaitsReply) {
        return new CallDummy<>();
    }

    @Override
    public Call<WhatMessage> createMessage(@Body WhatMessage whatMessage) {
        return new CallDummy<>();
    }

    @Override
    public Call<WhatMessage> updateMessage(@Body WhatMessage whatMessage) {
        return new CallDummy<>();
    }
}
