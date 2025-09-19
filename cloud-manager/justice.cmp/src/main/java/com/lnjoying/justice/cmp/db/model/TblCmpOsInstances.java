package com.lnjoying.justice.cmp.db.model;

import com.lnjoying.justice.commonweb.handler.aspect.annotation.ResourceInstanceName;

import java.io.Serializable;
import java.util.Date;

public class TblCmpOsInstances extends TblCmpOsInstancesKey implements Serializable {
    private Date createdAt;

    private Date updatedAt;

    private Date deletedAt;

    private Integer id;

    private Integer internalId;

    private String userId;

    private String projectId;

    private String imageRef;

    private String kernelId;

    private String ramdiskId;

    private Integer launchIndex;

    private String keyName;

    private String keyData;

    private Integer powerState;

    private String vmState;

    private Integer memoryMb;

    private Integer vcpus;

    private String hostname;

    private String host;

    private String userData;

    private String reservationId;

    private Date launchedAt;

    private Date terminatedAt;

    @ResourceInstanceName
    private String displayName;

    private String displayDescription;

    private String availabilityZone;

    private Short locked;

    private String osType;

    private String launchedOn;

    private String instanceTypeId;

    private String vmMode;

    private String architecture;

    private String rootDeviceName;

    private String accessIpV4;

    private String accessIpV6;

    private String configDrive;

    private String taskState;

    private String defaultEphemeralDevice;

    private String defaultSwapDevice;

    private Integer progress;

    private Short autoDiskConfig;

    private Short shutdownTerminate;

    private Short disableTerminate;

    private Integer rootGb;

    private Integer ephemeralGb;

    private String cellName;

    private String node;

    private Integer deleted;

    private String lockedBy;

    private Integer cleand;

    private String ephemeralKeyUuid;

    private Short hidden;

    private Integer eeStatus;

    private String eeBp;

    private String eeUser;

    private String status;

    private String networkInfo;

    private Integer chargeType;

    private Integer priceUnit;

    private Integer period;

    private Date expireTime;

