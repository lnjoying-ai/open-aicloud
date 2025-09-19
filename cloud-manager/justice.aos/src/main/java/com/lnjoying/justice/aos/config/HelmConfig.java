package com.lnjoying.justice.aos.config;

import com.alibaba.nacos.api.config.annotation.NacosConfigurationProperties;
import com.alibaba.nacos.api.config.annotation.NacosProperty;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2022/8/6 20:07
 */

@Data
@NacosConfigurationProperties(dataId = "com.justice.aos.config.helm", groupId = "aos", autoRefreshed = true)
public class HelmConfig
{

    @NacosProperty(value = "helm_cache_home")
    private String helmCacheHome;


    @NacosProperty(value = "helm_config_home")
    private String helmConfigHome;


    @NacosProperty(value = "helm_data_home")
    private String helmDataHome;

    @NacosProperty(value = "helm_chart_tar_home")
    private String helmChartTarHome;

    @NacosProperty(value = "helm_push")
    private boolean  helmPush;
}
