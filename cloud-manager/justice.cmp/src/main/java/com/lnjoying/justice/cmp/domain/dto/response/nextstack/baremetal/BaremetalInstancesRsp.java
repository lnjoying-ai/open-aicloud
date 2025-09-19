package com.lnjoying.justice.cmp.domain.dto.response.nextstack.baremetal;

import com.lnjoying.justice.cmp.domain.info.baremetal.BaremetalInstanceInfo;
import lombok.Data;

import java.util.List;

@Data
public class BaremetalInstancesRsp
{
    private long totalNum;

    private List<BaremetalInstanceInfo> instances;
}
