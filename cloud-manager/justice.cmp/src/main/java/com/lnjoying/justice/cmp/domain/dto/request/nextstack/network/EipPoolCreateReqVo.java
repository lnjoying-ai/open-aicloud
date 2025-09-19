package com.lnjoying.justice.cmp.domain.dto.request.nextstack.network;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
public class EipPoolCreateReqVo
{
    @Length(max = 64, message = "name length must be less than 64")
    @NotBlank(message = "name is required")
    private String name;

    private String description;

    private Integer vlanId;
}
