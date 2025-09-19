package com.lnjoying.justice.servicegw.model;

import lombok.Data;

/**
 * @Description:
 * @Author: Regulus
 * @Date: 1/13/23 4:40 PM
 */
@Data
public class ClusterInfo
{
    private String vendor;
    private String clusterType;
    private String clusterId;
    private String token;
    private String resources = "";
}
