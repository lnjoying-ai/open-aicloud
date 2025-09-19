package com.lnjoying.justice.cmp.domain.dto.request.nextstack.vm;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
public class SnapCreateReq
{
    @NotBlank(message = "name is required")
    @Length(max = 64, message = "name length must be less than 64")
    private String name;

    private String description;

    @NotBlank(message = "vmInstanceId is required")
    private String vmInstanceId;
}
