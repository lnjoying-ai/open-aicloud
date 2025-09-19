package com.lnjoying.justice.cmp.entity.search.easystack;

import com.lnjoying.justice.cmp.entity.search.openstack.OSRouterSearchCritical;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class ESRouterSearchCritical extends OSRouterSearchCritical
{
    private String firewallId;

    public ESRouterSearchCritical(String id, String tenantId, String projectId, String name, String description, Boolean adminStateUp,
                                  Integer revisionNumber, String sortDir, String sortKey, String tags, String tagsAny, String notTags,
                                  String notTagsAny, String fields, Integer pageNum, Integer pageSize, String status, String firewallId)
    {
        super(id, tenantId, projectId, name, description, adminStateUp, revisionNumber, sortDir, sortKey, tags, tagsAny,
                notTags, notTagsAny, fields, pageNum, pageSize, status);
        this.firewallId = firewallId;
    }
}
