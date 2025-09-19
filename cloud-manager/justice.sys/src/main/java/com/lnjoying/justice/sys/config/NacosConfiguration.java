package com.lnjoying.justice.sys.config;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2022/5/23 17:56
 */

import com.alibaba.nacos.api.annotation.NacosProperties;
import com.alibaba.nacos.spring.context.annotation.config.EnableNacosConfig;
import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySources;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.annotation.Order;

import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;

@Configuration
@PropertySource(value = "classpath:application.properties")
@EnableNacosConfig(globalProperties = @NacosProperties())
@NacosPropertySources({
})
@Order(HIGHEST_PRECEDENCE)
public class NacosConfiguration
{
}
