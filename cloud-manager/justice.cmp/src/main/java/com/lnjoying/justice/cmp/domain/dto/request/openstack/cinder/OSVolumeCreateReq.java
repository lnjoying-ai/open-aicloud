package com.lnjoying.justice.cmp.domain.dto.request.openstack.cinder;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Map;

@Data
public class OSVolumeCreateReq
{
    private static final long serialVersionUID = 1L;
    private OSVolumeCreate volume;

    @Data
    public static class OSVolumeCreate
    {
        @JsonProperty("size")
        @SerializedName("size")
        private Integer size;
        @JsonProperty("availability_zone")
        @SerializedName("availability_zone")
        private String zone;
        @JsonProperty("source_volid")
        @SerializedName("source_volid")
        private String sourceVolid;
        @JsonProperty("description")
        @SerializedName("description")
        private String description;
        @JsonProperty("snapshot_id")
        @SerializedName("snapshot_id")
        private String snapshotId;
        @JsonProperty("backup_id")
        @SerializedName("backup_id")
        private String backupId;
        @JsonProperty("name")
        @SerializedName("name")
        private String name;
        @JsonProperty("imageRef")
        @SerializedName("imageRef")
        private String imageRef;
        @JsonProperty("volume_type")
        @SerializedName("volume_type")
        private String volumeType;
        @JsonProperty("metadata")
        @SerializedName("metadata")
        private Map<String, String> metadata;
        @JsonProperty("consistencygroup_id")
        @SerializedName("consistencygroup_id")
        private String consistencygroupId;
    }
}
