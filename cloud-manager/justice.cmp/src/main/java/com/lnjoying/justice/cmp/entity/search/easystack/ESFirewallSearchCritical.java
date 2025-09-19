package com.lnjoying.justice.cmp.entity.search.easystack;

import com.lnjoying.justice.cmp.entity.search.CmpSearchCritical;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class ESFirewallSearchCritical extends CmpSearchCritical
{
    private String id;
    private String tenantId;
    private String projectId;
    private String name;
    private String description;
    private String firewallPolicyId;
    private String routerId;
    private String status;

    private Integer pageNum = 1;
    private Integer pageSize = 100;

    public ESFirewallSearchCritical(String id, String tenantId, String projectId, String name, String description,
                                    String firewallPolicyId, String routerId, String status, Integer pageNum, Integer pageSize)
    {
        this.id = id;
        this.tenantId = tenantId;
        this.projectId = projectId;
        this.name = name;
        this.description = description;
        this.firewallPolicyId = firewallPolicyId;
        this.routerId = routerId;
        this.status = status;

        if (pageNum != null) this.pageNum = pageNum;
        if (pageSize != null) this.pageSize = pageSize;
    }
}
