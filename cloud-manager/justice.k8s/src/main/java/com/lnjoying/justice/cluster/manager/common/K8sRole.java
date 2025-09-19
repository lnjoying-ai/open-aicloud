package com.lnjoying.justice.cluster.manager.common;

public interface K8sRole
{
    String ETCD          = "etcd";
    String WORKER        = "worker";
    String CONTROLLER    = "controller";
    String SYSTEM_ADDON  = "system_addon";
}
