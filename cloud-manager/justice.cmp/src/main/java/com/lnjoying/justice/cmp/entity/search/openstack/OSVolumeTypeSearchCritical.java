package com.lnjoying.justice.cmp.entity.search.openstack;

import com.lnjoying.justice.cmp.entity.search.CmpSearchCritical;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class OSVolumeTypeSearchCritical extends CmpSearchCritical
{
    private String projectId;
    private Boolean isPublic;
    private String sort;
    private Integer limit;
    private Integer offset;
    private String marker;

    private Integer pageNum = 1;
    private Integer pageSize = 100;

    public OSVolumeTypeSearchCritical(String projectId, Boolean isPublic, String sort, Integer limit, Integer offset,
                                      String marker, Integer pageNum, Integer pageSize)
    {
        this.projectId = projectId;
        this.isPublic = isPublic;
        this.sort = sort;
        this.limit = limit;
        this.offset = offset;
        this.marker = marker;

        if (pageNum != null) this.pageNum = pageNum;
        if (pageSize != null) this.pageSize = pageSize;
    }
}
