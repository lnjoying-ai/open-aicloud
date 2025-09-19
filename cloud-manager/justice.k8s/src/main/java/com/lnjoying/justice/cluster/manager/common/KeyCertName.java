package com.lnjoying.justice.cluster.manager.common;

/**
 * @Author: Regulus
 * @Date: 11/17/21 10:02 AM
 * @Description:
 */
public interface KeyCertName
{
    String CACertName                 = "kube-ca";
    String RequestHeaderCACertName    = "kube-apiserver-requestheader-ca";
    String KubeAPICertName            = "kube-apiserver";
    String KubeControllerCertName     = "kube-controller-manager";
    String KubeSchedulerCertName      = "kube-scheduler";
    String KubeProxyCertName          = "kube-proxy";
    String KubeNodeCertName           = "kube-node";
    String KubeletCertName            = "kube-kubelet";
    String EtcdCertName               = "kube-etcd";
    String EtcdClientCACertName       = "kube-etcd-client-ca";
    String EtcdClientCertName         = "kube-etcd-client";
    String APIProxyClientCertName     = "kube-apiserver-proxy-client";
    String ServiceAccountTokenKeyName = "kube-service-account-token";
    String KubeNodeCommonName         = "system:node";
    String KubeNodeOrganizationName   = "system:nodes";
    String KubeAdminCertName          = "kube-admin";
    String KubeAdminOrganizationName  = "system:masters";
    String KubeAdminConfigPrefix      = "kube_config_";
    String ClusterConfig              = "cluster.yml";
    String ClusterServerCACertName    = "cluster-server-ca";
    String ClusterServerCertName    = "cluster-server";

    /**
     * k3s server ca
     */
    String K3S_SERVER_CA = "server-ca";
    String K3S_KUBE_API = "client-kube-apiserver";


}
