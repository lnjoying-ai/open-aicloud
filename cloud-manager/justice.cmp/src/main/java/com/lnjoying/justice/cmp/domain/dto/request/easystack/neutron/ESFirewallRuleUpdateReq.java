package com.lnjoying.justice.cmp.domain.dto.request.easystack.neutron;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class ESFirewallRuleUpdateReq
{
    private static final long serialVersionUID = 1L;
    @JsonProperty("firewall_rule")
    @SerializedName("firewall_rule")
    private ESFirewallRuleCreate firewallRule;

    @Data
    public static class ESFirewallRuleCreate
    {
        private String action;
        private String description;
        @JsonProperty("destination_ip_address")
        @SerializedName("destination_ip_address")
        private String destinationIpAddress;
        @JsonProperty("destination_port")
        @SerializedName("destination_port")
        private String destinationPort;
        private String name;
        private String protocol;
        private Boolean shared;
        @JsonProperty("source_ip_address")
        @SerializedName("source_ip_address")
        private String sourceIpAddress;
        @JsonProperty("source_port")
        @SerializedName("source_port")
        private String sourcePort;
    }
}
