package com.lnjoying.justice.omc.config;

import com.lnjoying.justice.omc.grafana.client.GrafanaClient;
import com.lnjoying.justice.omc.prometheus.client.PrometheusClient;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/11/3 17:10
 */

@Slf4j
@Configuration
public class OmcConfiguration
{

    @Bean
    public RestTemplate customRestTemplate() {
        return new RestTemplate(generateHttpsRequestFactory());
    }


    @Bean
    public PrometheusClient prometheusClient(OmcConfig omcConfig, RestTemplate customRestTemplate)
    {
        return new PrometheusClient(omcConfig, customRestTemplate);
    }


    @Bean
    public GrafanaClient grafanaClient(OmcConfig omcConfig, RestTemplate customRestTemplate)
    {
        return new GrafanaClient(omcConfig, customRestTemplate);
    }


    public HttpComponentsClientHttpRequestFactory generateHttpsRequestFactory() {
        try {
            TrustStrategy acceptingTrustStrategy = (x509Certificates, authType) -> true;
            SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy).build();
            SSLConnectionSocketFactory connectionSocketFactory =
                    new SSLConnectionSocketFactory(sslContext, new NoopHostnameVerifier());

            HttpClientBuilder httpClientBuilder = HttpClients.custom();
            httpClientBuilder.setSSLSocketFactory(connectionSocketFactory);
            CloseableHttpClient httpClient = httpClientBuilder.build();
            HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
            factory.setHttpClient(httpClient);
            factory.setConnectTimeout(10 * 1000);
            factory.setReadTimeout(30 * 1000);
            return factory;
        } catch (Exception e) {
            log.error("failed to create https rest template", e);
            throw new RuntimeException("failed to create https rest template", e);
        }

    }

}
