package com.lnjoying.justice.sys.config;

import com.alibaba.nacos.api.config.annotation.NacosConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.PropertySource;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2022/5/28 12:46
 */
@DependsOn("nacosListener")
@NacosConfigurationProperties(dataId = "com.justice.mongo.config", groupId = "mongo", autoRefreshed = true)
public class MongoConfig
{
}
