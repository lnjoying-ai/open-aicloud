package com.lnjoying.justice.cmp.domain.dto.response.openstack.neutron;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class OSSecurityGroupWithDetailsRsp
{
    @JsonProperty("security_group")
    @SerializedName("security_group")
    private OSSecurityGroupInfo securityGroup;
}
