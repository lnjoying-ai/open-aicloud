package com.lnjoying.justice.cmp.domain.dto.response.nextstack.vm;

import lombok.Data;

import java.util.List;

@Data
public class SnapsTreeRsp extends VmSnapInfo
{
    private String parentId;

    private List<SnapsTreeRsp> children;
}
