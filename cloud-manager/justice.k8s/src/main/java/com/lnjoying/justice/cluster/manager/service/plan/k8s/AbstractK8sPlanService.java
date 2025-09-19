package com.lnjoying.justice.cluster.manager.service.plan.k8s;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import com.lnjoying.justice.cluster.manager.common.*;
import com.lnjoying.justice.cluster.manager.domain.dto.model.cluster.*;
import com.lnjoying.justice.cluster.manager.domain.model.ClusterNodeInfo;
import com.lnjoying.justice.cluster.manager.domain.model.ClusterNodePlanInfo;
import com.lnjoying.justice.cluster.manager.domain.model.NetworkOption;
import com.lnjoying.justice.cluster.manager.domain.model.X509CertificateInfo;
import com.lnjoying.justice.cluster.manager.service.cert.X509CertServiceImpl;
import com.lnjoying.justice.cluster.manager.service.data.ClusterDataService;
import com.lnjoying.justice.cluster.manager.service.plan.ContainerPlan;
import com.lnjoying.justice.cluster.manager.service.plan.PlanService;
import com.lnjoying.justice.cluster.manager.service.rpc.CombRpcService;
import com.lnjoying.justice.cluster.manager.util.ClsUtils;
import com.lnjoying.justice.cluster.manager.util.EnvUtil;
import com.lnjoying.justice.commonweb.exception.WebSystemException;
import com.lnjoying.justice.commonweb.util.Base64Utils;
import com.lnjoying.justice.commonweb.util.CollectionUtils;
import com.lnjoying.justice.commonweb.util.JsonUtils;
import com.lnjoying.justice.schema.common.ErrorCode;
import com.lnjoying.justice.schema.common.ErrorLevel;
import com.lnjoying.justice.schema.entity.docker.DockerInfo;
import com.lnjoying.justice.schema.entity.docker.DockerInstParams;
import com.lnjoying.justice.schema.entity.docker.RestartPolicy;
import com.micro.core.common.Utils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * @description k8s plan service
 * @author Regulus
 * @date 11/29/21 7:25 P
 */
public abstract class AbstractK8sPlanService implements PlanService
{
    private static Logger LOGGER = LogManager.getLogger();
    
    @Autowired
    CombRpcService combRpcService;
    
    @Autowired
    ClusterDataService clusterDataService;
    
    @Autowired
    X509CertServiceImpl x509CertService;
    
    final String HOST_PROC_PATH_BIND          = "/proc:/host/proc";
    final String HOST_DEV_PATH_BIND           = "/dev:/host/dev";
    final String DOCKER_SOCKET_BIND           = "/var/run/docker.sock:/var/run/docker.sock";
    final String ETC_K8S_PATH_BIND          = "/etc/kubernetes:/etc/kubernetes:z";
    final String REGISTRY_PATH_BIND         = "/var/lib/kubelet:/var/lib/kubelet";
    final String EtcdPathPrefix             = "/registry";
    final String DefaultToolsEntrypoint     = "/opt/jke-tools/entrypoint.sh";
    final String CloudConfigSumEnv          = "RKE_CLOUD_CONFIG_CHECKSUM";
    final String ContainerNameLabel         = "com.lnjoying.jke.container.name";
    final String RBACAuthorizationMode      = "rbac";
    final String MaxK8s121Version           = "v1.21.99";
    final String UnschedulableControlTaint  = "node-role.kubernetes.io/controller=true:NoSchedule";
    final String KubeletCRIDockerdNameEnv   = "JKE_KUBELET_CRIDOCKERD";
    final String KubeletDockerConfigEnv     = "JKE_KUBELET_DOCKER_CONFIG";
    final String KubeletDockerConfigPath    = "/var/lib/kubelet/config.json";
    final String KubeletDockerConfigFileEnv = "JKE_KUBELET_DOCKER_FILE";
    final String CloudConfigFileName        = "/etc/kubernetes/cloud-config";
    final String EtcdDataDir                = "/var/lib/joy/etcd/";
    int KubeletPort0        = 10250;
    int KubeletPort         = 10248;
    int KubeproxyPort       = 10256;
    int KubeApiPort         = 6443;
    int KubeControlPort     = 10252;
    int KubeControlPortSsl  = 10257;
    int KubeSchedulerPort   = 10251;
    int EtcdPort1           = 2379;
    int EtcdPort2           = 2380;
    final String MaxEtcdPort4001Version    = "lnjoying/mirrored-coreos-etcd:v3.4.3";
    final String MaxEtcdNoStrictTLSVersion = "lnjoying/mirrored-coreos-etcd:v3.4.14";
    final String MaxEtcdOldEnvVersion      = "lnjoying/mirrored-coreos-etcd:v3.2.99";
    
    final String CLOUDCOFIG                            = "cloud-config";
    
    String getNeedCheckPorts(String clusterRoles)
    {
        List<Integer> ports = new ArrayList<>();
        ports.add(KubeletPort);
        ports.add(KubeproxyPort);
        if (clusterRoles.contains(K8sRole.ETCD))
        {
            ports.add(EtcdPort1);
            ports.add(EtcdPort2);
        }
        
        if (clusterRoles.contains(K8sRole.CONTROLLER))
        {
            ports.add(KubeApiPort);
            ports.add(KubeControlPort);
            ports.add(KubeSchedulerPort);
        }
        
        if (clusterRoles.equals(K8sRole.WORKER))
        {
            ports.add(KubeApiPort);
        }
        return StringUtils.join(ports, ",");
    }
    
    /**
     * @description build cert deploy plan
     * @author Regulus
     * @date 11/19/21 3:07 PM
     * @param jkeConfig
     * @param certMap
     * @param clusterNodePlanInfo 
     * @return int
     */
    int buildCertDeployerPlan(JkeConfig jkeConfig, Map<String, X509CertificateInfo> certMap, ClusterNodePlanInfo clusterNodePlanInfo) throws WebSystemException
    {
        ClusterNodeInfo nodeInfo = clusterNodePlanInfo.getClusterNodeInfo();
        LOGGER.info("build  cert deploy plan for {} @{} roles:{}", clusterNodePlanInfo.getClusterId(), nodeInfo.getNodeId(), nodeInfo.getClusterRoles());
    
        ContainerPlan containerPlan = new ContainerPlan(DeployContainerName.CERT_DEPLOYER, jkeConfig.getRegistries());
        assembleCertDeployerParams(jkeConfig, certMap, clusterNodePlanInfo.getClusterNodeInfo(),containerPlan);
        clusterNodePlanInfo.addContainerPlan(containerPlan);
        containerPlan.setContainerName(Utils.buildStr(containerPlan.getContainerName(), "_", clusterNodePlanInfo.getClusterId().substring(0,4)));
        return 0;
    }
    
    /**
     * @description assemble nnginx proxy params
     * @author Regulus
     * @date 11/27/21 5:25 PM
     * @param jkeConfig
     * @param containerPlan 
     * @return com.lnjoying.justice.schema.common.ErrorCode
     */
    ErrorCode assembleNginxProxyParams(JkeConfig jkeConfig, ContainerPlan containerPlan, Map<String, List<ClusterNodePlanInfo>> nodePlanMaps) throws WebSystemException
    {
        DockerInstParams dockerInstParams = containerPlan.getDockerInstParams();
        dockerInstParams.getHostConfig().setPrivileged(false);
        dockerInstParams.getHostConfig().setNetworkMode(DockerNetworkMode.HOST);
        dockerInstParams.setEntrypoint(new String[]{"nginx-proxy"});
        List<ClusterNodePlanInfo> nodePlanInfos = nodePlanMaps.get(nodePlanMaps.keySet().stream().filter(key -> key.contains(K8sRole.CONTROLLER)).findFirst().orElse(K8sRole.CONTROLLER));
        if (CollectionUtils.isEmpty(nodePlanInfos))
        {
            return ErrorCode.CLUSTER_K8S_CONTROLLER_EMPTY;
        }
        
        String image = clusterDataService.getImage(jkeConfig.getK8sVersion(), containerPlan.getContainerName());
        if (StringUtils.isEmpty(image))
        {
            throw new WebSystemException(ErrorCode.CLUSTER_K8S_IMAGE_NOT_CFG, ErrorLevel.INFO);
        }
    
        List<String> controlPlanList = new ArrayList<>();
        nodePlanInfos.forEach(item -> {
            if (StringUtils.isNotBlank(item.getClusterNodeInfo().getExternalAddress()))
            {
                controlPlanList.add(item.getClusterNodeInfo().getExternalAddress());
            }
            
            if (StringUtils.isNotBlank(item.getClusterNodeInfo().getInternalAddress()))
            {
                controlPlanList.add(item.getClusterNodeInfo().getInternalAddress());
            }
        });
        
        String controlPlanStr = StringUtils.join(controlPlanList, ",");
        
        String controlHost=String.format("ControlPlane_Hosts=%s", controlPlanStr);
        dockerInstParams.setEnv(Arrays.asList(controlHost));
        dockerInstParams.setImage(image);
        dockerInstParams.getHostConfig().setNetworkMode(DockerNetworkMode.HOST);
        RestartPolicy restartPolicy = new RestartPolicy();
        restartPolicy.setName(DockerRestartPolicy.ALWAYS);
        dockerInstParams.getHostConfig().setRestartPolicy(restartPolicy);
        containerPlan.setAutoRun(true);
        
        Map<String, String> labels = new HashMap<>();
        labels.put(ContainerNameLabel, containerPlan.getContainerName());
        dockerInstParams.setLabels(labels);
        
        return ErrorCode.SUCCESS;
    }
    
