package com.lnjoying.justice.cmp.domain.dto.response.nextstack.network;

import lombok.Data;

import java.util.List;

@Data
public class GetSecurityGroupDetailInfoRsp
{
    private String name;
    private String sgId;
    private String description;
    private Integer phaseStatus;
    private List<RpcSecurityGroupRule> rules;
    private List<VmInstance> vmInstances;

    private String eeBp;
    private String eeUser;
    private String eeBpName;
    private String eeUserName;

    @Data
    public  static final class VmInstance extends InstanceCommonInfo
    {
        String ip;
    }
}
