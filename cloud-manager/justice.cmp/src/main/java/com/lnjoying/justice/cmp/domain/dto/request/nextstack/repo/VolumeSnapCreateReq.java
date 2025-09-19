package com.lnjoying.justice.cmp.domain.dto.request.nextstack.repo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Data
public class VolumeSnapCreateReq
{
    @NotBlank(message = "volumeId is required")
    private String volumeId;

    @NotEmpty(message = "name is required")
    private String name;

    private String description;
}
