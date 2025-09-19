package com.lnjoying.justice.cluster.manager.service.plan.k8s;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.collect.Lists;
import com.lnjoying.justice.cluster.manager.common.*;
import com.lnjoying.justice.cluster.manager.config.EnvCfg;
import com.lnjoying.justice.cluster.manager.domain.dto.model.admission.AdmissionConfiguration;
import com.lnjoying.justice.cluster.manager.domain.dto.model.cluster.*;
import com.lnjoying.justice.cluster.manager.domain.model.*;
import com.lnjoying.justice.cluster.manager.domain.model.ClusterNodeInfo;
import com.lnjoying.justice.commonweb.util.CollectionUtils;
import com.lnjoying.justice.commonweb.util.JsonUtils;
import com.lnjoying.justice.schema.entity.docker.DockerInstParams;
import com.lnjoying.justice.schema.entity.docker.RestartPolicy;
import com.lnjoying.justice.cluster.manager.service.plan.ContainerPlan;
import com.lnjoying.justice.cluster.manager.util.ClsUtils;
import com.lnjoying.justice.cluster.manager.util.EnvUtil;
import com.lnjoying.justice.commonweb.exception.WebSystemException;
import com.lnjoying.justice.schema.common.ErrorCode;
import com.micro.core.common.Utils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.Yaml;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * @description service for building controller plan
 * @author Regulus
 * @date 11/30/21 8:36 PM
 */
@Service("controllerPlanService")
public class ControllerPlanServiceImpl extends AbstractK8sPlanService
{
    private static Logger LOGGER = LogManager.getLogger();
    
    @Autowired
    EnvCfg envCfg;
    
    final String AuthnWebhookFileName  = "/etc/kubernetes/kube-api-authn-webhook.yaml";
    final String EncryptionProviderFilePath = "/etc/kubernetes/ssl/encryption.yaml";
    
    final String EncryptionProviderConfigArgument      = "encryption-provider-config";
    
    final List<String> admissionControlOptionNames     = Arrays.asList("enable-admission-plugins", "admission-control");
    
    final String KubeAPIArgAuditLogPath                = "audit-log-path";
    final String KubeAPIArgAuditLogMaxAge              = "audit-log-maxage";
    final String KubeAPIArgAuditLogMaxBackup           = "audit-log-maxbackup";
    final String KubeAPIArgAuditLogMaxSize             = "audit-log-maxsize";
    final String KubeAPIArgAuditLogFormat              = "audit-log-format";
    final String KubeAPIArgAuditPolicyFile             = "audit-policy-file";
    final String DefaultKubeAPIArgAuditLogPathValue    = "/var/log/kube-audit/audit-log.json";
    final String AUDIT_PLICY_File_Prefix = "audit-policy";
    final String DefaultKubeAPIArgAuditPolicyFileValue = "/etc/kubernetes/audit-policy.yaml";
    final String AuditLogConfigSumEnv                  = "JKE_AUDITLOG_CONFIG_CHECKSUM";
    final String KubeAPIArgAdmissionControlConfigFile = "admission-control-config-file";
    final String DefaultKubeAPIArgAdmissionControlConfigFileValue = "/etc/kubernetes/admission.yaml";
    final String AdmissionFileNamePrefix = "admission";
    final String AuthnWebhookFilePrefix = "kube-api-authn-webhook";
    Yaml yaml = new Yaml();
    
    @Override
    public int buildNodeDeployPlan(ClusterInnerInfo clusterInnerInfo, ClusterNodePlanInfo clusterNodePlanInfo)
    {
        LOGGER.info("begin build controller plan on node {}, for cluster {}", clusterNodePlanInfo.getClusterNodeInfo().getNodeId(), clusterNodePlanInfo.getClusterId());
    
        try
        {
            JkeConfig jkeConfig =  clusterInnerInfo.getJkeConfig();
            Map<String, List<ClusterNodePlanInfo>>  clusterNodePlans = clusterInnerInfo.getClusterNodePlanMap();
            String roles = clusterNodePlanInfo.getClusterNodeInfo().getClusterRoles();
    
            buildClusterCleanerDeployPlan(clusterInnerInfo.getJkeConfig(), clusterNodePlanInfo);
    
            buildPortCheckerPlan(jkeConfig, clusterNodePlanInfo, getNeedCheckPorts(roles));
            Map<String, X509CertificateInfo> certMap = clusterInnerInfo.getCertMap();
            buildCertDeployerPlan(jkeConfig, certMap, clusterNodePlanInfo);
            buildServiceSideKickPlan(jkeConfig, clusterNodePlanInfo);
            if (clusterNodePlanInfo.getClusterNodeInfo().getClusterRoles().contains(K8sRole.ETCD))
            {
                buildEtcdFixPerm(jkeConfig, clusterNodePlanInfo);
                buildEtcdPlan(jkeConfig, clusterNodePlanInfo, clusterNodePlans);
                buildHealthCheckEtcd(jkeConfig, clusterNodePlanInfo);
            }
    
            buildFileDeployer(jkeConfig, clusterNodePlanInfo);
            buildKubeApiserverPlan(jkeConfig, clusterNodePlanInfo, clusterNodePlans);
            buildHealthCheckApiServer(jkeConfig, clusterNodePlanInfo);
            buildKubeControllerManagerPlan(jkeConfig,certMap,clusterNodePlanInfo);
            buildHealthCheckController(jkeConfig, clusterNodePlanInfo);
            buildKubeSchedulerPlan(jkeConfig,certMap,clusterNodePlanInfo);
            buildHealthCheckScheduler(jkeConfig, clusterNodePlanInfo);
            buildAuthnResource(jkeConfig, clusterNodePlanInfo);
            buildKubeletPlan(jkeConfig,clusterNodePlanInfo, clusterInnerInfo.getNetworkOption());
            buildHealthCheckKubelet(jkeConfig, clusterNodePlanInfo);
            buildKubeProxyPlan(jkeConfig,clusterNodePlanInfo);
            buildHealthChecKubeProxy(jkeConfig, clusterNodePlanInfo);
            buildLogLinkerPlan(jkeConfig, clusterNodePlanInfo);
            buildNodeLabelCfgDeployer(jkeConfig, clusterNodePlanInfo);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("build controller deploy plan for cluster:{} error:{}", clusterInnerInfo.getClusterId(), e);
            return ErrorCode.CLUSTER_K8S_PLAN_ABNORMAL.getCode();
        }
    
        return ErrorCode.SUCCESS.getCode();
    }
    
