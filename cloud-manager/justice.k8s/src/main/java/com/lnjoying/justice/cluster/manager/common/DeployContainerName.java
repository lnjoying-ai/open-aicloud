package com.lnjoying.justice.cluster.manager.common;

public interface DeployContainerName
{
    String KUBE_APISERVER            = "kube-apiserver";
    String KUBE_SCHEDULER            = "kube-scheduler";
    String KUBE_CONTROLLER_MANAGER   = "kube-controller-manager";
    String KUBELET                   = "kubelet";
    String KUBE_PROXY                = "kube-proxy";
    String ETCD                      = "etcd";
    String NGINX_PROXY               = "nginx-proxy";
    String JKE_CLUSTER_CLEANER       = "jke-cluster-cleaner";
    String JKE_SIDEKICK              = "jke-sidekick";
    String JKE_PORT_CHECKER          = "jke-port-checker";
    String CERT_DEPLOYER             = "cert-deployer";
    String FILE_DEPLOYER             = "file-deployer";
    String JKE_LOG_LINKER            = "jke-log-linker";
    String JKE_RESOURCE_AUTHER       = "jke-resource-auther";
    String JKE_HEALTH_CHECKER        = "jke-health-checker";
    String JKE_ADDON_DEPLOYER        = "jke-addon-deployer";
    String JKE_CLUSTER_AGENT         = "jke-cluster-agent";
    String ETCD_FIX_PERM             = "etcd-fix-perm";
    String NODE_LABEL_DEPLOYER       = "node-label-deployer";

    String JKE_K3S_CLEANER = "jke-k3s-cleaner";

    // String KUBE_CONTROLLER_MANAGER   = "kube-controller-manager";
    String JKE_K3S_CONTROLLER = "k3s-server";

    String JKE_K3S_WORKER = "k3s-worker";

    String JKE_HEALTH_CHECK_K3S = "jke-health-check-k3s";

    String JKE_CERT_DEPLOYER             = "jke-cert-deployer";

    String FILE_DOWNLOAD = "file-download";
}
