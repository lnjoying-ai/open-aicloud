package com.lnjoying.justice.cmp.domain.dto.response.nextstack.network;

import com.lnjoying.justice.cmp.db.model.TblCmpEipPool;
import lombok.Data;

@Data
public class EipPoolRspVo extends TblCmpEipPool
{
    private boolean available = false;

    public void setEipPoolRspVo(TblCmpEipPool tblCmpEipPool)
    {
        this.setCloudId(tblCmpEipPool.getCloudId());
        this.setPoolId(tblCmpEipPool.getPoolId());
        this.setName(tblCmpEipPool.getName());
        this.setDescription(tblCmpEipPool.getDescription());
        this.setPhaseStatus(tblCmpEipPool.getPhaseStatus());
        this.setCreateTime(tblCmpEipPool.getCreateTime());
        this.setUpdateTime(tblCmpEipPool.getUpdateTime());
        this.setEeBp(tblCmpEipPool.getEeBp());
        this.setEeUser(tblCmpEipPool.getEeUser());
        this.setEeStatus(tblCmpEipPool.getEeStatus());
    }
}
