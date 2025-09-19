package com.lnjoying.justice.cmp.domain.dto.response.nextstack.vm;

import com.google.gson.annotations.SerializedName;
import com.lnjoying.justice.cmp.db.model.TblCmpVmSnap;
import com.micro.core.common.Utils;
import lombok.Data;

@Data
public class VmSnapInfo
{
    private String snapId;
    private String name;
    private String description;
    private String vmInstanceId;
    private String createTime;
    private String updateTime;
    private int phaseStatus;
    private String vmInstanceName;
    @SerializedName(value="current")
    private boolean isCurrent;
    private String parentId;

    private String eeBp;
    private String eeUser;
    private String eeBpName;
    private String eeUserName;

    public void setSnapInfo(TblCmpVmSnap tblCmpVmSnap)
    {
        this.snapId = tblCmpVmSnap.getSnapId();
        this.name = tblCmpVmSnap.getName();
        this.vmInstanceId = tblCmpVmSnap.getVmInstanceId();
        this.phaseStatus = tblCmpVmSnap.getPhaseStatus();
        this.createTime = Utils.formatDate(tblCmpVmSnap.getCreateTime());
        this.updateTime = Utils.formatDate(tblCmpVmSnap.getUpdateTime());
        this.isCurrent = tblCmpVmSnap.getIsCurrent();
        this.description = tblCmpVmSnap.getDescription();
        this.parentId = tblCmpVmSnap.getParentId();

        this.eeBp = tblCmpVmSnap.getEeBp();
        this.eeUser = tblCmpVmSnap.getEeUser();
    }
}
