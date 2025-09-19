package com.lnjoying.justice.commonweb.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 功能描述
 *
 * @author: Robin
 * @date: 2023年11月08日 15:19
 */
@Slf4j
public class ReflectionUtil
{
    public static <T1> String getStaticValueFieldName(Class clazz, Class<T1> fieldValueClass, T1 queryValue)
    {
        if (clazz == null || fieldValueClass == null || queryValue == null)
        {
            return "";
        }
        try
        {
            for (Field field : Arrays.stream(clazz.getFields()).filter(x -> Modifier.isStatic(x.getModifiers())
                    && x.getType() == fieldValueClass).collect(Collectors.toList()))
            {
                if (Objects.equals(field.get(null), queryValue))
                {
                    return field.getName();
                }
            }

            return "";
        } catch (Throwable e)
        {
            log.error("提取类静态值的字段名时出错！error: {}", e.getMessage());
            return "";
        }
    }

    public static <T> Optional<Object> getFieldValueByAnnotation(T obj, Class<? extends Annotation> annotation)
    {
        if (obj == null)
        {
            return Optional.empty();
        }

        try
        {
            Class<?> aClass = obj.getClass();
            Field[] declaredFields = aClass.getDeclaredFields();
            for (Field declaredField : declaredFields)
            {
                Annotation[] declaredAnnotations = declaredField.getDeclaredAnnotations();
                if (declaredAnnotations != null)
                {
                    if (declaredField.isAnnotationPresent(annotation))
                    {
                        declaredField.setAccessible(true);
                        Object value = declaredField.get(obj);
                        return Optional.ofNullable(value);
                    }
                }
            }
        } catch (IllegalAccessException e)
        {
            log.error("get field value by annotation failed! stackTrace:{}, errorMessage:{}",
                    ExceptionUtils.getStackTrace(e), e.getMessage());
            return Optional.empty();
        }

        return Optional.empty();
    }
}
