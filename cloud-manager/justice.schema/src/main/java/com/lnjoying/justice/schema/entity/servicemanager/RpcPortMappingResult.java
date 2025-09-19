package com.lnjoying.justice.schema.entity.servicemanager;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class RpcPortMappingResult
{
    private String reqId;

    private String servicePortId;

    private List<TargetService> targetServices = new ArrayList<>();

    @Data
    public static class TargetService
    {
        private String targetServiceId;

        private String protocol;

        private String service;

        private String targetIp;

        private Integer targetPort;

        private String endpoint;
    }
}
