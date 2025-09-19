package com.lnjoying.justice.cmp.domain.dto.request.nextstack.vm;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
public class InstanceGroupCreateReq
{
    @NotBlank(message = "name is required")
    @Length(max = 64, message = "name length must be less than 64")
    String name;

    String description;

    List<String> vmInstanceIds;
}
