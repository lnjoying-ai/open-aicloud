package com.lnjoying.justice.omc.handler;


import com.alibaba.nacos.api.annotation.NacosInjected;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.ConfigType;
import com.alibaba.nacos.api.config.annotation.NacosConfigListener;
import com.lnjoying.justice.commonweb.util.NacosConfigLoaderUtils;
import com.lnjoying.justice.omc.config.OmcConfig;
import com.lnjoying.justice.omc.service.PrometheusService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import javax.annotation.PostConstruct;
import java.util.concurrent.*;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2022/6/1 11:20
 */

@Slf4j
@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
public class NacosListener
{

    @NacosInjected
    private ConfigService configService;

    @Autowired
    private PrometheusService prometheusService;

    private Executor executor = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(),
            new ThreadFactory() {
                @Override
                public Thread newThread(Runnable r) {
                    Thread thread = new Thread(r);
                    thread.setName("omc-config-update-thread");
                    return thread;
                }
            });

    @PostConstruct
    public void publishConfig() {
        NacosConfigLoaderUtils.doPublish(configService,"prometheus-agent", "system-monitor-prometheus-agent", ConfigType.YAML.getType());
    }

    @NacosConfigListener(dataId = "com.justice.omc.config", groupId = "omc", type = ConfigType.YAML, converter = OmcConfigConverter.class)
    public void onChange(OmcConfig omcConfig) throws Exception {
        log.info("omc config: {}", omcConfig);
        executor.execute(() ->  prometheusService.updatePrometheusServer(omcConfig));
    }
}
