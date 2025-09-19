package com.lnjoying.justice.cluster.manager.service.plan;

import com.lnjoying.justice.cluster.manager.domain.model.ClusterInnerInfo;
import com.lnjoying.justice.cluster.manager.domain.model.ClusterNodePlanInfo;

/**
 * @description interface for plan service
 * @author Regulus
 * @date 11/30/21 9:52 PM
 */
public interface PlanService
{
    int buildNodeDeployPlan(ClusterInnerInfo clusterInnerInfo, ClusterNodePlanInfo clusterNodePlanInfo)throws Exception;
    int buildNodeUnDeployPlan(ClusterInnerInfo clusterInnerInfo, ClusterNodePlanInfo clusterNodePlanInfo)throws Exception;
    
}
