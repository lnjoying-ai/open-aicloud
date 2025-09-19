package com.lnjoying.justice.cmp.domain.dto.response.easystack.neturon;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class ESFirewallRuleRsp
{
    @JsonProperty("firewall_rule")
    @SerializedName("firewall_rule")
    private ESFirewallRuleInfo firewallRule;
}
