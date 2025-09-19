package com.lnjoying.justice.schema.entity.servicemanager;

import lombok.Data;

@Data
public class PortStatus
{
    private String servicePortId;
    private String targetType;
    private String deployment;

    private String targetServiceId;
    private String service;

    private Integer targetPort;

    private String internalIp;
    private String externalIp;
    private Integer port;
    private Integer listenPort;

    private String status;
}
