package com.lnjoying.justice.cluster.manager.common;

public interface ClusterMsg
{
    String ASSIGN                = "assign_dev";
    String RELEASE               = "release_dev";
    String UPDATE_CLUSTER        = "update_cluster";
    String UPDATE_NODE           = "update_node";
    
    String PREPARE_BUILD           = "prepare_build";
    
    String GEN_CERT              = "gen_cert";
    String BUILD_DEPLOY_PLAN     = "build_deploy_plan";
    String BUILD_UNDEPLOY_PLAN   = "build_undeploy_plan";
    String DEPLOY                = "deploy";
    String UNDEPLOY              = "undeploy";
    String MONITOR               = "monitor";
    String SYNC_WORKER_IF_STATE  = "sync_worker_if_state";
}
