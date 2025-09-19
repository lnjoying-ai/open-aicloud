package com.lnjoying.justice.cmp.domain.dto.response.openstack.neutron;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class OSExtSecurityGroupsWithDetailsRsp extends OSSecurityGroupsWithDetailsRsp
{
    @SerializedName("total_num")
    @JsonProperty("total_num")
    private long totalNum;
}
