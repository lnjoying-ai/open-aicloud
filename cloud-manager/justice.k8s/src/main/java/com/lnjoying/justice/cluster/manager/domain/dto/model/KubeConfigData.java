package com.lnjoying.justice.cluster.manager.domain.dto.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: kubeconfig file
 * @Author: Merak
 * @Date: 2022/3/16 19:12
 */
@Data
public class KubeConfigData
{
    private String ClusterName;

    private String host;

    private String user;

    private String clusterId;

    private String cert;

    private String username;

    private String password;

    private String token;

    private boolean endpointEnabled;

    private List<KubeNode> nodes = new ArrayList<>();

    @Data
    public static class KubeNode
    {
        private String clusterName;

        private String server;

        private String cert;

        private String user;
    }
}
