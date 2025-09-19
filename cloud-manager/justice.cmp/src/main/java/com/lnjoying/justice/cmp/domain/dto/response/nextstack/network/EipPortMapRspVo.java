package com.lnjoying.justice.cmp.domain.dto.response.nextstack.network;

import com.lnjoying.justice.cmp.domain.dto.request.nextstack.network.EipPortMapCreateReqVo;
import lombok.Data;

import java.util.List;

@Data
public class EipPortMapRspVo
{
    private String eipMapId;
    private String eipId;
    private String mapName;
    private String userId;
    private String eipAddress;
    private String vpcId;
    private String vpcName;
    private String subnetCidr;
    private String subnetName;
    private String subnetId;
    private String insideIp;
    private List<EipPortMapCreateReqVo.portMap> portMaps;
    private String bandwidth;
    private String instanceName;
    private String instanceId;
    private Boolean vm;
    private String createTime;
    private Boolean oneToOne;
    private Integer phaseStatus;
    private String publicIp;
}
