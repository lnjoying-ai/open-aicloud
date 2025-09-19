package com.lnjoying.justice.cmp.domain.dto.request.easystack.neutron;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

@Data
public class ESFirewallUpdateReq
{
    private static final long serialVersionUID = 1L;
    private ESFirewallUpdate firewall;

    @Data
    public static class ESFirewallUpdate
    {
        private String name;
        private String description;
        @JsonProperty("router_ids")
        @SerializedName("router_ids")
        private List<String> routerIds;
    }
}
