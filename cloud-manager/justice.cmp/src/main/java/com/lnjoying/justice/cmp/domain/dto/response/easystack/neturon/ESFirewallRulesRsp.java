package com.lnjoying.justice.cmp.domain.dto.response.easystack.neturon;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

@Data
public class ESFirewallRulesRsp
{
    @JsonProperty("firewall_rules")
    @SerializedName("firewall_rules")
    private List<ESFirewallRuleInfo> firewallRules;
}
