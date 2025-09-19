package com.lnjoying.justice.iam.config;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2022/5/23 17:56
 */

import com.alibaba.nacos.spring.context.annotation.config.EnableNacosConfig;
import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource;
import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySources;
import com.lnjoying.justice.commonweb.handler.ConfigBootListener;
import com.lnjoying.justice.iam.config.opa.OpaProperties;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;


@Configuration
@EnableNacosConfig()
@NacosPropertySources({
        @NacosPropertySource(dataId = "com.justice.db.config", groupId = "db", autoRefreshed = true),
        @NacosPropertySource(dataId = "com.justice.redis.config", groupId = "redis", autoRefreshed = true)
})
@Import({ConfigBootListener.class})
public class NacosConfiguration
{
    @Bean
    OpaProperties opaProperties(OpaProperties.ResourceOptions resourceOptions)
    {
        return new OpaProperties(resourceOptions);
    }


    @Bean
    CommonProperties commonProperties()
    {
        return new CommonProperties();
    }


    @Bean
    OpaProperties.ResourceOptions resourceOptions()
    {
        return new OpaProperties.ResourceOptions();
    }
}

