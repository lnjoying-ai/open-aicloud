package com.lnjoying.justice.cmp.domain.dto.request.nextstack.vm;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.AssertTrue;

@Data
public class VmInstanceUpdateReq
{
    private String name;

    private String description;

    private String flavorId;

    private String bootDev;

    private Integer cpuCount;

    private Integer memorySize;

    @AssertTrue(message = "name,description,flavorId,bootDev is required")
    private boolean isValid()
    {
        return !StringUtils.isBlank(name) || !StringUtils.isBlank(description) ||
                !StringUtils.isBlank(flavorId) || !StringUtils.isBlank(bootDev) || cpuCount != null || memorySize != null;
    }
}
