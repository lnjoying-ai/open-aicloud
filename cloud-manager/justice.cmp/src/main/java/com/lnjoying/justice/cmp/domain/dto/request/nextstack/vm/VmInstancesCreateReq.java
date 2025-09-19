package com.lnjoying.justice.cmp.domain.dto.request.nextstack.vm;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@Data
public class VmInstancesCreateReq
{
    @Min(1)
    private Integer count;

    @Valid
    private VmInstanceCreateReq vmInstanceCreateReq;
}
