package com.lnjoying.justice.cmp.domain.dto.request.openstack.cinder;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class OSVolumeUpdateReq
{
    private static final long serialVersionUID = 1L;
    private OSVolumeUpdate volume;

    @Data
    public static class OSVolumeUpdate
    {
        @JsonProperty("description")
        @SerializedName("description")
        private String description;
        @JsonProperty("name")
        @SerializedName("name")
        private String name;
    }
}
