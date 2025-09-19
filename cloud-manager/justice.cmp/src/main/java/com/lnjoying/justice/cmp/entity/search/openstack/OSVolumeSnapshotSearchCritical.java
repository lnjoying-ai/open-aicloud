package com.lnjoying.justice.cmp.entity.search.openstack;

import com.lnjoying.justice.cmp.entity.search.CmpSearchCritical;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class OSVolumeSnapshotSearchCritical extends CmpSearchCritical
{
    private String projectId;
    private String allTenants;
    private String sort;
    private Integer limit;
    private Integer offset;
    private String marker;
    private Boolean withCount;
    private Boolean consumesQuota;
    private String volumeId;

    private Integer pageNum = 1;
    private Integer pageSize = 100;
    private String name;
    private String status;

    public OSVolumeSnapshotSearchCritical(String projectId, String allTenants, String sort, Integer limit, Integer offset,
                                          String marker, Boolean withCount, Boolean consumesQuota, String volumeId, Integer pageNum,
                                          Integer pageSize, String name, String status)
    {
        this.projectId = projectId;
        this.allTenants = allTenants;
        this.sort = sort;
        this.limit = limit;
        this.offset = offset;
        this.marker = marker;
        this.withCount = withCount;
        this.consumesQuota = consumesQuota;
        this.volumeId = volumeId;

        if (pageNum != null) this.pageNum = pageNum;
        if (pageSize != null) this.pageSize = pageSize;
        this.name = name;
        this.status = status;
    }
}
