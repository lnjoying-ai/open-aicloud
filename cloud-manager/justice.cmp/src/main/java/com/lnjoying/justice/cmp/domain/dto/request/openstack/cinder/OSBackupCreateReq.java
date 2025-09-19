package com.lnjoying.justice.cmp.domain.dto.request.openstack.cinder;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.Map;

@Data
public class OSBackupCreateReq
{
    private static final long serialVersionUID = 1L;
    private OSBackupCreate backup;

    @Data
    public static class OSBackupCreate
    {
        @JsonProperty("volume_id")
        @SerializedName("volume_id")
        private String volumeId;
        @JsonProperty("container")
        @SerializedName("container")
        private String container;
        @JsonProperty("description")
        @SerializedName("description")
        private String description;
        private Boolean incremental;
        private Boolean force;
        @JsonProperty("name")
        @SerializedName("name")
        private String name;
        @JsonProperty("snapshot_id")
        @SerializedName("snapshot_id")
        private String snapshotId;
        @JsonProperty("metadata")
        @SerializedName("metadata")
        private Map<String, String> metadata;
        @JsonProperty("availability_zone")
        @SerializedName("availability_zone")
        private String availabilityZone;
    }
}
