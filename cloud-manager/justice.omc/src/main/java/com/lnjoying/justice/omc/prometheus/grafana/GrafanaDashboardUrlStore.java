package com.lnjoying.justice.omc.prometheus.grafana;

import com.lnjoying.justice.omc.domain.model.DashboardLink;
import com.lnjoying.justice.omc.domain.model.IntegrationTaskTypeEnum;
import com.lnjoying.justice.omc.prometheus.util.GrafanaUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static com.lnjoying.justice.omc.domain.model.IntegrationTaskTypeEnum.LIGHTWEIGHT_NODE_DEPLOYMENT_TASK;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/11/7 17:01
 */

@Component
public class GrafanaDashboardUrlStore
{

    public Map<String, DashboardLink> dashboardLinkMap = new ConcurrentHashMap<>();


    public Collection<DashboardLink> getDashboardLinks(Integer taskType)
    {
        if (Objects.isNull(taskType))
        {
            return dashboardLinkMap.values();
        }

        IntegrationTaskTypeEnum integrationTaskTypeEnum = IntegrationTaskTypeEnum.fromValue(taskType);
        return dashboardLinkMap.entrySet().stream().filter(entry ->  entry.getKey().equalsIgnoreCase(integrationTaskTypeEnum.name()))
                .map(entry -> entry.getValue()).collect(Collectors.toList());
    }

    public void addDashboardLink(String key, DashboardLink link) {
        dashboardLinkMap.put(key, link);
    }

    public void addDashboardLinks(Map<String, DashboardLink> linksToAdd) {
        dashboardLinkMap.putAll(linksToAdd);
    }

    @PostConstruct
    public  Map<String, DashboardLink> initDashboardLinkMap()
    {

        DashboardLink nodeDashboardLink = new DashboardLink();
        nodeDashboardLink.setName("node");
        // todo
        //dashboardLink.setLink(GrafanaUtils.assembleGrafanaDashboardUrl(null));
        nodeDashboardLink.setLink("http://localhost:13000/d/a3bb6fb8-fa14-46db-84cc-3f4996286784/lnjoying-node-exporter?orgId=1&from=1700214439507&to=1700218039507");
        dashboardLinkMap.put("node", nodeDashboardLink);

        DashboardLink cAdvisorDashboardLink = new DashboardLink();
        cAdvisorDashboardLink.setName("cAdvisor");
        nodeDashboardLink.setLink("http://localhost:13000/d/fc4612a9-d34d-46fd-82de-73966992f71f/lnjoying-cadvisor-exporter?orgId=1&refresh=5s&from=1700216904573&to=1700217804573");
        dashboardLinkMap.put("node", nodeDashboardLink);
        return dashboardLinkMap;
    }
}
