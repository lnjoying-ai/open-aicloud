package com.lnjoying.justice.cmp.domain.dto.response.nextstack.repo;

import com.lnjoying.justice.cmp.db.model.TblCmpFlavor;
import com.micro.core.common.Utils;
import lombok.Data;

@Data
public class FlavorDetailInfoRsp extends RpcFlavorInfo
{

    private Integer phaseStatus;
    private String createTime;
    private String updateTime;

    private boolean available;

    public void setFlavorDetailInfoRsp(TblCmpFlavor tblFlavor)
    {
        this.flavorId = tblFlavor.getFlavorId();
        this.name = tblFlavor.getName();
        this.type = tblFlavor.getType() == null ? null : tblFlavor.getType().intValue();
        this.cpu = tblFlavor.getCpu();
        this.mem = tblFlavor.getMem();
        this.createTime =  Utils.formatDate(tblFlavor.getCreateTime());
        this.updateTime =  Utils.formatDate(tblFlavor.getUpdateTime());
        this.gpuCount = tblFlavor.getGpuCount();
        this.gpuName = tblFlavor.getGpuName();
        this.phaseStatus = tblFlavor.getPhaseStatus();
        this.needIb = tblFlavor.getNeedIb();
    }
}
