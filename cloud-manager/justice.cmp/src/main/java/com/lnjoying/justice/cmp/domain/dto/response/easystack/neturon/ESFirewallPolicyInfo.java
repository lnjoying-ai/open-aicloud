package com.lnjoying.justice.cmp.domain.dto.response.easystack.neturon;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import com.lnjoying.justice.cmp.db.model.*;
import com.lnjoying.justice.cmp.utils.BoolUtil;
import lombok.Data;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class ESFirewallPolicyInfo
{
    private static final long serialVersionUID = 1L;

    private Boolean audited;
    private String description;
    @JsonProperty("firewall_rules")
    @SerializedName("firewall_rules")
    private List<String> firewallRules;
    private String id;
    private String name;
    @JsonProperty("project_id")
    @SerializedName("project_id")
    private String projectId;
    private Boolean shared;
    @JsonProperty("tenant_id")
    @SerializedName("tenant_id")
    private String tenantId;
    @JsonProperty("firewall_list")
    @SerializedName("firewall_list")
    private List<String> firewallList;

    @JsonProperty("firewall_rule_list")
    @SerializedName("firewall_rule_list")
    private List<ESFirewallRuleInfo> firewallRuleList;

    public void setFirewallPolicyInfo(TblCmpEsFirewallPolicies TblCmpEsFirewallPolicy)
    {
        this.audited = BoolUtil.short2Bool(TblCmpEsFirewallPolicy.getAudited());
        this.description = TblCmpEsFirewallPolicy.getDescription();
        this.id = TblCmpEsFirewallPolicy.getId();
        this.name = TblCmpEsFirewallPolicy.getName();
        this.projectId = TblCmpEsFirewallPolicy.getProjectId();
        this.shared =  BoolUtil.short2Bool(TblCmpEsFirewallPolicy.getShared());
        this.tenantId = TblCmpEsFirewallPolicy.getTenantId();
    }

    public void setRules(List<TblCmpEsFirewallRules> firewallRules)
    {
        if (! CollectionUtils.isEmpty(firewallRules))
        {
            this.firewallRules = firewallRules.stream().map(TblCmpEsFirewallRules::getId).collect(Collectors.toList());
            this.firewallRuleList = firewallRules.stream().map(tblCmpEsFirewallRule -> {
                ESFirewallRuleInfo firewallRuleInfo = new ESFirewallRuleInfo();
                firewallRuleInfo.setFirewallRuleInfo(tblCmpEsFirewallRule);
                return firewallRuleInfo;
            }).collect(Collectors.toList());
        }
    }

    public void setFirewalls(List<TblCmpEsFirewalls> firewalls)
    {
        if (! CollectionUtils.isEmpty(firewalls))
        {
            this.firewallList = firewalls.stream().map(TblCmpEsFirewalls::getId).collect(Collectors.toList());
        }
    }
}
