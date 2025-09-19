package com.lnjoying.justice.cmp.domain.dto.response.nextstack.network;

import lombok.Data;

import java.io.Serializable;

@Data
public class RpcTenantNetworkPort implements Serializable
{
    private String portId;
    private Integer phaseStatus;
    private String macAddress;
    private String ipAddress;
    private String ofport;
    private String vpcCidr;
    private String subnetCidr;
    private String vlanId;
    private String subnetName;
    private String vpcName;
}