    ErrorCode assembleResourceAuthParams(JkeConfig jkeConfig, ContainerPlan containerPlan) throws WebSystemException
    {
        DockerInstParams dockerInstParams = containerPlan.getDockerInstParams();
        dockerInstParams.setImage(clusterDataService.getImage(jkeConfig.getK8sVersion(), containerPlan.getContainerName()));
        Map<String, String> labels = new HashMap<>();
        labels.put(ContainerNameLabel, containerPlan.getContainerName());
        dockerInstParams.setLabels(labels);
        dockerInstParams.getHostConfig().setNetworkMode(DockerNetworkMode.NONE);
        dockerInstParams.getHostConfig().setPrivileged(true);
        dockerInstParams.getHostConfig().setPidMode(PidMode.HOST);
        List<String> cmds = new ArrayList<>();

        cmds.add("-c");
        String cmd = Utils.buildStr("curl --insecure -sfL ", envCfg.getAddonFileUrl(), "authz-resource.yaml", " | kubectl --kubeconfig /etc/kubernetes/ssl/kubecfg-kube-admin.yaml apply -f -");
        cmds.add(cmd);
        
        dockerInstParams.setCmd(cmds.toArray(new String[0]));
        dockerInstParams.setEntrypoint(new String[]{"/bin/bash"});
        dockerInstParams.getHostConfig().setBinds(Arrays.asList(ETC_K8S_PATH_BIND));
        dockerInstParams.getHostConfig().setNetworkMode(DockerNetworkMode.HOST);
        return ErrorCode.SUCCESS;
    }
    
    void buildAuthnResource(JkeConfig jkeConfig, ClusterNodePlanInfo clusterNodePlanInfo)
    {
        LOGGER.info("build auth resource  plan for {}", clusterNodePlanInfo.getClusterId());
        ContainerPlan containerPlan = new ContainerPlan(DeployContainerName.JKE_RESOURCE_AUTHER, jkeConfig.getRegistries());
        clusterNodePlanInfo.addContainerPlan(containerPlan);
        assembleResourceAuthParams(jkeConfig, containerPlan);
        containerPlan.setContainerName(Utils.buildStr(containerPlan.getContainerName(), "_", clusterNodePlanInfo.getClusterId().substring(0,4)));
    
    }
    
    void buildUndeployApiServer(ClusterNodePlanInfo clusterNodePlanInfo)
    {
        ContainerPlan containerPlan = new ContainerPlan(DeployContainerName.KUBE_APISERVER);
        containerPlan.setNextAction(PlanAction.REMOVE_AFTER);
        clusterNodePlanInfo.addContainerUPlan(containerPlan);
    }
    
    void buildUndeployScheduler(ClusterNodePlanInfo clusterNodePlanInfo)
    {
        ContainerPlan containerPlan = new ContainerPlan(DeployContainerName.KUBE_SCHEDULER);
        containerPlan.setNextAction(PlanAction.REMOVE_AFTER);
        clusterNodePlanInfo.addContainerUPlan(containerPlan);
    }
    
    void buildUndeployController(ClusterNodePlanInfo clusterNodePlanInfo)
    {
        ContainerPlan containerPlan = new ContainerPlan(DeployContainerName.KUBE_CONTROLLER_MANAGER);
        containerPlan.setNextAction(PlanAction.REMOVE_AFTER);
        clusterNodePlanInfo.addContainerUPlan(containerPlan);
    }
    
