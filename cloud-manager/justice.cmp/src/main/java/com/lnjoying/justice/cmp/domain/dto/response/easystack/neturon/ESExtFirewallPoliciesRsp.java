package com.lnjoying.justice.cmp.domain.dto.response.easystack.neturon;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

@Data
public class ESExtFirewallPoliciesRsp
{
    @JsonProperty("firewall_policies")
    @SerializedName("firewall_policies")
    private List<ESFirewallPolicyInfo> firewallPolicies;

    @SerializedName("total_num")
    @JsonProperty("total_num")
    private long totalNum;
}
