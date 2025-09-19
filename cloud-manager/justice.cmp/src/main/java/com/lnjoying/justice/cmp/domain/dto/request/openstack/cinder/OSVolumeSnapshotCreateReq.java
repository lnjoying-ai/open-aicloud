package com.lnjoying.justice.cmp.domain.dto.request.openstack.cinder;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.Map;

@Data
public class OSVolumeSnapshotCreateReq
{
    private static final long serialVersionUID = 1L;
    private OSVolumeSnapshotCreate snapshot;

    @Data
    public static class OSVolumeSnapshotCreate
    {
        @JsonProperty("volume_id")
        @SerializedName("volume_id")
        private String volumeId;
        @JsonProperty("name")
        private String name;
        @JsonProperty("description")
        private String description;
        private Boolean force;
        private Map<String, String> metadata;
    }
}
