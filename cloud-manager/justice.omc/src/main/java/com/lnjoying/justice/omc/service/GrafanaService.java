package com.lnjoying.justice.omc.service;

import com.lnjoying.justice.omc.domain.dto.rsp.DashboardLinksRsp;
import com.lnjoying.justice.omc.domain.model.DashboardLink;
import com.lnjoying.justice.omc.domain.model.IntegrationTaskTypeEnum;
import com.lnjoying.justice.omc.grafana.client.GrafanaClient;
import com.lnjoying.justice.omc.grafana.model.GrafanaDashboard;
import com.lnjoying.justice.omc.prometheus.client.PrometheusClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/12/12 20:25
 */

@Slf4j
@Service
public class GrafanaService
{

    @Autowired
    private GrafanaClient grafanaClient;


    public DashboardLinksRsp getDashboardLinkList(List<String> type, Integer pageNum, Integer pageSize)
    {
        List<String> tags = new ArrayList<>();
        // default tags
//        tags.add("service_light_node");
        tags.add("omc");
        // user tags
        if (!CollectionUtils.isEmpty(type))
        {
            type.stream().forEach(t -> {
                if (StringUtils.hasText(t))
                {
                    tags.add(t);
                }
            });

        }

        List<GrafanaDashboard> grafanaDashboards = grafanaClient.getDashboards(null, tags);
        if (!CollectionUtils.isEmpty(grafanaDashboards))
        {
            List<DashboardLink> dashboardLinks = grafanaDashboards.stream().map(dashboard ->
            {
                String url = dashboard.getUrl();
                String title = dashboard.getTitle();
                return new DashboardLink(title, url);
            }).collect(Collectors.toList());

            return DashboardLinksRsp.builder().totalNum(dashboardLinks.size()).links(dashboardLinks).build();
        }

        return DashboardLinksRsp.builder().totalNum(0).links(null).build();
    }
}
