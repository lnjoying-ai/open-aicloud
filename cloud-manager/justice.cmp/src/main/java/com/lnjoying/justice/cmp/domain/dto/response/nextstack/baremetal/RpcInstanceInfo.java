package com.lnjoying.justice.cmp.domain.dto.response.nextstack.baremetal;

import lombok.Data;

import java.io.Serializable;

@Data
public class RpcInstanceInfo implements Serializable
{
    private String instanceId;
    private String name;
    private String portId;
    private String ip;
    private boolean isVm;
}
