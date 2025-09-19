package com.lnjoying.justice.cmp.domain.dto.response.nextstack.baremetal;

import com.lnjoying.justice.cmp.db.model.TblCmpBaremetalDevice;
import com.lnjoying.justice.cmp.db.model.TblCmpBaremetalDeviceSpec;
import com.micro.core.common.Utils;
import lombok.Data;

@Data
public class BaremetalDeviceDetailInfoRsp
{
    private String deviceId;
    private String instanceId;
    private String instanceName;
    private String name;
    private String description;
    private String clusterId;
    private String clusterName;
    private String userId;
    private String userName;
    private String ipmiIp;
    private int ipmiPort;
    private String ipmiUsername;
    private String mac;
    private int phaseStatus;
    private String createTime;
    private String updateTime;
    private String architecture;
    private int cpuNum;
    private String cpuModelName;
    private double cpuFrequency;
    private long memTotal;
    private long diskTotal;

    public void setBaremetalDeviceDetailInfo(TblCmpBaremetalDevice tblCmpBaremetalDevice, TblCmpBaremetalDeviceSpec tblCmpBaremetalDeviceSpec)
    {
        this.deviceId = tblCmpBaremetalDevice.getDeviceId();
        this.name = tblCmpBaremetalDevice.getName();
        this.description = tblCmpBaremetalDevice.getDescription();
        this.clusterId = tblCmpBaremetalDevice.getClusterId();
        this.userId = tblCmpBaremetalDevice.getEeUser();
        this.ipmiIp = tblCmpBaremetalDevice.getIpmiIp();
        this.ipmiPort = tblCmpBaremetalDevice.getIpmiPort();
        this.ipmiUsername = tblCmpBaremetalDevice.getIpmiUsername();
        this.mac = tblCmpBaremetalDevice.getIpmiMac();
        this.phaseStatus = tblCmpBaremetalDevice.getPhaseStatus();
        if (null != tblCmpBaremetalDeviceSpec)
        {
            this.architecture = tblCmpBaremetalDeviceSpec.getArchitecture();
            this.cpuNum = tblCmpBaremetalDeviceSpec.getCpuNum();
            this.cpuFrequency = tblCmpBaremetalDeviceSpec.getCpuFrequency();
            this.cpuModelName = tblCmpBaremetalDeviceSpec.getCpuModelName();
            this.memTotal = tblCmpBaremetalDeviceSpec.getMemTotal();
            this.diskTotal = tblCmpBaremetalDeviceSpec.getDiskTotal();
        }
        this.createTime = Utils.formatDate(tblCmpBaremetalDevice.getCreateTime());
        this.updateTime = Utils.formatDate(tblCmpBaremetalDevice.getUpdateTime());
    }
}
