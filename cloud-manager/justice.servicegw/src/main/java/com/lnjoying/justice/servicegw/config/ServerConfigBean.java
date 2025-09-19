package com.lnjoying.justice.servicegw.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @Description:
 * @Author: Regulus
 * @Date: 2/28/22 5:38 PM
 */
@Data
@Configuration
@PropertySource(value = "classpath:application.properties", encoding="utf-8")
public class ServerConfigBean
{
    private String svrRootCrt;
    private String svrCrt;
    private String svrKey;
    @Value("${serviceGateway.svrPort}")
    private int svrPort;
    @Value("${serviceGateway.cloudSvrPort}")
    private int cloudsvrPort;
    @Value("${servicePort.cacheTime}")
    private int servicePortCacheTime;
    @Value("${servicePort.nodeInfo.cacheTime}")
    private int nodeInfoCacheTime;
    @Value("${serviceGateway.domainSock}")
    private String serviceGatewaySock;
}
