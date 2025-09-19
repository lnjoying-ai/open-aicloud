package com.lnjoying.justice.cmp.domain.dto.response.nextstack.baremetal;

import lombok.Data;

import java.io.Serializable;

@Data
public class RpcInstance implements Serializable
{
    private String instanceId;
    private String instanceName;
    private String portId;
}
