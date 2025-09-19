package com.lnjoying.justice.cmp.domain.dto.response.openstack.neutron;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class OSSecurityGroupRuleWithDetailsRsp
{
    @JsonProperty("security_group_rule")
    @SerializedName("security_group_rule")
    private OSSecurityGroupRuleInfo securityGroupRule;
}
