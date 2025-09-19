package com.lnjoying.justice.cmp.domain.dto.request.nextstack.vm;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class PciDeviceAttachReq
{
    @NotBlank(message = "vmInstanceId is required")
    private String vmInstanceId;
}