    @Override
    public int buildNodeUnDeployPlan(ClusterInnerInfo clusterInnerInfo, ClusterNodePlanInfo clusterNodePlanInfo) throws Exception
    {
        JkeConfig jkeConfig = clusterInnerInfo.getJkeConfig();
        buildClusterCleanerUnDployPlan(jkeConfig, clusterNodePlanInfo);
        buildCleanJke(clusterNodePlanInfo);
        return 0;
    }
    
    AdmissionConfiguration getAdmissionConfiguration(KubeApiServiceInfo apiServiceInfo)
    {
        return null;
    }
    
    protected void assembleFileDeployer(JkeConfig jkeConfig, ClusterNodePlanInfo clusterNodePlanInfo, ContainerPlan containerPlan) throws JsonProcessingException
    {
        Map<String, String> fileMap = new HashMap<>();
        if (jkeConfig.getEncryptionConfig() != null
                && CollectionUtils.hasContent(jkeConfig.getEncryptionConfig().getEncryptionProviderFile()))
        {
            fileMap.put(EncryptionProviderConfigArgument, EncryptionProviderFilePath);
        }
        KubeApiServiceInfo apiService = jkeConfig.getServices().getKubeApiService();
        AuditLog auditLog = apiService.getAuditLog();
        if (auditLog != null && auditLog.getEnable() && auditLog.getConfiguration() != null)
        {
            String auditLogstr = yaml.dump(auditLog.getConfiguration().getPolicy());
            fileMap.put(AUDIT_PLICY_File_Prefix, auditLogstr);
        }
        
        if (jkeConfig.getCloudProvider() != null
                && CollectionUtils.hasContent(jkeConfig.getCloudProvider().getProvider()))
        {
            fileMap.put(CLOUDCOFIG, jkeConfig.getCloudProvider().getCloudConfigFile());
        }
    
        if (jkeConfig.getAuthentication() != null)
        {
            AuthWebhookConfig webhook = jkeConfig.getAuthentication().getWebhook();
            if (webhook != null)
            {
                if (CollectionUtils.hasContent(webhook.getConfigFile()))
                {
                    fileMap.put(AuthnWebhookFilePrefix, webhook.getConfigFile());
                }
            }
        }
        
        if (! CollectionUtils.isEmpty(apiService.getExtraArgs()))
        {
            if (CollectionUtils.hasContent(apiService.getExtraArgs().get(KubeAPIArgAdmissionControlConfigFile)))
            {
                if (apiService.getEventRateLimit() != null && apiService.getEventRateLimit().getEnabled())
                {
                    AdmissionConfiguration admissionConfiguration = getAdmissionConfiguration(apiService);
                    String yamlStr = ClsUtils.toYamlStr(admissionConfiguration);
                    fileMap.put(AdmissionFileNamePrefix, yamlStr);
                }
            }
        }
    
        
    
        List<String> envList = new ArrayList<>();
        if (! CollectionUtils.isEmpty(fileMap))
        {
            fileMap.forEach((k,v)->{
                String key=String.format("JOYFILE_%s",k);
                String env = String.format("%s=%s",EnvUtil.getEnvFromName(key),v);
                envList.add(env);
            });
        }
    
        if (! CollectionUtils.isEmpty(jkeConfig.getRegistries()))
        {
            try
            {
                String kubeletDockerConfig = getKubeletDockerConfig(jkeConfig.getRegistries());
                String dockerEnv = String.format("%s=%s", KubeletDockerConfigEnv, ClsUtils.getBase64(kubeletDockerConfig));
                envList.add(dockerEnv);
                
                String dockerFileEnv = String.format("%s=%s", KubeletDockerConfigFileEnv, KubeletDockerConfigPath);
                envList.add(dockerFileEnv);
            }
            catch (UnsupportedEncodingException e)
            {
                e.printStackTrace();
            }
        }
        
        DockerInstParams dockerInstParams = containerPlan.getDockerInstParams();
    
        dockerInstParams.getHostConfig().setPrivileged(false);
        dockerInstParams.getHostConfig().setNetworkMode(DockerNetworkMode.HOST);
        String image = clusterDataService.getImage(jkeConfig.getK8sVersion(), containerPlan.getContainerName());
        dockerInstParams.setImage(image);
        clusterNodePlanInfo.getContainerPlan().setAutoRun(true);
        dockerInstParams.setEntrypoint(new String[]{"file-deploy"});
        dockerInstParams.getHostConfig().setBinds(Arrays.asList(ETC_K8S_PATH_BIND, REGISTRY_PATH_BIND));
        dockerInstParams.setEnv(envList);
    }
    
