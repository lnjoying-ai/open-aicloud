package com.lnjoying.justice.cmp.domain.dto.response.nextstack.vm;

import lombok.Data;

import java.util.List;

@Data
public class VmInstancesRsp
{
    private long totalNum;

    private List<VmInstanceInfo> vmInstancesInfo;
}
