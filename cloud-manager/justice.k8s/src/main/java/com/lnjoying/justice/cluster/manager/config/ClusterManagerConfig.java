package com.lnjoying.justice.cluster.manager.config;

import com.alibaba.nacos.api.config.ConfigType;
import com.alibaba.nacos.api.config.annotation.NacosConfigurationProperties;
import com.alibaba.nacos.api.config.annotation.NacosProperty;
import lombok.Data;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2022/6/18 17:01
 */

@Data
@NacosConfigurationProperties(dataId = "com.justice.cluster-manager.config", groupId = "cluster-manager", type = ConfigType.YAML, autoRefreshed = true)
public class ClusterManagerConfig
{
    @NacosProperty(value = "clusterServer.url")
    private String clusterServerUrl;
    
    
    @NacosProperty(value = "clusterServer.inner")
    private String clusterServerInner;

    @NacosProperty(value = "cluster.registry-url")
    private String clusterComponentRegistryUrl;
}
