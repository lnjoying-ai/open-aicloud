package com.lnjoying.justice.cmp.entity.search.nextstack.vm;

import com.lnjoying.justice.schema.entity.search.PageSearchCritical;
import lombok.Data;

@Data
public class VmInstanceSearchCritical extends PageSearchCritical
{
    private String name;
    private String vmInstanceId;
    private Boolean portIdIsNull;
    private Boolean eipMapIsUsing;
    private String subnetId;
    private String nodeId;
    private String instanceGroupId;
    private Boolean instanceGroupIdIsNull;
    private String eipId;
    private Boolean eipIdIsNull;
    private Boolean ignoreFailed;
}
