package com.lnjoying.justice.cmp.domain.dto.response.easystack.neturon;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import com.lnjoying.justice.cmp.db.model.TblCmpEsFirewallBindings;
import com.lnjoying.justice.cmp.db.model.TblCmpEsFirewallPolicies;
import com.lnjoying.justice.cmp.db.model.TblCmpEsFirewalls;
import com.lnjoying.justice.cmp.db.model.TblCmpOsRouters;
import com.lnjoying.justice.cmp.utils.BoolUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class ESExtFirewallInfo
{
    private static final long serialVersionUID = 1L;
    private String status;
    @JsonProperty("router_ids")
    @SerializedName("router_ids")
    private List<String> routerIds;
    private String name;
    @JsonProperty("admin_state_up")
    @SerializedName("admin_state_up")
    private Boolean adminStateUp;
    @JsonProperty("tenant_id")
    @SerializedName("tenant_id")
    private String tenantId;
    @JsonProperty("firewall_policy_id")
    @SerializedName("firewall_policy_id")
    private String firewallPolicyId;
    @JsonProperty("project_id")
    @SerializedName("project_id")
    private String projectId;
    private String id;
    private String description;

    @JsonProperty("firewall_policy_name")
    @SerializedName("firewall_policy_name")
    private String firewallPolicyName;
    private List<ESRouter> routers;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ESRouter
    {
        @JsonProperty("router_id")
        @SerializedName("router_id")
        private String router_id;
        @JsonProperty("router_name")
        @SerializedName("router_name")
        private String router_name;
    }

    public void setFirewallInfo(TblCmpEsFirewalls tblCmpEsFirewall)
    {
        this.status = tblCmpEsFirewall.getStatus();
        this.name = tblCmpEsFirewall.getName();
        this.adminStateUp = BoolUtil.short2Bool(tblCmpEsFirewall.getAdminStateUp());
        this.tenantId = tblCmpEsFirewall.getProjectId();
        this.firewallPolicyId = tblCmpEsFirewall.getFirewallPolicyId();
        this.projectId = tblCmpEsFirewall.getProjectId();
        this.id = tblCmpEsFirewall.getId();
        this.description = tblCmpEsFirewall.getDescription();
    }

    public void setRouterIds(List<TblCmpEsFirewallBindings> firewallBindings)
    {
        if (! CollectionUtils.isEmpty(firewallBindings))
        {
            this.routerIds = firewallBindings.stream().map(TblCmpEsFirewallBindings::getRouterId).collect(Collectors.toList());
        }
    }

    public void setFirewallPolicy(TblCmpEsFirewallPolicies tblCmpEsFirewallPolicy)
    {
        if (tblCmpEsFirewallPolicy != null)
        {
            this.firewallPolicyName = tblCmpEsFirewallPolicy.getName();
        }
    }

    public void setRouter(List<TblCmpOsRouters> tblCmpOsRouters)
    {
        if (! CollectionUtils.isEmpty(tblCmpOsRouters))
        {
            this.routers = tblCmpOsRouters.stream().map(tblCmpOsRouter -> new ESRouter(tblCmpOsRouter.getId(), tblCmpOsRouter.getName())).collect(Collectors.toList());
        }
    }
}
