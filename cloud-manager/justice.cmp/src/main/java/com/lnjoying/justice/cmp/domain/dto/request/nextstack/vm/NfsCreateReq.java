package com.lnjoying.justice.cmp.domain.dto.request.nextstack.vm;

import com.lnjoying.justice.cmp.domain.dto.request.nextstack.baremetal.CommonReq;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class NfsCreateReq extends CommonReq
{
    @NotNull
    private String vpcId;

    @NotNull
    private String subnetId;

    @Min(1)
    private Integer size;
}
