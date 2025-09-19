package com.lnjoying.justice.cmp.domain.dto.response.openstack.cinder;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

@Data
public class OSVolumeTypesWithDetailsRsp
{
    @JsonProperty("volume_types")
    @SerializedName("volume_types")
    private List<OSVolumeTypeInfo> volumeTypes;
}
