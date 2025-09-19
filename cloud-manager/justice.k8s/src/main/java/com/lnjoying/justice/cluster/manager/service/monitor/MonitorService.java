package com.lnjoying.justice.cluster.manager.service.monitor;

import com.lnjoying.justice.cluster.manager.domain.model.ClusterNodePlanInfo;
import com.lnjoying.justice.commonweb.exception.WebSystemException;
import com.lnjoying.justice.schema.common.ErrorCode;

public interface MonitorService
{
    ErrorCode monitor(ClusterNodePlanInfo clusterNodePlanInfo)  throws WebSystemException;
}
