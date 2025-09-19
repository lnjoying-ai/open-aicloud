package com.lnjoying.justice.cmp.domain.dto.response.nextstack.vm;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import com.lnjoying.justice.cmp.db.model.TblCmpVmInstance;
import com.lnjoying.justice.cmp.domain.info.baremetal.*;
import com.micro.core.common.Utils;
import lombok.Data;

import java.util.Date;

@Data
public class VmInstanceInfo
{
    private String instanceId;

    private String name;

    private int phaseStatus;

    private String hostname;

    private ImageAbbrInfo imageInfo;

    private VpcAbbrInfo vpcInfo;

    private SubnetAbbrInfo subnetInfo;

    private PortAbbrInfo portInfo;

    private String description;

    private String createTime;

    private String updateTime;

    private String volumeId;

    private String hypervisorNodeId;

    private String eipId;

    private String eip;

    private String publicIp;

    private String boundType;

    private Integer boundPhaseStatus;

    private String eeBp;
    private String eeUser;
    private String eeBpName;
    private String eeUserName;
    private Integer eeStatus;
    private Integer chargeType;
    private Integer priceUnit;
    private Integer period;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JsonProperty("expire_time")
    @SerializedName("expire_time")
    private Date expireTime;
    private String cloudId;

    private String sysUsername;

    public void setInstanceInfo(TblCmpVmInstance tblVmInstance)
    {
        this.instanceId = tblVmInstance.getVmInstanceId();
        this.name = tblVmInstance.getName();
        this.phaseStatus = tblVmInstance.getPhaseStatus();
        this.hostname = tblVmInstance.getHostName();
        this.volumeId = tblVmInstance.getVolumeId();
        this.description = tblVmInstance.getDescription();
        this.createTime = Utils.formatDate(tblVmInstance.getCreateTime());
        this.updateTime = Utils.formatDate(tblVmInstance.getUpdateTime());

        this.eeBp = tblVmInstance.getEeBp();
        this.eeUser = tblVmInstance.getEeUser();
        this.eeStatus = tblVmInstance.getEeStatus();
        this.chargeType = tblVmInstance.getChargeType();
        this.priceUnit = tblVmInstance.getPriceUnit();
        this.period = tblVmInstance.getPeriod();
        this.expireTime = tblVmInstance.getExpireTime();
        this.cloudId = tblVmInstance.getCloudId();

        this.sysUsername = tblVmInstance.getSysUsername();
    }
}
