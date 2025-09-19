package com.lnjoying.justice.cmp.domain.dto.response.nextstack.vm;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import com.lnjoying.justice.cmp.db.model.TblCmpVmInstance;
import com.lnjoying.justice.cmp.domain.dto.response.nextstack.network.RpcNetworkDetailInfo;
import com.lnjoying.justice.cmp.domain.dto.response.nextstack.network.RpcSgInfo;
import com.lnjoying.justice.cmp.domain.dto.response.nextstack.repo.RpcFlavorInfo;
import com.lnjoying.justice.cmp.domain.dto.response.nextstack.repo.RpcVolumeInfo;
import com.micro.core.common.Utils;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.List;

@Data
public class VmInstanceDetailInfoRsp
{
    private String instanceId;

    private String name;

    private Integer phaseStatus;

    private String imageId;

    private String imageName;

    private Integer imageOsType;

    private String vpcId;

    private String subnetId;

    private String description;

    private String hostname;

    private String sysUsername;

    private String pubkeyId ;

    private String hypervisorNodeId;

    private String hypervisorNodeName;

    private String hypervisorNodeIp;

    private String userId;

    private String flavorId;

    private String flavorName;

    private String volumeId;

    private int cpu;

    private int mem;

    private int rootDisk;

    private List<RpcSgInfo> sgInfos;

    private List<SnapInfo> snapInfos;

    private List<RpcNetworkDetailInfo> networkDetailInfos;

    private List<RpcVolumeInfo> diskInfos;

    private String createTime;

    private String updateTime;

    private List<PciDeviceInfo> pciInfos;

    private String bootDev;

    private Boolean ib;

    private String eeBp;
    private String eeUser;
    private String eeBpName;
    private String eeUserName;
    private Integer chargeType;
    private Integer priceUnit;
    private Integer period;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JsonProperty("expire_time")
    @SerializedName("expire_time")
    private Date expireTime;
    private String cloudId;
    private String storagePoolId;

    @Data
    public static class SnapInfo
    {
        String snapName;
        String snapId;
        Integer phaseStatus;
        String createTime;
        String updateTime;
        Boolean isCurrent;
    }

    public void setInstanceDetailInfo(TblCmpVmInstance tblVmInstance, RpcFlavorInfo tblFlavor)
    {
        if(null == tblVmInstance || null == tblFlavor)
        {
            return;
        }
        this.ib = null != tblFlavor.getNeedIb() && tblFlavor.getNeedIb();
        this.instanceId = tblVmInstance.getVmInstanceId();
        this.name = tblVmInstance.getName();
        this.hypervisorNodeId = tblVmInstance.getNodeId();
        this.phaseStatus = tblVmInstance.getPhaseStatus();
        this.imageId = tblVmInstance.getImageId();
        this.volumeId = tblVmInstance.getVolumeId();
        this.vpcId = tblVmInstance.getVpcId();
        this.subnetId = tblVmInstance.getSubnetId();
        this.bootDev = tblVmInstance.getBootDev();
        if (StringUtils.isBlank(bootDev) || bootDev.equals("hd"))
        {
            this.bootDev = "hd";
        }
        this.userId = tblVmInstance.getUserId();
        this.description = tblVmInstance.getDescription();
        this.hostname = tblVmInstance.getHostName();
        this.sysUsername = tblVmInstance.getSysUsername();
        this.pubkeyId = tblVmInstance.getPubkeyId();
        this.flavorId = tblFlavor.getFlavorId();
        this.flavorName = tblFlavor.getName();
        if (null != tblVmInstance.getCpuCount() && tblVmInstance.getCpuCount()>0)
        {
            this.cpu = tblVmInstance.getCpuCount();
        }
        else
        {
            this.cpu = tblFlavor.getCpu();
        }
        if (null != tblVmInstance.getMemSize() && tblVmInstance.getMemSize()>0)
        {
            this.mem = tblVmInstance.getMemSize();
        }
        else
        {
            this.mem = tblFlavor.getMem();
        }
        this.rootDisk = tblVmInstance.getRootDisk();
        this.createTime = Utils.formatDate(tblVmInstance.getCreateTime());
        this.updateTime = Utils.formatDate(tblVmInstance.getUpdateTime());

        this.eeBp = tblVmInstance.getEeBp();
        this.eeUser = tblVmInstance.getEeUser();
        this.chargeType = tblVmInstance.getChargeType();
        this.priceUnit = tblVmInstance.getPriceUnit();
        this.period = tblVmInstance.getPeriod();
        this.expireTime = tblVmInstance.getExpireTime();
        this.cloudId = tblVmInstance.getCloudId();
    }
}