package com.lnjoying.justice.cmp.domain.info.baremetal;

import com.lnjoying.justice.cmp.db.model.TblCmpNicInfo;
import lombok.Data;

@Data
public class NicInfo
{
    private String nicId;
    private Integer networkType;
    private String ip;
    private Integer addressType;

    public void setNicInfo(TblCmpNicInfo tblCmpNicInfo)
    {
        this.nicId = tblCmpNicInfo.getNicId();
        this.networkType = (int) tblCmpNicInfo.getNetworkType();
        this.ip = tblCmpNicInfo.getIp();
        this.addressType = (int) tblCmpNicInfo.getAddressType();
    }

}
