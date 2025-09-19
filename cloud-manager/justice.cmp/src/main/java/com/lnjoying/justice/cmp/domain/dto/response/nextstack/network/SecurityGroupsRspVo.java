package com.lnjoying.justice.cmp.domain.dto.response.nextstack.network;

import lombok.Data;

import java.util.List;

@Data
public class SecurityGroupsRspVo
{
    private long totalNum;
    private List<SecurityGroupRspVo> securityGroups;
}
