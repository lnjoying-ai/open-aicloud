package com.lnjoying.justice.cmp.domain.dto.request.nextstack.network;

import lombok.Data;

import java.io.Serializable;

@Data
public class NetworkDetailInfoReq implements Serializable
{
    String vpcId;
    String subnetId;
    String portId;
    String instanceId;
}
