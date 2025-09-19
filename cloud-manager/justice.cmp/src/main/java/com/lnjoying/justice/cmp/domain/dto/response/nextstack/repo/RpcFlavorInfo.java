package com.lnjoying.justice.cmp.domain.dto.response.nextstack.repo;

import lombok.Data;

import java.io.Serializable;

@Data
public class RpcFlavorInfo implements Serializable
{
    protected Integer cpu;

    protected Integer mem;

    protected Integer type;

    protected String flavorId;

    protected Integer gpuCount;

    protected String gpuName;

    protected String name;

    protected Boolean needIb;
}