package com.lnjoying.justice.cluster.manager.config;

import com.alibaba.nacos.api.config.ConfigType;
import com.alibaba.nacos.api.config.annotation.NacosConfigurationProperties;
import com.alibaba.nacos.api.config.annotation.NacosProperty;
import com.lnjoying.justice.commonweb.util.CollectionUtils;
import lombok.Data;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * @Description:
 * @Author: Regulus
 * @Date: 1/18/22 6:29 PM
 */
@Data
@NacosConfigurationProperties(dataId = "com.justice.cluster-manager.config", groupId = "cluster-manager", type = ConfigType.YAML, autoRefreshed = true)
public class EnvCfg
{
    @NacosProperty(value = "clusterManager.addonFilePath")
    private String addonFilePath = "/root/down/k8s/addoncfg";

    @NacosProperty(value = "clusterManager.addonFileUrl")
    private String addonFileUrl    = "http://27.221.79.190:9080/k8s/addoncfg/";
}
