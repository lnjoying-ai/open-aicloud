package com.lnjoying.justice.cmp.domain.dto.response.easystack.neturon;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import com.lnjoying.justice.cmp.db.model.TblCmpEsFirewallRules;
import com.lnjoying.justice.cmp.utils.BoolUtil;
import lombok.Data;

@Data
public class ESFirewallRuleInfo
{
    private static final long serialVersionUID = 1L;

    private String action;
    private String description;
    @JsonProperty("destination_ip_address")
    @SerializedName("destination_ip_address")
    private String destinationIpAddress;
    @JsonProperty("destination_port")
    @SerializedName("destination_port")
    private String destinationPort;
    private Boolean enabled;
    @JsonProperty("firewall_policy_id")
    @SerializedName("firewall_policy_id")
    private String firewallPolicyId;
    private String id;
    @JsonProperty("ip_version")
    @SerializedName("ip_version")
    private Integer ipVersion;
    private String name;
    @JsonProperty("project_id")
    @SerializedName("project_id")
    private String projectId;
    private String protocol;
    private Boolean shared;
    @JsonProperty("source_ip_address")
    @SerializedName("source_ip_address")
    private String sourceIpAddress;
    @JsonProperty("source_port")
    @SerializedName("source_port")
    private String sourcePort;
    @JsonProperty("tenant_id")
    @SerializedName("tenant_id")
    private String tenantId;
    private Integer position;

    public void setFirewallRuleInfo(TblCmpEsFirewallRules tblCmpEsFirewallRule)
    {
        this.action = tblCmpEsFirewallRule.getAction();
        this.description = tblCmpEsFirewallRule.getDescription();
        this.destinationIpAddress = tblCmpEsFirewallRule.getDestinationIpAddress();
        this.destinationPort = tblCmpEsFirewallRule.getDestinationPort();
        this.enabled = BoolUtil.short2Bool(tblCmpEsFirewallRule.getEnabled());
        this.firewallPolicyId = tblCmpEsFirewallRule.getFirewallPolicyId();
        this.id = tblCmpEsFirewallRule.getId();
        this.ipVersion = tblCmpEsFirewallRule.getIpVersion();
        this.name = tblCmpEsFirewallRule.getName();
        this.projectId = tblCmpEsFirewallRule.getProjectId();
        this.protocol = tblCmpEsFirewallRule.getProtocol();
        this.shared =  BoolUtil.short2Bool(tblCmpEsFirewallRule.getShared());
        this.sourceIpAddress = tblCmpEsFirewallRule.getSourceIpAddress();
        this.sourcePort = tblCmpEsFirewallRule.getSourcePort();
        this.tenantId = tblCmpEsFirewallRule.getTenantId();
        this.position = tblCmpEsFirewallRule.getPosition();
    }
}
