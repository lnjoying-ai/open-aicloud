package com.lnjoying.justice.cmp.domain.dto.response.openstack.nova;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import com.lnjoying.justice.cmp.db.model.*;
import com.lnjoying.justice.cmp.domain.dto.response.openstack.OSLink;
import com.lnjoying.justice.cmp.domain.dto.response.openstack.cinder.OSExtVolumeInfo;
import com.lnjoying.justice.cmp.utils.BoolUtil;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.openstack4j.model.compute.Server;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
public class OSExtServerInfo
{
    private static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS").serializeNulls().create();

    public static final long serialVersionUID = 1L;
    private String accessIPv4;
    private String accessIPv6;
    private Map<String, List<OSAddress>> addresses;
    @JsonProperty("config_drive")
    @SerializedName("config_drive")
    private String configDrive;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date created;
    private OSFlavor flavor;
    private String hostId;
    private String id;
    private Object image;
    @JsonProperty("key_name")
    @SerializedName("key_name")
    private String keyName;
    private List<OSLink> links;
    public Map<String, String> metadata;
    private String name;
    @JsonProperty("OS-DCF:diskConfig")
    @SerializedName("OS-DCF:diskConfig")
    private Server.DiskConfig diskConfig;
    @JsonProperty("OS-EXT-AZ:availability_zone")
    @SerializedName("OS-EXT-AZ:availability_zone")
    private String availabilityZone;
    @JsonProperty("OS-EXT-SRV-ATTR:host")
    @SerializedName("OS-EXT-SRV-ATTR:host")
    private String host;
    @JsonProperty("OS-EXT-SRV-ATTR:hostname")
    @SerializedName("OS-EXT-SRV-ATTR:hostname")
    private String hostname;
    @JsonProperty("OS-EXT-SRV-ATTR:hypervisor_hostname")
    @SerializedName("OS-EXT-SRV-ATTR:hypervisor_hostname")
    private String hypervisorHostname;
    @JsonProperty("OS-EXT-SRV-ATTR:instance_name")
    @SerializedName("OS-EXT-SRV-ATTR:instance_name")
    private String instanceName;
    @JsonProperty("OS-EXT-SRV-ATTR:kernel_id")
    @SerializedName("OS-EXT-SRV-ATTR:kernel_id")
    private String kernelid;
    @JsonProperty("OS-EXT-SRV-ATTR:launch_index")
    @SerializedName("OS-EXT-SRV-ATTR:launch_index")
    private Integer launchIndex;
    @JsonProperty("OS-EXT-SRV-ATTR:ramdisk_id")
    @SerializedName("OS-EXT-SRV-ATTR:ramdisk_id")
    private String ramdiskId;
    @JsonProperty("OS-EXT-SRV-ATTR:reservation_id")
    @SerializedName("OS-EXT-SRV-ATTR:reservation_id")
    private String reservationId;
    @JsonProperty("OS-EXT-SRV-ATTR:root_device_name")
    @SerializedName("OS-EXT-SRV-ATTR:root_device_name")
    private String rootDeviceName;
    @JsonProperty("OS-EXT-SRV-ATTR:user_data")
    @SerializedName("OS-EXT-SRV-ATTR:user_data")
    private String userData;
    @JsonProperty("OS-EXT-STS:power_state")
    @SerializedName("OS-EXT-STS:power_state")
    private Integer powerState;
    @JsonProperty("OS-EXT-STS:task_state")
    @SerializedName("OS-EXT-STS:task_state")
    private String taskState;
    @JsonProperty("OS-EXT-STS:vm_state")
    @SerializedName("OS-EXT-STS:vm_state")
    private String vmState;
    @JsonProperty("os-extended-volumes:volumes_attached")
    @SerializedName("os-extended-volumes:volumes_attached")
    private List<OSExtendedVolumesAttach> osExtendedVolumesAttached;
    @JsonProperty("OS-SRV-USG:launched_at")
    @SerializedName("OS-SRV-USG:launched_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date launchedAt;
    @JsonProperty("OS-SRV-USG:terminated_at")
    @SerializedName("OS-SRV-USG:terminated_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date terminatedAt;
    public String status;
    @JsonProperty("tenant_id")
    @SerializedName("tenant_id")
    public String tenantId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Date updated;
    @JsonProperty("user_id")
    @SerializedName("user_id")
    public String userId;
    public OSFault fault;
    public Integer progress;
    @JsonProperty("security_groups")
    @SerializedName("security_groups")
    private List<OSSecurityGroup> securityGroups;
    private Boolean locked;
    @JsonProperty("host_status")
    @SerializedName("host_status")
    private String hostStatus;
    private String description;
    private List<String> tags;
    @JsonProperty("trusted_image_certificates")
    @SerializedName("trusted_image_certificates")
    private List<String> trustedImageCertificates;
    @JsonProperty("locked_reason")
    @SerializedName("locked_reason")
    private String lockedReason;

    @JsonProperty("fixed_addresses")
    @SerializedName("fixed_addresses")
    private List<String> fixedAddresses;
    @JsonProperty("floating_addresses")
    @SerializedName("floating_addresses")
    private List<String> floatingAddresses;

    private Integer chargeType;
    private Integer priceUnit;
    private Integer period;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JsonProperty("expire_time")
    @SerializedName("expire_time")
    private Date expireTime;
    private String cloudId;

    @Data
    public static class OSFlavor
    {
        private String id;
        private Integer ephemeral;
        private Integer ram;
        @JsonProperty("original_name")
        @SerializedName("original_name")
        private String originalName;
        private Integer vcpus;
        private Integer swap;
        private Integer disk;
        private List<OSLink> links;
    }

    @Data
    public static class OSImage
    {
        private String id;
        private String name;
        private List<OSLink> links;
    }

    @Data
    public static class OSExtendedVolumesAttach
    {
        private String id;
        @JsonProperty("delete_on_termination")
        @SerializedName("delete_on_termination")
        private Boolean deleteOnTermination;
        private OSExtVolumeInfo volume;
    }

    @Data
    public static class OSAddress
    {
        @JsonProperty("OS-EXT-IPS-MAC:mac_addr")
        @SerializedName("OS-EXT-IPS-MAC:mac_addr")
        private String macAddr;
        private int version;
        private String addr;
        @JsonProperty("OS-EXT-IPS:type")
        @SerializedName("OS-EXT-IPS:type")
        private String type;
    }

    @Data
    public static class OSSecurityGroup
    {
        private String name;
        private String id;
    }

    public void setServerInfo(TblCmpOsInstances tblCmpOsInstance)
    {
        this.accessIPv4 = tblCmpOsInstance.getAccessIpV4();
        this.accessIPv6 = tblCmpOsInstance.getAccessIpV6();
        this.configDrive = tblCmpOsInstance.getConfigDrive();
        this.created = tblCmpOsInstance.getCreatedAt();
        this.id = tblCmpOsInstance.getUuid();
        this.keyName = tblCmpOsInstance.getKeyName();
        this.name = tblCmpOsInstance.getDisplayName();
        this.availabilityZone = tblCmpOsInstance.getAvailabilityZone();
        this.host = tblCmpOsInstance.getHost();
        this.hostname = tblCmpOsInstance.getHostname();
        this.kernelid = tblCmpOsInstance.getKernelId();
        this.launchIndex = tblCmpOsInstance.getLaunchIndex();
        this.ramdiskId = tblCmpOsInstance.getRamdiskId();
        this.reservationId = tblCmpOsInstance.getReservationId();
        this.rootDeviceName = tblCmpOsInstance.getRootDeviceName();
        this.userData = tblCmpOsInstance.getUserData();
        this.powerState = tblCmpOsInstance.getPowerState();
        this.taskState = tblCmpOsInstance.getTaskState();
        this.vmState = tblCmpOsInstance.getVmState();
        this.launchedAt = tblCmpOsInstance.getLaunchedAt();
        this.terminatedAt = tblCmpOsInstance.getTerminatedAt();
        this.status = tblCmpOsInstance.getStatus();
        this.tenantId = tblCmpOsInstance.getProjectId();
        this.updated = tblCmpOsInstance.getUpdatedAt();
        this.userId = tblCmpOsInstance.getUserId();
        this.progress = tblCmpOsInstance.getProgress();
        this.locked = BoolUtil.short2Bool(tblCmpOsInstance.getLocked(), false);
        this.description = tblCmpOsInstance.getDisplayDescription();
        this.lockedReason = tblCmpOsInstance.getLockedBy();
        if (StringUtils.isNotEmpty(tblCmpOsInstance.getInstanceTypeId()))
        {
            this.flavor = new OSFlavor();
            this.flavor.setId(tblCmpOsInstance.getInstanceTypeId());
        }
        if (StringUtils.isNotEmpty(tblCmpOsInstance.getImageRef()))
        {
            this.image = new OSImage();
            ((OSImage)this.image).setId(tblCmpOsInstance.getImageRef());
        }
        this.addresses = gson.fromJson(tblCmpOsInstance.getNetworkInfo(), new com.google.gson.reflect.TypeToken<Map<String, List<OSAddress>>>(){}.getType());

        this.fixedAddresses = new ArrayList<>();
        this.floatingAddresses = new ArrayList<>();

        if (this.addresses != null)
        {
            this.addresses.forEach((key, value) ->{
                value.forEach(osAddress -> {
                    if (osAddress.getType() == null)
                    {
                    }
                    else if (osAddress.getType().equals("fixed"))
                    {
                        this.fixedAddresses.add(osAddress.getAddr());
                    }
                    else if (osAddress.getType().equals("floating"))
                    {
                        this.floatingAddresses.add(osAddress.getAddr());
                    }
                });
            });
        }
        this.chargeType = tblCmpOsInstance.getChargeType();
        this.priceUnit = tblCmpOsInstance.getPriceUnit();
        this.period = tblCmpOsInstance.getPeriod();
        this.expireTime = tblCmpOsInstance.getExpireTime();
        this.cloudId = tblCmpOsInstance.getCloudId();
    }

    public void setInstanceExtra(TblCmpOsInstanceExtra tblCmpOsInstanceExtra)
    {

    }

    public void setInstanceFault(TblCmpOsInstanceFaults tblCmpOsInstanceFaults)
    {
        if (tblCmpOsInstanceFaults != null)
        {
            OSFault fault = new OSFault();
            fault.setCode(tblCmpOsInstanceFaults.getCode());
            fault.setCreated(tblCmpOsInstanceFaults.getCreatedAt());
            fault.setMessage(tblCmpOsInstanceFaults.getMessage());
            fault.setDetails(tblCmpOsInstanceFaults.getDetails());
            this.fault = fault;
        }
    }

    public void setVolumeAttachment(List<TblCmpOsVolumeAttachment> tblCmpOsVolumeAttachments)
    {
        if (! CollectionUtils.isEmpty(tblCmpOsVolumeAttachments))
        {
            this.osExtendedVolumesAttached = tblCmpOsVolumeAttachments.stream().map(tblCmpOsVolumeAttachment -> {
                OSExtendedVolumesAttach osExtendedVolumesAttach = new OSExtendedVolumesAttach();
                osExtendedVolumesAttach.setId(tblCmpOsVolumeAttachment.getId());
                return osExtendedVolumesAttach;
            }).collect(Collectors.toList());
        }
    }

    public void setOsFlavor(TblCmpOsFlavors tblCmpOsFlavor)
    {
        if (tblCmpOsFlavor != null)
        {
            if (this.flavor == null)
            {
                this.flavor = new OSFlavor();
            }
            this.flavor.setDisk(tblCmpOsFlavor.getRootGb());
            this.flavor.setEphemeral(tblCmpOsFlavor.getEphemeralGb());
            this.flavor.setVcpus(tblCmpOsFlavor.getVcpus());
            this.flavor.setRam(tblCmpOsFlavor.getMemoryMb());
            this.flavor.setSwap(tblCmpOsFlavor.getSwap());
            this.flavor.setOriginalName(tblCmpOsFlavor.getName());
        }
    }

    public void setOsImage(TblCmpOsImages tblCmpOsImage)
    {
        if (tblCmpOsImage != null)
        {
            if (this.image == null)
            {
                this.image = new OSImage();
            }
            ((OSImage) this.image).setName(tblCmpOsImage.getName());
        }
    }

    public void setSecuritygroup(List<TblCmpOsSecuritygroups> tblCmpOsSecuritygroups)
    {
        if (! CollectionUtils.isEmpty(tblCmpOsSecuritygroups))
        {
            this.securityGroups = tblCmpOsSecuritygroups.stream().map(tblCmpOsSecuritygroup -> {
                OSSecurityGroup osSecurityGroup = new OSSecurityGroup();
                osSecurityGroup.setId(tblCmpOsSecuritygroup.getId());
                osSecurityGroup.setName(tblCmpOsSecuritygroup.getName());
                return osSecurityGroup;
            }).collect(Collectors.toList());
        }
    }
}