    /**
     * @description build kube controller manager plan
     * @author Regulus
     * @date 11/30/21 8:43 PM
     * @param jkeConfig
     * @param certMap
     * @param clusterNodePlanInfo 
     * @return int
     */
    int buildKubeControllerManagerPlan(JkeConfig jkeConfig, Map<String, X509CertificateInfo> certMap, ClusterNodePlanInfo clusterNodePlanInfo) throws WebSystemException, NoSuchAlgorithmException
    {
        LOGGER.info("build kube-controller plan for {}", clusterNodePlanInfo.getClusterId());
        ContainerPlan containerPlan = new ContainerPlan(DeployContainerName.KUBE_CONTROLLER_MANAGER, jkeConfig.getRegistries());
        clusterNodePlanInfo.addContainerPlan(containerPlan);
        assembleKubeControllerPlan(jkeConfig, containerPlan);
        containerPlan.setCoreContainer(true);
        String sideKick = Utils.buildStr(DeployContainerName.JKE_SIDEKICK, "_", clusterNodePlanInfo.getClusterId().substring(0,4));
        containerPlan.getDockerInstParams().getHostConfig().setVolumesFrom(Arrays.asList(sideKick));
        containerPlan.setContainerName(Utils.buildStr(containerPlan.getContainerName(), "_", clusterNodePlanInfo.getClusterId().substring(0,4)));
        //containerPlan.setPlanActionList(getCommonActionListNoRemoveAf());
        return 0;
    }
    
    /**
     * @description assemble kube scheduler plan
     * @author Regulus
     * @date 11/30/21 8:47 PM
     * @param jkeConfig
     * @param containerPlan
     * @return int
     */
    int assembleKubeSchedulerPlan(JkeConfig jkeConfig, ContainerPlan containerPlan) throws WebSystemException, NoSuchAlgorithmException
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
        dockerInstParams.getHostConfig().setBinds(Arrays.asList(ETC_K8S_PATH_BIND));
        dockerInstParams.getHostConfig().setPrivileged(false);
        
        Map<String,String> commandArgs = new HashMap<>();
        
        
        commandArgs.put("kubeconfig",                        EnvUtil.getConfigPath(KeyCertName.KubeSchedulerCertName));
        
        if (jkeConfig.getDind())
        {
            commandArgs.put("address","0.0.0.0");
        }
        
        setCmdServiceOption(commandArgs, jkeConfig.getK8sVersion(), K8sServiceModule.KUBE_SCHEDULER);
    
        KubeSchedulerServiceInfo kubeSchedulerServiceInfo = jkeConfig.getServices().getKubeSchedulerServiceInfo();
    
        setCmdExtraArgs(commandArgs, kubeSchedulerServiceInfo.getExtraArgs());
        
        setExtraInfo(dockerInstParams, kubeSchedulerServiceInfo);
    
        setDockerInstParamsCmd(dockerInstParams, commandArgs, "kube-scheduler");
    
