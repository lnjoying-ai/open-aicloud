package com.lnjoying.justice.servicegw.model;

import com.lnjoying.justice.schema.entity.cluster.ClusterEntity;
import lombok.Data;

/**
 * @Description:
 * @Author: Regulus
 * @Date: 2/26/22 11:52 AM
 */
@Data
public class ClusterProxyMsg
{
    private Object reqObj;
    private String srcHost;
    private ClusterEntity clusterEntity;
}
