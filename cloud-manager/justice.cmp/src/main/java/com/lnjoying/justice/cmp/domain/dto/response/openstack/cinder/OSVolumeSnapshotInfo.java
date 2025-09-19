package com.lnjoying.justice.cmp.domain.dto.response.openstack.cinder;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import com.lnjoying.justice.cmp.db.model.TblCmpOsSnapshots;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OSVolumeSnapshotInfo
{
    private static final long serialVersionUID = 1L;
    private String status;
    @JsonProperty("os-extended-snapshot-attributes:progress")
    @SerializedName("os-extended-snapshot-attributes:progress")
    private String progress;
    @JsonProperty("description")
    @SerializedName("description")
    private String description;
    @JsonProperty("created_at")
    @SerializedName("created_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss", timezone = "GMT+8")
    private Date createdAt;
    @JsonProperty("name")
    @SerializedName("name")
    private String name;
    @JsonProperty("user_id")
    @SerializedName("user_id")
    private String userId;
    @JsonProperty("volume_id")
    @SerializedName("volume_id")
    private String volumeId;
    @JsonProperty("os-extended-snapshot-attributes:project_id")
    @SerializedName("os-extended-snapshot-attributes:project_id")
    private String projectId;
    @JsonProperty("size")
    @SerializedName("size")
    private Integer size;
    private String id;
    @JsonProperty("metadata")
    @SerializedName("metadata")
    private Map<String, String> metadata;
    @JsonProperty("updated_at")
    @SerializedName("updated_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss", timezone = "GMT+8")
    private Date updatedAt;
    @JsonProperty("group_snapshot_id")
    @SerializedName("group_snapshot_id")
    private String groupSnapshotId;
    @JsonProperty("consumes_quota")
    @SerializedName("consumes_quota")
    private Boolean consumesQuota;

    public void setSnapshotInfo(TblCmpOsSnapshots tblCmpOsSnapshot)
    {
        this.status = tblCmpOsSnapshot.getStatus();
        this.progress = tblCmpOsSnapshot.getProgress();
        this.description = tblCmpOsSnapshot.getDisplayDescription();
        this.createdAt = tblCmpOsSnapshot.getCreatedAt();
        this.name = tblCmpOsSnapshot.getDisplayName();
        this.userId = tblCmpOsSnapshot.getUserId();
        this.volumeId = tblCmpOsSnapshot.getVolumeId();
        this.projectId = tblCmpOsSnapshot.getProjectId();
        this.size = tblCmpOsSnapshot.getVolumeSize();
        this.id = tblCmpOsSnapshot.getId();
        this.updatedAt = tblCmpOsSnapshot.getUpdatedAt();
        this.groupSnapshotId = tblCmpOsSnapshot.getGroupSnapshotId();
    }
}
