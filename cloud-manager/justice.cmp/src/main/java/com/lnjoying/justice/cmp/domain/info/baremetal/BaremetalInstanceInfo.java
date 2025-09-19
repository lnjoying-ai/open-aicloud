package com.lnjoying.justice.cmp.domain.info.baremetal;

import com.lnjoying.justice.cmp.db.model.TblCmpBaremetalInstance;
import com.micro.core.common.Utils;
import lombok.Data;

@Data
public class BaremetalInstanceInfo
{
    private String instanceId;
    private String name;
    private int phaseStatus;
    private String hostname;

    private BaremetalDeviceAbbrInfo deviceInfo;

    private ImageAbbrInfo imageInfo;
    private VpcAbbrInfo vpcInfo;
    private SubnetAbbrInfo subnetInfo;
    private PortAbbrInfo portInfo;

    private String description;
    private String createTime;
    private String updateTime;

    public void setInstanceInfo(TblCmpBaremetalInstance tblCmpBaremetalInstance)
    {
        this.instanceId = tblCmpBaremetalInstance.getInstanceId();
        this.name = tblCmpBaremetalInstance.getName();
        this.phaseStatus = tblCmpBaremetalInstance.getPhaseStatus();
        this.hostname = tblCmpBaremetalInstance.getHostName();

        this.description = tblCmpBaremetalInstance.getDescription();
        this.createTime = Utils.formatDate(tblCmpBaremetalInstance.getCreateTime());
        this.updateTime = Utils.formatDate(tblCmpBaremetalInstance.getUpdateTime());
    }
}
