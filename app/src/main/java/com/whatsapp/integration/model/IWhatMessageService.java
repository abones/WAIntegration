package com.whatsapp.integration.model;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

/**
 *
 */

public interface IWhatMessageService {
    @GET("/api/messages/")
    Call<List<WhatMessage>> getMessages(@Query("awaits_reply") int awaitsReply);

    @POST("/api/messages/")
    Call<WhatMessage> createMessage(@Body WhatMessage whatMessage);

    @PUT("/api/messages/")
    Call<WhatMessage> updateMessage(@Body WhatMessage whatMessage);
}
