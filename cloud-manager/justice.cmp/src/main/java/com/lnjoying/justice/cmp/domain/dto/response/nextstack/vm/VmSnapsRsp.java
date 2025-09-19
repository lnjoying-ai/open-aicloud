package com.lnjoying.justice.cmp.domain.dto.response.nextstack.vm;

import lombok.Data;

import java.util.List;

@Data
public class VmSnapsRsp
{
    private long totalNum;
    private List<VmSnapInfo> snaps;
}
