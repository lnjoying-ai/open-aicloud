package com.lnjoying.justice.iam.config.opa;

import com.lnjoying.justice.iam.authz.opa.client.OpaClient;
import com.lnjoying.justice.iam.authz.opa.pep.OpaEnforcer;
import com.lnjoying.justice.iam.authz.opa.pep.pathMatcher.SwaggerRequestMapping;
import com.lnjoying.justice.iam.domain.model.ActionsCache;
import com.lnjoying.justice.iam.domain.model.ServiceResourceEnCnNameCache;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustAllStrategy;
import org.apache.http.impl.client.*;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.util.concurrent.TimeUnit;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/2/23 19:13
 */

@Configuration
public class OpaConfiguration
{

    @Bean
    public OpaClient opaClient(OpaProperties opaProperties, RestTemplate restTemplate)
    {
        return OpaClient.builder().endPointUrl(opaProperties.getUrl()).allowPath(opaProperties.getQueryPath())
                .restTemplate(restTemplate).build();
    }

    @Bean
    public OpaEnforcer opaEnforcer(OpaClient opaClient, OpaProperties opaProperties)
    {
        return new OpaEnforcer(opaClient.getOpaQueryApi(), opaProperties.getQueryPath(), opaProperties.getAdHocQueryPath());
    }

    @Bean
    public SwaggerRequestMapping swaggerRequestMapping()
    {
        return new SwaggerRequestMapping();
    }

    @Bean
    public ActionsCache actionsCache()
    {
        return new ActionsCache();
    }

    @Bean
    public ServiceResourceEnCnNameCache serviceResourceEnCnNameCache()
    {
        return new ServiceResourceEnCnNameCache();
    }

    private ClientHttpRequestFactory getHttpRequestFactory()
    {
        SSLContext sslContext = null;
        try
        {
            sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustAllStrategy()).build();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


        SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext,
                new String[]{"TLSv1.2"},
                null,
                NoopHostnameVerifier.INSTANCE);

        // 长连接保持30秒
        Registry registry = RegistryBuilder.create().register("http", PlainConnectionSocketFactory.getSocketFactory()).register("https", csf).build();
        PoolingHttpClientConnectionManager pollingConnectionManager = new PoolingHttpClientConnectionManager(registry, null, null, null, 30, TimeUnit.SECONDS);
        // 总连接数
        pollingConnectionManager.setMaxTotal(1000);
        // 同路由的并发数
        pollingConnectionManager.setDefaultMaxPerRoute(1000);

        HttpClientBuilder httpClientBuilder = HttpClients.custom();
        httpClientBuilder.setConnectionManager(pollingConnectionManager);
        // 重试次数，默认是3次，没有开启
        httpClientBuilder.setRetryHandler(new DefaultHttpRequestRetryHandler(2, true));
        // 保持长连接配置，需要在头添加Keep-Alive
        httpClientBuilder.setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy());
        httpClientBuilder.disableCookieManagement();
        httpClientBuilder.setSSLSocketFactory(csf);
        CloseableHttpClient httpClient = httpClientBuilder.build();

        HttpComponentsClientHttpRequestFactory requestFactory =
                new HttpComponentsClientHttpRequestFactory();
        System.setProperty("jsse.enableSNIExtension", "false");
        requestFactory.setHttpClient(httpClient);
        return requestFactory;
    }

}
