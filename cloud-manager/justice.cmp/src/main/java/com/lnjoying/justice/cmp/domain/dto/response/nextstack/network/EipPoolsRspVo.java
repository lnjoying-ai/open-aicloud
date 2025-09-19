package com.lnjoying.justice.cmp.domain.dto.response.nextstack.network;

import lombok.Data;

import java.util.List;

@Data
public class EipPoolsRspVo
{
    private long totalNum;
    private List<EipPoolRspVo> eipPools;
    private boolean available = false;
}
