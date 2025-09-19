package com.lnjoying.justice.cmp.domain.dto.response.nextstack.network;

import lombok.Data;

import java.io.Serializable;

@Data
public class InstanceCommonInfo implements Serializable
{
    String instanceId;
    String name;
    Integer phaseStatus;
    String flavorName;
}
