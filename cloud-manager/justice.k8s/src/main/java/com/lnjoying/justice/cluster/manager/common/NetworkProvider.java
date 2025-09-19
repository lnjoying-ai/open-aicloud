package com.lnjoying.justice.cluster.manager.common;

/**
 * @Author: Regulus
 * @Date: 11/26/21 10:41 AM
 * @Description:
 */
public interface NetworkProvider
{
    String 	FlannelNetworkPlugin    = "flannel";
    String 	CalicoNetworkPlugin     = "calico";
    String 	CanalNetworkPlugin      = "canal";
    String  WeaveNetworkPlugin      = "weave";
    String  AciNetworkPlugin        = "aci";
}
