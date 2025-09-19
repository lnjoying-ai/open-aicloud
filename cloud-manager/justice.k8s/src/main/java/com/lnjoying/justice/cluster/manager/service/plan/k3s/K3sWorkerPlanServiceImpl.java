package com.lnjoying.justice.cluster.manager.service.plan.k3s;

import com.lnjoying.justice.cluster.manager.common.*;
import com.lnjoying.justice.cluster.manager.domain.dto.model.cluster.K3sConfig;
import com.lnjoying.justice.cluster.manager.domain.model.ClusterInnerInfo;
import com.lnjoying.justice.cluster.manager.domain.model.ClusterNodePlanInfo;
import com.lnjoying.justice.cluster.manager.domain.model.X509CertificateInfo;
import com.lnjoying.justice.cluster.manager.service.data.K3sClusterDataService;
import com.lnjoying.justice.cluster.manager.service.plan.ContainerPlan;
import com.lnjoying.justice.cluster.manager.util.ClsUtils;
import com.lnjoying.justice.cluster.manager.util.EnvUtil;
import com.lnjoying.justice.schema.common.ErrorCode;
import com.lnjoying.justice.schema.entity.docker.DockerInstParams;
import com.lnjoying.justice.schema.entity.docker.HostConfig;
import com.lnjoying.justice.schema.entity.docker.LogConfig;
import com.lnjoying.justice.schema.entity.docker.RestartPolicy;
import com.micro.core.common.Utils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.lnjoying.justice.cluster.manager.service.plan.k3s.AbstractK3sPlanService.K3sOption.CONTAINER_ENV_KEY_K3S_NODE_NAME;
import static com.lnjoying.justice.cluster.manager.service.plan.k3s.K3sControllerPlanServiceImpl.CONTAINER_CONTROLLER_INIT;
import static com.lnjoying.justice.cluster.manager.service.plan.k3s.K3sControllerPlanServiceImpl.CONTAINER_ROLE_LABEL;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2022/8/15 19:55
 */

@Service("k3sWorkerPlanServiceImpl")
public class K3sWorkerPlanServiceImpl extends AbstractK3sPlanService
{

    private static Logger LOGGER = LogManager.getLogger();

    @Autowired
    private K3sClusterDataService k3sClusterDataService;

    @Override
    public int buildNodeDeployPlan(ClusterInnerInfo clusterInnerInfo, ClusterNodePlanInfo clusterNodePlanInfo) throws Exception
    {
        try
        {
            K3sConfig k3sConfig = clusterInnerInfo.getK3sConfig();

            String roles = clusterNodePlanInfo.getClusterNodeInfo().getClusterRoles();
            buildClusterCleanerDeployPlan(k3sConfig, clusterNodePlanInfo);
           buildPortCheckerPlan(k3sConfig, clusterNodePlanInfo, getNeedCheckPorts(roles));
            Map<String, X509CertificateInfo> certMap = clusterInnerInfo.getCertMap();
            buildServiceSideKickPlan(k3sConfig, clusterNodePlanInfo);
            buildWorkerPlan(k3sConfig, clusterNodePlanInfo);

        }catch (Exception e)
        {
            LOGGER.error("build k3s worker deploy plan for cluster:{} error:{}", clusterInnerInfo.getClusterId(), e);
            return ErrorCode.CLUSTER_K3S_PLAN_ABNORMAL.getCode();
        }

        return ErrorCode.SUCCESS.getCode();
    }

    @Override
    public int buildNodeUnDeployPlan(ClusterInnerInfo clusterInnerInfo, ClusterNodePlanInfo clusterNodePlanInfo)
    {
        K3sConfig k3sConfig = clusterInnerInfo.getK3sConfig();
        buildClusterCleanerUnDeployPlan(k3sConfig, clusterNodePlanInfo);
        buildCleanJke(clusterNodePlanInfo);
        return 0;
    }

    private int buildWorkerPlan(K3sConfig k3sConfig, ClusterNodePlanInfo clusterNodePlanInfo)
    {
        LOGGER.info("build k3s worker plan for {}", clusterNodePlanInfo.getClusterId());

        ContainerPlan containerPlan = new ContainerPlan(DeployContainerName.JKE_K3S_WORKER, k3sConfig.getRegistries());
        clusterNodePlanInfo.addContainerPlan(containerPlan);
        assembleK3sWorkPlan(k3sConfig, containerPlan, clusterNodePlanInfo);
        containerPlan.setCoreContainer(true);

        containerPlan.setContainerName(Utils.buildStr(containerPlan.getContainerName(), "_", clusterNodePlanInfo.getClusterId().substring(0,4)));
        return 0;
    }

