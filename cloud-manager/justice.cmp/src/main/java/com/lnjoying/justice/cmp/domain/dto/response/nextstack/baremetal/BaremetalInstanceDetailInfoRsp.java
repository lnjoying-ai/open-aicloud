package com.lnjoying.justice.cmp.domain.dto.response.nextstack.baremetal;

import com.lnjoying.justice.cmp.db.model.TblCmpBaremetalInstance;
import com.lnjoying.justice.cmp.domain.info.baremetal.NicInfo;
import com.micro.core.common.Utils;
import lombok.Data;

import java.util.List;

@Data
public class BaremetalInstanceDetailInfoRsp
{
    private String instanceId;
    private String name;
    private String deviceId;
    private long phaseStatus;
    private String imageId;
    private String imageName;
    private Integer imageOsType;
    private String vpcId;
    private String vpcName;
    private String subnetId;
    private String subnetName;
    private String subnetCidr;
    private String ip;
    private List<NicInfo> nicInfos;
    private String sysUsername;
    private String hostname;
    private String pubkeyId;
    private String description;
    private String createTime;
    private String updateTime;

    public void setInstanceDetailInfo(TblCmpBaremetalInstance tblCmpBaremetalInstance)
    {
        this.instanceId = tblCmpBaremetalInstance.getInstanceId();
        this.name = tblCmpBaremetalInstance.getName();
        this.deviceId = tblCmpBaremetalInstance.getDeviceId();
        this.phaseStatus = tblCmpBaremetalInstance.getPhaseStatus();
        this.imageId = tblCmpBaremetalInstance.getImageId();
        this.vpcId = tblCmpBaremetalInstance.getVpcId();
        this.subnetId = tblCmpBaremetalInstance.getSubnetId();
        this.description = tblCmpBaremetalInstance.getDescription();
        this.hostname = tblCmpBaremetalInstance.getHostName();
        this.sysUsername = tblCmpBaremetalInstance.getSysUsername();
        this.pubkeyId = tblCmpBaremetalInstance.getPubkeyId();
        this.createTime = Utils.formatDate(tblCmpBaremetalInstance.getCreateTime());
        this.updateTime = Utils.formatDate(tblCmpBaremetalInstance.getUpdateTime());
    }
}
