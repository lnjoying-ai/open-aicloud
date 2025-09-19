package com.lnjoying.justice.cluster.manager.service.plan.k8s;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lnjoying.justice.cluster.manager.common.DeployContainerName;
import com.lnjoying.justice.cluster.manager.common.DockerNetworkMode;
import com.lnjoying.justice.cluster.manager.domain.dto.model.cluster.JkeConfig;
import com.lnjoying.justice.cluster.manager.domain.model.ClusterInnerInfo;
import com.lnjoying.justice.cluster.manager.domain.model.ClusterNodePlanInfo;
import com.lnjoying.justice.cluster.manager.service.plan.ContainerPlan;
import com.lnjoying.justice.cluster.manager.util.ClsUtils;
import com.lnjoying.justice.commonweb.exception.WebSystemException;
import com.lnjoying.justice.commonweb.util.CollectionUtils;
import com.lnjoying.justice.schema.common.ErrorCode;
import com.lnjoying.justice.schema.entity.docker.DockerInstParams;
import com.micro.core.common.Utils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @description worker plan service
 * @author Regulus
 * @date 11/30/21 9:46 PM
 */
@Service("workerPlanService")
public class WorkerPlanServiceImpl extends AbstractK8sPlanService
{
    private static Logger LOGGER = LogManager.getLogger();

    /**
     * @description log linker plan
     * @author Regulus
     * @date 11/30/21 9:47 PM
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
        LOGGER.info("begin build worker plan on node {}, for cluster {}", clusterNodePlanInfo.getClusterNodeInfo().getNodeId(), clusterNodePlanInfo.getClusterId());
        try
        {
            JkeConfig jkeConfig = clusterInnerInfo.getJkeConfig();
            String roles = clusterNodePlanInfo.getClusterNodeInfo().getClusterRoles();
            buildClusterCleanerDeployPlan(clusterInnerInfo.getJkeConfig(), clusterNodePlanInfo);
    
            buildPortCheckerPlan(jkeConfig, clusterNodePlanInfo, getNeedCheckPorts(roles));
            buildFileDeployer(jkeConfig, clusterNodePlanInfo);
            buildCertDeployerPlan(jkeConfig, clusterInnerInfo.getCertMap(), clusterNodePlanInfo);
            buildServiceSideKickPlan(jkeConfig, clusterNodePlanInfo);
            buildNginxProxyPlan(jkeConfig, clusterNodePlanInfo, clusterInnerInfo.getClusterNodePlanMap());
            buildKubeletPlan(jkeConfig, clusterNodePlanInfo, clusterInnerInfo.getNetworkOption());
            buildHealthCheckKubelet(jkeConfig, clusterNodePlanInfo);
            buildKubeProxyPlan(jkeConfig,clusterNodePlanInfo);
            buildHealthChecKubeProxy(jkeConfig, clusterNodePlanInfo);
            buildLogLinkerPlan(jkeConfig, clusterNodePlanInfo);
            buildNodeLabelCfgDeployer(jkeConfig, clusterNodePlanInfo);
        }
        catch (Exception e)
        {
            LOGGER.error("build work deploy plan for cluster:{} error:{}", clusterInnerInfo.getClusterId(), e);
            return ErrorCode.CLUSTER_K8S_PLAN_ABNORMAL.getCode();
        }
        return ErrorCode.SUCCESS.getCode();
    }
    
    @Override
    public int buildNodeUnDeployPlan(ClusterInnerInfo clusterInnerInfo, ClusterNodePlanInfo clusterNodePlanInfo) throws Exception
    {
        JkeConfig jkeConfig = clusterInnerInfo.getJkeConfig();
        buildClusterCleanerUnDployPlan(jkeConfig, clusterNodePlanInfo);
        buildCleanJke(clusterNodePlanInfo);
        return 0;
    }
    
    protected void assembleFileDeployer(JkeConfig jkeConfig, ClusterNodePlanInfo clusterNodePlanInfo, ContainerPlan containerPlan ) throws JsonProcessingException
    {
        DockerInstParams dockerInstParams = containerPlan.getDockerInstParams();
        dockerInstParams.setEntrypoint(new String[]{DefaultToolsEntrypoint});
        dockerInstParams.setImage(clusterDataService.getImage(jkeConfig.getK8sVersion(), containerPlan.getContainerName()));
        
        
        if (! CollectionUtils.isEmpty(jkeConfig.getRegistries()))
        {
            List<String> envList = new ArrayList<>();
    
            try
            {
                String kubeletDockerConfig = getKubeletDockerConfig(jkeConfig.getRegistries());
                String dockerEnv = String.format("%s=%s", KubeletDockerConfigEnv, ClsUtils.getBase64(kubeletDockerConfig));
                envList.add(dockerEnv);
    
                String dockerFileEnv = String.format("%s=%s", KubeletDockerConfigFileEnv, KubeletDockerConfigPath);
                envList.add(dockerFileEnv);
                dockerInstParams.setEnv(envList);
            }
            catch (UnsupportedEncodingException e)
            {
                e.printStackTrace();
            }
        }
        
        dockerInstParams.getHostConfig().setPrivileged(false);
        dockerInstParams.getHostConfig().setNetworkMode(DockerNetworkMode.HOST);
        clusterNodePlanInfo.getContainerPlan().setAutoRun(true);
        dockerInstParams.setEntrypoint(new String[]{"file-deploy"});
        dockerInstParams.getHostConfig().setBinds(Arrays.asList(ETC_K8S_PATH_BIND, REGISTRY_PATH_BIND));
    }
    
    protected int  buildFileDeployer(JkeConfig jkeConfig, ClusterNodePlanInfo clusterNodePlanInfo) throws JsonProcessingException
    {
        if (CollectionUtils.isEmpty(jkeConfig.getRegistries()))
        {
            LOGGER.info("registry is empty ,use default for cluster {}", clusterNodePlanInfo.getClusterId());
    
            return 0;
        }
        
        LOGGER.info("build kubelet registry plan for {}", clusterNodePlanInfo.getClusterId());
        ContainerPlan containerPlan = new ContainerPlan(DeployContainerName.FILE_DEPLOYER, jkeConfig.getRegistries());
        assembleFileDeployer(jkeConfig, clusterNodePlanInfo, containerPlan);
        clusterNodePlanInfo.addContainerPlan(containerPlan);
        return 0;
    }
}
