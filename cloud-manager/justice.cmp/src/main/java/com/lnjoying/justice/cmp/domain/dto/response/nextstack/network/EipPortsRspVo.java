package com.lnjoying.justice.cmp.domain.dto.response.nextstack.network;

import lombok.Data;

import java.util.List;

@Data
public class EipPortsRspVo
{
    private String eipId;

    private List<Integer> ports;

    private Integer protocol;
}
