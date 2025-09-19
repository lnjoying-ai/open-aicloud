package com.lnjoying.justice.commonweb.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 功能描述
 *
 * @author: Robin
 * @date: 2024年01月31日 09:47
 */
public class FieldPathResolverUtil
{
    private static final String CUSTOM_CLASS_PACKAGE_PREFIX = String.join(".", Arrays.copyOfRange(FieldPathResolverUtil.class.getPackage().getName().split("\\."), 0, 2));

    private FieldPathResolverUtil()
    {

    }

    public static List<String> getFieldPath(Class<?> clazz, String fieldName)
    {
        List<String> pathList = new ArrayList<>();
        if (buildPath(clazz, fieldName, pathList))
        {
            return pathList;
        }
        return Collections.emptyList();
    }

    public static List<Field> getAllFields(List<Field> fields, Class<?> type)
    {
        // 添加当前类声明的所有字段
        for (Field field : type.getDeclaredFields())
        {
            fields.add(field);
        }

        // 如果有超类，递归其字段
        if (type.getSuperclass() != null)
        {
            fields = getAllFields(fields, type.getSuperclass());
        }

        return fields;
    }

    private static boolean buildPath(Class<?> clazz, String fieldName, List<String> pathList)
    {
        List<Field> declaredFields = getAllFields(new ArrayList<>(), clazz);
        for (Field field : declaredFields)
        {
            pathList.add(field.getName());
            if (field.getName().equals(fieldName))
            {
                return true;
            }
            if (!field.getType().isPrimitive() && field.getType().getPackage().getName().startsWith(CUSTOM_CLASS_PACKAGE_PREFIX) && buildPath(field.getType(), fieldName, pathList))
            {
                return true;
            }
            pathList.remove(pathList.size() - 1);
        }
        return false;
    }
}
