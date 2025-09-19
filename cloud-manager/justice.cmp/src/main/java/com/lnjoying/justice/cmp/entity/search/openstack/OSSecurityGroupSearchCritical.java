package com.lnjoying.justice.cmp.entity.search.openstack;

import com.lnjoying.justice.cmp.entity.search.CmpSearchCritical;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class OSSecurityGroupSearchCritical extends CmpSearchCritical
{
    private String id;
    private String tenantId;
    private String projectId;
    private Integer revisionNumber;
    private String name;
    private String description;
    private String sortDir;
    private String sortKey;
    private Boolean shared;
    private String tags;
    private String tagsAny;
    private String notTags;
    private String notTagsAny;
    private String fields;

    private Integer pageNum = 1;
    private Integer pageSize = 100;

    public OSSecurityGroupSearchCritical(String id, String tenantId, String projectId, Integer revisionNumber, String name,
                                         String description, String sortDir, String sortKey, Boolean shared, String tags,
                                         String tagsAny, String notTags, String notTagsAny, String fields, Integer pageNum,
                                         Integer pageSize)
    {
        this.id = id;
        this.tenantId = tenantId;
        this.projectId = projectId;
        this.revisionNumber = revisionNumber;
        this.name = name;
        this.description = description;
        this.sortDir = sortDir;
        this.sortKey = sortKey;
        this.shared = shared;
        this.tags = tags;
        this.tagsAny = tagsAny;
        this.notTags = notTags;
        this.notTagsAny = notTagsAny;
        this.fields = fields;

        if (pageNum != null) this.pageNum = pageNum;
        if (pageSize != null) this.pageSize = pageSize;
    }
}
