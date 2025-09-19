package com.lnjoying.justice.cmp.domain.dto.request.nextstack.baremetal;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ResetBaremetalInstanceReq
{
    @NotBlank
    private String hostName;

    private String sysPassword;

    private String pubkeyId;
}
