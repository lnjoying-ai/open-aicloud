package com.lnjoying.justice.cmp.domain.dto.request.nextstack.vm;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class VmInstanceMigrateReq
{
    @NotBlank(message = "destNodeId is required")
    private String destNodeId;
}
