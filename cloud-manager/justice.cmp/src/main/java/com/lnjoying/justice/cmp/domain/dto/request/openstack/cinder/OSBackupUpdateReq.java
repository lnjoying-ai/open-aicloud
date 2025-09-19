package com.lnjoying.justice.cmp.domain.dto.request.openstack.cinder;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.Map;

@Data
public class OSBackupUpdateReq
{
    private static final long serialVersionUID = 1L;
    private OSBackupUpdate backup;

    @Data
    public static class OSBackupUpdate
    {
        @JsonProperty("description")
        @SerializedName("description")
        private String description;
        @JsonProperty("name")
        @SerializedName("name")
        private String name;
        @JsonProperty("metadata")
        @SerializedName("metadata")
        private Map<String, String> metadata;
    }
}
