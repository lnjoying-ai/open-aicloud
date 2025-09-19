package com.lnjoying.justice.cluster.manager.service.plan.k3s;

import com.lnjoying.justice.cluster.manager.common.*;
import com.lnjoying.justice.cluster.manager.config.EnvCfg;
import com.lnjoying.justice.cluster.manager.db.model.TblClusterInfo;
import com.lnjoying.justice.cluster.manager.db.repo.ClusterRepo;
import com.lnjoying.justice.cluster.manager.domain.dto.model.cluster.*;
import com.lnjoying.justice.cluster.manager.domain.model.ClusterInnerInfo;
import com.lnjoying.justice.cluster.manager.domain.model.ClusterNodePlanInfo;
import com.lnjoying.justice.cluster.manager.service.data.ClusterDataService;
import com.lnjoying.justice.cluster.manager.service.data.K3sClusterDataService;
import com.lnjoying.justice.cluster.manager.service.plan.ContainerPlan;
import com.lnjoying.justice.cluster.manager.service.rpc.CombRpcService;
import com.lnjoying.justice.cluster.manager.util.ClsUtils;
import com.lnjoying.justice.commonweb.exception.WebSystemException;
import com.lnjoying.justice.commonweb.util.CollectionUtils;
import com.lnjoying.justice.schema.common.ErrorCode;
import com.lnjoying.justice.schema.common.ErrorLevel;
import com.lnjoying.justice.schema.entity.docker.DockerInstParams;
import com.micro.core.common.Utils;
import freemarker.template.TemplateException;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
public class K3sSystemAddonPlanServiceImpl extends AbstractK3sPlanService
{
    private static Logger LOGGER = LogManager.getLogger();

    @Autowired
    private ClusterDataService clusterDataService;

    @Autowired
    private K3sClusterDataService k3sClusterDataService;

    @Autowired
    private ClusterRepo clusterRepo;
    
    @Autowired
    EnvCfg envCfg;

    @Autowired
    CombRpcService combRpcService;

    final String AGENT_ADDON_FILENAME   = "agent.yaml";
    final String TERMINAL_ADDON_FILENAME   = "terminal.yaml";
    final String ADDON_FILEPATH = "/opt/jke-files";
    final String ADDON_FILE_BIND = "/opt/jke-files:/opt/jke-files";

    private ContainerPlan buildSystemPlan(K3sConfig k3sConfig, ClusterNodePlanInfo clusterNodePlanInfo) throws Exception
    {
        ContainerPlan containerPlan = initSystemAddon(k3sConfig);
        String clusterAgentParams = buildClusterAgentAddon(k3sConfig, clusterNodePlanInfo);
        String joyTerminalParams = buildJoyTerminalAddon(k3sConfig);

        String dstPath = Utils.buildStr(envCfg.getAddonFilePath(), "/", clusterNodePlanInfo.getClusterNodeInfo().getNodeId());

        if (CollectionUtils.hasContent(clusterAgentParams))
        {
            ClsUtils.writeFile(clusterAgentParams, dstPath, AGENT_ADDON_FILENAME);
        }

        if (CollectionUtils.hasContent(joyTerminalParams))
        {
            ClsUtils.writeFile(joyTerminalParams, dstPath, TERMINAL_ADDON_FILENAME);
        }
        
        List<String> envList = new ArrayList<>();

        String filePath = String.format("JKE_FILE_PATH=%s", ADDON_FILEPATH);

        envList.add(filePath);

        DockerInstParams dockerInstParams =  containerPlan.getDockerInstParams();
        dockerInstParams.setEnv(envList);
        List<String> binds = new ArrayList<>();
        binds.add(K3S_PATH_BIND);
        binds.add(ADDON_FILE_BIND);
        binds.add(ETC_K3S_PATH_BIND);
        dockerInstParams.getHostConfig().setBinds(binds);
        dockerInstParams.setCmd(new String[] {"/opt/jke-tools/k3s-addon-deploy", AGENT_ADDON_FILENAME, TERMINAL_ADDON_FILENAME});

        String sideKick = Utils.buildStr(DeployContainerName.JKE_SIDEKICK, "_", clusterNodePlanInfo.getClusterId().substring(0,4));
        containerPlan.getDockerInstParams().getHostConfig().setVolumesFrom(Arrays.asList(sideKick));
    
        containerPlan.setContainerName(Utils.buildStr(containerPlan.getContainerName(), "_", clusterNodePlanInfo.getClusterId().substring(0,4)));
        return containerPlan;
    }

    ContainerPlan initSystemAddon(K3sConfig k3sConfig) throws WebSystemException
    {
        ContainerPlan containerPlan = new ContainerPlan(DeployContainerName.JKE_ADDON_DEPLOYER, k3sConfig.getRegistries());
        DockerInstParams dockerInstParams = containerPlan.getDockerInstParams();
        dockerInstParams.setEntrypoint(new String[]{"/bin/sh"});
        dockerInstParams.getHostConfig().setPrivileged(true);
        dockerInstParams.getHostConfig().setNetworkMode(DockerNetworkMode.HOST);
        containerPlan.setAutoRun(true);
        String image = k3sClusterDataService.getImage(k3sConfig.getK3sVersion(), containerPlan.getContainerName());
        
        if (StringUtils.isEmpty(image))
        {
            throw new WebSystemException(ErrorCode.CLUSTER_K3S_IMAGE_NOT_CFG, ErrorLevel.INFO);
        }
        
        dockerInstParams.setImage(image);
        Map<String, String> labels = new HashMap<>();
        labels.put("com.lnjoying.jke.container.name", containerPlan.getContainerName());
        dockerInstParams.setLabels(labels);
        return containerPlan;
    }
    
