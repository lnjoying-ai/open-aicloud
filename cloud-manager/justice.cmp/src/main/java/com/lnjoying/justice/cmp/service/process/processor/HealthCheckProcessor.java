package com.lnjoying.justice.cmp.service.process.processor;

import com.lnjoying.justice.cmp.common.*;
import com.lnjoying.justice.cmp.config.CloudManagerConfig;
import com.lnjoying.justice.cmp.db.model.TblCloudInfo;
import com.lnjoying.justice.cmp.db.repo.CloudRepository;
import com.lnjoying.justice.cmp.domain.model.Authorization;
import com.lnjoying.justice.cmp.domain.model.HealthCheck;
import com.lnjoying.justice.cmp.service.cloud.CloudService;
import com.lnjoying.justice.cmp.utils.CustomErrorHandler;
import com.lnjoying.justice.commonweb.util.JsonUtils;
import com.lnjoying.justice.schema.msg.MessagePack;
import com.micro.core.common.Utils;
import com.micro.core.persistence.redis.RedisUtil;
import com.micro.core.process.processor.AbstractRunnableProcessor;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URL;
import java.util.concurrent.BlockingQueue;

@Component
public class HealthCheckProcessor extends AbstractRunnableProcessor
{
    private static Logger LOGGER = LogManager.getLogger();
    
    @Autowired
    private CloudRepository cloudRepository;

    @Autowired
    private CloudManagerConfig cloudManagerConfig;
    
    @Override
    public void start()
    {
        LOGGER.info("health check processor started");
    }
    
    @Override
    public void stop()
    {
        LOGGER.info("health check processor stopped");
    }
    
    @Override
    public void run()
    {
        BlockingQueue<Object> queue = getBlockQueue();
        while (true)
        {
            try
            {
                MessagePack pack = (MessagePack) queue.take();
                process(pack);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
                LOGGER.error("HealthCheckProcessor.run error {}", e.getMessage());
            }
        }
    }
    
    void process(MessagePack pack)
    {
        String cloudId = (String) pack.getMessageObj();
        TblCloudInfo tblCloudInfo = cloudRepository.getCloud(cloudId);

        if (tblCloudInfo.getStatus() <= CloudStatus.UPDATING.getCode() || tblCloudInfo.getStatus() >= CloudStatus.IMPORT_FILED.getCode())
        {
            RedisUtil.zrem(RedisCache.CLOUD_HEALTH_IDS, cloudId);
            return;
        }

        HealthCheck healthCheck = null;
        boolean helath = false;

        try
        {
            healthCheck = JsonUtils.fromJson(tblCloudInfo.getHealthCheck(), HealthCheck.class);
            if (healthCheck == null)
            {
                return;
            }

            String healthUrl = healthCheck.getHealthUrl();

            RestTemplate restTemplate;
            HttpHeaders requestHeaders = new HttpHeaders();

            switch (tblCloudInfo.getMode())
            {
                case Mode.DIRECT:
                    if (healthUrl.startsWith("https://"))
                    {
                        SSLConnectionSocketFactory scsf = new SSLConnectionSocketFactory(
                                SSLContexts.custom().loadTrustMaterial(null, new TrustSelfSignedStrategy()).build(),
                                NoopHostnameVerifier.INSTANCE);
                        CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(scsf).build();
                        HttpComponentsClientHttpRequestFactory requestFactory =
                                new HttpComponentsClientHttpRequestFactory();
                        requestFactory.setHttpClient(httpClient);
                        restTemplate = new RestTemplate(requestFactory);
                    }
                    else if (healthUrl.startsWith("http://"))
                    {
                        restTemplate = new RestTemplate();
                    }
                    else
                    {
                        restTemplate = new RestTemplate();
                        healthUrl = "http://" + healthUrl;
                    }

                    Authorization authorization = JsonUtils.fromJson(tblCloudInfo.getAuthorization(), Authorization.class);

                    if (tblCloudInfo.getProduct().equalsIgnoreCase(CloudProduct.NEXTSTACK.getName()))
                    {
                        requestHeaders.add("X-Access-Key", authorization.getAccessKey().getId());
                        requestHeaders.add("X-Access-Secret", authorization.getAccessKey().getSecret());
                    }

                    break;
                case Mode.PROXY:
                    restTemplate = new RestTemplate();

                    URL cloudUrl = new URL(healthCheck.getHealthUrl());

                    healthUrl = Utils.buildStr("http://", cloudManagerConfig.getServiceGwUrl(), "/proxy/",
                            CloudProduct.fromName(tblCloudInfo.getProduct()).getShortName(), "/clouds/", cloudId, cloudUrl.getPath());

                    requestHeaders.set("X-Access-Token", Utils.buildStr("systemadmin", cloudManagerConfig.getCloudManagerToken()));
                    requestHeaders.set("Connection", "close");
                    break;
                default:
                    return;
            }

            HttpEntity<String> requestEntity = new HttpEntity<>(null, requestHeaders);
            restTemplate.setErrorHandler(new CustomErrorHandler());
            ResponseEntity<String> response = restTemplate.exchange(healthUrl, HttpMethod.GET, requestEntity, String.class);
            if (! response.getStatusCode().isError())
            {
                RedisUtil.set(RedisCache.CLOUD_HEALTH_STATUS + cloudId, String.valueOf(HealthStatus.HEALTHY.getCode()));
                helath = true;

                CloudService.updateCloudHealthStatus(cloudId, HealthStatus.HEALTHY);
            }
            else
            {
                RedisUtil.set(RedisCache.CLOUD_HEALTH_STATUS + cloudId, String.valueOf(HealthStatus.UNHEALTHY.getCode()));

                CloudService.updateCloudHealthStatus(cloudId, HealthStatus.UNHEALTHY);
            }
        }
        catch (Exception e)
        {
            RedisUtil.set(RedisCache.CLOUD_HEALTH_STATUS + cloudId, String.valueOf(HealthStatus.UNKNOWN.getCode()));
            LOGGER.error("{} health check error, msg {}", cloudId, e.getMessage());
            e.printStackTrace();
        }
        finally
        {
            if (healthCheck == null)
            {
                RedisUtil.set(RedisCache.CLOUD_HEALTH_STATUS + cloudId, String.valueOf(HealthStatus.UNKNOWN.getCode()));
                RedisUtil.zrem(RedisCache.CLOUD_HEALTH_IDS, cloudId);
            }
            else
            {
                double interval = helath ? 2 * 60 * 1000 : 5 * 1000;
                if (healthCheck.getInterval() != null && healthCheck.getInterval() > 0)
                {
                    interval = healthCheck.getInterval();
                }
                RedisUtil.zincrby(RedisCache.CLOUD_HEALTH_IDS, interval, cloudId);
            }
        }
    }
}
