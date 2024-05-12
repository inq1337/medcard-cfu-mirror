package org.cfuv.medcard.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.function.Consumer;

@Slf4j
public final class JsonUtils {

    public final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static <T> T readFromString(String str, Class<T> clazz) {
        T result = null;
        try {
            result = OBJECT_MAPPER.readValue(str, clazz);
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return result;
    }

    public static <T> List<T> readCollectionFromString(String str, Class<T> clazz) {
        ObjectReader reader = OBJECT_MAPPER.readerFor(clazz);
        List<T> result = null;
        try {
            result = reader.<T>readValues(str).readAll();
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return result;
    }

    public static <T> T readFromString(String str, Class<T> clazz, Consumer<String> onError) {
        T result = null;
        try {
            result = OBJECT_MAPPER.readValue(str, clazz);
        } catch (Exception e) {
            log.error(e.getMessage());
            onError.accept(e.getMessage());
        }

        return result;
    }

    public static JsonNode readTreeFromString(String str) {
        JsonNode result = null;
        try {
            result = OBJECT_MAPPER.readTree(str);
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return result;
    }

    public static <T> String writeValue(T obj) {
        try {
            return OBJECT_MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Invalid write to JSON:\n'" + obj + "'", e);
        }
    }

}
