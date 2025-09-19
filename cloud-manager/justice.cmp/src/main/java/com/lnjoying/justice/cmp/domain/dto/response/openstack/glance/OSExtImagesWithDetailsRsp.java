package com.lnjoying.justice.cmp.domain.dto.response.openstack.glance;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class OSExtImagesWithDetailsRsp extends OSImagesWithDetailsRsp
{
    @SerializedName("total_num")
    @JsonProperty("total_num")
    private long totalNum;
}
