package com.lnjoying.justice.cluster.manager.domain.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: Regulus
 * @Date: 12/8/21 8:45 PM
 * @Description:
 */
@Data
public class NetworkOption implements Serializable
{
    private String                                      clusterDomain;
    private String                                   clusterDnsServer;
    private String                                        clusterCidr;
}
