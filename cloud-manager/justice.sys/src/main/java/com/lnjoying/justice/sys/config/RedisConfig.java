package com.lnjoying.justice.sys.config;

import com.alibaba.nacos.api.config.annotation.NacosConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.PropertySource;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2022/5/28 15:25
 */
@DependsOn("nacosListener")
@NacosConfigurationProperties(dataId = "com.justice.redis.config", groupId = "redis", autoRefreshed = true)
public class RedisConfig extends com.micro.core.config.RedisConfig
{
}
