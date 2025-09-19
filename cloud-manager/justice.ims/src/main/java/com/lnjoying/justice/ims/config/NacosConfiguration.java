package com.lnjoying.justice.ims.config;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2022/5/23 17:56
 */

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
})
@Import({ConfigBootListener.class})
public class NacosConfiguration
{
    @Bean
    public ImsConfig imsConfig()
    {
        return new ImsConfig();
    }
}

