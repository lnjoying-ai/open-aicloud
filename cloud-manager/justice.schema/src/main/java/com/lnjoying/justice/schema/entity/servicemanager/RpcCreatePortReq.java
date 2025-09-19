package com.lnjoying.justice.schema.entity.servicemanager;

import lombok.Data;

import java.util.List;

@Data
public class RpcCreatePortReq
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

    private String cert;

    private String protocol;

    List<TargetPort> targets;
}
