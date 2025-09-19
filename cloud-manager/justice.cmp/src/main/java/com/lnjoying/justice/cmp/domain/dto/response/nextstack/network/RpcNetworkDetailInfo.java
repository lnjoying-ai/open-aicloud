package com.lnjoying.justice.cmp.domain.dto.response.nextstack.network;

import lombok.Data;

import java.io.Serializable;

@Data
public class RpcNetworkDetailInfo implements Serializable
{
    String vpcId;
    String vpcName;
    String vpcCidr;
    String subnetId;
    String subnetName;
    String subnetCidr;
    String portId;
    String ipAddress;
    Boolean isVip;
    String eipId;
    String eip;
    Integer boundPhaseStatus;
    String boundType;
    String publicIp;
}
