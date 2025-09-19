package com.lnjoying.justice.cmp.domain.dto.response.nextstack.network;

import com.lnjoying.justice.cmp.db.model.TblCmpEipPool;
import com.micro.core.common.Utils;
import lombok.Data;

@Data
public class EipPoolDetailInfoRspVo
{
    private String name;
    private String description;
    private String createTime;
    private String updateTime;
    private Integer vlanId;
    private Integer phaseStatus;

    public void setEipPool(TblCmpEipPool tblCmpEipPool)
    {
        name = tblCmpEipPool.getName();
        description = tblCmpEipPool.getDescription();
        createTime = Utils.formatDate(tblCmpEipPool.getCreateTime());
        updateTime = Utils.formatDate(tblCmpEipPool.getUpdateTime());
        phaseStatus = tblCmpEipPool.getPhaseStatus();
    }
}


