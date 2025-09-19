package com.lnjoying.justice.cmp.domain.dto.response.openstack.neutron;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

@Data
public class OSSecurityGroupsWithDetailsRsp
{
    @JsonProperty("security_groups")
    @SerializedName("security_groups")
    private List<OSSecurityGroupInfo> securityGroups;
}
