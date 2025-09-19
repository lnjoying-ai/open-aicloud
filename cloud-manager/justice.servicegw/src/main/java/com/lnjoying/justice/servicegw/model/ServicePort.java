package com.lnjoying.justice.servicegw.model;

import com.lnjoying.justice.schema.entity.servicemanager.RpcCreatePortReq;
import com.lnjoying.justice.schema.entity.servicemanager.TargetPort;
import com.micro.core.nework.tcp.TcpAcceptor;
import lombok.Data;

import java.util.List;

@Data
public class ServicePort
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

    private long timestamp;

    public static ServicePort of(RpcCreatePortReq rpcCreatePortReq)
    {
        ServicePort servicePort = new ServicePort();
        servicePort.setServicePortId(rpcCreatePortReq.getServicePortId());
        servicePort.setTargetType(rpcCreatePortReq.getTargetType());
        servicePort.setDeployment(rpcCreatePortReq.getDeployment());
        servicePort.setService(rpcCreatePortReq.getService());
        servicePort.setTargetPort(rpcCreatePortReq.getTargetPort());
        servicePort.setTargetServiceId(rpcCreatePortReq.getTargetServiceId());
        servicePort.setInternalIp(rpcCreatePortReq.getInternalIp());
        servicePort.setExternalIp(rpcCreatePortReq.getExternalIp());
        servicePort.setPort(rpcCreatePortReq.getPort());
        servicePort.setListenPort(rpcCreatePortReq.getListenPort() == null ? rpcCreatePortReq.getPort() : rpcCreatePortReq.getListenPort());
        servicePort.setTargets(rpcCreatePortReq.getTargets());
        servicePort.setCert(rpcCreatePortReq.getCert());
        servicePort.setProtocol(rpcCreatePortReq.getProtocol());
        return servicePort;
    }
}
