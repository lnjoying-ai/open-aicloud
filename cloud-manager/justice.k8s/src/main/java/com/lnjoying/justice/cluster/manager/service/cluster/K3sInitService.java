package com.lnjoying.justice.cluster.manager.service.cluster;

import com.lnjoying.justice.cluster.manager.domain.dto.model.cluster.K3sConfig;
import com.lnjoying.justice.cluster.manager.domain.model.ClusterInnerInfo;
import com.lnjoying.justice.cluster.manager.domain.model.NetworkOption;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2022/8/12 20:42
 */

@Service
public class K3sInitService
{
    public void initNetworkOption(ClusterInnerInfo clusterInnerInfo)
    {
        NetworkOption networkOption = clusterInnerInfo.getNetworkOption();
        K3sConfig k3sConfig = clusterInnerInfo.getK3sConfig();
        K3sConfig.NetworkConfig networkConfig = k3sConfig.getNetworkConfig();
        if (Objects.nonNull(networkConfig))
        {
            networkOption.setClusterCidr(networkConfig.getClusterCidr());
            networkOption.setClusterDnsServer(networkConfig.getClusterDns());
            networkOption.setClusterDomain(networkConfig.getClusterDomain());
        }

    }
}
