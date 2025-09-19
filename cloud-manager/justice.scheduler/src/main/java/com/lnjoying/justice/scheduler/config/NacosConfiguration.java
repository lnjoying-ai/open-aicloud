package com.lnjoying.justice.scheduler.config;

import com.alibaba.nacos.spring.context.annotation.config.EnableNacosConfig;
import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource;
import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySources;
import com.lnjoying.justice.commonweb.handler.ConfigBootListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;


@Configuration
@EnableNacosConfig()
@NacosPropertySources({
        @NacosPropertySource(dataId = "com.justice.db.config", groupId = "db", autoRefreshed = true),
        @NacosPropertySource(dataId = "com.justice.redis.config", groupId = "redis", autoRefreshed = true),
})
@Import({ConfigBootListener.class})
public class NacosConfiguration
{

}

