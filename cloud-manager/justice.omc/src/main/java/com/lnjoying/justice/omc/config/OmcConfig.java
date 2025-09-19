package com.lnjoying.justice.omc.config;

import com.alibaba.nacos.api.config.annotation.NacosConfigurationProperties;
import com.alibaba.nacos.api.config.annotation.NacosProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2022/5/27 10:43
 */

@Data
@NacosConfigurationProperties(dataId = "com.justice.omc.config", groupId = "omc", autoRefreshed = true)
@NoArgsConstructor
public class OmcConfig
{

    @NacosProperty(value = "prometheus.setting.rule.path")
    private String ruleFilePath;


    @NacosProperty(value = "prometheus.server.internal.address")
    private String prometheusServerInternalAddress;

    @NacosProperty(value = "prometheus.server.external.address")
    private String prometheusServerExternalAddress;

    @NacosProperty(value = "prometheus.server.username")
    private String prometheusServerUsername;


    @NacosProperty(value = "prometheus.server.password")
    private String prometheusServerPassword;

    @NacosProperty(value = "monitor.admin.username")
    private String monitorAdminUsername;


    @NacosProperty(value = "monitor.admin.password")
    private String monitorAdminPassword;


    @NacosProperty(value = "grafana.address")
    private String grafanaAddress;

    @NacosProperty(value = "send.sms")
    private String sendSms;

    @NacosProperty(value = "send.email")
    private String sendEmail;

}

