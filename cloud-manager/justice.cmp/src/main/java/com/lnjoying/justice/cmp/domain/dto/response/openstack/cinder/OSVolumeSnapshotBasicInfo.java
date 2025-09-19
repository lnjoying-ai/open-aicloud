package com.lnjoying.justice.cmp.domain.dto.response.openstack.cinder;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import com.lnjoying.justice.cmp.db.model.TblCmpOsSnapshots;
import lombok.Data;

import java.util.Date;
import java.util.Map;

@Data
public class OSVolumeSnapshotBasicInfo
{
    private static final long serialVersionUID = 1L;
    private String status;
    @JsonProperty("description")
    @SerializedName("description")
    private String description;
    @JsonProperty("created_at")
    @SerializedName("created_at")
    private Date createdAt;
    @JsonProperty("name")
    @SerializedName("name")
    private String name;
    @JsonProperty("volume_id")
    @SerializedName("volume_id")
    private String volumeId;
    @JsonProperty("metadata")
    @SerializedName("metadata")
    private Map<String, String> metadata;
    private String id;
    @JsonProperty("size")
    @SerializedName("size")
    private Integer size;
    @JsonProperty("updated_at")
    @SerializedName("updated_at")
    private Date updatedAt;

    public void setSnapshotBasicInfo(TblCmpOsSnapshots tblCmpOsSnapshot)
    {
        this.status = tblCmpOsSnapshot.getStatus();
        this.description = tblCmpOsSnapshot.getDisplayDescription();
        this.createdAt = tblCmpOsSnapshot.getCreatedAt();
        this.name = tblCmpOsSnapshot.getDisplayName();
        this.volumeId = tblCmpOsSnapshot.getVolumeId();
        this.id = tblCmpOsSnapshot.getId();
        this.size = tblCmpOsSnapshot.getVolumeSize();
        this.updatedAt = tblCmpOsSnapshot.getUpdatedAt();
    }
}
