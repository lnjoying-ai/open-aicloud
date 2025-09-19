package com.lnjoying.justice.commonweb.handler.aspect.util;

import com.lnjoying.justice.commonweb.handler.aspect.annotation.SensitiveField;
import com.lnjoying.justice.commonweb.handler.aspect.enums.SensitiveType;
import com.lnjoying.justice.commonweb.handler.aspect.model.SensitiveFieldDefinition;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 功能描述
 *
 * @author: Robin
 * @date: 2024年01月30日 19:06
 */
public class SensitiveFieldScanner
{
    public static Map<String, SensitiveFieldDefinition> findSensitiveFields(Object object)
    {
        if (object == null)
        {
            return Collections.emptyMap();
        }

        Map<String, SensitiveFieldDefinition> resultMap = new HashMap<>();
        doFindSensitiveField(object, resultMap);
        return resultMap;
    }

    private static void doFindSensitiveField(Object object, Map<String, SensitiveFieldDefinition> resultMap)
    {
        ReflectionUtils.doWithFields(object.getClass(), field ->
        {
            SensitiveField sensitive = field.getAnnotation(SensitiveField.class);
            field.setAccessible(true);
            Object value = field.get(object);
            if (sensitive != null)
            {
                SensitiveFieldDefinition definition = SensitiveFieldDefinition.builder()
                        .field(field)
                        .fieldValue(value)
                        .obj(object)
                        .sensitiveType(sensitive.value())
                        .build();
                resultMap.put(field.getName(), definition);
            }

            if (value != null && value.getClass().getPackage().getName().startsWith("com.lnjoying"))
            {
                doFindSensitiveField(value, resultMap);
            }
        });
    }
}
