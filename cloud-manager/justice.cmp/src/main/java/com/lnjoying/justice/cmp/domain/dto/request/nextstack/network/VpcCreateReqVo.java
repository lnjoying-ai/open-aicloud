package com.lnjoying.justice.cmp.domain.dto.request.nextstack.network;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
public class VpcCreateReqVo
{
    @Length(max = 64, message = "name length must be less than 64")
    @NotBlank(message = "name is required")
    private String name;

    @NotBlank
    private String cidr;

    @Min(1)
    @Max(4094)
    private Integer vlanId;

    private Short addressType;
}
