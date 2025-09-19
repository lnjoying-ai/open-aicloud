package com.lnjoying.justice.schema.entity.scheduler;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * @ClassName RefBindNode
 */
@Data
public class ClusterBindNode implements Serializable
{
    String                    clusterRoles;
    String                          nodeId;
    String                        regionId;
    String                          siteId;
    String                        nodeName;
    
    String                 internalAddress;
    String                 externalAddress;
    
//    DockerInfo dockerInfo;
    Map<String,String>       softWareInfos;
    Map<String,String>              labels;
    Map<String,String>              taints;
    Map<String,String>         annotations;
}