    /**
     * @description build nginx proxy plan. nginx proxy supply an anti-proxy for kubelet & kubeproxy to access kube-apiserver.
     * @author Regulus
     * @date 11/29/21 7:35 PM
     * @param jkeConfig
     * @param clusterNodePlanInfo 
     * @return int
     */
    int buildNginxProxyPlan(JkeConfig jkeConfig, ClusterNodePlanInfo clusterNodePlanInfo, Map<String, List<ClusterNodePlanInfo>> nodePlans) throws WebSystemException
    {
        ClusterNodeInfo nodeInfo = clusterNodePlanInfo.getClusterNodeInfo();
        LOGGER.info("build nginx proxy plan for {} @{} roles:{}", clusterNodePlanInfo.getClusterId(), nodeInfo.getNodeId(), nodeInfo.getClusterRoles());
        ContainerPlan containerPlan = new ContainerPlan(DeployContainerName.NGINX_PROXY, jkeConfig.getRegistries());
        clusterNodePlanInfo.addContainerPlan(containerPlan);
        assembleNginxProxyParams(jkeConfig, containerPlan, nodePlans);
        containerPlan.setCoreContainer(true);
        String sideKick = Utils.buildStr(DeployContainerName.JKE_SIDEKICK, "_", clusterNodePlanInfo.getClusterId().substring(0,4));
        containerPlan.getDockerInstParams().getHostConfig().setVolumesFrom(Arrays.asList(sideKick));
        containerPlan.setContainerName(Utils.buildStr(containerPlan.getContainerName(), "_", clusterNodePlanInfo.getClusterId().substring(0,4)));
        return 0;
    }
    
    String getKubeletDockerConfig(List<RegistryInfo> registrys) throws UnsupportedEncodingException
    {
        Map<String,Map<String, String>> authConfigs = new HashMap<>();
        Map<String,Map<String, Map<String,String>>> authConfigMaps = new HashMap<>();
    
        for (RegistryInfo registry : registrys)
        {
            String basicAuthSrc=String.format("%s:%s", registry.getUsername(), Base64Utils.decodeToString(registry.getPassword()));
            String base64AuthInfo = ClsUtils.getBase64(basicAuthSrc);
            Map<String, String> authMap = new HashMap<>();
            authMap.put("auth", base64AuthInfo);
            authConfigs.put(registry.getServer(), authMap);
    
        }
        authConfigMaps.put("auths", authConfigs);
        return JsonUtils.toJson(authConfigMaps);
    }
    
    /**
     * @description get cert name by node roles
     * @author Regulus
     * @date 11/30/21 9:48 AM
     * @param clusterNodeInfo
     * @return java.util.List<java.lang.String>
     */
    List<String> getCertKeysByRole(ClusterNodeInfo clusterNodeInfo)
    {
        List<String> certKeyList = new ArrayList<>();
        certKeyList.add(KeyCertName.CACertName);
        certKeyList.add(KeyCertName.KubeProxyCertName);
        certKeyList.add(KeyCertName.KubeNodeCertName);
        certKeyList.add(KeyCertName.KubeAdminCertName);
        
        if (clusterNodeInfo.getClusterRoles().contains(K8sRole.CONTROLLER))
        {
            certKeyList.add(KeyCertName.KubeAPICertName);
            certKeyList.add(KeyCertName.ServiceAccountTokenKeyName);
            certKeyList.add(KeyCertName.KubeControllerCertName);
            certKeyList.add(KeyCertName.KubeSchedulerCertName);
    
            certKeyList.add(KeyCertName.EtcdClientCertName);
            certKeyList.add(KeyCertName.EtcdClientCACertName);
            certKeyList.add(KeyCertName.RequestHeaderCACertName);
            certKeyList.add(KeyCertName.APIProxyClientCertName);
        }
    
        if (clusterNodeInfo.getClusterRoles().contains(K8sRole.ETCD))
        {
            String certName = EnvUtil.getCertNameForHost(clusterNodeInfo, KeyCertName.EtcdCertName);
            certKeyList.add(certName);
        }
        
        return certKeyList;
    }
    
