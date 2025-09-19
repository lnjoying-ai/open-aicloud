package com.lnjoying.justice.schema.entity.servicemanager;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TargetServiceInfo
{
    private String serviceId;
    private String regionId;
    private String siteId;
    private String nodeId;
    private long timestamp;
}
