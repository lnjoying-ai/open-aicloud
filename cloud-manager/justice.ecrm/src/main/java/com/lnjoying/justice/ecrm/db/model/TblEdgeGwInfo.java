package com.lnjoying.justice.ecrm.db.model;

import io.swagger.annotations.ApiModel;

import java.io.Serializable;
import java.util.Date;

@ApiModel(value = "TblEdgeGwInfo")
public class TblEdgeGwInfo implements Serializable {
    private String nodeId;

    private String nodeName;

    private String hostName;

    private String hostPublicIp;

    private Integer hostPublicPort;

    private String hostInnerIp;

    private Integer hostInnerPort;

    private String network;

    private Integer activeStatus;

    private Integer onlineStatus;

    private Integer cpuLimit;

    private Long memLimit;

    private String vender;

    private String uuid;

    private String product;

    private String sn;

    private Integer cpuLogicNum;

    private Integer cpuPhysicalNum;

    private String cpuModel;

    private Double cpuFrequency;

    private Long memTotal;

    private Long diskTotal;

    private String diskType;

    private String diskDetail;

    private String os;

    private String coreVersion;

    private String softwareVersion;

    private Date createTime;

    private Date updateTime;

    private static final long serialVersionUID = 1L;

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId == null ? null : nodeId.trim();
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName == null ? null : nodeName.trim();
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName == null ? null : hostName.trim();
    }

    public String getHostPublicIp() {
        return hostPublicIp;
    }

    public void setHostPublicIp(String hostPublicIp) {
        this.hostPublicIp = hostPublicIp == null ? null : hostPublicIp.trim();
    }

    public Integer getHostPublicPort() {
        return hostPublicPort;
    }

    public void setHostPublicPort(Integer hostPublicPort) {
        this.hostPublicPort = hostPublicPort;
    }

    public String getHostInnerIp() {
        return hostInnerIp;
    }

    public void setHostInnerIp(String hostInnerIp) {
        this.hostInnerIp = hostInnerIp == null ? null : hostInnerIp.trim();
    }

    public Integer getHostInnerPort() {
        return hostInnerPort;
    }

    public void setHostInnerPort(Integer hostInnerPort) {
        this.hostInnerPort = hostInnerPort;
    }

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network == null ? null : network.trim();
    }

    public Integer getActiveStatus() {
        return activeStatus;
    }

    public void setActiveStatus(Integer activeStatus) {
        this.activeStatus = activeStatus;
    }

    public Integer getOnlineStatus() {
        return onlineStatus;
    }

    public void setOnlineStatus(Integer onlineStatus) {
        this.onlineStatus = onlineStatus;
    }

    public Integer getCpuLimit() {
        return cpuLimit;
    }

    public void setCpuLimit(Integer cpuLimit) {
        this.cpuLimit = cpuLimit;
    }

    public Long getMemLimit() {
        return memLimit;
    }

    public void setMemLimit(Long memLimit) {
        this.memLimit = memLimit;
    }

    public String getVender() {
        return vender;
    }

    public void setVender(String vender) {
        this.vender = vender == null ? null : vender.trim();
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid == null ? null : uuid.trim();
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product == null ? null : product.trim();
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn == null ? null : sn.trim();
    }

    public Integer getCpuLogicNum() {
        return cpuLogicNum;
    }

    public void setCpuLogicNum(Integer cpuLogicNum) {
        this.cpuLogicNum = cpuLogicNum;
    }

    public Integer getCpuPhysicalNum() {
        return cpuPhysicalNum;
    }

    public void setCpuPhysicalNum(Integer cpuPhysicalNum) {
        this.cpuPhysicalNum = cpuPhysicalNum;
    }

    public String getCpuModel() {
        return cpuModel;
    }

    public void setCpuModel(String cpuModel) {
        this.cpuModel = cpuModel == null ? null : cpuModel.trim();
    }

    public Double getCpuFrequency() {
        return cpuFrequency;
    }

    public void setCpuFrequency(Double cpuFrequency) {
        this.cpuFrequency = cpuFrequency;
    }

    public Long getMemTotal() {
        return memTotal;
    }

    public void setMemTotal(Long memTotal) {
        this.memTotal = memTotal;
    }

    public Long getDiskTotal() {
        return diskTotal;
    }

    public void setDiskTotal(Long diskTotal) {
        this.diskTotal = diskTotal;
    }

    public String getDiskType() {
        return diskType;
    }

    public void setDiskType(String diskType) {
        this.diskType = diskType == null ? null : diskType.trim();
    }

    public String getDiskDetail() {
        return diskDetail;
    }

    public void setDiskDetail(String diskDetail) {
        this.diskDetail = diskDetail == null ? null : diskDetail.trim();
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os == null ? null : os.trim();
    }

    public String getCoreVersion() {
        return coreVersion;
    }

    public void setCoreVersion(String coreVersion) {
        this.coreVersion = coreVersion == null ? null : coreVersion.trim();
    }

    public String getSoftwareVersion() {
        return softwareVersion;
    }

    public void setSoftwareVersion(String softwareVersion) {
        this.softwareVersion = softwareVersion == null ? null : softwareVersion.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}