package com.lnjoying.justice.iam.service;

import com.lnjoying.justice.iam.authz.opa.topic.DisruptorManager;
import com.lnjoying.justice.iam.authz.opa.common.EventType;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.lnjoying.justice.iam.authz.opa.data.DataEvent.DATA_TYPE;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/3/1 21:15
 */

@Service
public class DisruptorService
{

    private static final Map<Class<?>, String> policyTypeCache = new ConcurrentHashMap<>(16);

    @Autowired
    private DisruptorManager disruptorManager;

    public void publish(Object msg)
    {
        Class<?> clazz = msg.getClass();
        objectType(clazz);

        this.disruptorManager.publish(msg, policyTypeCache.get(clazz));
    }

    private static void objectType(Class<?> clazz)
    {
        String policyType = policyTypeCache.get(clazz);
        if (StringUtils.isEmpty(policyType))
        {
            boolean annotationPresent = clazz.isAnnotationPresent(EventType.class);
            if (annotationPresent)
            {
                policyTypeCache.put(clazz, clazz.getAnnotation(EventType.class).value());
            }
            else
            {
                // set default data type
                policyTypeCache.put(clazz, DATA_TYPE);
            }
        }
    }
}
