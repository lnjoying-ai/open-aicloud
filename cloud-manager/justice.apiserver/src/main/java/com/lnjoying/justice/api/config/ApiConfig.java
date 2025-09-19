package com.lnjoying.justice.api.config;

import com.alibaba.nacos.api.config.ConfigType;
import com.alibaba.nacos.api.config.annotation.NacosConfigurationProperties;
import com.alibaba.nacos.api.config.annotation.NacosProperty;
import com.lnjoying.justice.api.handler.ApiYamlPropertySourceFactory;
import com.lnjoying.justice.commonweb.handler.YamlPropertySourceFactory;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.Map;

@Data
@NacosConfigurationProperties(dataId = "com.justice.api.config.notify", groupId = "api", type = ConfigType.YAML, autoRefreshed = true)
public class ApiConfig
{
    Map<String, NotifyTemplate> notifyTemplateCode;

    Map<String, String> notifyTemplate;
}
