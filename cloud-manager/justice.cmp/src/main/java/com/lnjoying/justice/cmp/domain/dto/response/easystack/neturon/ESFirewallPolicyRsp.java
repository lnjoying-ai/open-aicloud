package com.lnjoying.justice.cmp.domain.dto.response.easystack.neturon;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class ESFirewallPolicyRsp
{
    @JsonProperty("firewall_policy")
    @SerializedName("firewall_policy")
    private ESFirewallPolicyInfo firewallPolicy;
}
