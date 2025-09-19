package com.lnjoying.justice.cmp.domain.dto.response.openstack.neutron;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

@Data
public class OSSecurityGroupRulesWithDetailsRsp
{
    @JsonProperty("security_group_rules")
    @SerializedName("security_group_rules")
    private List<OSSecurityGroupRuleInfo> securityGroupRules;
}
