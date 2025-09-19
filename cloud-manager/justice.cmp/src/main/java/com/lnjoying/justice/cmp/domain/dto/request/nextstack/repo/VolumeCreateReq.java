package com.lnjoying.justice.cmp.domain.dto.request.nextstack.repo;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class VolumeCreateReq
{
    @NotNull
    @Min(1)
    private Integer size;

    @NotBlank(message = "storagePoolId is required")
    private String storagePoolId;

    private String name;

    private String description;
}
