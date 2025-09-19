package com.lnjoying.justice.schema.entity.servicemanager;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
public class RpcAddServicePortReq
{
    private String microServiceName;
    private String reqId;

    //用途 service_user\inner_monitor
    private String purpose;

    //目标类型 container\compose\cloud\k8s\edge
    private String targetType = "edge";

    //目标id containerSpecId\composeSpecId\cloudId\clusterId\nodeId
    private String deployment;

    private List<TargetService> targetServices = new ArrayList<>();

    private List<String> tags;

    @Data
    @NoArgsConstructor
    public static class TargetService
    {
        //端口协议 tcp\http\https
        private String protocol = "http";

        //目标服务id containerId\composeId\null\null\null
        private String service;

        private String targetIp;

        private Integer targetPort;

        private String cert;

        public TargetService(Integer targetPort)
        {
            this.targetPort = targetPort;
        }
    }

    public void addTargetPort(Integer port)
    {
        targetServices.add(new TargetService(port));
    }

    public void addTargetPorts(Integer... ports)
    {
        for (Integer port : ports)
        {
            addTargetPort(port);
        }
    }

    public void addTargetPorts(List<Integer> ports)
    {
        for (Integer port : ports)
        {
            addTargetPort(port);
        }
    }
}
