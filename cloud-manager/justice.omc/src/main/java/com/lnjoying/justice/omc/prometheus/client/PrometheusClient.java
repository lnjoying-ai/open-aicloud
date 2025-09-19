package com.lnjoying.justice.omc.prometheus.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.lnjoying.justice.commonweb.exception.WebSystemException;
import com.lnjoying.justice.commonweb.util.JacksonUtils;
import com.lnjoying.justice.omc.config.OmcConfig;
import com.lnjoying.justice.omc.util.DateUtils;
import com.lnjoying.justice.schema.common.ErrorLevel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.UriUtils;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.regex.Pattern;

import static com.lnjoying.justice.schema.common.ErrorCode.PROMETHEUS_RELOAD_ERROR;
import static com.lnjoying.justice.schema.common.ErrorCode.PROMETHEUS_RELOAD_FAILED;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/10/26 17:12
 */

@Slf4j
public class PrometheusClient
{
    private static final Pattern ENCODED_PATTERN = Pattern.compile("%[0-9A-Fa-f]{2}");


    private RestTemplate restTemplate;

    private OmcConfig omcConfig;

    public PrometheusClient(OmcConfig omcConfig, RestTemplate restTemplate)
    {
        this.omcConfig = omcConfig;
        this.restTemplate = restTemplate;
    }

    /**
     * if return false, reload failed
     * @return
     */
    public boolean performConfigReload()
    {
        String reloadPath = omcConfig.getPrometheusServerInternalAddress() + "/-/reload";

        try
        {
            HttpEntity<String> request = getHttpEntity();
            String error = restTemplate.postForObject(reloadPath, request, String.class);
            if (StringUtils.hasText(error))
            {
                log.error("reload prometheus config error:{}", error);
                return false;
            }
            else
            {
                return true;
            }
        }
        catch (Exception e)
        {
            log.error("reload prometheus config failed:{}", e);
            return false;
        }

    }

    public String getConfig()
    {
        try
        {
            String configPath = omcConfig.getPrometheusServerInternalAddress() + "/api/v1/status/config";
            HttpEntity<String> request = getHttpEntity();
            ResponseEntity<String> response = restTemplate.exchange(configPath, HttpMethod.GET, request, String.class, new HashMap<>());
            if (response.getStatusCode().is2xxSuccessful())
            {
                String responseBody = response.getBody();
                JsonNode jsonNode = JacksonUtils.readTreeDefault(responseBody);
                String config = jsonNode.path("data").path("yaml").asText();
                return config;
            }
            else
            {
                log.error("get prometheus config failed, status code:{}", response.getStatusCode());
            }
        }
        catch (Exception e)
        {
            log.error("get prometheus config failed:{}", e);
            return "";
        }

        return "";
    }

    public Object getJobs(String prometheusId)
    {
        try
        {
            String jobsPath = omcConfig.getPrometheusServerInternalAddress() + "api/v1/targets?state=active";
            HttpEntity<String> request = getHttpEntity();
            ResponseEntity<String> response = restTemplate.exchange(jobsPath, HttpMethod.GET, request, String.class, new HashMap<>());
            if (response.getStatusCode().is2xxSuccessful())
            {
                String responseBody = response.getBody();
                JsonNode jsonNode = JacksonUtils.readTreeDefault(responseBody);
                JsonNode data = jsonNode.path("data");
                return data;
            }
            else
            {
                log.error("get prometheus targets failed, status code:{}", response.getStatusCode());
            }
        }
        catch (Exception e)
        {
            log.error("get prometheus targets failed:{}", e);
            return null;
        }

        return null;
    }

    public String query(String query,  String time, Integer timeout)
    {
        try
        {
            String path = omcConfig.getPrometheusServerInternalAddress() + "/api/v1/query";
            HttpEntity<String> request = getHttpEntity();

            if (StringUtils.isEmpty(time))
            {
                time = String.valueOf(Instant.now().getEpochSecond());
            }

            if (!isUriEncoded(query))
            {
                query = UriUtils.encode(query, "UTF-8");
            }
            UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(path)
                    .queryParam("time", time)
                    .queryParam("timeout", timeout)
                    .queryParam("query",  query);

            ResponseEntity<String> response = restTemplate.exchange(new URI(uriBuilder.build(false).toUriString()), HttpMethod.GET, request, String.class);
            if (response.getStatusCode().is2xxSuccessful())
            {
                String responseBody = response.getBody();
                return responseBody;
            }
            else
            {
                log.error("get prometheus query data failed, status code:{}", response.getStatusCode());
            }
        }
        catch (Exception e)
        {
            log.error("get prometheus query data failed:{}", e);
            return "";
        }

        return "";
    }

    public String queryRange(String query, String step, String startTime, String endTime, Integer timeout)
    {
        try
        {
            String path = omcConfig.getPrometheusServerInternalAddress() + "/api/v1/query_range";
            HttpEntity<String> request = getHttpEntity();

            if (StringUtils.isEmpty(startTime))
            {
                startTime = String.valueOf(Instant.now().getEpochSecond());
            }

            if (StringUtils.isEmpty(endTime))
            {
                endTime = String.valueOf(Instant.now().getEpochSecond());
            }

            if (StringUtils.isEmpty(step))
            {
                step = "15s";
            }


            UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(path)
                    .queryParam("start", startTime)
                    .queryParam("end", endTime)
                    .queryParam("step", step)
                    .queryParam("timeout", timeout)
                    .queryParam("query", query);

            ResponseEntity<String> response = restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.GET, request, String.class, new HashMap<>());
            if (response.getStatusCode().is2xxSuccessful())
            {
                String responseBody = response.getBody();
                return responseBody;
            }
            else
            {
                log.error("get prometheus query range data failed, status code:{}", response.getStatusCode());
            }
        }
        catch (Exception e)
        {
            log.error("get prometheus query range data failed:{}", e);
            return "";
        }

        return "";
    }


    private HttpEntity<String> getHttpEntity()
    {
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(omcConfig.getPrometheusServerUsername(), omcConfig.getPrometheusServerPassword());
        HttpEntity<String> request = new HttpEntity<>(headers);
        return request;
    }

    private boolean isUriEncoded(String input)
    {
        return ENCODED_PATTERN.matcher(input).find();
    }
}
