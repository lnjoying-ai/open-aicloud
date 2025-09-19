package com.lnjoying.justice.cmp.entity.search.openstack;

import com.lnjoying.justice.cmp.entity.search.CmpSearchCritical;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class OSVolumeBackupSearchCritical extends CmpSearchCritical
{
    private String projectId;
    private String allTenants;
    private String sort;
    private Integer limit;
    private Integer offset;
    private String marker;
    private Boolean withCount;
    private String volumeId;

    private Integer pageNum = 1;
    private Integer pageSize = 100;
    private String name;

    public OSVolumeBackupSearchCritical(String projectId, String allTenants, String sort, Integer limit, Integer offset,
                                        String marker, Boolean withCount, String volumeId, Integer pageNum, Integer pageSize, String name)
    {
        this.projectId = projectId;
        this.allTenants = allTenants;
        this.sort = sort;
        this.limit = limit;
        this.offset = offset;
        this.marker = marker;
        this.withCount = withCount;
        this.volumeId = volumeId;

        if (pageNum != null) this.pageNum = pageNum;
        if (pageSize != null) this.pageSize = pageSize;
        this.name = name;
    }
}
