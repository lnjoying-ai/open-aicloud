package com.lnjoying.justice.cmp.domain.dto.request.nextstack.repo;

import com.lnjoying.justice.cmp.common.constant.FlavorType;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
public class FlavorCreateReq
{
    @NotBlank(message = "name is required")
    private String name;

    @Min(1)
    private int cpu;

    @Min(1)
    private int mem;

    @Min(0)
    Integer gpuCount;

    String gpuName;

    @Min(FlavorType.ALL_FLAVOR_TYPE) @Max(FlavorType.BAREMETAL_FLAVOR_TYPE)
    private short type;

    private Boolean needIb;
}
