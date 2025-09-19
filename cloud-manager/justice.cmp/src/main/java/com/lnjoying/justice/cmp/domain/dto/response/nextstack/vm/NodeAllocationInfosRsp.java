package com.lnjoying.justice.cmp.domain.dto.response.nextstack.vm;

import lombok.Data;

import java.util.List;

@Data
public class NodeAllocationInfosRsp
{
    private long totalNum;

    private List<HypervisorNodeAllocationInfo> nodeAllocationInfos;
}
