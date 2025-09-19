package com.lnjoying.justice.cmp.domain.dto.response.nextstack.network;

import lombok.Data;

import java.util.List;

@Data
public class VpcsRspVo
{
    private long totalNum;

    private List<VpcDetailInfoRspVo> vpcs;
}
