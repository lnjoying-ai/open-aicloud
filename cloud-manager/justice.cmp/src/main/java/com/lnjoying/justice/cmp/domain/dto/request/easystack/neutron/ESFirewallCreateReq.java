package com.lnjoying.justice.cmp.domain.dto.request.easystack.neutron;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

@Data
public class ESFirewallCreateReq
{
    private static final long serialVersionUID = 1L;

    private ESFirewallCreate firewall;

    @Data
    public static class ESFirewallCreate
    {
        private String name;
        private String description;
        @JsonProperty("firewall_policy_id")
        @SerializedName("firewall_policy_id")
        private String firewallPolicyId;
        @JsonProperty("router_ids")
        @SerializedName("router_ids")
        private List<String> routerIds;
    }
}
