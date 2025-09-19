package com.lnjoying.justice.cmp.domain.dto.response.nextstack.vm;

import lombok.Data;

@Data
public class HypervisorNodeVmsInfo
{
    String nodeId;

    Integer vmCount;

    String manageIp;
}
