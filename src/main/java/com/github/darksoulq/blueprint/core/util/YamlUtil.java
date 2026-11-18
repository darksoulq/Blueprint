package com.github.darksoulq.blueprint.core.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.util.List;
import java.util.Map;

public class YamlUtil {

    public static JsonElement toJson(Object raw) {
        if (raw instanceof Map<?, ?> map) {
            JsonObject obj = new JsonObject();
            map.forEach((k, v) -> obj.add(String.valueOf(k), toJson(v)));
            return obj;
        } else if (raw instanceof List<?> list) {
            JsonArray arr = new JsonArray();
            list.forEach(v -> arr.add(toJson(v)));
            return arr;
        } else if (raw instanceof Number n) {
            return new JsonPrimitive(n);
        } else if (raw instanceof Boolean b) {
            return new JsonPrimitive(b);
        } else if (raw != null) {
            return new JsonPrimitive(String.valueOf(raw));
        }
        return JsonNull.INSTANCE;
    }
}