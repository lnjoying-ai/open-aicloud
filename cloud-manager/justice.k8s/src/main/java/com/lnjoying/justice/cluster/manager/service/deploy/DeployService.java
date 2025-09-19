package com.lnjoying.justice.cluster.manager.service.deploy;

import com.lnjoying.justice.cluster.manager.domain.model.ClusterNodePlanInfo;
import com.lnjoying.justice.commonweb.exception.WebSystemException;
import com.lnjoying.justice.schema.common.ErrorCode;

public interface DeployService
{
    ErrorCode deploy(ClusterNodePlanInfo clusterNodePlanInfo)  throws WebSystemException;
    ErrorCode undeploy(ClusterNodePlanInfo clusterNodePlanInfo)  throws WebSystemException;
}
