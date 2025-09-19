package com.lnjoying.justice.cmp.domain.dto.response.openstack.cinder;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import com.lnjoying.justice.cmp.db.model.TblCmpOsBackups;
import com.lnjoying.justice.cmp.db.model.TblCmpOsVolumes;
import com.lnjoying.justice.cmp.domain.dto.response.openstack.OSLink;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OSExtVolumeBackupInfo
{
    private static final long serialVersionUID = 1L;
    private String status;
    @JsonProperty("object_count")
    @SerializedName("object_count")
    private Integer objectCount;
    @JsonProperty("fail_reason")
    @SerializedName("fail_reason")
    private String failReason;
    private String description;
    private List<OSLink> links;
    @JsonProperty("availability_zone")
    @SerializedName("availability_zone")
    private String zone;
    @JsonProperty("created_at")
    @SerializedName("created_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdAt;
    @JsonProperty("updated_at")
    @SerializedName("updated_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updatedAt;
    @JsonProperty("name")
    @SerializedName("name")
    private String name;
    @JsonProperty("has_dependent_backups")
    @SerializedName("has_dependent_backups")
    private Boolean hasDependentBackups;
    @JsonProperty("volume_id")
    @SerializedName("volume_id")
    private String volumeId;
    private String container;
    @JsonProperty("size")
    @SerializedName("size")
    private Integer size;
    private String id;
    @JsonProperty("is_incremental")
    @SerializedName("is_incremental")
    private Boolean isIncremental = false;
    @JsonProperty("data_timestamp")
    @SerializedName("data_timestamp")
    private Date dataTimestamp;
    @JsonProperty("snapshot_id")
    @SerializedName("snapshot_id")
    private String snapshotId;
    @JsonProperty("os-backup-project-attr:project_id")
    @SerializedName("os-backup-project-attr:project_id")
    private String projectId;
    @JsonProperty("metadata")
    @SerializedName("metadata")
    private Map<String, String> metadata;
    @JsonProperty("user_id")
    @SerializedName("user_id")
    private String userId;
    @JsonProperty("encryption_key_id")
    @SerializedName("encryption_key_id")
    private String encryptionKeyId;

    @JsonProperty("volume_name")
    @SerializedName("volume_name")
    private String volumeName;

    public void setBackupInfo(TblCmpOsBackups tblCmpOsBackup)
    {
        this.status = tblCmpOsBackup.getStatus();
        this.objectCount = tblCmpOsBackup.getObjectCount();
        this.failReason = tblCmpOsBackup.getFailReason();
        this.description = tblCmpOsBackup.getDisplayDescription();
        this.zone = tblCmpOsBackup.getAvailabilityZone();
        this.createdAt = tblCmpOsBackup.getCreatedAt();
        this.updatedAt = tblCmpOsBackup.getUpdatedAt();
        this.name = tblCmpOsBackup.getDisplayName();
        this.volumeId = tblCmpOsBackup.getVolumeId();
        this.container = tblCmpOsBackup.getContainer();
        this.size = tblCmpOsBackup.getSize();
        this.id = tblCmpOsBackup.getId();
        this.dataTimestamp = tblCmpOsBackup.getDataTimestamp();
        this.snapshotId = tblCmpOsBackup.getSnapshotId();
        this.projectId = tblCmpOsBackup.getProjectId();
        this.userId = tblCmpOsBackup.getUserId();
        this.encryptionKeyId = tblCmpOsBackup.getEncryptionKeyId();
    }

    public void setVolumeInfo(TblCmpOsVolumes tblCmpOsVolume)
    {
        if (tblCmpOsVolume != null)
        {
            this.volumeName = tblCmpOsVolume.getDisplayName();
        }
    }
}
