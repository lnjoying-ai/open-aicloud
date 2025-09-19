package com.lnjoying.justice.servicemanager.domain.model.servicecomb;

import lombok.Data;

import java.util.List;

@Data
public class Instance
{
    private String instanceId;
    private String serviceId;
    private List<String> endpoints;
    private String hostName;
    private String status;
    private Object healthCheck;
    private String timestamp;
    private String modTimestamp;
    private String version;
}
