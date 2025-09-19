package com.lnjoying.justice.cmp.domain.dto.request.nextstack.network;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class EipAttachReq
{
    @NotBlank(message = "eipId不能为空")
    private String portId;
}
