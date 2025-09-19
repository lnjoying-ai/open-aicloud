package com.lnjoying.justice.cmp.domain.dto.request.nextstack.repo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class VolumeAttachReq
{
    @NotBlank(message = "vmId is required")
    private String vmId;
}
