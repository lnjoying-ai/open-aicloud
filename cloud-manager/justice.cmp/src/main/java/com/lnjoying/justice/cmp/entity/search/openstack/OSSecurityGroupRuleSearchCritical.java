package com.lnjoying.justice.cmp.entity.search.openstack;

import com.lnjoying.justice.cmp.entity.search.CmpSearchCritical;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class OSSecurityGroupRuleSearchCritical extends CmpSearchCritical
{
    private String remoteGroupId;
    private String direction;
    private String protocol;
    private String ethertype;
    private Integer portRangeMax;
    private String securityGroupId;
    private String tenantId;
    private String projectId;
    private Integer portRangeMin;
    private String remoteIpPrefix;
    private Integer revisionNumber;
    private String id;
    private String description;
    private String sortDir;
    private String sortKey;
    private String fields;

    private Integer pageNum = 1;
    private Integer pageSize = 100;

    public OSSecurityGroupRuleSearchCritical(String remoteGroupId, String direction, String protocol, String ethertype,
                                             Integer portRangeMax, String securityGroupId, String tenantId, String projectId,
                                             Integer portRangeMin, String remoteIpPrefix, Integer revisionNumber, String id,
                                             String description, String sortDir, String sortKey, String fields, Integer pageNum,
                                             Integer pageSize)
    {
        this.remoteGroupId = remoteGroupId;
        this.direction = direction;
        this.protocol = protocol;
        this.ethertype = ethertype;
        this.portRangeMax = portRangeMax;
        this.securityGroupId = securityGroupId;
        this.tenantId = tenantId;
        this.projectId = projectId;
        this.portRangeMin = portRangeMin;
        this.remoteIpPrefix = remoteIpPrefix;
        this.revisionNumber = revisionNumber;
        this.id = id;
        this.description = description;
        this.sortDir = sortDir;
        this.sortKey = sortKey;
        this.fields = fields;

        if (pageNum != null) this.pageNum = pageNum;
        if (pageSize != null) this.pageSize = pageSize;
    }
}
