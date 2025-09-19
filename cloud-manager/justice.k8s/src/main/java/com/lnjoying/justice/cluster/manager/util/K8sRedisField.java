package com.lnjoying.justice.cluster.manager.util;

import com.lnjoying.justice.schema.common.RedisCacheField;

public interface K8sRedisField
{
    String ADDONFTL_PREFIX           = "cluster-manager:addon-ftl:";
    String ADDONFTL_KEY_PREFIX       = ADDONFTL_PREFIX + "key:";
    String ADDONFTL_TS_PREFIX        = ADDONFTL_PREFIX + "timestamp:";  //ts:timestamp
    
    String K8S_SERVICE_VERSION       = "cluster-manager:service-version:";
    String K8S_IMAGE_VERSION         = "cluster-manager:image-version:";
    String K8S_ADDON_VERSION         = "cluster-manager:addon-version:";
    String PLAN_CLUSTERID_SET        = "cluster-manager:plan-cluster-set";
    String DEPLOY_CLUSTERID_SET      = "cluster-manager:deploy-cluster-set";
    String UNDEPLOY_CLUSTERID_SET    = "cluster-manager:undeploy-cluster-set";
    String MONITOR_CLUSTERID_SET    = "cluster-manager:monitor-cluster-set";
    String DEPLOY_CLUSTER_NODE_LIST  = "cluster-manager:deploy-node-list:";
    String UNDEPLOY_CLUSTER_NODE_LIST  = "cluster-manager:undeploy-node-list:";
    String MONITOR_CLUSTER_NODE_LIST  = "cluster-manager:monitor-node-list:";

    String DEPLOY_CLUSTER_NODE       = "cluster-manager:deploy-cluster-node:%s:%s";
    String UNDEPLOY_CLUSTER_NODE       = "cluster-manager:undeploy-cluster-node:%s:%s";
    String MONITOR_CLUSTER_NODE       = "cluster-manager:monitor-cluster-node:%s:%s";

    //    String K8S_SERVICE_CONFIG_FILE   = "cluster-manager:service_config_file";
    String K8S_LOCAL_CONFIG_FILE     = "cluster-manager:local_config_file";
    String K8S_CLUSTER_INNER_INFO    = "cluster-manager:inner_info:";
//    String K8S_UNDEPLOYING_SET       = "cluster-manager:udeploy-cluster-set";

    //CLS:WORKER:IF:{worker_id}                  worker if.
    String WORKER_IF = RedisCacheField.CLS + ":" + RedisCacheField.WORKER + ":" + RedisCacheField.IF + ":";
    //CLS:REGION:{region_id}:{gw_id}
    String REGION_GW_WORKER = RedisCacheField.CLS + ":" + RedisCacheField.REGION + ":";
}
