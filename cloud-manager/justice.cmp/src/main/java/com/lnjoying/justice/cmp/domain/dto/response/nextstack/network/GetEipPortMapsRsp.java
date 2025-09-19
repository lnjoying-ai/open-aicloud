package com.lnjoying.justice.cmp.domain.dto.response.nextstack.network;

import lombok.Data;

import java.util.List;

@Data
public class GetEipPortMapsRsp
{
    private long totalNum;
    private List<EipPortMapRspVo> eipPortMaps;
}
