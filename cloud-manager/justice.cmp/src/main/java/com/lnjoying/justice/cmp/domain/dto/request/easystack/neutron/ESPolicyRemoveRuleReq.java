package com.lnjoying.justice.cmp.domain.dto.request.easystack.neutron;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class ESPolicyRemoveRuleReq
{
    @JsonProperty("firewall_rule_id")
    @SerializedName("firewall_rule_id")
    private String firewallRuleId;
}
