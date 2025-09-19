package com.lnjoying.justice.cluster.manager.service.plan.k3s;

import com.lnjoying.justice.cluster.manager.common.*;
import com.lnjoying.justice.cluster.manager.domain.dto.model.cluster.*;
import com.lnjoying.justice.cluster.manager.domain.model.ClusterNodeInfo;
import com.lnjoying.justice.cluster.manager.domain.model.ClusterNodePlanInfo;
import com.lnjoying.justice.cluster.manager.domain.model.X509CertificateInfo;
import com.lnjoying.justice.cluster.manager.service.data.K3sClusterDataService;
import com.lnjoying.justice.cluster.manager.service.plan.ContainerPlan;
import com.lnjoying.justice.cluster.manager.service.plan.PlanService;
import com.lnjoying.justice.commonweb.exception.WebSystemException;
import com.lnjoying.justice.commonweb.util.CollectionUtils;
import com.lnjoying.justice.schema.common.ErrorCode;
import com.lnjoying.justice.schema.common.ErrorLevel;
import com.lnjoying.justice.schema.entity.docker.DockerInstParams;
import com.micro.core.common.Utils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.*;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2022/8/12 10:08
 */

public abstract class AbstractK3sPlanService implements PlanService
{

    private static Logger LOGGER = LogManager.getLogger();

    public static final  int KUBE_API_PORT         = 6443;
    public static final  int LB_SERVER_PORT         = 6444;

    private static final int ETCD_PORT1           = 2379;
    private static final int ETCD_PORT2           = 2380;
    private static final int ETCD_METRIC_PORT          = 2381;

    private static final int KUBELET_PORT_0        = 10250;
    private static final int KUBELET_HEALTH_PORT         = 10248;
    private static final int KUBE_PROXY_METRIC_PORT  = 10249;
    private static final int KUBE_PROXY_HEALTH_PORT       = 10256;
    private static final int KUBE_CONTROL_PORT     = 10252;
    private static final int KUBE_CONTROL_PORT_SSL  = 10257;
    private static final int CLOUD_CONTROLLER_MANAGER_PORT  = 10258;
    private static final int KUBE_SCHEDULER_PORT  = 10259;

    public static final String K3S_PATH_BIND = "/var/lib/rancher/k3s/server/tls:/var/lib/rancher/k3s/server/tls";

    protected static final String DEFAULT_ENTRYPOINT = "/bin/k3s";

    protected static final String CONTAINER_NAME_LABEL = "com.lnjoying.jke.container.name";

    protected static final String HOST_PROC_PATH_BIND = "/proc:/host/proc";
    protected static final String HOST_DEV_PATH_BIND = "/dev:/host/dev";
    protected static final String DOCKER_SOCKET_BIND = "/var/run/docker.sock:/var/run/docker.sock";
    protected static final String ETC_K3S_PATH_BIND = "/etc/rancher/k3s:/etc/rancher/k3s:z";

    @Autowired
    @Qualifier("k3sClusterDataService")
    private K3sClusterDataService clusterDataService;

    protected int buildClusterCleanerDeployPlan(K3sConfig k3sConfig, ClusterNodePlanInfo clusterNodePlanInfo) throws WebSystemException
    {
        LOGGER.info("build k3s cluster cleaner plan for {}", clusterNodePlanInfo.getClusterId());

        ContainerPlan containerPlan = new ContainerPlan(DeployContainerName.JKE_K3S_CLEANER, k3sConfig.getRegistries());
        clusterNodePlanInfo.addContainerPlan(containerPlan);
        assembleClusterCleanerParams(k3sConfig, containerPlan);
        containerPlan.setNextAction(PlanAction.REMOVE_BEFORE);
        containerPlan.setContainerName(Utils.buildStr(containerPlan.getContainerName(), "_", clusterNodePlanInfo.getClusterId().substring(0,4), "_", "deploy"));
        return 0;
    }