    @Override
    public int buildNodeDeployPlan(ClusterInnerInfo clusterInnerInfo, ClusterNodePlanInfo clusterNodePlanInfo)
    {
        try
        {
            LOGGER.info("build node deploy plan.");
            buildFileDownload(clusterInnerInfo.getK3sConfig(), clusterNodePlanInfo);
            ContainerPlan containerPlan = buildSystemPlan(clusterInnerInfo.getK3sConfig(), clusterNodePlanInfo);
            clusterNodePlanInfo.addContainerPlan(containerPlan);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("build system addon deploy plan failed.");
            return ErrorCode.CLUSTER_K8S_PLAN_ABNORMAL.getCode();
        }
        
        return ErrorCode.SUCCESS.getCode();
    }
    
    @Override
    public int buildNodeUnDeployPlan(ClusterInnerInfo clusterInnerInfo, ClusterNodePlanInfo clusterNodePlanInfo) throws Exception
    {
        return 0;
    }

    String buildClusterAgentAddon(K3sConfig k3sConfig, ClusterNodePlanInfo clusterNodePlanInfo) throws IOException, TemplateException
    {
        String agentService = "cluster-agent";

        String image = k3sClusterDataService.getImage(k3sConfig.getK3sVersion(), agentService);
        if (StringUtils.isEmpty(image))
        {
            throw new WebSystemException(ErrorCode.CLUSTER_K3S_IMAGE_NOT_CFG, ErrorLevel.INFO);
        }

        String region = clusterNodePlanInfo.getClusterNodeInfo().getRegionId();
        List<Toleration> tolerations = null;
        TblClusterInfo tblClusterInfo = clusterRepo.getCluster(clusterNodePlanInfo.getClusterId());
        String gateway = combRpcService.getEdgeResourceService().getOnlineGwByRegion(region);
        Map<String, Boolean> features = null;
        Map<String, String> agentEnvVars = null;

        Map<String, Object> clusterAgentMap = new HashMap<>();
        clusterAgentMap.put("PrivateRegistryConfig", null);
        clusterAgentMap.put("Tolerations", ClsUtils.listObjToMap(tolerations));
        clusterAgentMap.put("Token", tblClusterInfo.getToken());
        clusterAgentMap.put("region_id", region);
        clusterAgentMap.put("gateway", gateway);
        clusterAgentMap.put("Features", ClsUtils.featureToString(features));
        clusterAgentMap.put("IsJKE", "true");
        clusterAgentMap.put("URLPlain", null);
        clusterAgentMap.put("CAChecksum", null);
        clusterAgentMap.put("ClusterRegistry", null);
        clusterAgentMap.put("AgentImage", image);
        clusterAgentMap.put("AuthImage", null);
        clusterAgentMap.put("AgentEnvVars", agentEnvVars);

        return clusterDataService.getTemplate("cluster-agent", clusterAgentMap);
    }

    String buildJoyTerminalAddon(K3sConfig k3sConfig) throws IOException, TemplateException
    {
        String image = k3sClusterDataService.getImage(k3sConfig.getK3sVersion(), "k3s-server");
        if (StringUtils.isEmpty(image))
        {
            throw new WebSystemException(ErrorCode.CLUSTER_K3S_IMAGE_NOT_CFG, ErrorLevel.INFO);
        }

        Map<String, Object> joyTerminalMap = new HashMap<>();
        joyTerminalMap.put("IsK3s", "true");
        joyTerminalMap.put("TerminalImage", image);

        return clusterDataService.getTemplate("joy-terminal", joyTerminalMap);
    }

    protected int buildFileDownload(K3sConfig k3sConfig, ClusterNodePlanInfo clusterNodePlanInfo)
    {
        LOGGER.info("build file deployer plan for {}", clusterNodePlanInfo.getClusterId());
        ContainerPlan containerPlan = new ContainerPlan(DeployContainerName.FILE_DOWNLOAD, k3sConfig.getRegistries());
        assembleFileDownload(k3sConfig, clusterNodePlanInfo, containerPlan);
        clusterNodePlanInfo.addContainerPlan(containerPlan);
        containerPlan.setContainerName(Utils.buildStr(containerPlan.getContainerName(), "_", clusterNodePlanInfo.getClusterId().substring(0,4)));
        return 0;
    }

    protected void assembleFileDownload(K3sConfig k3sConfig, ClusterNodePlanInfo clusterNodePlanInfo, ContainerPlan containerPlan)
    {
        DockerInstParams dockerInstParams = containerPlan.getDockerInstParams();
        dockerInstParams.getHostConfig().setPrivileged(false);
        String image = k3sClusterDataService.getImage(k3sConfig.getK3sVersion(), containerPlan.getContainerName());

        dockerInstParams.setImage(image);

        String agentFileUrl = String.format("%s%s/%s" ,envCfg.getAddonFileUrl(), clusterNodePlanInfo.getClusterNodeInfo().getNodeId(), AGENT_ADDON_FILENAME);
        String terminalFileUrl = String.format("%s%s/%s" ,envCfg.getAddonFileUrl(), clusterNodePlanInfo.getClusterNodeInfo().getNodeId(), TERMINAL_ADDON_FILENAME);
        dockerInstParams.setCmd(new String[] { agentFileUrl, terminalFileUrl});
        dockerInstParams.setEntrypoint(new String[]{"file-download"});

        containerPlan.setAutoRun(true);

        dockerInstParams.getHostConfig().setBinds(Arrays.asList(ADDON_FILE_BIND));

        List<String> envList = new ArrayList<>();
        String filePath = String.format("TARGET_PATH=%s", ADDON_FILEPATH);
        envList.add(filePath);
        dockerInstParams.setEnv(envList);
    }
}

