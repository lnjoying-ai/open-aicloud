package com.lnjoying.justice.schema.entity.servicemanager;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TargetPort
{
    private String regionId;
    private String siteId;
    private String nodeId;

    private String agentId;

    private String targetIp;
    private Integer targetPort;
}
