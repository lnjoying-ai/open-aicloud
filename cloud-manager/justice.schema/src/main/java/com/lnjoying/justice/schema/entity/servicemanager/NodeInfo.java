package com.lnjoying.justice.schema.entity.servicemanager;

import lombok.Data;

@Data
public class NodeInfo
{
    private String regionId;
    private String siteId;
    private String nodeId;
    private String ip;
    private long timestamp;
}
