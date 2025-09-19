package com.lnjoying.justice.cmp.domain.dto.response.nextstack.vm;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lnjoying.justice.cmp.db.model.TblCmpPciDevice;
import com.micro.core.common.Utils;
import lombok.Data;

@Data
public class PciDeviceInfo
{
    private String pciDeviceName;

    private String pciDeviceType;

    @JsonIgnore
    private String userId;

    private Integer phaseStatus;

    private String deviceId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss", timezone = "GMT+8")
    private String createTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss", timezone = "GMT+8")
    private String updateTime;

    public void setPciDevice(TblCmpPciDevice tblPciDevice)
    {
        this.createTime = Utils.formatDate(tblPciDevice.getCreateTime());
        this.updateTime = Utils.formatDate(tblPciDevice.getUpdateTime());

        this.deviceId = tblPciDevice.getDeviceId();
        this.phaseStatus = tblPciDevice.getPhaseStatus();

        this.userId = tblPciDevice.getUserId();

        this.pciDeviceName = tblPciDevice.getName();
        this.pciDeviceType = tblPciDevice.getType();
    }
}
