package com.lnjoying.justice.cmp.entity.search.openstack;

import com.lnjoying.justice.cmp.entity.search.CmpSearchCritical;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class OSKeyPairSearchCritical extends CmpSearchCritical
{
    private String userId;
    private Integer limit;
    private String marker;

    private Integer pageNum = 1;
    private Integer pageSize = 100;
    private String name;

    public OSKeyPairSearchCritical(String userId, Integer limit, String marker, Integer pageNum, Integer pageSize, String name)
    {
        this.userId = userId;
        this.limit = limit;
        this.marker = marker;

        if (pageNum != null) this.pageNum = pageNum;
        if (pageSize != null) this.pageSize = pageSize;
        this.name = name;
    }
}
