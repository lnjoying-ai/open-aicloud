package com.lnjoying.justice.cmp.domain.dto.request.openstack.cinder;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class OSBackupRestoreReq
{
    private static final long serialVersionUID = 1L;
    private OSBackupRestore restore;

    @Data
    public static class OSBackupRestore
    {
        @JsonProperty("volume_id")
        @SerializedName("volume_id")
        private String volumeId;
        @JsonProperty("name")
        @SerializedName("name")
        private String name;
    }
}
