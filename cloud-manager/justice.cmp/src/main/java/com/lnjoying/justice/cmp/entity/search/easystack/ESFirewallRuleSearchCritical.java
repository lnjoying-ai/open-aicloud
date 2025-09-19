package com.lnjoying.justice.cmp.entity.search.easystack;

import com.lnjoying.justice.cmp.entity.search.CmpSearchCritical;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class ESFirewallRuleSearchCritical extends CmpSearchCritical
{
    private String id;
    private String tenantId;
    private String projectId;
    private String name;
    private String description;
    private String firewallPolicyId;
    private String action;
    private String protocol;
    private String sourceIpAddress;
    private String sourcePort;
    private String destinationIpAddress;
    private String destinationPort;
    private Boolean shared;
    private Integer ipVersion;

    private Integer pageNum = 1;
    private Integer pageSize = 100;

    public ESFirewallRuleSearchCritical(String id, String tenantId, String projectId, String name, String description,
                                        String firewallPolicyId, String action, String protocol, String sourceIpAddress,
                                        String sourcePort, String destinationIpAddress, String destinationPort, Boolean shared,
                                        Integer ipVersion, Integer pageNum, Integer pageSize)
    {
        this.id = id;
        this.tenantId = tenantId;
        this.projectId = projectId;
        this.name = name;
        this.description = description;
        this.firewallPolicyId = firewallPolicyId;
        this.action = action;
        this.protocol = protocol;
        this.sourceIpAddress = sourceIpAddress;
        this.sourcePort = sourcePort;
        this.destinationIpAddress = destinationIpAddress;
        this.destinationPort = destinationPort;
        this.shared = shared;
        this.ipVersion = ipVersion;

        if (pageNum != null) this.pageNum = pageNum;
        if (pageSize != null) this.pageSize = pageSize;
    }
}
