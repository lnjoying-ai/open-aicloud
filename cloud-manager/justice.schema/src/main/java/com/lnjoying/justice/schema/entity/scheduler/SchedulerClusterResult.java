package com.lnjoying.justice.schema.entity.scheduler;

import lombok.Data;

import java.util.List;


/**
 *desc assign dev to cluster
 */
@Data
public class SchedulerClusterResult
{
    //scheduler state
    private int schedulerState;
    
    //result
    private List<ClusterBindNode> bindNodes;
    
    //specId
    private String clusterId;    //clusterId
}
