package com.lnjoying.justice.iam.domain.model;

import lombok.Data;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ConcurrentReferenceHashMap;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/3/23 14:26
 */

public class ActionsCache
{

    /**
     * key: action_name
     * value: serviceAction
     */
    private final Map<String, ServiceAction> cache;

    public ActionsCache()
    {
        this.cache = new ConcurrentHashMap<>();
    }

    public ServiceAction get(String actionName)
    {
       return cache.get(actionName);
    }

    public void create(String actionName, ServiceAction action)
    {
        cache.put(actionName, action);
    }

    public void create(Map<String, ServiceAction> map)
    {
        if (!CollectionUtils.isEmpty(map))
        {
            cache.putAll(map);
        }
    }


    @Data
    public static class ServiceAction
    {
        private String serviceId;

        private String serviceName;

        private String iamCode;

        private String resourceId;

        private String resourceName;

        private String resourceType;

        private String actionId;

        private String actionName;

        private String serviceDisplayName;

        private String resourceDescription;
    }
}