        return ErrorCode.SUCCESS.getCode();
    }
    
    /**
     * @description assemble kube scheduler
     * @author Regulus
     * @date 11/30/21 8:47 PM
     * @param jkeConfig
     * @param certMap
     * @param clusterNodePlanInfo 
     * @return int
     */
    int buildKubeSchedulerPlan(JkeConfig jkeConfig, Map<String, X509CertificateInfo> certMap, ClusterNodePlanInfo clusterNodePlanInfo) throws WebSystemException, NoSuchAlgorithmException
    {
        LOGGER.info("build kube-scheduler plan for {}", clusterNodePlanInfo.getClusterId());
        ContainerPlan containerPlan = new ContainerPlan(DeployContainerName.KUBE_SCHEDULER, jkeConfig.getRegistries());
        clusterNodePlanInfo.addContainerPlan(containerPlan);
        assembleKubeSchedulerPlan(jkeConfig, containerPlan);
        containerPlan.setCoreContainer(true);
        String sideKick = Utils.buildStr(DeployContainerName.JKE_SIDEKICK, "_", clusterNodePlanInfo.getClusterId().substring(0,4));
        containerPlan.getDockerInstParams().getHostConfig().setVolumesFrom(Arrays.asList(sideKick));
    
        containerPlan.setContainerName(Utils.buildStr(containerPlan.getContainerName(), "_", clusterNodePlanInfo.getClusterId().substring(0,4)));
        return 0;
    }
    
    /**
     * @description get etcd conn string
     * @author Regulus
     * @date 11/30/21 8:49 PM
     * @param clusterNodeInfo
     * @param clusterNodePlans
     * @return java.lang.String
     */
    String getEtcdConnString(ClusterNodeInfo clusterNodeInfo, Map<String, List<ClusterNodePlanInfo>>  clusterNodePlans)
    {
        List<String> addressList = new ArrayList<>();
    
        for (Map.Entry<String, List<ClusterNodePlanInfo>> entry : clusterNodePlans.entrySet())
        {
            if (entry.getKey().contains(K8sRole.ETCD))
            {
                for (ClusterNodePlanInfo clusterNodePlanInfo : entry.getValue())
                {
                    String access = null;
                    if (clusterNodePlanInfo.getClusterNodeInfo().getNodeId().equals(clusterNodeInfo.getNodeId()))
                    {
                        access = String.format("https://%s:2379", clusterNodeInfo.getInternalAddress());
                    }
                    else
                    {
                        access = String.format("https://%s:2379", clusterNodePlanInfo.getClusterNodeInfo().getInternalAddress());
                    }
                    addressList.add(access);
                }
            }
        }
        
        return StringUtils.join(addressList, ",");
    }
    
    /**
     * @description assemble kube api server plan
     * @author Regulus
     * @date 11/30/21 8:50 PM
     * @param jkeConfig
     * @param containerPlan
     * @param clusterNodeInfo
     * @param clusterNodePlans
     * @return int
     */
    int assembleKubeApiServerPlan(JkeConfig jkeConfig, ContainerPlan containerPlan, ClusterNodeInfo clusterNodeInfo, Map<String, List<ClusterNodePlanInfo>>  clusterNodePlans) throws WebSystemException, NoSuchAlgorithmException
    {
        String etcdConnectionStr = getEtcdConnString(clusterNodeInfo, clusterNodePlans);
        String etcdPathPrefix = EtcdPathPrefix;
        String etcdClientCert = EnvUtil.getCertPath(KeyCertName.KubeNodeCertName);
        String etcdClientKey  = EnvUtil.getKeyPath(KeyCertName.KubeNodeCertName);
        String etcdCAClientCert = EnvUtil.getCertPath(KeyCertName.CACertName);
    
        EtcdServiceInfo etcdServiceInfo = jkeConfig.getServices().getEtcdServiceInfo();
        KubeApiServiceInfo kubeApiServiceInfo = jkeConfig.getServices().getKubeApiService();
        if (etcdServiceInfo != null)
        {
            ExternalEtcdConfig externalEtcdConfig = etcdServiceInfo.getExternalConfig();
            if (externalEtcdConfig != null)
            {
                if (! CollectionUtils.isEmpty(externalEtcdConfig.getUrls()))
                {
                    etcdConnectionStr = StringUtils.join(externalEtcdConfig.getUrls(), ",");
                    etcdClientCert = EnvUtil.getCertPath(KeyCertName.EtcdClientCertName);
                    etcdClientKey  = EnvUtil.getKeyPath(KeyCertName.EtcdClientCertName);
                    etcdCAClientCert = EnvUtil.getCertPath(KeyCertName.EtcdClientCACertName);
                }
            }
        }
    
        DockerInstParams dockerInstParams = containerPlan.getDockerInstParams();
        dockerInstParams.setEntrypoint(new String[] {DefaultToolsEntrypoint});
        dockerInstParams.setImage(clusterDataService.getImage(jkeConfig.getK8sVersion(), DeployContainerName.KUBE_APISERVER));
        RestartPolicy restartPolicy = new RestartPolicy();
        restartPolicy.setName(DockerRestartPolicy.ALWAYS);
        dockerInstParams.getHostConfig().setRestartPolicy(restartPolicy);
        Map<String,String> labels = new HashMap<>();
        labels.put(ContainerNameLabel, containerPlan.getContainerName());
        dockerInstParams.setLabels(labels);
        dockerInstParams.getHostConfig().setNetworkMode(DockerNetworkMode.HOST);
        dockerInstParams.getHostConfig().setPrivileged(false);
        
        Map<String,String> commandArgs = new HashMap<>();
    
        commandArgs.put("client-ca-file",               EnvUtil.getCertPath(KeyCertName.CACertName));
        if (jkeConfig.getCloudProvider() != null)
        {
            commandArgs.put("cloud-provider",               jkeConfig.getCloudProvider().getProvider());
            if (! StringUtils.isEmpty(jkeConfig.getCloudProvider().getProvider()))
            {
                commandArgs.put("cloud-config", CloudConfigFileName);
                String env = String.format("%s=%s", CloudConfigSumEnv, ClsUtils.getStringChecksum(jkeConfig.getCloudProvider().getCloudConfigFile()));
                kubeApiServiceInfo.getExtraEnvs().add(env);
            }
        }
        commandArgs.put("etcd-cafile",                  etcdCAClientCert);
        commandArgs.put("etcd-certfile",                etcdClientCert);
        commandArgs.put("etcd-keyfile",                 etcdClientKey);
        commandArgs.put("etcd-prefix",                  etcdPathPrefix);
        commandArgs.put("etcd-servers",                 etcdConnectionStr);
        commandArgs.put("kubelet-client-certificate",   EnvUtil.getCertPath(KeyCertName.KubeAPICertName));
        commandArgs.put("kubelet-client-key",           EnvUtil.getKeyPath(KeyCertName.KubeAPICertName));
        commandArgs.put("proxy-client-cert-file",       EnvUtil.getCertPath(KeyCertName.APIProxyClientCertName));
        commandArgs.put("proxy-client-key-file",        EnvUtil.getKeyPath(KeyCertName.APIProxyClientCertName));
        commandArgs.put("requestheader-allowed-names",  KeyCertName.APIProxyClientCertName);
        commandArgs.put("requestheader-client-ca-file", EnvUtil.getCertPath(KeyCertName.RequestHeaderCACertName));
        commandArgs.put("service-account-key-file",     EnvUtil.getKeyPath(KeyCertName.ServiceAccountTokenKeyName));
        commandArgs.put("service-cluster-ip-range",     kubeApiServiceInfo.getServiceClusterIpRange());
        commandArgs.put("service-node-port-range",      kubeApiServiceInfo.getServiceNodePortRange());
        commandArgs.put("tls-cert-file",                EnvUtil.getCertPath(KeyCertName.KubeAPICertName));
        commandArgs.put("tls-private-key-file",         EnvUtil.getKeyPath(KeyCertName.KubeAPICertName));
        
        
        
        if (jkeConfig.getAuthentication() != null && jkeConfig.getAuthentication().getWebhook() != null)
        {
            commandArgs.put("authentication-token-webhook-config-file", AuthnWebhookFileName);
            commandArgs.put("authentication-token-webhook-cache-ttl", jkeConfig.getAuthentication().getWebhook().getCacheTimeout());
        }
    
        if (jkeConfig.getEncryptionConfig() != null &&
            ! StringUtils.isEmpty(jkeConfig.getEncryptionConfig().getEncryptionProviderFile()))
        {
            commandArgs.put(EncryptionProviderConfigArgument, EncryptionProviderFilePath);
        }
        
        if (jkeConfig.getServices().getKubeletServiceInfo().getGenerateServingCertificate() == true)
        {
            commandArgs.put("kubelet-certificate-authority", EnvUtil.getCertPath(KeyCertName.CACertName));
        }
    
        setCmdServiceOption(commandArgs, jkeConfig.getK8sVersion(), K8sServiceModule.KUBE_APISERVER);
        
        if (! StringUtils.isEmpty(clusterNodeInfo.getInternalAddress()) && ClsUtils.isIpAdress(clusterNodeInfo.getInternalAddress()))
        {
            commandArgs.put("advertise-address", clusterNodeInfo.getInternalAddress());
        }
        
        if (jkeConfig.getAuthorization().getMode().equals(RBACAuthorizationMode))
        {
            commandArgs.put("authorization-mode", "Node,RBAC");
        }
        
        String admissionControlOptionName = "";
        String admissionsValue = "";
        for (String admission : admissionControlOptionNames)
        {
            admissionsValue = commandArgs.get(admission);
            if (! StringUtils.isEmpty(admissionsValue))
            {
                admissionControlOptionName = admission;
                break;
            }
        }
    
        if (! StringUtils.isEmpty(admissionControlOptionName))
        {
            String extraMissions = "";
            if (kubeApiServiceInfo.getPodSecurityPolicy())
            {
                commandArgs.put("runtime-config", "policy/v1beta1/podsecuritypolicy=true");
        
                extraMissions = Utils.buildStr(extraMissions, ",PodSecurityPolicy");
            }
    
            if (kubeApiServiceInfo.getAlwaysPullImages())
            {
                extraMissions = Utils.buildStr(extraMissions, ",AlwaysPullImages");
            }
    
            if (kubeApiServiceInfo.getEventRateLimit() != null  && kubeApiServiceInfo.getEventRateLimit().getEnabled())
            {
                commandArgs.put(KubeAPIArgAdmissionControlConfigFile, DefaultKubeAPIArgAdmissionControlConfigFileValue);
                extraMissions = Utils.buildStr(extraMissions, ",EventRateLimit");
            }
    
            admissionsValue = Utils.buildStr(admissionsValue, extraMissions);
            commandArgs.put(admissionControlOptionName, admissionsValue);
        }
    
        dockerInstParams.getHostConfig().setBinds(Lists.newArrayList(ETC_K8S_PATH_BIND));
        if (kubeApiServiceInfo.getAuditLog() != null && kubeApiServiceInfo.getAuditLog().getEnable())
        {
            AuditLogConfig alc = kubeApiServiceInfo.getAuditLog().getConfiguration();
            commandArgs.put(KubeAPIArgAuditLogPath, alc.getPath());
            commandArgs.put(KubeAPIArgAuditLogMaxAge, alc.getPath());
            commandArgs.put(KubeAPIArgAuditLogMaxBackup, Integer.toString(alc.getMaxBackup()));
            commandArgs.put(KubeAPIArgAuditLogMaxSize, Integer.toString(alc.getMaxSize()));
            commandArgs.put(KubeAPIArgAuditLogFormat, alc.getFormat());
            commandArgs.put(KubeAPIArgAuditPolicyFile, DefaultKubeAPIArgAuditPolicyFileValue);
            commandArgs.put(AuditLogConfigSumEnv, ClsUtils.getMD5(JsonUtils.toJson(alc.getPolicy())));
            
            dockerInstParams.getHostConfig().getBinds().add("/var/log/kube-audit:/var/log/kube-audit:z");
        }
    
        setCmdExtraArgs(commandArgs, kubeApiServiceInfo.getExtraArgs());
        
        if (! CollectionUtils.isEmpty(kubeApiServiceInfo.getExtraArgs()))
        {
            dockerInstParams.getHostConfig().getBinds().addAll(kubeApiServiceInfo.getExtraBinds());
        }
        
        setExtraInfo(dockerInstParams, kubeApiServiceInfo);
        
        setDockerInstParamsCmd(dockerInstParams, commandArgs, "kube-apiserver");
    
        return ErrorCode.SUCCESS.getCode();
    }
    
    /**
     * @description assemble kube controller plan
     * @author Regulus
     * @date 11/30/21 8:53 PM
     * @param jkeConfig
     * @param containerPlan
     * @return int
     */
    int assembleKubeControllerPlan(JkeConfig jkeConfig, ContainerPlan containerPlan) throws WebSystemException, NoSuchAlgorithmException
    {
        DockerInstParams dockerInstParams = containerPlan.getDockerInstParams();
        dockerInstParams.setEntrypoint(new String[]{DefaultToolsEntrypoint});
        dockerInstParams.setImage(clusterDataService.getImage(jkeConfig.getK8sVersion(), containerPlan.getContainerName()));
        RestartPolicy restartPolicy = new RestartPolicy();
        restartPolicy.setName(DockerRestartPolicy.ALWAYS);
        dockerInstParams.getHostConfig().setPrivileged(false);
        dockerInstParams.getHostConfig().setRestartPolicy(restartPolicy);
        Map<String, String> labels = new HashMap<>();
        labels.put(ContainerNameLabel, containerPlan.getContainerName());
        dockerInstParams.setLabels(labels);
        dockerInstParams.getHostConfig().setNetworkMode(DockerNetworkMode.HOST);
        dockerInstParams.getHostConfig().setBinds(Arrays.asList(ETC_K8S_PATH_BIND));
    
        Map<String,String> commandArgs = new HashMap<>();
        
        KubeControllerServiceInfo kubeControllerServiceInfo = jkeConfig.getServices().getKubeControllerService();
    
        if (jkeConfig.getCloudProvider() != null)
        {
            commandArgs.put("cloud-provider",                    jkeConfig.getCloudProvider().getProvider());
            if (StringUtils.isEmpty(jkeConfig.getCloudProvider().getProvider()))
            {
                commandArgs.put("cloud-config", CloudConfigFileName);
                String env = String.format("%s=%s", CloudConfigSumEnv, ClsUtils.getStringChecksum(jkeConfig.getCloudProvider().getCloudConfigFile()));
                kubeControllerServiceInfo.getExtraEnvs().add(env);
            }
        }
        commandArgs.put( "cluster-cidr",                     kubeControllerServiceInfo.getClusterCidr());
        commandArgs.put("kubeconfig",                        EnvUtil.getConfigPath(KeyCertName.KubeControllerCertName));
        commandArgs.put( "root-ca-file",                     EnvUtil.getCertPath(KeyCertName.CACertName));
        commandArgs.put( "service-account-private-key-file", EnvUtil.getKeyPath(KeyCertName.ServiceAccountTokenKeyName));
        commandArgs.put( "service-cluster-ip-range",         kubeControllerServiceInfo.getServiceClusterIpRange());
        
        if (jkeConfig.getDind())
        {
            commandArgs.put("address","0.0.0.0");
        }
        
        
        setCmdServiceOption(commandArgs, jkeConfig.getK8sVersion(), K8sServiceModule.KUBE_CONTROLLER);
        
        if (jkeConfig.getAuthorization().getMode().equals(RBACAuthorizationMode))
        {
            commandArgs.put("use-service-account-credentials", "true");
        }
    
        setCmdExtraArgs(commandArgs, kubeControllerServiceInfo.getExtraArgs());
        setExtraInfo(dockerInstParams, kubeControllerServiceInfo);
        setDockerInstParamsCmd(dockerInstParams, commandArgs, "kube-controller-manager");
        return ErrorCode.SUCCESS.getCode();
    }
    
    /**
     * @description build kubeapiserver plan
     * @author Regulus
     * @date 11/30/21 8:53 PM
     * @param jkeConfig
     * @param clusterNodePlanInfo
     * @param clusterNodePlans
     * @return int
     */
    int buildKubeApiserverPlan(JkeConfig jkeConfig, ClusterNodePlanInfo clusterNodePlanInfo, Map<String, List<ClusterNodePlanInfo>>  clusterNodePlans) throws WebSystemException, NoSuchAlgorithmException
    {
        LOGGER.info("build kube-apiserver plan for {}", clusterNodePlanInfo.getClusterId());
        ContainerPlan containerPlan = new ContainerPlan(DeployContainerName.KUBE_APISERVER, jkeConfig.getRegistries());
        clusterNodePlanInfo.addContainerPlan(containerPlan);
        assembleKubeApiServerPlan(jkeConfig, containerPlan, clusterNodePlanInfo.getClusterNodeInfo(), clusterNodePlans);
    
        String sideKick = Utils.buildStr(DeployContainerName.JKE_SIDEKICK, "_", clusterNodePlanInfo.getClusterId().substring(0,4));
        containerPlan.getDockerInstParams().getHostConfig().setVolumesFrom(Arrays.asList(sideKick));
        containerPlan.setCoreContainer(true);
        containerPlan.setContainerName(Utils.buildStr(containerPlan.getContainerName(), "_", clusterNodePlanInfo.getClusterId().substring(0,4)));
        return 0;
    }
    
    /**
     * @description build loglinker plan
     * @author Regulus
     * @date 11/30/21 8:54 PM
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
        cmds.add(DeployContainerName.KUBE_APISERVER);
        cmds.add(DeployContainerName.KUBE_CONTROLLER_MANAGER);
        cmds.add(DeployContainerName.KUBE_SCHEDULER);
        cmds.add(DeployContainerName.KUBELET);
        cmds.add(DeployContainerName.KUBE_PROXY);
        assembleLogLinkerParams(jkeConfig, containerPlan, cmds);
        containerPlan.setContainerName(Utils.buildStr(containerPlan.getContainerName(), "_", clusterNodePlanInfo.getClusterId().substring(0,4)));
        return 0;
    }
    
    /**
     * @description build health check api server plan
     * @author Regulus
     * @date 11/30/21 8:55 PM
     * @param jkeConfig
     * @param clusterNodePlanInfo
     * @return int
     */
    int buildHealthCheckApiServer(JkeConfig jkeConfig, ClusterNodePlanInfo clusterNodePlanInfo) throws WebSystemException
    {
        LOGGER.info("build kube-apiserver health checker plan for {}", clusterNodePlanInfo.getClusterId());
        ContainerPlan containerPlan = new ContainerPlan(DeployContainerName.JKE_HEALTH_CHECKER, jkeConfig.getRegistries());
        clusterNodePlanInfo.addContainerPlan(containerPlan);
        String certPath = EnvUtil.getCertPath(KeyCertName.KubeAPICertName);
        String keyPath = EnvUtil.getKeyPath(KeyCertName.KubeAPICertName);
        String healthCheckUrl = ClsUtils.getHealthCheckURL(true, KubeApiPort);
        assembleHttpsHealthCheckParams(jkeConfig, containerPlan, certPath, keyPath, healthCheckUrl);
        containerPlan.setLogSucess("ok");
        String containerName = String.format("%s_%d_%s", containerPlan.getContainerName(), KubeApiPort, clusterNodePlanInfo.getClusterId().substring(0,4));
        containerPlan.setContainerName(containerName);
        return 0;
    }
    
    /**
     * @description build health check plan for kuebe controller
     * @author Regulus
     * @date 11/30/21 8:56 PM
     * @param jkeConfig
     * @param clusterNodePlanInfo
     * @return int
     */
    int buildHealthCheckController(JkeConfig jkeConfig, ClusterNodePlanInfo clusterNodePlanInfo) throws WebSystemException
    {
        LOGGER.info("build kube-controller health checker plan for {}", clusterNodePlanInfo.getClusterId());
        ContainerPlan containerPlan = new ContainerPlan(DeployContainerName.JKE_HEALTH_CHECKER, jkeConfig.getRegistries());
        clusterNodePlanInfo.addContainerPlan(containerPlan);
        if (ClsUtils.versionCompare(jkeConfig.getK8sVersion(), MaxK8s121Version) > 0)
        {
            String certPath = EnvUtil.getCertPath(KeyCertName.KubeControllerCertName);
            String keyPath = EnvUtil.getKeyPath(KeyCertName.KubeControllerCertName);
            String healthCheckUrl = ClsUtils.getHealthCheckURL(true, KubeControlPortSsl);
            assembleHttpsHealthCheckParams(jkeConfig, containerPlan, certPath, keyPath, healthCheckUrl);
        }
        else
        {
            String healthCheckUrl = ClsUtils.getHealthCheckURL(false, KubeControlPort);
            assembleHttpHealthCheckParams(jkeConfig, containerPlan, healthCheckUrl);
        }
    
        containerPlan.setLogSucess("ok");
        containerPlan.setContainerName(Utils.buildStr(containerPlan.getContainerName(), "_", clusterNodePlanInfo.getClusterId().substring(0,4)));
        return 0;
    }
    
    /**
     * @description build health check for kube scheduler
     * @author Regulus
     * @date 11/30/21 9:00 PM
     * @param jkeConfig
     * @param clusterNodePlanInfo
     * @return int
     */
    int buildHealthCheckScheduler(JkeConfig jkeConfig, ClusterNodePlanInfo clusterNodePlanInfo) throws WebSystemException
    {
        LOGGER.info("build kube-scheduler health checker plan for {}", clusterNodePlanInfo.getClusterId());
        
        buildHealthCheck(jkeConfig, clusterNodePlanInfo, KubeSchedulerPort);
        return 0;
    }
}
