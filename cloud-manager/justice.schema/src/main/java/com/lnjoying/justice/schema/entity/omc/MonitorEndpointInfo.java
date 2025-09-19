package com.lnjoying.justice.schema.entity.omc;

import lombok.Data;
import lombok.ToString;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/12/15 16:15
 */

@Data
@ToString
public class MonitorEndpointInfo
{
    private String uniqueId;

    private String bpId;

    private String userId;

    private String siteId;

    private String nodeId;

    /**
     * eg: node | cadvisor
     */
    private String type;

    private String endpoint;
}
