package com.lnjoying.justice.cmp.domain.dto.response.nextstack.baremetal;

import com.lnjoying.justice.cmp.domain.dto.response.nextstack.network.InstanceCommonInfo;
import lombok.Data;

import java.io.Serializable;

@Data
public class RpcInstanceDetailInfo extends InstanceCommonInfo implements Serializable
{
    private String portId;
    private String flavorId;
}
