package com.lnjoying.justice.cmp.entity.search.openstack;

import com.lnjoying.justice.cmp.entity.search.CmpSearchCritical;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class OSFlavorSearchCritical extends CmpSearchCritical
{
    private String sortKey;
    private String sortDir;
    private Integer limit;
    private String marker;
    private Integer minDisk;
    private Integer minRam;
    private String isPublic;

    private Integer pageNum = 1;
    private Integer pageSize = 100;

    public OSFlavorSearchCritical(String sortKey, String sortDir, Integer limit, String marker, Integer minDisk, Integer minRam,
                                  String isPublic, Integer pageNum, Integer pageSize)
    {
        this.sortKey = sortKey;
        this.sortDir = sortDir;
        this.limit = limit;
        this.marker = marker;
        this.minDisk = minDisk;
        this.minRam = minRam;
        this.isPublic = isPublic;

        if (pageNum != null) this.pageNum = pageNum;
        if (pageSize != null) this.pageSize = pageSize;
    }
}
