package com.lnjoying.justice.cluster.manager.service.plan.k8s;

import com.lnjoying.justice.cluster.manager.common.DeployContainerName;
import com.lnjoying.justice.cluster.manager.domain.dto.model.cluster.JkeConfig;
import com.lnjoying.justice.cluster.manager.domain.model.ClusterInnerInfo;
import com.lnjoying.justice.cluster.manager.domain.model.ClusterNodePlanInfo;
import com.lnjoying.justice.cluster.manager.service.plan.ContainerPlan;
import com.lnjoying.justice.commonweb.exception.WebSystemException;
import com.lnjoying.justice.schema.common.ErrorCode;
import com.micro.core.common.Utils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @description service for building etcd plan
 * @author Regulus
 * @date 11/30/21 9:01 PM
 */
@Service("etcdPlanService")
public class EtcdPlanServiceImpl extends AbstractK8sPlanService
{
    private static Logger LOGGER = LogManager.getLogger();
    
    /**
     * @description build log linker plan
     * @author Regulus
     * @date 11/30/21 9:02 PM
     * @param jkeConfig
     * @param clusterNodePlanInfo
     * @return int
     */
    int buildLogLinkerPlan(JkeConfig jkeConfig, ClusterNodePlanInfo clusterNodePlanInfo) throws NoSuchAlgorithmException, UnsupportedEncodingException, WebSystemException
    {
        LOGGER.info("build log link  plan for {}", clusterNodePlanInfo.getClusterId());
        ContainerPlan containerPlan = new ContainerPlan(DeployContainerName.JKE_LOG_LINKER, jkeConfig.getRegistries());
        clusterNodePlanInfo.addContainerPlan(containerPlan);
        List<String> cmds = new ArrayList<>();
        cmds.add(DeployContainerName.ETCD);
        cmds.add(DeployContainerName.KUBELET);
        cmds.add(DeployContainerName.KUBE_PROXY);
        cmds.add(DeployContainerName.NGINX_PROXY);
        assembleLogLinkerParams(jkeConfig, containerPlan, cmds);
        containerPlan.setContainerName(Utils.buildStr(containerPlan.getContainerName(), "_", clusterNodePlanInfo.getClusterId().substring(0,4)));
    
        return 0;
    }
    
    @Override
    public int buildNodeDeployPlan(ClusterInnerInfo clusterInnerInfo, ClusterNodePlanInfo clusterNodePlanInfo) throws Exception
    {
        LOGGER.info("begin build etcd plan on node {}, for cluster {}", clusterNodePlanInfo.getClusterNodeInfo().getNodeId(), clusterNodePlanInfo.getClusterId());
        try
        {
           JkeConfig jkeConfig = clusterInnerInfo.getJkeConfig();
           String roles = clusterNodePlanInfo.getClusterNodeInfo().getClusterRoles();
            buildClusterCleanerDeployPlan(clusterInnerInfo.getJkeConfig(), clusterNodePlanInfo);
           
           buildPortCheckerPlan(jkeConfig, clusterNodePlanInfo, getNeedCheckPorts(roles));
           buildServiceSideKickPlan(jkeConfig, clusterNodePlanInfo);
           buildCertDeployerPlan(jkeConfig, clusterInnerInfo.getCertMap(), clusterNodePlanInfo);
           buildEtcdFixPerm(jkeConfig, clusterNodePlanInfo);
           Map<String, List<ClusterNodePlanInfo>>  clusterNodePlans = clusterInnerInfo.getClusterNodePlanMap();
           buildEtcdPlan(jkeConfig,clusterNodePlanInfo, clusterNodePlans);
           buildHealthCheckEtcd(jkeConfig, clusterNodePlanInfo);
        
           buildNginxProxyPlan(jkeConfig, clusterNodePlanInfo, clusterNodePlans);
           buildKubeletPlan(jkeConfig, clusterNodePlanInfo, clusterInnerInfo.getNetworkOption());
           buildHealthCheckKubelet(jkeConfig, clusterNodePlanInfo);
           buildKubeProxyPlan(jkeConfig,clusterNodePlanInfo);
           buildHealthChecKubeProxy(jkeConfig, clusterNodePlanInfo);
           buildLogLinkerPlan(jkeConfig, clusterNodePlanInfo);
           buildNodeLabelCfgDeployer(jkeConfig, clusterNodePlanInfo);
        }
        catch (Exception e)
        {
           LOGGER.error("build etcd deploy plan for cluster:{} error:{}", clusterInnerInfo.getClusterId(), e);
           return ErrorCode.CLUSTER_K8S_PLAN_ABNORMAL.getCode();
        }
    
        return ErrorCode.SUCCESS.getCode();
    }
    
    @Override
    public int buildNodeUnDeployPlan(ClusterInnerInfo clusterInnerInfo, ClusterNodePlanInfo clusterNodePlanInfo) throws Exception
    {
        buildClusterCleanerUnDployPlan(clusterInnerInfo.getJkeConfig(), clusterNodePlanInfo);
        buildCleanJke(clusterNodePlanInfo);
    
        return 0;
    }
}
