package com.lnjoying.justice.cluster.manager.common;

/**
 * @Author: Regulus
 * @Date: 12/9/21 2:22 PM
 * @Description:
 */
public interface K8sDefaultValue
{
    String DefaultDockerSockPath = "/var/run/docker.sock";
    
    String DefaultAuthStrategy      = "x509";
    String RBACAuthorizationMode = "rbac";
    
    String DefaultAuthnCacheTimeout = "5s";
    String DefaultAuthnWebhookFile = "authn-webhook-file";
    
    String DefaultNetworkCloudProvider = "none";
    String NginxIngressController    = "nginx";
    String K8sVersionCoreDNS  = "v1.14.0";
    String DefaultCalicoFlexVolPluginDirectory = "/usr/libexec/kubernetes/kubelet-plugins/volume/exec/nodeagent~uds";
    String DefaultCanalFlexVolPluginDirectory  = "/usr/libexec/kubernetes/kubelet-plugins/volume/exec/nodeagent~uds";
    String DefaultMonitoringProvider            = "metrics-server";
    
    String DefaultFlannelBackendVxLan     = "vxlan";
    String DefaultFlannelBackendVxLanPort = "8472";
    String DefaultFlannelBackendVxLanVNI  = "1";
    boolean DefaultEtcdSnapshot                  = true;
    
    String DefaultServiceClusterIPRange = "10.43.0.0/16";
    String DefaultNodePortRange         = "30000-32767";
    String DefaultClusterCIDR           = "10.42.0.0/16";
    String DefaultClusterDNSService     = "10.43.0.10";
    String DefaultClusterDomain         = "cluster.local";
    String DefaultClusterName           = "local";
    String DefaultClusterSSHKeyPath     = "~/.ssh/id_rsa";
    String DefaultEtcdBackupCreationPeriod      = "12h";
    String DefaultEtcdBackupRetentionPeriod     = "72h";
    String 	DefaultEtcdElectionTimeoutValue     = "5000";
    String 	DefaultEtcdHeartbeatIntervalValue = "500";
    int DefaultEtcdBackupConfigIntervalHours = 12;
    int DefaultEtcdBackupConfigRetention     = 6;
    int DefaultEtcdBackupConfigTimeout       = DockerDefaultValue.WaitTimeout;
    String ServerLimitType  = "Server";
    String DefaultKubeAPIArgAuditLogPathValue    = "/var/log/kube-audit/audit-log.json";
    String 	DefaultNetworkMode                = "hostNetwork";
    String DefaultNetworkModeV121            = "hostPort";
    int DefaultHTTPPort                   = 80;
    int DefaultHTTPSPort                  = 443;
    
    String DefaultMaxUnavailableWorker       = "10%";
    String DefaultMaxUnavailableController   = "1";
    
    int DefaultNodeDrainTimeout           = 120;
    int DefaultNodeDrainGracePeriod       = -1;
    boolean DefaultNodeDrainIgnoreDaemonsets = true;
    int DeployJobTime = 120;
}
