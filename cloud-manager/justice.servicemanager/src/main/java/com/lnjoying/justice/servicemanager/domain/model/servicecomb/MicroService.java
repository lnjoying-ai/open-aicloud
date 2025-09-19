package com.lnjoying.justice.servicemanager.domain.model.servicecomb;

import lombok.Data;

@Data
public class MicroService
{
    private String serviceId;
    private String appId;
    private String serviceName;
    private String version;
    private String level;
    private String status;
    private String timestamp;
    private String modTimestamp;
    private String environment;
    private String registerBy;
    private Object framework;
}
