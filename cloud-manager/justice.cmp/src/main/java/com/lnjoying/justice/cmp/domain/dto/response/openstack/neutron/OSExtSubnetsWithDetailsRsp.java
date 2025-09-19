package com.lnjoying.justice.cmp.domain.dto.response.openstack.neutron;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class OSExtSubnetsWithDetailsRsp extends OSSubnetsWithDetailsRsp
{
    @SerializedName("total_num")
    @JsonProperty("total_num")
    private long totalNum;
}
