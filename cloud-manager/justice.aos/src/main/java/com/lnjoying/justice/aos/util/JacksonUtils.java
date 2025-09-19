package com.lnjoying.justice.aos.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.text.SimpleDateFormat;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2022/5/27 10:45
 */

public class JacksonUtils
{
    private static ObjectMapper objectMapper;

    private static final String CONTENT_TYPE_JSON = "application/json";

    private static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    static
    {
        objectMapper = new ObjectMapper();
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        objectMapper.setDateFormat(new SimpleDateFormat(DEFAULT_DATE_FORMAT));
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    public static String objToStr(Object value) throws IOException
    {
        return objectMapper.writeValueAsString(value);
    }

    public static <T> T strToObj(String content, Class<T> valueType) throws IOException {
        return objectMapper.readValue(content, valueType);
    }

    public static <T> T strToObjType(String content, TypeReference<T> valueTypeRef) throws IOException {
        return objectMapper.readValue(content, valueTypeRef);
    }
}
