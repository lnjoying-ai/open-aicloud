package com.lnjoying.justice.cluster.manager.util;

import com.lnjoying.justice.cluster.manager.config.ClusterManagerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2022/12/2 19:36
 */

@Component
public class ClusterConfigUtils
{
    private static ClusterManagerConfig clusterManagerConfig;

    @Autowired
    public void setClusterManagerConfig(ClusterManagerConfig clusterManagerConfig)
    {
        ClusterConfigUtils.clusterManagerConfig = clusterManagerConfig;
    }

    public static String getSystemRegistryUrl()
    {
        return clusterManagerConfig.getClusterComponentRegistryUrl();
    }
}
