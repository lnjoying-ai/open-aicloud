package com.lnjoying.justice.aos.config;

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
@NacosConfigurationProperties(dataId = "com.justice.aos.config.frp", groupId = "aos", autoRefreshed = true)
public class AosFrpConfig
{
    //@NacosValue(value = "tty.proxy_server_url")
    @NacosProperty(value = "proxy_server_url")
    private String proxyServerUrl;

    //@NacosValue(value = "tty.frps_server_port")
    @NacosProperty(value = "frps_server_port")
    private String frpsServerPort;

    //@NacosValue(value = "tty.frps_server_addr")
    @NacosProperty(value = "frps_server_addr")
    private String frpsServerAddr;

    //@NacosValue(value = "tty.frp_addr")
    @NacosProperty(value = "frp_addr")
    private String frpAddr;
}

