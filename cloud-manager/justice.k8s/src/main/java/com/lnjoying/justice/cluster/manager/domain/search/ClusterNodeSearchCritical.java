package com.lnjoying.justice.cluster.manager.domain.search;

import com.lnjoying.justice.schema.entity.search.PageSearchCritical;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: Regulus
 * @Date: 12/14/21 10:44 AM
 * @Description:
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClusterNodeSearchCritical extends PageSearchCritical
{
    private String clusterId;
    private String operUserId;
    private String operBpId;
}
