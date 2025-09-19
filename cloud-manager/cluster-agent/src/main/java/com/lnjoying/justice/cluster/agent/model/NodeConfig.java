package com.lnjoying.justice.cluster.agent.model;

import lombok.Data;

/**
 * @Description:
 * @Author: Regulus
 * @Date: 5/19/22 8:43 PM
 */
@Data
public class NodeConfig
{
    String region_id;
    String node_id;
    String private_key;
    String core_version;
    String protocol_version;
    String node_name;
    int    magic_num = 0XE8D1A111;
    int regFlag;
    String peer_ip;
    int peer_port;
    String token;
    String k8sServiceIP;
    int k8sServicePort;
    String saToken;
    String kubeCa;
    String workerType = "k8s-agent";
    public void reset()
    {
        node_name = "";
        node_id = "";
        private_key = "";
        core_version = "";
        protocol_version = "";
    }
}