package com.surfergraphy.surf.utils;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import org.threeten.bp.LocalDateTime;
import org.threeten.bp.format.DateTimeFormatter;

import java.lang.reflect.Type;
import java.util.Locale;

/**
 * Created by ddfactory on 2017-06-13.
 */

public class DateDeserializer implements JsonDeserializer<LocalDateTime> {
    @Override
    public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        String date = json.getAsString();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE',' dd MMM yyyy HH:mm:ss 'GMT'", Locale.ENGLISH);
        return LocalDateTime.parse(date, formatter);
    }
}
