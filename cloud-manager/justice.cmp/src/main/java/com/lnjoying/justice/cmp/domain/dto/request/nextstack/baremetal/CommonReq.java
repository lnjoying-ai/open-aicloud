package com.lnjoying.justice.cmp.domain.dto.request.nextstack.baremetal;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.AssertTrue;

@Data
public class CommonReq
{
    @Length(max = 64, message = "name length must be less than 64")
    private String name;
    private String description;

    @AssertTrue(message = "name or description is required")
    private boolean isValid()
    {
        return !StringUtils.isBlank(name) || !StringUtils.isBlank(description);
    }
}
