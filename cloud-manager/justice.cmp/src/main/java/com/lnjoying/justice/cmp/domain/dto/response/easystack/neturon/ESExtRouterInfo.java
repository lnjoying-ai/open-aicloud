package com.lnjoying.justice.cmp.domain.dto.response.easystack.neturon;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import com.lnjoying.justice.cmp.db.model.*;
import com.lnjoying.justice.cmp.domain.dto.response.openstack.neutron.OSExtRouterInfo;
import lombok.Data;

@Data
public class ESExtRouterInfo extends OSExtRouterInfo
{
    @JsonProperty("firewall_id")
    @SerializedName("firewall_id")
    private String firewallId;
    @JsonProperty("firewall_name")
    @SerializedName("firewall_name")
    private String firewallName;

    public void setFirewall(TblCmpEsFirewalls firewall)
    {
        if (firewall != null)
        {
            this.firewallName = firewall.getName();
        }
    }
}