    private static final long serialVersionUID = 1L;

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Date getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Date deletedAt) {
        this.deletedAt = deletedAt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getInternalId() {
        return internalId;
    }

    public void setInternalId(Integer internalId) {
        this.internalId = internalId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId == null ? null : projectId.trim();
    }

    public String getImageRef() {
        return imageRef;
    }

    public void setImageRef(String imageRef) {
        this.imageRef = imageRef == null ? null : imageRef.trim();
    }

    public String getKernelId() {
        return kernelId;
    }

    public void setKernelId(String kernelId) {
        this.kernelId = kernelId == null ? null : kernelId.trim();
    }

    public String getRamdiskId() {
        return ramdiskId;
    }

    public void setRamdiskId(String ramdiskId) {
        this.ramdiskId = ramdiskId == null ? null : ramdiskId.trim();
    }

    public Integer getLaunchIndex() {
        return launchIndex;
    }

    public void setLaunchIndex(Integer launchIndex) {
        this.launchIndex = launchIndex;
    }

    public String getKeyName() {
        return keyName;
    }

    public void setKeyName(String keyName) {
        this.keyName = keyName == null ? null : keyName.trim();
    }

    public String getKeyData() {
        return keyData;
    }

    public void setKeyData(String keyData) {
        this.keyData = keyData == null ? null : keyData.trim();
    }

    public Integer getPowerState() {
        return powerState;
    }

    public void setPowerState(Integer powerState) {
        this.powerState = powerState;
    }

    public String getVmState() {
        return vmState;
    }

    public void setVmState(String vmState) {
        this.vmState = vmState == null ? null : vmState.trim();
    }

    public Integer getMemoryMb() {
        return memoryMb;
    }

    public void setMemoryMb(Integer memoryMb) {
        this.memoryMb = memoryMb;
    }

    public Integer getVcpus() {
        return vcpus;
    }

    public void setVcpus(Integer vcpus) {
        this.vcpus = vcpus;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname == null ? null : hostname.trim();
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host == null ? null : host.trim();
    }

    public String getUserData() {
        return userData;
    }

    public void setUserData(String userData) {
        this.userData = userData == null ? null : userData.trim();
    }

    public String getReservationId() {
        return reservationId;
    }

    public void setReservationId(String reservationId) {
        this.reservationId = reservationId == null ? null : reservationId.trim();
    }

    public Date getLaunchedAt() {
        return launchedAt;
    }

    public void setLaunchedAt(Date launchedAt) {
        this.launchedAt = launchedAt;
    }

    public Date getTerminatedAt() {
        return terminatedAt;
    }

    public void setTerminatedAt(Date terminatedAt) {
        this.terminatedAt = terminatedAt;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName == null ? null : displayName.trim();
    }

    public String getDisplayDescription() {
        return displayDescription;
    }

    public void setDisplayDescription(String displayDescription) {
        this.displayDescription = displayDescription == null ? null : displayDescription.trim();
    }

    public String getAvailabilityZone() {
        return availabilityZone;
    }

    public void setAvailabilityZone(String availabilityZone) {
        this.availabilityZone = availabilityZone == null ? null : availabilityZone.trim();
    }

    public Short getLocked() {
        return locked;
    }

    public void setLocked(Short locked) {
        this.locked = locked;
    }

    public String getOsType() {
        return osType;
    }

    public void setOsType(String osType) {
        this.osType = osType == null ? null : osType.trim();
    }

    public String getLaunchedOn() {
        return launchedOn;
    }

    public void setLaunchedOn(String launchedOn) {
        this.launchedOn = launchedOn == null ? null : launchedOn.trim();
    }

    public String getInstanceTypeId() {
        return instanceTypeId;
    }

    public void setInstanceTypeId(String instanceTypeId) {
        this.instanceTypeId = instanceTypeId == null ? null : instanceTypeId.trim();
    }

    public String getVmMode() {
        return vmMode;
    }

    public void setVmMode(String vmMode) {
        this.vmMode = vmMode == null ? null : vmMode.trim();
    }

    public String getArchitecture() {
        return architecture;
    }

    public void setArchitecture(String architecture) {
        this.architecture = architecture == null ? null : architecture.trim();
    }

    public String getRootDeviceName() {
        return rootDeviceName;
    }

    public void setRootDeviceName(String rootDeviceName) {
        this.rootDeviceName = rootDeviceName == null ? null : rootDeviceName.trim();
    }

    public String getAccessIpV4() {
        return accessIpV4;
    }

    public void setAccessIpV4(String accessIpV4) {
        this.accessIpV4 = accessIpV4 == null ? null : accessIpV4.trim();
    }

    public String getAccessIpV6() {
        return accessIpV6;
    }

    public void setAccessIpV6(String accessIpV6) {
        this.accessIpV6 = accessIpV6 == null ? null : accessIpV6.trim();
    }

    public String getConfigDrive() {
        return configDrive;
    }

    public void setConfigDrive(String configDrive) {
        this.configDrive = configDrive == null ? null : configDrive.trim();
    }

    public String getTaskState() {
        return taskState;
    }

    public void setTaskState(String taskState) {
        this.taskState = taskState == null ? null : taskState.trim();
    }

    public String getDefaultEphemeralDevice() {
        return defaultEphemeralDevice;
    }

    public void setDefaultEphemeralDevice(String defaultEphemeralDevice) {
        this.defaultEphemeralDevice = defaultEphemeralDevice == null ? null : defaultEphemeralDevice.trim();
    }

    public String getDefaultSwapDevice() {
        return defaultSwapDevice;
    }

    public void setDefaultSwapDevice(String defaultSwapDevice) {
        this.defaultSwapDevice = defaultSwapDevice == null ? null : defaultSwapDevice.trim();
    }

    public Integer getProgress() {
        return progress;
    }

    public void setProgress(Integer progress) {
        this.progress = progress;
    }

    public Short getAutoDiskConfig() {
        return autoDiskConfig;
    }

    public void setAutoDiskConfig(Short autoDiskConfig) {
        this.autoDiskConfig = autoDiskConfig;
    }

    public Short getShutdownTerminate() {
        return shutdownTerminate;
    }

    public void setShutdownTerminate(Short shutdownTerminate) {
        this.shutdownTerminate = shutdownTerminate;
    }

    public Short getDisableTerminate() {
        return disableTerminate;
    }

    public void setDisableTerminate(Short disableTerminate) {
        this.disableTerminate = disableTerminate;
    }

    public Integer getRootGb() {
        return rootGb;
    }

    public void setRootGb(Integer rootGb) {
        this.rootGb = rootGb;
    }

    public Integer getEphemeralGb() {
        return ephemeralGb;
    }

    public void setEphemeralGb(Integer ephemeralGb) {
        this.ephemeralGb = ephemeralGb;
    }

    public String getCellName() {
        return cellName;
    }

    public void setCellName(String cellName) {
        this.cellName = cellName == null ? null : cellName.trim();
    }

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node == null ? null : node.trim();
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    public String getLockedBy() {
        return lockedBy;
    }

    public void setLockedBy(String lockedBy) {
        this.lockedBy = lockedBy == null ? null : lockedBy.trim();
    }

    public Integer getCleand() {
        return cleand;
    }

    public void setCleand(Integer cleand) {
        this.cleand = cleand;
    }

    public String getEphemeralKeyUuid() {
        return ephemeralKeyUuid;
    }

    public void setEphemeralKeyUuid(String ephemeralKeyUuid) {
        this.ephemeralKeyUuid = ephemeralKeyUuid == null ? null : ephemeralKeyUuid.trim();
    }

    public Short getHidden() {
        return hidden;
    }

    public void setHidden(Short hidden) {
        this.hidden = hidden;
    }

    public Integer getEeStatus() {
        return eeStatus;
    }

    public void setEeStatus(Integer eeStatus) {
        this.eeStatus = eeStatus;
    }

    public String getEeBp() {
        return eeBp;
    }

    public void setEeBp(String eeBp) {
        this.eeBp = eeBp == null ? null : eeBp.trim();
    }

    public String getEeUser() {
        return eeUser;
    }

    public void setEeUser(String eeUser) {
        this.eeUser = eeUser == null ? null : eeUser.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public String getNetworkInfo() {
        return networkInfo;
    }

    public void setNetworkInfo(String networkInfo) {
        this.networkInfo = networkInfo == null ? null : networkInfo.trim();
    }

    public Integer getChargeType() {
        return chargeType;
    }

    public void setChargeType(Integer chargeType) {
        this.chargeType = chargeType;
    }

    public Integer getPriceUnit() {
        return priceUnit;
    }

    public void setPriceUnit(Integer priceUnit) {
        this.priceUnit = priceUnit;
    }

    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }

    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }
}