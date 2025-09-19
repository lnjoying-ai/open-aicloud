package com.lnjoying.justice.cmp.domain.dto.request.nextstack.repo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class VolumeExportReq
{
    @NotBlank(message = "imageName is required")
    private String imageName;

    @NotNull
    private Boolean isPublic;
}
