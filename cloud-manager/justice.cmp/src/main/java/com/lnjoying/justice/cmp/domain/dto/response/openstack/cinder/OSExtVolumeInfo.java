package com.lnjoying.justice.cmp.domain.dto.response.openstack.cinder;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.lnjoying.justice.cmp.db.model.TblCmpOsSnapshots;
import com.lnjoying.justice.cmp.db.model.TblCmpOsVolumeAttachment;
import com.lnjoying.justice.cmp.db.model.TblCmpOsVolumeTypes;
import com.lnjoying.justice.cmp.db.model.TblCmpOsVolumes;
import com.lnjoying.justice.cmp.domain.dto.response.openstack.OSLink;
import com.lnjoying.justice.cmp.utils.BoolUtil;
import com.lnjoying.justice.commonweb.util.JsonUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.openstack4j.model.storage.block.Volume;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OSExtVolumeInfo
{

    private static final long serialVersionUID = 1L;
    @JsonProperty("migration_status")
    @SerializedName("migration_status")
    private String migrationStatus;
    @JsonProperty("attachments")
    @SerializedName("attachments")
    private List<OSExtVolumeAttachmentInfo> attachments;
    private List<OSLink> links;
    @JsonProperty("availability_zone")
    @SerializedName("availability_zone")
    private String zone;
    @JsonProperty("os-vol-host-attr:host")
    @SerializedName("os-vol-host-attr:host")
    private String host;
    @JsonProperty("encrypted")
    @SerializedName("encrypted")
    private Boolean encrypted;
    @JsonProperty("encryption_key_id")
    @SerializedName("encryption_key_id")
    private String encryptionKeyId;
    @JsonProperty("updated_at")
    @SerializedName("updated_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss", timezone = "GMT+8")
    private Date updatedAt;
    @JsonProperty("replication_status")
    @SerializedName("replication_status")
    private String replicationStatus;
    @JsonProperty("snapshot_id")
    @SerializedName("snapshot_id")
    private String snapshotId;
    private String id;
    @JsonProperty("size")
    @SerializedName("size")
    private Integer size;
    @JsonProperty("user_id")
    @SerializedName("user_id")
    private String userId;
    @JsonProperty("os-vol-tenant-attr:tenant_id")
    @SerializedName("os-vol-tenant-attr:tenant_id")
    private String tenantId;
    @JsonProperty("os-vol-mig-status-attr:migstat")
    @SerializedName("os-vol-mig-status-attr:migstat")
    private Volume.MigrationStatus migrateStatus;
    @JsonProperty("metadata")
    @SerializedName("metadata")
    private Map<String, String> metadata;
    private String status;
    @JsonProperty("volume_image_metadata")
    @SerializedName("volume_image_metadata")
    private Map<String, Object> imageMetadata;
    @JsonProperty("description")
    @SerializedName("description")
    private String description;
    @JsonProperty("multiattach")
    @SerializedName("multiattach")
    private Boolean multiattach;
    @JsonProperty("source_volid")
    @SerializedName("source_volid")
    private String sourceVolid;
    @JsonProperty("consistencygroup_id")
    @SerializedName("consistencygroup_id")
    private String consistencygroupId;
    @JsonProperty("os-vol-mig-status-attr:name_id")
    @SerializedName("os-vol-mig-status-attr:name_id")
    private String nameId;
    @JsonProperty("name")
    @SerializedName("name")
    private String name;
    @JsonProperty("bootable")
    @SerializedName("bootable")
    private Boolean bootable;
    @JsonProperty("created_at")
    @SerializedName("created_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss", timezone = "GMT+8")
    private Date createdAt;
    @JsonProperty("volume_type")
    @SerializedName("volume_type")
    private String volumeType;
    @JsonProperty("volume_type_id")
    @SerializedName("volume_type_id")
    private String volumeTypeId;
    @JsonProperty("group_id")
    @SerializedName("group_id")
    private String groupId;
    @JsonProperty("provider_id")
    @SerializedName("provider_id")
    private String providerId;
    @JsonProperty("service_uuid")
    @SerializedName("service_uuid")
    private String serviceUuid;
    @JsonProperty("shared_targets")
    @SerializedName("shared_targets")
    private Boolean sharedTargets;
    @JsonProperty("cluster_name")
    @SerializedName("cluster_name")
    private String clusterName;
    @JsonProperty("consumes_quota")
    @SerializedName("consumes_quota")
    private Boolean consumesQuota;

    private Integer chargeType;
    private Integer priceUnit;
    private Integer period;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JsonProperty("expire_time")
    @SerializedName("expire_time")
    private Date expireTime;
    @JsonProperty("snapshot_name")
    @SerializedName("snapshot_name")
    private String snapshotName;
    private Integer source;
    @JsonProperty("delete_on_termination")
    @SerializedName("delete_on_termination")
    private Boolean deleteOnTermination;

    @Data
    public static class OSExtVolumeAttachmentInfo
    {
        @JsonProperty("server_id")
        @SerializedName("server_id")
        private String serverId;
        @JsonProperty("attachment_id")
        @SerializedName("attachment_id")
        private String attachmentId;
        @JsonProperty("attached_at")
        @SerializedName("attached_at")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss", timezone = "GMT+8")
        private Date attachedAt;
        @JsonProperty("host_name")
        @SerializedName("host_name")
        private String hostName;
        @JsonProperty("volume_id")
        @SerializedName("volume_id")
        private String volumeId;
        private String device;
        private String id;

        @JsonProperty("server_name")
        @SerializedName("server_name")
        private String serverName;
    }

    public void setVolumeInfo(TblCmpOsVolumes tblCmpOsVolume)
    {
        this.migrationStatus = tblCmpOsVolume.getMigrationStatus();
        this.zone = tblCmpOsVolume.getAvailabilityZone();
        this.host = tblCmpOsVolume.getHost();
        this.encryptionKeyId = tblCmpOsVolume.getEncryptionKeyId();
        this.updatedAt = tblCmpOsVolume.getUpdatedAt();
        this.replicationStatus = tblCmpOsVolume.getReplicationStatus();
        this.snapshotId = tblCmpOsVolume.getSnapshotId();
        this.id = tblCmpOsVolume.getId();
        this.size = tblCmpOsVolume.getSize();
        this.userId = tblCmpOsVolume.getUserId();
        this.tenantId = tblCmpOsVolume.getProjectId();
        this.status = tblCmpOsVolume.getStatus();
        if (StringUtils.isNotEmpty(tblCmpOsVolume.getVolumeGlanceMetadata()))
        {
            this.imageMetadata = JsonUtils.fromJson(tblCmpOsVolume.getVolumeGlanceMetadata(), new TypeToken<Map<String, Object>>(){}.getType());
        }
        this.description = tblCmpOsVolume.getDisplayDescription();
        this.multiattach = BoolUtil.short2Bool(tblCmpOsVolume.getMultiattch());
        this.sourceVolid = tblCmpOsVolume.getSourceVolid();
        this.consistencygroupId = tblCmpOsVolume.getConsistencygroupId();
        this.nameId = tblCmpOsVolume.getNameId();
        this.name = tblCmpOsVolume.getDisplayName();
        this.bootable = BoolUtil.short2Bool(tblCmpOsVolume.getBootable());
        this.createdAt = tblCmpOsVolume.getCreatedAt();
        this.volumeTypeId = tblCmpOsVolume.getVolumeTypeId();
        this.groupId = tblCmpOsVolume.getGroupId();
        this.providerId = tblCmpOsVolume.getProviderId();
        this.serviceUuid = tblCmpOsVolume.getServiceUuid();
        this.sharedTargets = BoolUtil.short2Bool(tblCmpOsVolume.getSharedTargets());
        this.clusterName = tblCmpOsVolume.getClusterName();
        this.chargeType = tblCmpOsVolume.getChargeType();
        this.priceUnit = tblCmpOsVolume.getPriceUnit();
        this.period = tblCmpOsVolume.getPeriod();
        this.expireTime = tblCmpOsVolume.getExpireTime();
    }

    public void setVolumeAttachment(List<TblCmpOsVolumeAttachment> tblCmpOsVolumeAttachments)
    {
        if (! CollectionUtils.isEmpty(tblCmpOsVolumeAttachments))
        {
            this.attachments = tblCmpOsVolumeAttachments.stream().map(tblCmpOsVolumeAttachment -> {
                OSExtVolumeAttachmentInfo osExtendedVolumesAttach = new OSExtVolumeAttachmentInfo();
                osExtendedVolumesAttach.setServerId(tblCmpOsVolumeAttachment.getInstanceUuid());
                osExtendedVolumesAttach.setAttachmentId(tblCmpOsVolumeAttachment.getId());
                osExtendedVolumesAttach.setAttachedAt(tblCmpOsVolumeAttachment.getAttachTime());
                osExtendedVolumesAttach.setHostName(tblCmpOsVolumeAttachment.getAttachedHost());
                osExtendedVolumesAttach.setVolumeId(tblCmpOsVolumeAttachment.getVolumeId());
                osExtendedVolumesAttach.setDevice(tblCmpOsVolumeAttachment.getMountpoint());
                osExtendedVolumesAttach.setId(tblCmpOsVolumeAttachment.getId());
                return osExtendedVolumesAttach;
            }).collect(Collectors.toList());
        }
    }

    public void setSnapshotName(TblCmpOsSnapshots tblCmpOsSnapshot)
    {
        if (tblCmpOsSnapshot != null)
        {
            this.snapshotName = tblCmpOsSnapshot.getDisplayName();
        }
    }

    public void setVolumeType(TblCmpOsVolumeTypes tblCmpOsVolumeType)
    {
        if (tblCmpOsVolumeType != null)
        {
            this.volumeType = tblCmpOsVolumeType.getName();
        }
    }
}
