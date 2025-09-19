package com.lnjoying.justice.cmp.entity.search.openstack;

import com.lnjoying.justice.cmp.entity.search.CmpSearchCritical;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class OSImageSearchCritical extends CmpSearchCritical
{
    private Integer limit;
    private String marker;
    private String name;
    private String owner;
    private Boolean protect;
    private Integer status;
    private String tag;
    private String visibility;
    private Boolean osHidden;
    private String memberStatus;
    private String sizeMax;
    private String sizeMin;
    private String createdAt;
    private String updatedAt;
    private String sortDir;
    private String sortKey;
    private String sort;

    private Integer pageNum = 1;
    private Integer pageSize = 100;
    private String imageType;
    private String instanceId;
    private String statusStr;

    public OSImageSearchCritical(Integer limit, String marker, String name, String owner, Boolean protect, Integer status,
                                 String tag, String visibility, Boolean osHidden, String memberStatus, String sizeMax, String sizeMin,
                                 String createdAt, String updatedAt, String sortDir, String sortKey, String sort, Integer pageNum,
                                 Integer pageSize, String imageType, String instanceId, String statusStr)
    {
        this.limit = limit;
        this.marker = marker;
        this.name = name;
        this.owner = owner;
        this.protect = protect;
        this.status = status;
        this.tag = tag;
        this.visibility = visibility;
        this.osHidden = osHidden;
        this.memberStatus = memberStatus;
        this.sizeMax = sizeMax;
        this.sizeMin = sizeMin;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.sortDir = sortDir;
        this.sortKey = sortKey;
        this.sort = sort;

        if (pageNum != null) this.pageNum = pageNum;
        if (pageSize != null) this.pageSize = pageSize;
        this.imageType = imageType;
        this.instanceId = instanceId;
        this.statusStr = statusStr;
    }
}
