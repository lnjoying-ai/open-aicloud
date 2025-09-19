package com.lnjoying.justice.cmp.entity.search.openstack;

import com.lnjoying.justice.cmp.entity.search.CmpSearchCritical;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class OSFloatingIPSearchCritical extends CmpSearchCritical
{
    private String id;
    private String routerId;
    private String status;
    private String tenantId;
    private String projectId;
    private Integer revisionNumber;
    private String description;
    private Boolean distributed;
    private String floatingNetworkId;
    private String fixedIpAddress;
    private String floatingIpAddress;
    private String portId;
    private String sortDir;
    private String sortKey;
    private String tags;
    private String tagsAny;
    private String notTags;
    private String notTagsAny;
    private String fields;

    private Integer pageNum = 1;
    private Integer pageSize = 100;

    public OSFloatingIPSearchCritical(String id, String routerId, String status, String tenantId, String projectId, Integer revisionNumber,
                                      String description, Boolean distributed, String floatingNetworkId, String fixedIpAddress,
                                      String floatingIpAddress, String portId, String sortDir, String sortKey, String tags,
                                      String tagsAny, String notTags, String notTagsAny, String fields, Integer pageNum,
                                      Integer pageSize)
    {
        this.id = id;
        this.routerId = routerId;
        this.status = status;
        this.tenantId = tenantId;
        this.projectId = projectId;
        this.revisionNumber = revisionNumber;
        this.description = description;
        this.distributed = distributed;
        this.floatingNetworkId = floatingNetworkId;
        this.fixedIpAddress = fixedIpAddress;
        this.floatingIpAddress = floatingIpAddress;
        this.portId = portId;
        this.sortDir = sortDir;
        this.sortKey = sortKey;
        this.tags = tags;
        this.tagsAny = tagsAny;
        this.notTags = notTags;
        this.notTagsAny = notTagsAny;
        this.fields = fields;

        if (pageNum != null) this.pageNum = pageNum;
        if (pageSize != null) this.pageSize = pageSize;
    }
}
