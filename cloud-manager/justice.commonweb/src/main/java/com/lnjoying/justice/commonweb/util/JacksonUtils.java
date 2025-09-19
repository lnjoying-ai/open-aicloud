package com.lnjoying.justice.commonweb.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;

import java.io.IOException;
import java.text.SimpleDateFormat;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2022/5/27 10:45
 */

public class JacksonUtils
{
    public static ObjectMapper objectMapper;

    public static ObjectMapper objectMapperNonNull;

    private static final String CONTENT_TYPE_JSON = "application/json";

    private static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    static
    {
        objectMapper = new ObjectMapper();
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.setDateFormat(new SimpleDateFormat(DEFAULT_DATE_FORMAT));

        objectMapperNonNull = new ObjectMapper();
        objectMapperNonNull.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        objectMapperNonNull.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapperNonNull.setDateFormat(new SimpleDateFormat(DEFAULT_DATE_FORMAT));
        objectMapperNonNull.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapperNonNull.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    public static String objToStr(Object value) throws IOException
    {
        return objectMapper.writeValueAsString(value);
    }

    public static String objToStrDefault(Object value)
    {
        try
        {
            return objectMapper.writeValueAsString(value);
        }
        catch (JsonProcessingException e)
        {
            return  "";
        }
    }

    public static <T> T strToObj(String content, Class<T> valueType) throws IOException
    {
        return objectMapper.readValue(content, valueType);
    }


    public static <T> T strToObjDefault(String content, Class<T> valueType)
    {
        try
        {
            return objectMapper.readValue(content, valueType);
        }
        catch (Exception e)
        {
            return null;
        }
    }

    public static <T> T strToObjType(String content, TypeReference<T> valueTypeRef) throws IOException {
        return objectMapper.readValue(content, valueTypeRef);
    }

    public static <T> T strToObjTypeDefault(String content, TypeReference<T> valueTypeRef)
    {
        try
        {
            return objectMapper.readValue(content, valueTypeRef);
        }
        catch (Exception e)
        {
            return null;
        }
    }

    public static JsonNode readTree(String content) throws JsonProcessingException
    {
        JsonNode jsonNode = objectMapper.readTree(content);
        return jsonNode;
    }

    public static JsonNode readTreeDefault(String content)
    {
        try
        {
            JsonNode jsonNode = objectMapper.readTree(content);
            return jsonNode;
        }
        catch (JsonProcessingException e)
        {
           return null;
        }
    }

    public static <T> T convertValue(Object fromValue, TypeReference<T> toValueTypeRef)
    {
        return objectMapper.convertValue(fromValue, toValueTypeRef);
    }


    public static <T> T convertValue(Object fromValue, Class<T> toValueType)
    {
        return objectMapper.convertValue(fromValue, toValueType);
    }

    public static <T> T convertValueExcludeNull(Object fromValue, Class<T> toValueType)
    {
        return objectMapperNonNull.convertValue(fromValue, toValueType);
    }
}
