package com.lnjoying.justice.cmp.domain.dto.request.nextstack.network;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
public class EipPortMapCreateReqVo extends EipPortMapUpdateReqVo
{
    @NotBlank
    private String eipId;

    @NotBlank
    @Length(max = 64, message = "name length must be less than 64")
    private String mapName;

    private Short addressType;

    @NotBlank
    private String subnetId;

    private String insideIp;

    private boolean isStatic;

    private String portId;

    private String bandwidth;
}
