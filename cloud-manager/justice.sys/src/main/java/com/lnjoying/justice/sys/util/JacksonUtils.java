package com.lnjoying.justice.sys.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;

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
}
