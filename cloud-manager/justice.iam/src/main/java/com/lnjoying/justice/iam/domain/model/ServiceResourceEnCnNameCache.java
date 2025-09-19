package com.lnjoying.justice.iam.domain.model;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * 功能描述
 *
 * @author: Robin
 * @date: 2023年11月14日 11:23
 */
public class ServiceResourceEnCnNameCache
{
    private final HashMap<String, ServiceEnCnNameInfo> serviceMap = new HashMap<>();

    public void create(Map<String, ActionsCache.ServiceAction> map)
    {
        if (map == null)
        {
            return;
        }

        Set<Map.Entry<String, ActionsCache.ServiceAction>> entries = map.entrySet();
        for (Map.Entry<String, ActionsCache.ServiceAction> entry : entries)
        {
            ActionsCache.ServiceAction serviceAction = entry.getValue();
            if (!serviceMap.containsKey(serviceAction.getServiceName()))
            {
                ServiceEnCnNameInfo serviceEnCnNameInfo = new ServiceEnCnNameInfo();
                serviceEnCnNameInfo.setResources(new HashMap<>());
                serviceMap.put(serviceAction.getServiceName(), serviceEnCnNameInfo);
                serviceEnCnNameInfo.setServiceEnName(serviceAction.getServiceName());
                if (serviceAction != null)
                {
                    serviceEnCnNameInfo.setServiceCnName(serviceAction.getServiceDisplayName());
                }
            }
            ServiceEnCnNameInfo serviceEnCnNameInfo = serviceMap.get(serviceAction.getServiceName());
            if (!serviceEnCnNameInfo.getResources().containsKey(serviceAction.getResourceName()))
            {
                ResourceEnCnNameInfo resourceEnCnNameInfo = new ResourceEnCnNameInfo();
                resourceEnCnNameInfo.setResourceEnName(serviceAction.getResourceName());
                resourceEnCnNameInfo.setResourceCnName(serviceAction.getResourceDescription());
                serviceEnCnNameInfo.getResources().put(serviceAction.getResourceName(), resourceEnCnNameInfo);
            }
        }

    }

    public Optional<ServiceResourceCnNameQueryResult> queryServiceResourceCnName(String serviceEnName, String resourceEnName)
    {

        if (!serviceMap.containsKey(serviceEnName))
        {
            return Optional.empty();
        }

        ServiceEnCnNameInfo serviceEnCnNameInfo = serviceMap.get(serviceEnName);
        ServiceResourceCnNameQueryResult queryResult = new ServiceResourceCnNameQueryResult();
        queryResult.setServiceCnName(serviceEnCnNameInfo.getServiceCnName());

        ResourceEnCnNameInfo resourceEnCnNameInfo = serviceEnCnNameInfo.getResources().get(resourceEnName);
        if (resourceEnCnNameInfo == null)
        {
            return Optional.of(queryResult);
        }
        queryResult.setResourceCnName(resourceEnCnNameInfo.getResourceCnName());
        return Optional.of(queryResult);
    }

    @Data
    public static final class ServiceEnCnNameInfo
    {
        private String                                serviceEnName;
        private String                                serviceCnName;
        private HashMap<String, ResourceEnCnNameInfo> resources = new HashMap<>();
    }

    @Data
    public static final class ResourceEnCnNameInfo
    {
        private String resourceEnName;
        private String resourceCnName;
    }

    @Data
    public static final class ServiceResourceCnNameQueryResult
    {
        private String serviceCnName  = "";
        private String resourceCnName = "";
    }
}