    protected int buildClusterCleanerUnDeployPlan(K3sConfig k3sConfig, ClusterNodePlanInfo clusterNodePlanInfo) throws WebSystemException
    {
        LOGGER.info("build k3s cluster cleaner plan for {}", clusterNodePlanInfo.getClusterId());

        ContainerPlan containerPlan = new ContainerPlan(DeployContainerName.JKE_K3S_CLEANER, k3sConfig.getRegistries());
        clusterNodePlanInfo.addContainerPlan(containerPlan);
        assembleClusterCleanerParams(k3sConfig, containerPlan);
        containerPlan.setNextAction(PlanAction.REMOVE_BEFORE);
        containerPlan.setContainerName(Utils.buildStr(containerPlan.getContainerName(), "_", clusterNodePlanInfo.getClusterId().substring(0,4), "_", "deploy"));
        return 0;
    }

    protected void buildCleanJke(ClusterNodePlanInfo clusterNodePlanInfo)
    {
        ContainerPlan containerPlan = new ContainerPlan("");
        containerPlan.setNextAction(PlanAction.REMOVE_NODE_JKE);
        clusterNodePlanInfo.addContainerUPlan(containerPlan);
    }

    protected int buildPortCheckerPlan(K3sConfig k3sConfig, ClusterNodePlanInfo clusterNodePlanInfo, String ports) throws WebSystemException
    {
        LOGGER.info("build k3s port checker plan for {}", clusterNodePlanInfo.getClusterId());
        ContainerPlan containerPlan = new ContainerPlan(DeployContainerName.JKE_PORT_CHECKER, k3sConfig.getRegistries());
        clusterNodePlanInfo.addContainerPlan(containerPlan);
        assemblePortCheckerParams(k3sConfig, containerPlan, ports);
        containerPlan.setLogFailed("unavailable");
        containerPlan.setContainerName(Utils.buildStr(containerPlan.getContainerName(), "_",
                clusterNodePlanInfo.getClusterId().substring(0,4)));
        return 0;
    }

    protected int buildCertDeployerPlan(K3sConfig k3sConfig, Map<String, X509CertificateInfo> certMap,
                               ClusterNodePlanInfo clusterNodePlanInfo) throws WebSystemException
    {
        ClusterNodeInfo nodeInfo = clusterNodePlanInfo.getClusterNodeInfo();
        LOGGER.info("build k3s cert deploy plan for {} @{} roles:{}", clusterNodePlanInfo.getClusterId(),
                nodeInfo.getNodeId(), nodeInfo.getClusterRoles());
        ContainerPlan containerPlan = new ContainerPlan(DeployContainerName.JKE_CERT_DEPLOYER, k3sConfig.getRegistries());
        assembleCertDeployerParams(k3sConfig, certMap, clusterNodePlanInfo.getClusterNodeInfo(),containerPlan);
        clusterNodePlanInfo.addContainerPlan(containerPlan);
        containerPlan.setContainerName(Utils.buildStr(containerPlan.getContainerName(), "_", clusterNodePlanInfo.getClusterId().substring(0,4)));
        return 0;
    }

    protected int buildServiceSideKickPlan(K3sConfig k3sConfig, ClusterNodePlanInfo clusterNodePlanInfo) throws WebSystemException
    {
        LOGGER.info("build side kick plan for {}", clusterNodePlanInfo.getClusterId());

        ContainerPlan containerPlan = new ContainerPlan(DeployContainerName.JKE_SIDEKICK, k3sConfig.getRegistries());
        clusterNodePlanInfo.addContainerPlan(containerPlan);
        assembleServiceSideKickParams(k3sConfig, containerPlan);
        containerPlan.setContainerName(Utils.buildStr(containerPlan.getContainerName(), "_", clusterNodePlanInfo.getClusterId().substring(0,4)));
        return 0;
    }

    /**
     *
     * @param clusterRoles
     * @return
     */
    protected String getNeedCheckPorts(String clusterRoles)
    {
        List<Integer> ports = new ArrayList<>();
        if (clusterRoles.contains(K3sRole.CONTROLLER))
        {
            ports.add(KUBE_API_PORT);
            ports.add(LB_SERVER_PORT);
            ports.add(ETCD_PORT1);
            ports.add(ETCD_PORT2);
            ports.add(ETCD_METRIC_PORT);
            ports.add(KUBE_SCHEDULER_PORT);
            ports.add(KUBE_CONTROL_PORT_SSL);
        }

        if (clusterRoles.contains(K3sRole.WORKER))
        {
            ports.add(KUBELET_PORT_0);
            ports.add(KUBELET_HEALTH_PORT);
            ports.add(KUBE_PROXY_METRIC_PORT);
            ports.add(KUBE_PROXY_HEALTH_PORT);
            ports.add(LB_SERVER_PORT);
        }

        return StringUtils.join(ports, ",");
    }