    private void buildHealthCheckWorker(K3sConfig k3sConfig, ClusterNodePlanInfo clusterNodePlanInfo)
    {
        LOGGER.info("build k3s worker health checker plan for {}", clusterNodePlanInfo.getClusterId());
        ContainerPlan containerPlan = new ContainerPlan(DeployContainerName.JKE_HEALTH_CHECK_K3S, k3sConfig.getRegistries());
        clusterNodePlanInfo.addContainerPlan(containerPlan);
        String certPath = EnvUtil.getCertPath(KeyCertName.KubeAPICertName);
        String keyPath = EnvUtil.getKeyPath(KeyCertName.KubeAPICertName);
        String healthCheckUrl = ClsUtils.getHealthCheckURL(true, KUBE_API_PORT);
        assembleHttpsHealthCheckParams(k3sConfig, containerPlan, certPath, keyPath, healthCheckUrl);
        containerPlan.setLogSucess("ok");
        String containerName = String.format("%s_%d_%s", containerPlan.getContainerName(), KUBE_API_PORT, clusterNodePlanInfo.getClusterId().substring(0,4));
        containerPlan.setContainerName(containerName);
    }


    private int assembleK3sWorkPlan(K3sConfig k3sConfig, ContainerPlan containerPlan, ClusterNodePlanInfo clusterNodePlanInfo)
    {
        DockerInstParams dockerInstParams = containerPlan.getDockerInstParams();
        dockerInstParams.setEntrypoint(new String[]{DEFAULT_ENTRYPOINT});
        HostConfig hostConfig = new HostConfig();
        LogConfig logConfig = new LogConfig();
        logConfig.setType("json-file");
        Map<String, String> logMap = new HashMap<>();
        logMap.put("max-size", "100m");
        logMap.put("max-file", "3");
        logConfig.setConfig(logMap);
        hostConfig.setLogConfig(logConfig);
        dockerInstParams.setHostConfig(hostConfig);
        dockerInstParams.setImage(k3sClusterDataService.getImage(k3sConfig.getK3sVersion(), containerPlan.getContainerName()));
        RestartPolicy restartPolicy = new RestartPolicy();
        restartPolicy.setName(DockerRestartPolicy.ALWAYS);
        dockerInstParams.getHostConfig().setRestartPolicy(restartPolicy);
        dockerInstParams.getHostConfig().setPrivileged(true);
        Map<String, String> labels = new HashMap<>();
        labels.put(CONTAINER_NAME_LABEL, containerPlan.getContainerName());
        labels.put(CONTAINER_ROLE_LABEL, K3sRole.WORKER);
        ClusterNodePlanInfo.K3sContext k3sContext = clusterNodePlanInfo.getK3sContext();
        boolean init = k3sContext.isInit();
        labels.put(CONTAINER_CONTROLLER_INIT, String.valueOf(init));
        dockerInstParams.setLabels(labels);
        dockerInstParams.getHostConfig().setNetworkMode(DockerNetworkMode.HOST);

        List<String> commandListArgs  = new ArrayList<>();
        commandListArgs.add("agent");

        Map<String,String> commandMapArgs = new HashMap<>();
        setDockerInstParamsCmd(dockerInstParams, commandMapArgs, commandListArgs);

        Map<String, String> envArgs = new HashMap<>();
        k3sContext.getK3sToken().ifPresent(token -> {
            envArgs.put(K3sOption.CONTAINER_ENV_KEY_K3S_TOKEN, token);
        });
        k3sContext.getK3sUrl().ifPresent(url -> {
            envArgs.put(K3sOption.CONTAINER_ENV_KEY_K3S_URL, url);
        });
        k3sContext.getCriConfigFile().ifPresent(config -> {
            envArgs.put(K3sOption.CONTAINER_ENV_KEY_CRI_CONFIG_FILE, config);
        });
        k3sContext.getNodeName().ifPresent(nodeName -> {
            envArgs.put(CONTAINER_ENV_KEY_K3S_NODE_NAME, nodeName);
        });
        setDockerInstParamsEnv(dockerInstParams, envArgs);
        return ErrorCode.SUCCESS.getCode();
    }
}
