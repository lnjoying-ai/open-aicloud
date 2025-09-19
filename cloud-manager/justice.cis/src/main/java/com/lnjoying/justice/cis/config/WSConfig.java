package com.lnjoying.justice.cis.config;

import com.alibaba.nacos.api.config.annotation.NacosConfigurationProperties;
import com.alibaba.nacos.api.config.annotation.NacosProperty;
import com.lnjoying.justice.commonweb.handler.YamlPropertySourceFactory;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Data
//@Configuration("wsConfig")
@NacosConfigurationProperties(dataId = "com.justice.cis.config", groupId = "cis", autoRefreshed = true)
public class WSConfig
{
    int port = 7867;

    String urlPrefix;
}