    private ErrorCode assembleClusterCleanerParams(K3sConfig k3sConfig, ContainerPlan containerPlan)
    {
        DockerInstParams dockerInstParams = containerPlan.getDockerInstParams();
        dockerInstParams.setImage(clusterDataService.getImage(k3sConfig.getK3sVersion(), containerPlan.getContainerName()));
        Map<String, String> labels = new HashMap<>();
        labels.put(CONTAINER_NAME_LABEL, containerPlan.getContainerName());
        dockerInstParams.setLabels(labels);
        dockerInstParams.getHostConfig().setNetworkMode(DockerNetworkMode.NONE);
        dockerInstParams.getHostConfig().setPrivileged(true);
        dockerInstParams.getHostConfig().setPidMode(PidMode.HOST);
        dockerInstParams.getHostConfig().setBinds(Arrays.asList(HOST_PROC_PATH_BIND, HOST_DEV_PATH_BIND, DOCKER_SOCKET_BIND));
        dockerInstParams.setEntrypoint(new String[]{"cluster-clean"});
        return ErrorCode.SUCCESS;
    }

    private ErrorCode assemblePortCheckerParams(K3sConfig k3sConfig, ContainerPlan containerPlan, String ports)
    {
        DockerInstParams dockerInstParams = containerPlan.getDockerInstParams();
        dockerInstParams.getHostConfig().setPrivileged(false);
        dockerInstParams.getHostConfig().setNetworkMode(DockerNetworkMode.HOST);
        String image = clusterDataService.getImage(k3sConfig.getK3sVersion(), containerPlan.getContainerName());

        dockerInstParams.setImage(image);
        dockerInstParams.setCmd(ports.split(","));
        dockerInstParams.setEntrypoint(new String[]{"port-check"});
        dockerInstParams.getHostConfig().setPrivileged(true);
        containerPlan.setAutoRun(true);

        return ErrorCode.SUCCESS;
    }

    private ErrorCode assembleCertDeployerParams(K3sConfig k3sConfig, Map<String, X509CertificateInfo> certMap, ClusterNodeInfo clusterNodeInfo, ContainerPlan containerPlan)
    {
        List<String> certKeys = getCertKeysByRole(clusterNodeInfo);
        if (CollectionUtils.isEmpty(certKeys))
        {
            throw new WebSystemException(ErrorCode.CLUSTER_K3S_CERT_EMPTY, ErrorLevel.INFO);
        }
        DockerInstParams dockerInstParams = containerPlan.getDockerInstParams();
        List<String> envList = new ArrayList<>();

        for (String certKey : certKeys)
        {
            X509CertificateInfo certificateInfo = certMap.get(certKey);
            if (certificateInfo == null)
            {
                continue;
            }
            String certPemRecord = String.format("%s=%s", certificateInfo.getCertEnvName(), certificateInfo.getCertificatePem());
            envList.add(certPemRecord);
            String keyPemRecord = String.format("%s=%s", certificateInfo.getKeyEnvName(), certificateInfo.getKeyPem());
            envList.add(keyPemRecord);
            if (CollectionUtils.hasContent(certificateInfo.getConfigEnvName()))
            {
                String configRecord = String.format("%s=%s", certificateInfo.getConfigEnvName(), certificateInfo.getConfig());
                envList.add(configRecord);
            }
        }

        dockerInstParams.getHostConfig().setPrivileged(false);
        dockerInstParams.getHostConfig().setNetworkMode(DockerNetworkMode.HOST);
        String image = clusterDataService.getImage(k3sConfig.getK3sVersion(), containerPlan.getContainerName());
        dockerInstParams.setImage(image);
        containerPlan.setAutoRun(true);
        dockerInstParams.setEnv(envList);
        dockerInstParams.setEntrypoint(new String[]{"k3s-cert-deploy"});
        dockerInstParams.getHostConfig().setBinds(Arrays.asList(K3S_PATH_BIND));

        return ErrorCode.SUCCESS;
    }

