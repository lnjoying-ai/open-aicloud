package com.lnjoying.justice.cmp.domain.dto.response.openstack.cinder;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

@Data
public class OSExtVolumesWithDetailsRsp
{
    private List<OSExtVolumeInfo> volumes;

    @SerializedName("total_num")
    @JsonProperty("total_num")
    private long totalNum;
}
