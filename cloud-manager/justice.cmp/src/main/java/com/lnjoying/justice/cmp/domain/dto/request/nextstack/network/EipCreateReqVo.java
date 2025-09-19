package com.lnjoying.justice.cmp.domain.dto.request.nextstack.network;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class EipCreateReqVo
{
    @NotBlank
    private String startIpAddress;

    @NotBlank
    private String endIpAddress;

    private String startPublicIpAddress;

    private String endPublicIpAddress;

    private Short addressType;

    @NotBlank
    private String eipPoolId;
}
