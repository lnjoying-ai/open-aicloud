package com.lnjoying.justice.cmp.config;

import com.alibaba.nacos.api.config.ConfigType;
import com.alibaba.nacos.api.config.annotation.NacosConfigurationProperties;
import com.alibaba.nacos.api.config.annotation.NacosProperty;
import lombok.Data;

@Data
@NacosConfigurationProperties(dataId = "com.justice.cmp.config", groupId = "cmp", type = ConfigType.YAML, autoRefreshed = true)
public class CloudManagerConfig
{
    @NacosProperty(value = "cloud_server.url")
    private String cloudServerUrl;
    
    @NacosProperty(value = "service_gw.url")
    private String serviceGwUrl;

    @NacosProperty(value = "cloud_agent.image")
    private String cloudAgentImage;

    @NacosProperty(value = "cloud_server.token")
    private String cloudManagerToken;

    @NacosProperty(value = "expire_policy")
    private String expirePolicy;
}
