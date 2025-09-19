package com.lnjoying.justice.cluster.manager.common;

/**
 * @Author: Regulus
 * @Date: 11/19/21 7:39 PM
 * @Description:
 */
public interface K8sServiceModule
{
    String ETCD            = "etcd";
    String KUBE_APISERVER  = "kube-apiserver";
    String KUBELET         = "kubelet";
    String KUBE_PROXY      = "kube-proxy";
    String KUBE_CONTROLLER = "kube-controller";
    String KUBE_SCHEDULER  = "kube-scheduler";
}
