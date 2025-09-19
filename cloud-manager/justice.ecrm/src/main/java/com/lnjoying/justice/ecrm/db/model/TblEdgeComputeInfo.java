package com.lnjoying.justice.ecrm.db.model;

import com.lnjoying.justice.commonweb.handler.aspect.annotation.ResourceInstanceName;
import io.swagger.annotations.ApiModel;

import java.io.Serializable;
import java.util.Date;

@ApiModel(value = "TblEdgeComputeInfo")
public class TblEdgeComputeInfo implements Serializable {
    private String nodeId;

    @ResourceInstanceName
    private String nodeName;

    private String siteId;

    private String regionId;

    private String hostName;

    private String network;

    private Integer activeStatus;

    private Integer onlineStatus;

    private Integer cpuLimit;

    private Long memLimit;

    private Long diskLimit;

    private String vender;

    private String uuid;

    private String product;

    private String sn;

    private Integer cpuLogicNum;

    private Integer cpuPhysicalNum;

    private String cpuModel;

    private Double cpuFrequency;

    private Integer cpuNum;

    private Long memTotal;

    private Long diskTotal;

    private String diskType;

    private String diskDetail;

    private String os;

    private String coreVersion;

    private String softwareVersion;

    private String softwareMap;

    private String scope;

    private String userId;

    private String bpId;

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

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId == null ? null : siteId.trim();
    }

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId == null ? null : regionId.trim();
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName == null ? null : hostName.trim();
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

    public Long getDiskLimit() {
        return diskLimit;
    }

    public void setDiskLimit(Long diskLimit) {
        this.diskLimit = diskLimit;
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

    public Integer getCpuNum()
    {
        return cpuNum;
    }

    public void setCpuNum(Integer cpuNum)
    {
        this.cpuNum = cpuNum;
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

    public String getSoftwareMap() {
        return softwareMap;
    }

    public void setSoftwareMap(String softwareMap) {
        this.softwareMap = softwareMap == null ? null : softwareMap.trim();
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope == null ? null : scope.trim();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getBpId() {
        return bpId;
    }

    public void setBpId(String bpId) {
        this.bpId = bpId == null ? null : bpId.trim();
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