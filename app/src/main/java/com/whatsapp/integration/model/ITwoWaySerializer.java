package com.whatsapp.integration.model;

import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;

/**
 *
 */

interface ITwoWaySerializer<T>
        extends JsonSerializer<T>,
        JsonDeserializer<T> {
}
