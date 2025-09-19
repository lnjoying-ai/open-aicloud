package com.lnjoying.justice.commonweb.util;

import com.google.gson.*;

import java.lang.reflect.Type;

/**
 * Gson序列化、反序列化工具类
 *
 * @author: Robin
 * @date: 2023年12月08日 17:17
 */
public class JsonUtils
{
    private JsonUtils()
    {
    }

    private static final Gson INSTANCE = new GsonBuilder().create();
    private static final Gson SNAKE_CASE_INSTANCE = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();

    /**
     * 将对象序列化为JSON字符串
     *
     * @param obj
     * @return
     */
    public static String toJson(Object obj)
    {
        return INSTANCE.toJson(obj);
    }

    public static String toJsonWithSnakeCase(Object obj)
    {
        return SNAKE_CASE_INSTANCE.toJson(obj);
    }

    public static void replaceValue(JsonElement jsonElement, String[] path, JsonElement newValue) {
        if (path.length == 0) {
            return;
        }

        JsonObject jsonObject = jsonElement.getAsJsonObject();

        for (int i = 0; i < path.length - 1; i++) {
            String key = path[i];
            jsonElement = jsonObject.get(key);
            if (jsonElement == null || !jsonElement.isJsonObject()) {
                throw new IllegalArgumentException("Path not found: " + String.join(".", path));
            }
            jsonObject = jsonElement.getAsJsonObject();
        }

        String keyToReplace = path[path.length - 1];
        jsonObject.add(keyToReplace, newValue);
    }

    public static JsonElement toJsonTree(Object obj)
    {
        return INSTANCE.toJsonTree(obj);
    }

    public static JsonElement toJsonTreeWithSnakeCase(Object obj)
    {
        return SNAKE_CASE_INSTANCE.toJsonTree(obj);
    }

    /**
     * 将JSON字符串反序列化为对象
     *
     * @param json
     * @param targetType
     * @param <T>
     * @return
     */
    public static <T> T fromJson(String json, Class<T> targetType)
    {
        return INSTANCE.fromJson(json, targetType);
    }
    public static <T> T fromJsonWithSnakeCase(String json, Class<T> targetType)
    {
        return SNAKE_CASE_INSTANCE.fromJson(json, targetType);
    }

    public static <T> T fromJson(String json, Type typeOfT)
    {
        return INSTANCE.fromJson(json, typeOfT);
    }
    public static <T> T fromJsonWithSnakeCase(String json, Type typeOfT)
    {
        return SNAKE_CASE_INSTANCE.fromJson(json, typeOfT);
    }
}
