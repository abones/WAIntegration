package com.whatsapp.integration.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.rubius.androidshared.helpers.DateHelper;

import java.lang.reflect.Type;
import java.util.Date;

/**
 *
 */

public class WhatMessageSerializer
        implements ITwoWaySerializer<WhatMessage> {

    @Override
    public JsonElement serialize(
            WhatMessage src,
            Type typeOfSrc,
            JsonSerializationContext context) {
        JsonObject result = new JsonObject();

        result.addProperty(WhatMessage.idName, src.getId());
        result.addProperty(WhatMessage.requestName, src.getRequest());
        result.addProperty(WhatMessage.replyName, src.getReply());
        result.addProperty(WhatMessage.createdAtName, DateHelper.toIso8601(src.getCreatedAt()));

        return result;
    }

    @Override
    public WhatMessage deserialize(
            JsonElement json,
            Type typeOfT,
            JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        long id = jsonObject.get(WhatMessage.idName)
                            .getAsLong();
        String request = jsonObject.get(WhatMessage.requestName)
                                   .getAsString();
        String reply = jsonObject.get(WhatMessage.replyName)
                                 .getAsString();
        String createdAtString = jsonObject.get(WhatMessage.createdAtName)
                                           .getAsString();
        Date createdAt = DateHelper.parseIso8601(createdAtString);

        return new WhatMessage(id, request, reply, createdAt);
    }

}
