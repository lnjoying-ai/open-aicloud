package com.lnjoying.justice.cmp.entity.search.easystack;

import com.lnjoying.justice.cmp.entity.search.CmpSearchCritical;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class ESFirewallPoliciesSearchCritical extends CmpSearchCritical
{
    private String id;
    private String tenantId;
    private String projectId;
    private String name;
    private String description;
    private Boolean shared;

    private Integer pageNum = 1;
    private Integer pageSize = 100;

    public ESFirewallPoliciesSearchCritical(String id, String tenantId, String projectId, String name, String description,
                                            Boolean shared, Integer pageNum, Integer pageSize)
    {
        this.id = id;
        this.tenantId = tenantId;
        this.projectId = projectId;
        this.name = name;
        this.description = description;
        this.shared = shared;

        if (pageNum != null) this.pageNum = pageNum;
        if (pageSize != null) this.pageSize = pageSize;
    }
}
