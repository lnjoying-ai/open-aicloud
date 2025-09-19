package com.lnjoying.justice.cmp.domain.dto.request.easystack.neutron;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class ESFirewallPolicyUpdateReq
{
    private static final long serialVersionUID = 1L;

    @JsonProperty("firewall_policy")
    @SerializedName("firewall_policy")
    private ESFirewallPolicyUpdate firewallPolicy;

    @Data
    public static class ESFirewallPolicyUpdate
    {
        private String name;
        private String description;
    }
}
