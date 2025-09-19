package com.lnjoying.justice.sys.config;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2022/5/23 17:56
 */

import com.alibaba.nacos.spring.context.annotation.config.EnableNacosConfig;
import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource;
import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySources;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.annotation.Order;

import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;


@Configuration
@EnableNacosConfig()
@NacosPropertySources({
        @NacosPropertySource(dataId = "com.justice.lic", groupId = "sys", autoRefreshed = true)
})
@DependsOn("nacosListener")
@Order(HIGHEST_PRECEDENCE)
public class NacosBusinessConfiguration
{
    @Bean
    LicenseConfig licenseConfig()
    {
        return new LicenseConfig();
    }
}
