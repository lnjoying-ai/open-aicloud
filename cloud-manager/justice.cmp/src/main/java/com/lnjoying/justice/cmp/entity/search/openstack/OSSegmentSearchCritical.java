package com.lnjoying.justice.cmp.entity.search.openstack;

import com.lnjoying.justice.cmp.entity.search.CmpSearchCritical;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class OSSegmentSearchCritical extends CmpSearchCritical
{
    private String id;
    private String networkId;
    private String physicalNetwork;
    private String networkType;
    private Integer revisionNumber;
    private Integer segmentationId;
    private String name;
    private String description;
    private String sortDir;
    private String sortKey;
    private String fields;

    public OSSegmentSearchCritical(String id, String networkId, String physicalNetwork, String networkType, Integer revisionNumber,
                                   Integer segmentationId, String name, String description, String sortDir, String sortKey, String fields)
    {
        this.id = id;
        this.networkId = networkId;
        this.physicalNetwork = physicalNetwork;
        this.networkType = networkType;
        this.revisionNumber = revisionNumber;
        this.segmentationId = segmentationId;
        this.name = name;
        this.description = description;
        this.sortDir = sortDir;
        this.sortKey = sortKey;
        this.fields = fields;
    }
}
