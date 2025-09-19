package com.lnjoying.justice.cmp.domain.dto.response.nextstack.network;

import com.lnjoying.justice.cmp.domain.dto.response.nextstack.baremetal.RpcInstanceInfo;
import lombok.Data;

import java.util.List;

@Data
public class TopologyRspVo
{
    private String vpcId;
    private List<SubnetTopology> subnetTopologies;

    @Data
    public static class SubnetTopology{
        String subnetId;
        String subnetName;
        String cidr;
        List<RpcInstanceInfo> instanceInfos;
    }
}
