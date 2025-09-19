package com.lnjoying.justice.cluster.manager.domain.search;

import com.lnjoying.justice.schema.entity.search.PageSearchCritical;
import lombok.Data;

/**
 * @Author: Regulus
 * @Date: 12/13/21 11:11 AM
 * @Description:
 */
@Data
public class ClusterSearchCritical extends PageSearchCritical
{
    private String name;
    private String sharedUser;
    private String owner;
    private String operUserId;
    private String operBpId;
}
