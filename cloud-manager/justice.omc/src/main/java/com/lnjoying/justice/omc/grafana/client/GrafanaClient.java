package com.lnjoying.justice.omc.grafana.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.lnjoying.justice.commonweb.util.JacksonUtils;
import com.lnjoying.justice.omc.config.OmcConfig;
import com.lnjoying.justice.omc.grafana.model.GrafanaDashboard;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.List;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/12/12 19:13
 */

@Slf4j
public class GrafanaClient
{
    private RestTemplate restTemplate;

    private OmcConfig omcConfig;


    public GrafanaClient(OmcConfig omcConfig, RestTemplate restTemplate)
    {
        this.omcConfig = omcConfig;
        this.restTemplate = restTemplate;
    }


    public List<GrafanaDashboard> getDashboards(String query, List<String> tags)
    {
        String dashboardPath = omcConfig.getGrafanaAddress() + "/api/search";
        try
        {
            HttpHeaders headers = new HttpHeaders();
            headers.setBasicAuth(omcConfig.getMonitorAdminUsername(), omcConfig.getMonitorAdminPassword());
            HttpEntity<String> requestEntity = new HttpEntity<>(headers);

            UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(dashboardPath)
                    .queryParam("limit", 20)
                    .queryParam("type", "dash-db")
                    .queryParam("query", query)
                    .queryParam("tag", tags);

            ResponseEntity<String> response = restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.GET, requestEntity, String.class, new HashMap<>());

            if (response.getStatusCode().is2xxSuccessful()) {
                String responseBody = response.getBody();
                List<GrafanaDashboard> grafanaDashboards = JacksonUtils.strToObjType(responseBody, new TypeReference<List<GrafanaDashboard>>()
                {
                });

                return grafanaDashboards;
            } else {
                log.error("get dashboards failed, status code:{}, error:{}", response.getStatusCode(), response.getBody());
                return null;
            }

        }
        catch (Exception e)
        {
            log.error("get dashboards  failed:{}", e);
            return null;
        }

    }
}