    /**
     * @description assemble cert deployer params
     * @author Regulus
     * @date 11/30/21 9:51 AM
     * @param jkeConfig
     * @param certMap
     * @param clusterNodeInfo
     * @param containerPlan 
     * @return com.lnjoying.justice.schema.common.ErrorCode
     */
    protected ErrorCode assembleCertDeployerParams(JkeConfig jkeConfig, Map<String, X509CertificateInfo> certMap, ClusterNodeInfo clusterNodeInfo, ContainerPlan containerPlan) throws WebSystemException
    {
        List<String> certKeys = getCertKeysByRole(clusterNodeInfo);
        if (CollectionUtils.isEmpty(certKeys))
        {
            throw new WebSystemException(ErrorCode.CLUSTER_K8S_CERT_EMPTY, ErrorLevel.INFO);
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
        String image = clusterDataService.getImage(jkeConfig.getK8sVersion(), containerPlan.getContainerName());
        dockerInstParams.setImage(image);
        containerPlan.setAutoRun(true);
        dockerInstParams.setEnv(envList);
        dockerInstParams.setEntrypoint(new String[]{"cert-deploy"});
        dockerInstParams.getHostConfig().setBinds(Arrays.asList(ETC_K8S_PATH_BIND));
        
        return ErrorCode.SUCCESS;
    }
    
    /**
     * @description assemble side kick container params
     * @author Regulus
     * @date 11/30/21 9:53 AM
     * @param jkeConfig
     * @param containerPlan 
     * @return com.lnjoying.justice.schema.common.ErrorCode
     */
    ErrorCode assembleServiceSideKickParams(JkeConfig jkeConfig, ContainerPlan containerPlan) throws WebSystemException
    {
        DockerInstParams dockerInstParams = containerPlan.getDockerInstParams();
        dockerInstParams.getHostConfig().setPrivileged(false);
        dockerInstParams.getHostConfig().setNetworkMode(DockerNetworkMode.HOST);
        String image = clusterDataService.getImage(jkeConfig.getK8sVersion(), containerPlan.getContainerName());
        if (StringUtils.isEmpty(image))
        {
            throw new WebSystemException(ErrorCode.CLUSTER_K8S_IMAGE_NOT_CFG, ErrorLevel.INFO);
        }
    
        dockerInstParams.setImage(image);
        dockerInstParams.getHostConfig().setNetworkMode(DockerNetworkMode.NONE);
        containerPlan.setAutoRun(false);
    
        return ErrorCode.SUCCESS;
    }
    
    /**
     * @description assemble container params for https health check
     * @author Regulus
     * @date 11/30/21 9:54 AM
     * @param jkeConfig
     * @param containerPlan
     * @param certPath
     * @param keyPath
     * @param healthCheckUrl
     * @return com.lnjoying.justice.schema.common.ErrorCode
     */
    ErrorCode assembleHttpsHealthCheckParams(JkeConfig jkeConfig, ContainerPlan containerPlan, String certPath, String keyPath, String healthCheckUrl) throws WebSystemException
    {
        DockerInstParams dockerInstParams = containerPlan.getDockerInstParams();
        dockerInstParams.getHostConfig().setPrivileged(false);
        dockerInstParams.getHostConfig().setNetworkMode(DockerNetworkMode.HOST);
        String image = clusterDataService.getImage(jkeConfig.getK8sVersion(), containerPlan.getContainerName());
        dockerInstParams.setImage(image);
        dockerInstParams.getHostConfig().setNetworkMode(DockerNetworkMode.HOST);
        dockerInstParams.setEntrypoint(new String[]{"https-health-check"});
        dockerInstParams.setCmd(new String[]{certPath, keyPath, healthCheckUrl});
        containerPlan.setAutoRun(true);
        dockerInstParams.getHostConfig().setBinds(Arrays.asList(ETC_K8S_PATH_BIND));
        return ErrorCode.SUCCESS;
    }
    
    /**
     * @description assemble container params for http health check
     * @author Regulus
     * @date 11/30/21 9:57 AM
     * @param jkeConfig
     * @param containerPlan
     * @param healthCheckUrl 
     * @return com.lnjoying.justice.schema.common.ErrorCode
     */
    ErrorCode assembleHttpHealthCheckParams(JkeConfig jkeConfig, ContainerPlan containerPlan, String healthCheckUrl) throws WebSystemException
    {
        DockerInstParams dockerInstParams = containerPlan.getDockerInstParams();
        dockerInstParams.getHostConfig().setPrivileged(false);
        dockerInstParams.getHostConfig().setNetworkMode(DockerNetworkMode.HOST);
        String image = clusterDataService.getImage(jkeConfig.getK8sVersion(), containerPlan.getContainerName());
        dockerInstParams.setImage(image);
        dockerInstParams.getHostConfig().setNetworkMode(DockerNetworkMode.HOST);
        dockerInstParams.setEntrypoint(new String[]{"http-health-check"});
        dockerInstParams.setCmd(new String[]{healthCheckUrl});
        containerPlan.setAutoRun(true);
        dockerInstParams.getHostConfig().setBinds(Arrays.asList(ETC_K8S_PATH_BIND));
        
        return ErrorCode.SUCCESS;
    }
    
    /**
     * @description assemble container params for log linker
     * @author Regulus
     * @date 11/30/21 10:00 AM
     * @param jkeConfig
     * @param containerPlan
     * @param modules
     * @return com.lnjoying.justice.schema.common.ErrorCode
     */
    ErrorCode assembleLogLinkerParams(JkeConfig jkeConfig, ContainerPlan containerPlan, List<String> modules) throws WebSystemException, NoSuchAlgorithmException, UnsupportedEncodingException
    {
        DockerInstParams dockerInstParams = containerPlan.getDockerInstParams();
        dockerInstParams.setImage(clusterDataService.getImage(jkeConfig.getK8sVersion(), containerPlan.getContainerName()));
        Map<String, String> labels = new HashMap<>();
        labels.put(ContainerNameLabel, containerPlan.getContainerName());
        dockerInstParams.setLabels(labels);
        dockerInstParams.getHostConfig().setNetworkMode(DockerNetworkMode.NONE);
        dockerInstParams.getHostConfig().setPrivileged(true);
        dockerInstParams.getHostConfig().setPidMode(PidMode.HOST);
        dockerInstParams.setEntrypoint(new String[]{"log-link"});
        dockerInstParams.setCmd(modules.toArray(new String[0]));
        dockerInstParams.getHostConfig().setBinds(Arrays.asList("/var/lib/joy:/var/lib/joy"));
        
        return ErrorCode.SUCCESS;
    }
    
    /**
     * @description assemble container params for kube-proxy
     * @author Regulus
     * @date 11/30/21 10:01 AM
     * @param jkeConfig
     * @param containerPlan
     * @param clusterNodeInfo 
     * @return int
     */
    int assembleKubeProxyPlan(JkeConfig jkeConfig, ContainerPlan containerPlan, ClusterNodeInfo clusterNodeInfo) throws WebSystemException, NoSuchAlgorithmException, UnsupportedEncodingException
    {
        DockerInstParams dockerInstParams = containerPlan.getDockerInstParams();
        dockerInstParams.setEntrypoint(new String[]{DefaultToolsEntrypoint});
        dockerInstParams.setImage(clusterDataService.getImage(jkeConfig.getK8sVersion(), containerPlan.getContainerName()));
        RestartPolicy restartPolicy = new RestartPolicy();
        restartPolicy.setName(DockerRestartPolicy.ALWAYS);
        dockerInstParams.getHostConfig().setRestartPolicy(restartPolicy);
        Map<String, String> labels = new HashMap<>();
        labels.put(ContainerNameLabel, containerPlan.getContainerName());
        dockerInstParams.setLabels(labels);
        dockerInstParams.getHostConfig().setNetworkMode(DockerNetworkMode.HOST);
        dockerInstParams.getHostConfig().setPrivileged(true);
        dockerInstParams.getHostConfig().setPidMode(PidMode.HOST);
        
        Map<String,String> commandArgs = new HashMap<>();
        
        KubeProxyServiceInfo kubeProxyServiceInfo = jkeConfig.getServices().getKubeProxyServiceInfo();
        
        commandArgs.put("cluster-cidr", jkeConfig.getServices().getKubeControllerService().getClusterCidr());
        commandArgs.put("hostname-override", clusterNodeInfo.getNodeName());
        commandArgs.put("kubeconfig", EnvUtil.getConfigPath(KeyCertName.KubeProxyCertName));
        
        
        if (jkeConfig.getCloudProvider() != null &&
                StringUtils.isEmpty(jkeConfig.getCloudProvider().getProvider()))
        {
            commandArgs.put("bind-address", clusterNodeInfo.getInternalAddress());
        }
        
        
        {
            List<String> binds = new ArrayList<>();
            binds.add(ETC_K8S_PATH_BIND);
            binds.add("/run:/run");
            binds.add("/lib/modules:/lib/modules:ro");
            dockerInstParams.getHostConfig().setBinds(binds);
        }
        
        setCmdExtraArgs(commandArgs, kubeProxyServiceInfo.getExtraArgs());
        setCmdServiceOption(commandArgs, jkeConfig.getK8sVersion(), K8sServiceModule.KUBE_PROXY);
        setExtraInfo(dockerInstParams, kubeProxyServiceInfo);
        setDockerInstParamsCmd(dockerInstParams, commandArgs, "kube-proxy");
        return ErrorCode.SUCCESS.getCode();
    }
    
    /**
     * @description build service side kick container plan
     * @author Regulus
     * @date 11/30/21 10:04 AM
     * @param jkeConfig
     * @param clusterNodePlanInfo
     * @return int
     */
    int buildServiceSideKickPlan(JkeConfig jkeConfig, ClusterNodePlanInfo clusterNodePlanInfo) throws WebSystemException
    {
        LOGGER.info("build side kick plan for {}", clusterNodePlanInfo.getClusterId());
    
        ContainerPlan containerPlan = new ContainerPlan(DeployContainerName.JKE_SIDEKICK, jkeConfig.getRegistries());
        clusterNodePlanInfo.addContainerPlan(containerPlan);
        assembleServiceSideKickParams(jkeConfig, containerPlan);
        containerPlan.setContainerName(Utils.buildStr(containerPlan.getContainerName(), "_", clusterNodePlanInfo.getClusterId().substring(0,4)));
        
        return 0;
    }
    
    /**
     * @description build health check container plan for checking kubelet status
     * @author Regulus
     * @date 11/30/21 10:07 AM
     * @param jkeConfig
     * @param clusterNodePlanInfo
     * @return int
     */
    int buildHealthCheckKubelet(JkeConfig jkeConfig, ClusterNodePlanInfo clusterNodePlanInfo) throws WebSystemException
    {
        LOGGER.info("build kubelet health checker plan for {}", clusterNodePlanInfo.getClusterId());
        return buildHealthCheck(jkeConfig, clusterNodePlanInfo, KubeletPort);
    }
    
    /**
     * @description build heath check container plan for checking kube-proxy status
     * @author Regulus
     * @date 11/30/21 10:08 AM
     * @param jkeConfig
     * @param clusterNodePlanInfo 
     * @return int
     */
    int buildHealthChecKubeProxy(JkeConfig jkeConfig, ClusterNodePlanInfo clusterNodePlanInfo) throws WebSystemException
    {
        LOGGER.info("build kube proxy health checker plan for {}", clusterNodePlanInfo.getClusterId());
        buildHealthCheck(jkeConfig, clusterNodePlanInfo, KubeproxyPort);
        return ErrorCode.SUCCESS.getCode();
    }
    
    /**
     * @description get the etcd initial cluster param
     * @author Regulus
     * @date 11/30/21 10:10 AM
     * @param clusterNodePlans
     * @return java.lang.String
     */
    String getEtcdInitialCluster(Map<String, List<ClusterNodePlanInfo>>  clusterNodePlans)
    {
        List<String> initialClusters = new ArrayList<>();
    
        for (Map.Entry<String, List<ClusterNodePlanInfo>> entry : clusterNodePlans.entrySet())
        {
            if (entry.getKey().contains(K8sRole.ETCD))
            {
                for (ClusterNodePlanInfo clusterNodePlanInfo : entry.getValue())
                {
                    ClusterNodeInfo clusterNodeInfo = clusterNodePlanInfo.getClusterNodeInfo();
                    String ics = String.format("etcd-%s=https://%s:2380", clusterNodeInfo.getNodeName(), clusterNodeInfo.getInternalAddress());
                    initialClusters.add(ics);
                }
            }
        }
        return StringUtils.join(initialClusters, ",");
    }
    
    /**
     * @description assemble container params for etcd plan
     * @author Regulus
     * @date 11/30/21 10:20 AM
     * @param jkeConfig
     * @param containerPlan
     * @param clusterNodeInfo
     * @param clusterNodePlans 
     * @return com.lnjoying.justice.schema.common.ErrorCode
     */
    ErrorCode assembleEtcdPlan(JkeConfig jkeConfig, ContainerPlan containerPlan,  ClusterNodeInfo clusterNodeInfo, Map<String, List<ClusterNodePlanInfo>>  clusterNodePlans) throws WebSystemException
    {
        DockerInstParams dockerInstParams = containerPlan.getDockerInstParams();
        dockerInstParams.setEntrypoint(new String[]{"/usr/local/bin/etcd"});
        String imageName = clusterDataService.getImage(jkeConfig.getK8sVersion(), containerPlan.getContainerName());
        dockerInstParams.setImage(imageName);
        RestartPolicy restartPolicy = new RestartPolicy();
        restartPolicy.setName(DockerRestartPolicy.ALWAYS);
        dockerInstParams.getHostConfig().setRestartPolicy(restartPolicy);
        Map<String, String> labels = new HashMap<>();
        labels.put(ContainerNameLabel, containerPlan.getContainerName());
        dockerInstParams.setLabels(labels);
        dockerInstParams.getHostConfig().setNetworkMode(DockerNetworkMode.HOST);
    
        String initCluster = getEtcdInitialCluster(clusterNodePlans);
        String clusterState = "new";
        if (clusterNodeInfo.getExistingEtcdCluster())
        {
            clusterState = "existing";
        }
        String listenAddress = "0.0.0.0";
        Map<String,String> commandArgs = new HashMap<>();
    
        String nodeCertName = EnvUtil.getCertNameForHost(clusterNodeInfo, KeyCertName.EtcdCertName);
        
        commandArgs.put("name",                       "etcd-" + clusterNodeInfo.getNodeName());
        commandArgs.put("data-dir",                    EtcdDataDir);
        commandArgs.put("listen-client-urls",          "https://" + listenAddress + ":2379");
        commandArgs.put("initial-advertise-peer-urls", "https://" + clusterNodeInfo.getInternalAddress() + ":2380");
        commandArgs.put("listen-peer-urls",            "https://" + listenAddress + ":2380");
        commandArgs.put("initial-cluster-token",       "etcd-cluster-1");
        commandArgs.put("initial-cluster",             initCluster);
        commandArgs.put("initial-cluster-state",       clusterState);
        commandArgs.put("trusted-ca-file",             EnvUtil.getCertPath(KeyCertName.CACertName));
        commandArgs.put("peer-trusted-ca-file",        EnvUtil.getCertPath(KeyCertName.CACertName));
        commandArgs.put("cert-file",                   EnvUtil.getCertPath(nodeCertName));
        commandArgs.put("key-file",                    EnvUtil.getKeyPath(nodeCertName));
        commandArgs.put("peer-cert-file",              EnvUtil.getCertPath(nodeCertName));
        commandArgs.put("peer-key-file",               EnvUtil.getKeyPath(nodeCertName));
    
        StringBuilder advertiseUrlsBuilder = new StringBuilder();
        advertiseUrlsBuilder.append("https://").append(clusterNodeInfo.getInternalAddress()).append(":2379");
        if (ClsUtils.versionCompare(imageName, MaxEtcdPort4001Version) < 0)
        {
            advertiseUrlsBuilder.append(",https://").append(clusterNodeInfo.getInternalAddress()).append(":4001");
        }
        commandArgs.put("advertise-client-urls", advertiseUrlsBuilder.toString());
        
        if (ClsUtils.versionCompare(imageName, MaxEtcdNoStrictTLSVersion) > 0)
        {
            commandArgs.put("cipher-suites", "TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256,TLS_ECDHE_ECDSA_WITH_AES_256_GCM_SHA384,TLS_ECDHE_ECDSA_WITH_CHACHA20_POLY1305,TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256,TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384,TLS_ECDHE_RSA_WITH_CHACHA20_POLY1305");
        }
    
        setCmdServiceOption(commandArgs, jkeConfig.getK8sVersion(), K8sServiceModule.ETCD);
        if (! commandArgs.containsKey("client-cert-auth"))
        {
            commandArgs.put("client-cert-auth","");
        }
    
        if (! commandArgs.containsKey("peer-client-cert-auth"))
        {
            commandArgs.put("peer-client-cert-auth","");
        }
        
        List<String> envList = new ArrayList<>();
        if (ClsUtils.versionCompare(jkeConfig.getK8sVersion(), MaxEtcdOldEnvVersion) < 0)
        {
            String env = String.format("ETCDCTL_ENDPOINT=https://%s:2379", listenAddress);
            envList.add(env);
        }
        else
        {
            if (listenAddress.equals("0.0.0.0"))
            {
                String env = String.format("ETCDCTL_ENDPOINT=https://%s:2379", "127.0.0.1");
                envList.add(env);
            }
            else
            {
                String env = String.format("ETCDCTL_ENDPOINT=https://%s:2379", listenAddress);
                envList.add(env);
            }
        }
    
        envList.add("ETCDCTL_API=3");
        envList.add(String.format("ETCDCTL_CACERT=%s", EnvUtil.getCertPath(KeyCertName.CACertName)));
        envList.add(String.format("ETCDCTL_CERT=%s", EnvUtil.getCertPath(nodeCertName)));
        envList.add(String.format("ETCDCTL_KEY=%s", EnvUtil.getKeyPath(nodeCertName)));
        if (clusterNodeInfo.getSoftWareInfos() != null)
        {
            DockerInfo dockerInfo = JsonUtils.fromJson(clusterNodeInfo.getSoftWareInfos().get("docker"), DockerInfo.class);
            if (dockerInfo != null)
            {
                String architecture = dockerInfo.getArchitecture();
                if (CollectionUtils.hasContent(architecture) && architecture.equals("aarch64"))
                {
                    architecture = "arm64";
                    String env = String.format("ETCD_UNSUPPORTED_ARCH=%s", architecture);
                    envList.add(env);
                }
            }
        }
    
        EtcdServiceInfo etcdServiceInfo = jkeConfig.getServices().getEtcdServiceInfo();
    
        if (! CollectionUtils.isEmpty(dockerInstParams.getEnv()))
        {
            dockerInstParams.getEnv().addAll(envList);
        }
        else
        {
            dockerInstParams.setEnv(envList);
        }
    
        if (jkeConfig.getCloudProvider() != null &&
                StringUtils.isEmpty(jkeConfig.getCloudProvider().getProvider()))
        {
            commandArgs.put("bind-address", clusterNodeInfo.getInternalAddress());
        }
    
    
        {
            List<String> binds = new ArrayList<>();
            binds.add(ETC_K8S_PATH_BIND);
            binds.add(String.format("/var/lib/etcd:%s",EtcdDataDir));
            dockerInstParams.getHostConfig().setBinds(binds);
        }
    
        setCmdExtraArgs(commandArgs, etcdServiceInfo.getExtraArgs());
        setExtraInfo(dockerInstParams, etcdServiceInfo);
        setDockerInstParamsCmd(dockerInstParams, commandArgs, "");
    
        return ErrorCode.SUCCESS;
    }
    
   /**
    * @description build etcd plan
    * @author Regulus
    * @date 11/30/21 11:01 AM
    * @param jkeConfig
    * @param clusterNodePlanInfo
    * @param clusterNodePlans
    * @return int
    */
    int buildEtcdPlan(JkeConfig jkeConfig,  ClusterNodePlanInfo clusterNodePlanInfo, Map<String, List<ClusterNodePlanInfo>>  clusterNodePlans) throws WebSystemException
    {
        LOGGER.info("build kubelet plan for {}", clusterNodePlanInfo.getClusterId());
        ContainerPlan containerPlan = new ContainerPlan(DeployContainerName.ETCD, jkeConfig.getRegistries());
        clusterNodePlanInfo.addContainerPlan(containerPlan);
        assembleEtcdPlan(jkeConfig, containerPlan, clusterNodePlanInfo.getClusterNodeInfo(), clusterNodePlans);
    
        String sideKickName = Utils.buildStr(DeployContainerName.JKE_SIDEKICK, "_", clusterNodePlanInfo.getClusterId().substring(0,4));
        containerPlan.getDockerInstParams().getHostConfig().setVolumesFrom(Arrays.asList(sideKickName));
        containerPlan.setCoreContainer(true);
        containerPlan.setContainerName(Utils.buildStr(containerPlan.getContainerName(), "_", clusterNodePlanInfo.getClusterId().substring(0,4)));
        
        return 0;
    }
    
    /**
     * @description assemble etcd fix params.
     * @author Regulus
     * @date 11/30/21 11:02 AM
     * @param jkeConfig
     * @param containerPlan 
     * @return com.lnjoying.justice.schema.common.ErrorCode
     */
    ErrorCode assembleEtcdFixParams(JkeConfig jkeConfig, ContainerPlan containerPlan) throws WebSystemException, NoSuchAlgorithmException, UnsupportedEncodingException
    {
        DockerInstParams dockerInstParams = containerPlan.getDockerInstParams();
        dockerInstParams.setImage(clusterDataService.getImage(jkeConfig.getK8sVersion(), containerPlan.getContainerName()));
        Map<String, String> labels = new HashMap<>();
        labels.put(ContainerNameLabel, containerPlan.getContainerName());
        dockerInstParams.setLabels(labels);
        dockerInstParams.getHostConfig().setNetworkMode(DockerNetworkMode.NONE);
        dockerInstParams.getHostConfig().setPrivileged(true);
        dockerInstParams.getHostConfig().setPidMode(PidMode.HOST);
        {
            List<String> binds = new ArrayList<>();
            binds.add(String.format("/var/lib/etcd:%s",EtcdDataDir));
            dockerInstParams.getHostConfig().setBinds(binds);
        }
        
        dockerInstParams.setEntrypoint(new String[]{"sh","-c"});
        String cmd = String.format("chmod 700 %s", EtcdDataDir);
        
        dockerInstParams.setCmd(new String [] {cmd});
        
        return ErrorCode.SUCCESS;
    }
    
    /**
     * @description Build etcd fix permission container. The container is used to fix etcd directory permission.
     * @author Regulus
     * @date 11/30/21 11:02 AM
     * @param jkeConfig
     * @param clusterNodePlanInfo
     * @return int
     */
    int buildEtcdFixPerm(JkeConfig jkeConfig, ClusterNodePlanInfo clusterNodePlanInfo) throws NoSuchAlgorithmException, UnsupportedEncodingException, WebSystemException
    {
        LOGGER.info("build etcd fix checker plan for {}", clusterNodePlanInfo.getClusterId());
        ContainerPlan containerPlan = new ContainerPlan(DeployContainerName.ETCD_FIX_PERM, jkeConfig.getRegistries());
        clusterNodePlanInfo.addContainerPlan(containerPlan);
    
        assembleEtcdFixParams(jkeConfig, containerPlan);
        containerPlan.setContainerName(Utils.buildStr(containerPlan.getContainerName(), "_", clusterNodePlanInfo.getClusterId().substring(0,4)));
        return 0;
    }
    
    
    /**
     * @description Build health check container plan. The container is used to check etcd healthy.
     * @author Regulus
     * @date 11/30/21 11:03 AM
     * @param jkeConfig
     * @param clusterNodePlanInfo 
     * @return int
     */
    int buildHealthCheckEtcd(JkeConfig jkeConfig, ClusterNodePlanInfo clusterNodePlanInfo) throws WebSystemException
    {
        LOGGER.info("build etcd health checker plan for {}", clusterNodePlanInfo.getClusterId());
        ContainerPlan containerPlan = new ContainerPlan(DeployContainerName.JKE_HEALTH_CHECKER, jkeConfig.getRegistries());
        clusterNodePlanInfo.addContainerPlan(containerPlan);
        String healthCheckUrl = "https://localhost:2379/health";
    
        String nodeCertName = EnvUtil.getCertNameForHost(clusterNodePlanInfo.getClusterNodeInfo(), KeyCertName.EtcdCertName);
        
        String certPath = EnvUtil.getCertPath(nodeCertName);
        String keyPath = EnvUtil.getKeyPath(nodeCertName);
        assembleHttpsHealthCheckParams(jkeConfig, containerPlan, certPath, keyPath, healthCheckUrl);
        
        containerPlan.setLogSucess("{\"health\":\"true\"}");
        String containerName = String.format("%s_%d_%s", containerPlan.getContainerName(), 2379, clusterNodePlanInfo.getClusterId().substring(0,4));
        containerPlan.setContainerName(containerName);
    
        return 0;
    }
    
    ErrorCode assembleClusterCleanerParams(JkeConfig jkeConfig, ContainerPlan containerPlan) throws WebSystemException
    {
        DockerInstParams dockerInstParams = containerPlan.getDockerInstParams();
        dockerInstParams.setImage(clusterDataService.getImage(jkeConfig.getK8sVersion(), containerPlan.getContainerName()));
        Map<String, String> labels = new HashMap<>();
        labels.put(ContainerNameLabel, containerPlan.getContainerName());
        dockerInstParams.setLabels(labels);
        dockerInstParams.getHostConfig().setNetworkMode(DockerNetworkMode.NONE);
        dockerInstParams.getHostConfig().setPrivileged(true);
        dockerInstParams.getHostConfig().setPidMode(PidMode.HOST);
        dockerInstParams.getHostConfig().setBinds(Arrays.asList(HOST_PROC_PATH_BIND, HOST_DEV_PATH_BIND, DOCKER_SOCKET_BIND));
        dockerInstParams.setEntrypoint(new String[]{"cluster-clean"});
        return ErrorCode.SUCCESS;
    }
    
    int buildClusterCleanerUnDployPlan(JkeConfig jkeConfig, ClusterNodePlanInfo clusterNodePlanInfo) throws WebSystemException
    {
        LOGGER.info("build cluster cleaner plan for {}", clusterNodePlanInfo.getClusterId());
    
        ContainerPlan containerPlan = new ContainerPlan(DeployContainerName.JKE_CLUSTER_CLEANER, jkeConfig.getRegistries());
        clusterNodePlanInfo.addContainerUPlan(containerPlan);
        assembleClusterCleanerParams(jkeConfig, containerPlan);
        containerPlan.setNextAction(PlanAction.REMOVE_BEFORE);
        containerPlan.setContainerName(Utils.buildStr(containerPlan.getContainerName(), "_", clusterNodePlanInfo.getClusterId().substring(0,4), "_",  "undeploy"));
        return 0;
    }
    
    int buildClusterCleanerDeployPlan(JkeConfig jkeConfig, ClusterNodePlanInfo clusterNodePlanInfo) throws WebSystemException
    {
        LOGGER.info("build cluster cleaner plan for {}", clusterNodePlanInfo.getClusterId());
        
        ContainerPlan containerPlan = new ContainerPlan(DeployContainerName.JKE_CLUSTER_CLEANER, jkeConfig.getRegistries());
        clusterNodePlanInfo.addContainerPlan(containerPlan);
        assembleClusterCleanerParams(jkeConfig, containerPlan);
        containerPlan.setNextAction(PlanAction.REMOVE_BEFORE);
        containerPlan.setContainerName(Utils.buildStr(containerPlan.getContainerName(), "_", clusterNodePlanInfo.getClusterId().substring(0,4), "_", "deploy"));
        return 0;
    }
    
    void buildCleanJke(ClusterNodePlanInfo clusterNodePlanInfo)
    {
        ContainerPlan containerPlan = new ContainerPlan("");
        containerPlan.setNextAction(PlanAction.REMOVE_NODE_JKE);
        clusterNodePlanInfo.addContainerUPlan(containerPlan);
    }
    
    Set<PlanAction> getCommonActionList()
    {
        Set<PlanAction> actionList = new HashSet<>();
        actionList.add(PlanAction.INIT);
        actionList.add(PlanAction.REMOVE_BEFORE);
        actionList.add(PlanAction.CREATE);
        actionList.add(PlanAction.LIST);
        actionList.add(PlanAction.LOG);
        actionList.add(PlanAction.LOG_CHECK);
        actionList.add(PlanAction.REMOVE_AFTER);
        return actionList;
    }

    Set<PlanAction> getCommonActionListNoRemoveAf()
    {
        Set<PlanAction> actionList = new HashSet<>();
        actionList.add(PlanAction.INIT);
        actionList.add(PlanAction.REMOVE_BEFORE);
        actionList.add(PlanAction.CREATE);
        actionList.add(PlanAction.LIST);
        actionList.add(PlanAction.LOG);
        actionList.add(PlanAction.LOG_CHECK);
        return actionList;
    }
    
    /**
     * @description Assemble container params for port checker.
     * @author Regulus
     * @date 11/30/21 11:07 AM
     * @param jkeConfig
     * @param containerPlan
     * @param ports
     * @return com.lnjoying.justice.schema.common.ErrorCode
     */
    ErrorCode assemblePortCheckerParams(JkeConfig jkeConfig, ContainerPlan containerPlan, String ports) throws WebSystemException
    {
        DockerInstParams dockerInstParams = containerPlan.getDockerInstParams();
        dockerInstParams.getHostConfig().setPrivileged(false);
        dockerInstParams.getHostConfig().setNetworkMode(DockerNetworkMode.HOST);
        String image = clusterDataService.getImage(jkeConfig.getK8sVersion(), containerPlan.getContainerName());
        if (StringUtils.isEmpty(image))
        {
            throw new WebSystemException(ErrorCode.CLUSTER_K8S_IMAGE_NOT_CFG, ErrorLevel.INFO);
        }
        
        dockerInstParams.setImage(image);
        dockerInstParams.setCmd(ports.split(","));
        dockerInstParams.setEntrypoint(new String[]{"port-check"});
        dockerInstParams.getHostConfig().setPrivileged(true);
        containerPlan.setAutoRun(true);
        
        return ErrorCode.SUCCESS;
    }
    
    /**
     * @description Build port checker plan.The container is used to check ports can be available.
     * @author Regulus
     * @date 11/30/21 11:08 AM
     * @param jkeConfig
     * @param clusterNodePlanInfo
     * @param ports
     * @return int
     */
    int buildPortCheckerPlan(JkeConfig jkeConfig, ClusterNodePlanInfo clusterNodePlanInfo, String ports) throws WebSystemException
    {
        LOGGER.info("build port checker plan for {}", clusterNodePlanInfo.getClusterId());
        ContainerPlan containerPlan = new ContainerPlan(DeployContainerName.JKE_PORT_CHECKER, jkeConfig.getRegistries());
        clusterNodePlanInfo.addContainerPlan(containerPlan);
        assemblePortCheckerParams(jkeConfig, containerPlan, ports);
        containerPlan.setLogFailed("unavailable");
        containerPlan.setContainerName(Utils.buildStr(containerPlan.getContainerName(), "_", clusterNodePlanInfo.getClusterId().substring(0,4)));
        return 0;
    }
    
    /**
     * @description Build health check container. Can be used to check k8s service healthy.
     * @author Regulus
     * @date 11/30/21 11:10 AM
     * @param jkeConfig
     * @param clusterNodePlanInfo
     * @param port
     * @return int
     */
    int buildHealthCheck(JkeConfig jkeConfig, ClusterNodePlanInfo clusterNodePlanInfo, int port) throws WebSystemException
    {
        LOGGER.info("build kube-health health checker plan for {}", clusterNodePlanInfo.getClusterId());
        ContainerPlan containerPlan = new ContainerPlan(DeployContainerName.JKE_HEALTH_CHECKER, jkeConfig.getRegistries());
        clusterNodePlanInfo.addContainerPlan(containerPlan);
        String healthCheckUrl = ClsUtils.getHealthCheckURL(false, port);
        assembleHttpHealthCheckParams(jkeConfig, containerPlan, healthCheckUrl);
        if (port != KubeproxyPort)
        {
            containerPlan.setLogSucess("ok");
        }
        
        String containerName = String.format("%s_%d_%s", containerPlan.getContainerName(), port, clusterNodePlanInfo.getClusterId().substring(0,4));
        containerPlan.setContainerName(containerName);
        
        return 0;
    }
    
    /**
     * @description A util function to assemble cmd extra args.
     * @author Regulus
     * @date 11/30/21 11:12 AM
     * @param commandArgs
     * @param extraArgs
     * @return void
     */
    void setCmdExtraArgs(Map<String,String> commandArgs, Map<String,String> extraArgs)
    {
        if (! CollectionUtils.isEmpty(extraArgs))
        {
            for(Map.Entry<String, String> entry: extraArgs.entrySet())
            {
                commandArgs.put(entry.getKey(), entry.getValue());
            }
        }
    }
    
    /**
     * @description A util function to inject k8s service options to command args
     * @author Regulus
     * @date 11/30/21 11:24 AM
     * @param commandArgs
     * @param k8sVersion
     * @param moduleName 
     * @return int
     */
    int setCmdServiceOption(Map<String,String> commandArgs, String k8sVersion, String moduleName) throws WebSystemException
    {
        Map<String,String> controllerOption = clusterDataService.getK8sServiceOption(k8sVersion, moduleName);
        if (! CollectionUtils.isEmpty(controllerOption))
        {
            for (String key : controllerOption.keySet())
            {
                Object value = controllerOption.get(key);
                if (value == null)
                {
                    continue;
                }
                
                String valueStr = value.toString();
                if (! StringUtils.isEmpty(valueStr))
                {
                    commandArgs.put(key, valueStr);
                }
                commandArgs.put(key, value.toString());
            }
        }
        
        return ErrorCode.SUCCESS.getCode();
    }
    
    /**
     * @description A extra info for docker inst.
     * @author Regulus
     * @date 11/30/21 11:28 AM
     * @param dockerInstParams
     * @param baseService 
     * @return void
     */
    void setExtraInfo(DockerInstParams dockerInstParams, BaseService baseService)
    {
        if (! CollectionUtils.isEmpty(baseService.getExtraBinds()))
        {
            if (CollectionUtils.isEmpty(dockerInstParams.getHostConfig().getBinds()))
            {
                dockerInstParams.getHostConfig().setBinds(baseService.getExtraBinds());
            }
            else
            {
                dockerInstParams.getHostConfig().getBinds().addAll(baseService.getExtraBinds());
            }
            
        }
        
        if (! CollectionUtils.isEmpty(baseService.getExtraEnvs()))
        {
            if (CollectionUtils.isEmpty(dockerInstParams.getEnv()))
            {
                dockerInstParams.setEnv(baseService.getExtraEnvs());
            }
            else
            {
                dockerInstParams.getEnv().addAll(baseService.getExtraEnvs());
            }
            
        }
    }
    
    /**
     * @description set docker container inst cmd args.
     * @author Regulus
     * @date 11/30/21 1:57 PM
     * @param dockerInstParams
     * @param commandArgs
     * @param program 
     * @return void
     */
    void setDockerInstParamsCmd(DockerInstParams dockerInstParams, Map<String,String> commandArgs, String program)
    {
        List<String> cmds = new ArrayList<>();
        if (! StringUtils.isEmpty(program))
        {
            cmds.add(program);
        }
        
        if (! CollectionUtils.isEmpty(commandArgs))
        {
            for(Map.Entry<String, String> entry: commandArgs.entrySet())
            {
                String cmdStr = String.format("--%s=%s", entry.getKey(), entry.getValue());
                cmds.add(cmdStr);
            }
            dockerInstParams.setCmd(cmds.toArray(new String[0]));
        }
    }
    
    /**
     * @description assemble container params for kubelet plan
     * @author Regulus
     * @date 11/30/21 2:03 PM
     * @param jkeConfig
     * @param containerPlan
     * @param clusterNodeInfo 
     * @return int
     */
    int assembleKubeletPlan(JkeConfig jkeConfig, NetworkOption networkOption, ContainerPlan containerPlan, ClusterNodeInfo clusterNodeInfo) throws WebSystemException, NoSuchAlgorithmException, UnsupportedEncodingException
    {
        DockerInstParams dockerInstParams = containerPlan.getDockerInstParams();
        dockerInstParams.setEntrypoint(new String[]{DefaultToolsEntrypoint});
        dockerInstParams.setImage(clusterDataService.getImage(jkeConfig.getK8sVersion(), containerPlan.getContainerName()));
        RestartPolicy restartPolicy = new RestartPolicy();
        restartPolicy.setName(DockerRestartPolicy.ALWAYS);
        dockerInstParams.getHostConfig().setRestartPolicy(restartPolicy);
        Map<String, String> labels = new HashMap<>();
        labels.put(ContainerNameLabel, containerPlan.getContainerName());
        dockerInstParams.setLabels(labels);
        dockerInstParams.getHostConfig().setNetworkMode(DockerNetworkMode.HOST);
        dockerInstParams.getHostConfig().setPrivileged(true);
        dockerInstParams.getHostConfig().setPidMode(PidMode.HOST);
        
        Map<String,String> commandArgs = new HashMap<>();
        
        String pauseImage = clusterDataService.getImage(jkeConfig.getK8sVersion(), "pod-infra-container");
        
        KubeletServiceInfo kubeletServiceInfo = jkeConfig.getServices().getKubeletServiceInfo();
        
        if (StringUtils.isEmpty(pauseImage))
        {
            throw new WebSystemException(ErrorCode.CLUSTER_K8S_IMAGE_NOT_CFG, ErrorLevel.INFO);
        }
        
        commandArgs.put("client-ca-file",             EnvUtil.getCertPath(KeyCertName.CACertName));
        if (jkeConfig.getCloudProvider() != null)
        {
            commandArgs.put("cloud-provider",             jkeConfig.getCloudProvider().getProvider());
        }
        commandArgs.put("cluster-dns",                networkOption.getClusterDnsServer());
        commandArgs.put("cluster-domain",             networkOption.getClusterDomain());
        commandArgs.put("fail-swap-on",               Boolean.toString(kubeletServiceInfo.getFailSwapOn()));
        commandArgs.put("hostname-override",          clusterNodeInfo.getNodeName());
        commandArgs.put("kubeconfig",                 EnvUtil.getConfigPath(KeyCertName.KubeNodeCertName));
        commandArgs.put("pod-infra-container-image",  pauseImage);
        commandArgs.put("root-dir",                  "/var/lib/kubelet");
        
        if (jkeConfig.getDind())
        {
            commandArgs.put("address","0.0.0.0");
        }
        
        if (clusterNodeInfo.getClusterRoles().contains(K8sRole.CONTROLLER) && ! clusterNodeInfo.getClusterRoles().contains(K8sRole.WORKER))
        {
            commandArgs.put("register-with-taints", UnschedulableControlTaint);
        }
        
        commandArgs.put("node-ip", clusterNodeInfo.getInternalAddress());
        
        if (jkeConfig.getCloudProvider() != null && StringUtils.isEmpty(jkeConfig.getCloudProvider().getProvider()))
        {
            commandArgs.put("cloud-config", CloudConfigFileName);
            String env = String.format("%s=%s", CloudConfigSumEnv, ClsUtils.getStringChecksum(jkeConfig.getCloudProvider().getCloudConfigFile()));
            kubeletServiceInfo.getExtraEnvs().add(env);
        }
        
        if (jkeConfig.getServices().getKubeletServiceInfo().getGenerateServingCertificate())
        {
            String certName = EnvUtil.getCertPath(EnvUtil.getCertNameForHost(clusterNodeInfo, KeyCertName.KubeletCertName));
            commandArgs.put("tls-cert-file", certName);
            commandArgs.put("tls-private-key-file", EnvUtil.getKeyPath(certName));
        }
        setCmdServiceOption(commandArgs, jkeConfig.getK8sVersion(), K8sServiceModule.KUBELET);
        
        
        if (jkeConfig.getCRIDockerdEnabled())
        {
            commandArgs.put("container-runtime", "remote");
            commandArgs.put("container-runtime-endpoint", "/var/run/dockershim.sock");
        }
        
        {
            List<String> binds = new ArrayList<>();
            binds.add(ETC_K8S_PATH_BIND);
            binds.add("/etc/cni:/etc/cni:rw,z");
            binds.add("/opt/cni:/opt/cni:rw,z");
            binds.add("/var/lib/cni:/var/lib/cni:z");
            binds.add("/var/lib/calico:/var/lib/calico:z");
            binds.add("/etc/resolv.conf:/etc/resolv.conf");
            binds.add("/sys:/sys:rprivate");
            binds.add("/var/lib/kubelet:/var/lib/kubelet:shared,z");
            String dockerRootDir = "/var/lib/docker";
            if (clusterNodeInfo.getSoftWareInfos() != null && clusterNodeInfo.getSoftWareInfos().get("docker") != null)
            {
                DockerInfo dockerInfo = JsonUtils.fromJson(clusterNodeInfo.getSoftWareInfos().get("docker"), DockerInfo.class);
    
                dockerRootDir = dockerInfo.getDockerRootDir();
            }
    
            String dockerRoot = String.format("%s:%s:rw,rslave,z", dockerRootDir, dockerRootDir);
            binds.add(dockerRoot);
            binds.add("/var/lib/joy:/var/lib/joy:shared,z");
            binds.add("/var/run:/var/run:rw,rprivate");
            binds.add("/run:/run:rprivate");
            binds.add("/etc/ceph:/etc/ceph");
            binds.add("/dev:/host/dev:rprivate");
            binds.add("/var/log/containers:/var/log/containers:z");
            binds.add("/var/log/pods:/var/log/pods:z");
            binds.add("/usr:/host/usr:ro");
            binds.add("/etc:/host/etc:ro");
            dockerInstParams.getHostConfig().setBinds(binds);
        }
        
        setCmdExtraArgs(commandArgs, kubeletServiceInfo.getExtraArgs());
        
        setExtraInfo(dockerInstParams, kubeletServiceInfo);
        
        if (! CollectionUtils.isEmpty(jkeConfig.getRegistries()))
        {
            String kubeletDockerConfig = getKubeletDockerConfig(jkeConfig.getRegistries());
            String dockerEnv = String.format("%s=%s", KubeletDockerConfigEnv, ClsUtils.getBase64(kubeletDockerConfig));
            if (CollectionUtils.isEmpty(dockerInstParams.getEnv()))
            {
                dockerInstParams.setEnv(new ArrayList<>());
            }
            dockerInstParams.getEnv().add(dockerEnv);

            String dockerFileEnv = String.format("%s=%s", KubeletDockerConfigFileEnv, KubeletDockerConfigPath);
            dockerInstParams.getEnv().add(dockerFileEnv);
        }
        
        DNSAddonInfo dnsAddonInfo = jkeConfig.getSystemAddons().getDns();
        if (dnsAddonInfo != null)
        {
            commandArgs.put("cluster-dns", dnsAddonInfo.getNodeLocal().getIpAddress());
        }
        setDockerInstParamsCmd(dockerInstParams, commandArgs, "kubelet");
    
        return ErrorCode.SUCCESS.getCode();
    }
    
    /**
     * @description Build kubelet plan
     * @author Regulus
     * @date 11/30/21 5:22 PM
     * @param jkeConfig
     * @param clusterNodePlanInfo 
     * @return int
     */
    int buildKubeletPlan(JkeConfig jkeConfig, ClusterNodePlanInfo clusterNodePlanInfo, NetworkOption networkOption) throws WebSystemException, NoSuchAlgorithmException, UnsupportedEncodingException
    {
        LOGGER.info("build kubelet plan for {}", clusterNodePlanInfo.getClusterId());
        ContainerPlan containerPlan = new ContainerPlan(DeployContainerName.KUBELET, jkeConfig.getRegistries());
        clusterNodePlanInfo.addContainerPlan(containerPlan);
        assembleKubeletPlan(jkeConfig, networkOption, containerPlan, clusterNodePlanInfo.getClusterNodeInfo());
        containerPlan.setCoreContainer(true);
        String sideKick = Utils.buildStr(DeployContainerName.JKE_SIDEKICK, "_", clusterNodePlanInfo.getClusterId().substring(0,4));
        containerPlan.getDockerInstParams().getHostConfig().setVolumesFrom(Arrays.asList(sideKick));
        containerPlan.setContainerName(Utils.buildStr(containerPlan.getContainerName(), "_", clusterNodePlanInfo.getClusterId().substring(0,4)));
        return 0;
    }
    
    /**
     * @description Builld kube-proxy plan
     * @author Regulus
     * @date 11/30/21 5:24 PM
     * @param jkeConfig
     * @param clusterNodePlanInfo 
     * @return int
     */
    int buildKubeProxyPlan(JkeConfig jkeConfig, ClusterNodePlanInfo clusterNodePlanInfo) throws NoSuchAlgorithmException, UnsupportedEncodingException, WebSystemException
    {
        LOGGER.info("build kube-proxy plan for {}", clusterNodePlanInfo.getClusterId());
        ContainerPlan containerPlan = new ContainerPlan(DeployContainerName.KUBE_PROXY, jkeConfig.getRegistries());
        clusterNodePlanInfo.addContainerPlan(containerPlan);
        assembleKubeProxyPlan(jkeConfig, containerPlan, clusterNodePlanInfo.getClusterNodeInfo());
        containerPlan.setCoreContainer(true);
        String sideKick = Utils.buildStr(DeployContainerName.JKE_SIDEKICK, "_", clusterNodePlanInfo.getClusterId().substring(0,4));
        containerPlan.getDockerInstParams().getHostConfig().setVolumesFrom(Arrays.asList(sideKick));
        containerPlan.setContainerName(Utils.buildStr(containerPlan.getContainerName(), "_", clusterNodePlanInfo.getClusterId().substring(0,4)));
        return 0;
    }
    
    protected  void buildUndeployKubelet(ClusterNodePlanInfo clusterNodePlanInfo)
    {
        ContainerPlan containerPlan = new ContainerPlan(DeployContainerName.KUBELET);
        containerPlan.setNextAction(PlanAction.REMOVE_AFTER);
        clusterNodePlanInfo.addContainerUPlan(containerPlan);
    }
    
    protected void buildUndeployKubeProxy(ClusterNodePlanInfo clusterNodePlanInfo)
    {
        ContainerPlan containerPlan = new ContainerPlan(DeployContainerName.KUBE_PROXY);
        containerPlan.setNextAction(PlanAction.REMOVE_AFTER);
        clusterNodePlanInfo.addContainerUPlan(containerPlan);
    }
    
    protected void buildUndeployNginxProxy(ClusterNodePlanInfo clusterNodePlanInfo)
    {
        ContainerPlan containerPlan = new ContainerPlan(DeployContainerName.NGINX_PROXY);
        containerPlan.setNextAction(PlanAction.REMOVE_AFTER);
        clusterNodePlanInfo.addContainerUPlan(containerPlan);
    }
    
    protected  void buildUndeployEtcd(ClusterNodePlanInfo clusterNodePlanInfo)
    {
        ContainerPlan containerPlan = new ContainerPlan(DeployContainerName.ETCD);
        containerPlan.setNextAction(PlanAction.REMOVE_AFTER);
        clusterNodePlanInfo.addContainerUPlan(containerPlan);
    }
    
    protected void assembleFileDeployer(JkeConfig jkeConfig, ClusterNodePlanInfo clusterNodePlanInfo, ContainerPlan containerPlan ) throws JsonProcessingException
    {
        Map<String, String> fileMap = new HashMap<>();
    
        if (jkeConfig.getCloudProvider() != null && CollectionUtils.hasContent(jkeConfig.getCloudProvider().getProvider()))
        {
            fileMap.put(CLOUDCOFIG, jkeConfig.getCloudProvider().getCloudConfigFile());
        }
    
        AuthWebhookConfig webhook = jkeConfig.getAuthentication().getWebhook();
        if (webhook != null)
        {
            if (CollectionUtils.hasContent(webhook.getConfigFile()))
            {
                fileMap.put("authentication-token-webhook-config-file", webhook.getConfigFile());
            }
        }
    }
    
    protected int  buildFileDeployer(JkeConfig jkeConfig, ClusterNodePlanInfo clusterNodePlanInfo) throws JsonProcessingException
    {
        LOGGER.info("build kube-controller plan for {}", clusterNodePlanInfo.getClusterId());
        ContainerPlan containerPlan = new ContainerPlan(DeployContainerName.FILE_DEPLOYER, jkeConfig.getRegistries());
        assembleFileDeployer(jkeConfig, clusterNodePlanInfo, containerPlan);
        clusterNodePlanInfo.addContainerPlan(containerPlan);
        containerPlan.setContainerName(Utils.buildStr(containerPlan.getContainerName(), "_", clusterNodePlanInfo.getClusterId().substring(0,4)));
        return 0;
    }
    
    
    ErrorCode assembleNodeLabelDeployerParams(JkeConfig jkeConfig, ClusterNodePlanInfo clusterNodePlanInfo) throws WebSystemException
    {
        ContainerPlan containerPlan = new ContainerPlan(DeployContainerName.NODE_LABEL_DEPLOYER, jkeConfig.getRegistries());
        clusterNodePlanInfo.addContainerPlan(containerPlan);
    
        DockerInstParams dockerInstParams = containerPlan.getDockerInstParams();
        dockerInstParams.setImage(clusterDataService.getImage(jkeConfig.getK8sVersion(), containerPlan.getContainerName()));
        Map<String, String> labels = new HashMap<>();
        labels.put(ContainerNameLabel, containerPlan.getContainerName());
        dockerInstParams.setLabels(labels);
        dockerInstParams.getHostConfig().setNetworkMode(DockerNetworkMode.NONE);
        dockerInstParams.getHostConfig().setPrivileged(true);
        dockerInstParams.getHostConfig().setPidMode(PidMode.HOST);
        
        List<String> envs = new ArrayList<>();
        Map<String, String> nodeRoles = clusterNodePlanInfo.getClusterNodeInfo().getLabels();
        List<String> cmds = new ArrayList<>();
    
        if (! CollectionUtils.isEmpty(nodeRoles))
        {
            nodeRoles.forEach((k,v) ->{
                String label = String.format("%s=%s", k,v);
                cmds.add(label);
            });
        }
       
        
        String nodeEnv = String.format("NODENAME=%s",clusterNodePlanInfo.getClusterNodeInfo().getNodeName());
        envs.add(nodeEnv);
        
        dockerInstParams.setEntrypoint(new String[]{"node-label"});
        dockerInstParams.setEnv(envs);
        dockerInstParams.setCmd(cmds.toArray(new String[0]));
        dockerInstParams.getHostConfig().setBinds(Arrays.asList(ETC_K8S_PATH_BIND));
        dockerInstParams.getHostConfig().setNetworkMode(DockerNetworkMode.HOST);
        containerPlan.setContainerName(Utils.buildStr(containerPlan.getContainerName(), "_", clusterNodePlanInfo.getClusterId().substring(0,4)));
        return ErrorCode.SUCCESS;
    }
    
    protected int buildNodeLabelCfgDeployer(JkeConfig jkeConfig, ClusterNodePlanInfo clusterNodePlanInfo)
    {
        LOGGER.info("build node label plan for {}", clusterNodePlanInfo.getClusterId());
        assembleNodeLabelDeployerParams(jkeConfig, clusterNodePlanInfo);
        return 0;
    }
}
