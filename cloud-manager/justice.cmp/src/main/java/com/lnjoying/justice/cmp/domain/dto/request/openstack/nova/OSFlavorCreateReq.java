package com.lnjoying.justice.cmp.domain.dto.request.openstack.nova;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class OSFlavorCreateReq
{
    private static final long serialVersionUID = 1L;
    private OSFlavorCreate flavor;

    @Data
    public static class OSFlavorCreate
    {
        private String id;
        private String name;
        private String description;

        private Integer ram;
        private Integer vcpus;
        private Integer disk;

        @JsonProperty("OS-FLV-EXT-DATA:ephemeral")
        @SerializedName("OS-FLV-EXT-DATA:ephemeral")
        private int ephemeral;
        private int swap;

        @JsonProperty("rxtx_factor")
        @SerializedName("rxtx_factor")
        private float rxtxFactor = 1.0F;

        @JsonProperty("os-flavor-access:is_public")
        @SerializedName("os-flavor-access:is_public")
        private Boolean isPublic = true;
    }
}