    public List<String> getCertKeysByRole(ClusterNodeInfo clusterNodeInfo)
    {
        List<String> certKeyList = new ArrayList<>();
        if (clusterNodeInfo.getClusterRoles().contains(K3sRole.CONTROLLER))
        {
            certKeyList.add(KeyCertName.K3S_SERVER_CA);
        }

       return certKeyList;
    }

    protected void setDockerInstParamsCmd(DockerInstParams dockerInstParams, Map<String,String> commandArgs,
                                    List<String> cmds)
    {
        if (CollectionUtils.isEmpty(cmds))
        {
            cmds = new ArrayList<>();
        }

        cmds.addAll(map2List(commandArgs));

        dockerInstParams.setCmd(cmds.toArray(new String[0]));
    }


    protected void setDockerInstParamsEnv(DockerInstParams dockerInstParams, Map<String,String> envArgs)
    {
        dockerInstParams.setEnv(map2List(envArgs));
    }

    protected List<String> map2List(Map<String, String> map)
    {
        List<String> res = new ArrayList<>();
        if (!CollectionUtils.isEmpty(map))
        {
            for(Map.Entry<String, String> entry: map.entrySet())
            {
                String cmdStr = String.format("%s=%s", entry.getKey(), entry.getValue());
                res.add(cmdStr);
            }
        }

        return res;
    }

    protected ErrorCode assembleHttpsHealthCheckParams(K3sConfig k3sConfig, ContainerPlan containerPlan, String certPath, String keyPath, String healthCheckUrl)
    {
        DockerInstParams dockerInstParams = containerPlan.getDockerInstParams();
        dockerInstParams.getHostConfig().setPrivileged(false);
        dockerInstParams.getHostConfig().setNetworkMode(DockerNetworkMode.HOST);
        String image = clusterDataService.getImage(k3sConfig.getK3sVersion(), containerPlan.getContainerName());
        dockerInstParams.setImage(image);
        dockerInstParams.getHostConfig().setNetworkMode(DockerNetworkMode.HOST);
        dockerInstParams.setEntrypoint(new String[]{"https-health-check"});
        dockerInstParams.setCmd(new String[]{certPath, keyPath, healthCheckUrl});
        containerPlan.setAutoRun(true);
        dockerInstParams.getHostConfig().setBinds(Arrays.asList(K3S_PATH_BIND));
        return ErrorCode.SUCCESS;
    }

    ErrorCode assembleServiceSideKickParams(K3sConfig k3sConfig, ContainerPlan containerPlan) throws WebSystemException
    {
        DockerInstParams dockerInstParams = containerPlan.getDockerInstParams();
        dockerInstParams.getHostConfig().setPrivileged(false);
        String image = clusterDataService.getImage(k3sConfig.getK3sVersion(), containerPlan.getContainerName());
        if (StringUtils.isEmpty(image))
        {
            throw new WebSystemException(ErrorCode.CLUSTER_K3S_IMAGE_NOT_CFG, ErrorLevel.INFO);
        }

        dockerInstParams.setImage(image);
        dockerInstParams.getHostConfig().setNetworkMode(DockerNetworkMode.NONE);
        containerPlan.setAutoRun(false);

        return ErrorCode.SUCCESS;
    }

    public class K3sOption
    {
        public static final String CONTAINER_ENV_KEY_K3S_TOKEN = "K3S_TOKEN";

        public static final String CONTAINER_ENV_KEY_K3S_KUBECONFIG_OUTPUT = "K3S_KUBECONFIG_OUTPUT";

        public static final String CONTAINER_ENV_KEY_K3S_URL = "K3S_URL";

        public static final String CONTAINER_ENV_KEY_CRI_CONFIG_FILE = "CRI_CONFIG_FILE";

        public static final String CONTAINER_ENV_KEY_K3S_NODE_NAME = "K3S_NODE_NAME";
    }
}
