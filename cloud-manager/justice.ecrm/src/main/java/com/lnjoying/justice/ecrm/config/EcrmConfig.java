package com.lnjoying.justice.ecrm.config;

import com.alibaba.nacos.api.config.ConfigType;
import com.alibaba.nacos.api.config.annotation.NacosConfigurationProperties;
import com.alibaba.nacos.api.config.annotation.NacosProperty;
import com.alibaba.nacos.api.config.annotation.NacosValue;
import com.lnjoying.justice.commonweb.handler.YamlPropertySourceFactory;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2022/5/27 10:43
 */

@Data
@NacosConfigurationProperties(dataId = "com.justice.ecrm.config", groupId = "ecrm", type = ConfigType.YAML, autoRefreshed = true)
public class EcrmConfig
{
    @NacosProperty(value = "install.installScriptUrl")
    private String installScriptUrl;

    @NacosProperty(value = "install.cloudAddr")
    private String cloudAddr;

    @NacosProperty(value = "install.gw.localPort")
    private String gwLocalPort;

    @NacosProperty(value = "install.gw.restPort")
    private String gwRestPort;

    @NacosProperty(value = "install.gw.image")
    private String gwImage;

    @NacosProperty(value = "install.agent.restPort")
    private String agentRestPort;

    @NacosProperty(value = "install.agent.image")
    private String agentImage;

    @NacosProperty(value = "version.agentVersions")
    private String agentVersions;

    @NacosProperty(value = "service_agent.image")
    private String serviceAgentImage;
}

