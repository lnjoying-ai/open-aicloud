package com.lnjoying.justice.cluster.manager.service.cluster;

import com.lnjoying.justice.cluster.manager.common.DnsProvider;
import com.lnjoying.justice.cluster.manager.common.K8sDefaultValue;
import com.lnjoying.justice.cluster.manager.common.NetworkProvider;
import com.lnjoying.justice.cluster.manager.domain.dto.model.cluster.*;
import com.lnjoying.justice.cluster.manager.domain.dto.model.secrect.EncryptionKey;
import com.lnjoying.justice.cluster.manager.domain.model.ClusterInnerInfo;
import com.lnjoying.justice.cluster.manager.domain.model.NetworkOption;
import com.lnjoying.justice.cluster.manager.service.data.ClusterDataService;
import com.lnjoying.justice.cluster.manager.util.ClsUtils;
import com.micro.core.common.Utils;
import freemarker.template.TemplateException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.security.SecureRandom;


/**
 * @Author: Regulus
 * @Date: 12/14/21 4:13 PM
 * @Description:
 */
@Service
public class K8sInitService
{
    private  final String DefaultAuthnCacheTimeout = "5s";
    @Autowired
    private ClusterDataService clusterDataService;
    
    public void initNetworkOption(ClusterInnerInfo clusterInnerInfo)
    {
        NetworkOption networkOption = clusterInnerInfo.getNetworkOption();
        JkeConfig jkeConfig = clusterInnerInfo.getJkeConfig();
        networkOption.setClusterCidr(jkeConfig.getServices().getKubeControllerService().getClusterCidr());
        networkOption.setClusterDnsServer(jkeConfig.getServices().getKubeletServiceInfo().getClusterDnsServer());
        networkOption.setClusterDomain(jkeConfig.getServices().getKubeletServiceInfo().getClusterDomain());
    }
    
    public void setDnsDefaults(ClusterInnerInfo clusterInnerInfo)
    {
        JkeConfig jkeConfig = clusterInnerInfo.getJkeConfig();
        DNSAddonInfo dnsAddonInfo = jkeConfig.getSystemAddons().getDns();
        if (dnsAddonInfo != null && StringUtils.isEmpty(dnsAddonInfo.getProvider()))
        {
            return;
        }
        
        if (null == dnsAddonInfo)
        {
            dnsAddonInfo = new DNSAddonInfo();
            jkeConfig.getSystemAddons().setDns(dnsAddonInfo);
        }
        String provider = DnsProvider.CoreDNSProvider;
        if (ClsUtils.versionCompare(jkeConfig.getK8sVersion(), K8sDefaultValue.K8sVersionCoreDNS) < 0)
        {
            provider = DnsProvider.KubeDNSProvider;
        }
        
        dnsAddonInfo.setProvider(provider);
    }
    
    /*
     * @Description: set network default params
     * @Author: Regulus
     * @Date: 1/6/22 9:02 PM
     * @Param: clusterInnerInfo
     * @Return: void
     */
    public void setNetworkDefault(ClusterInnerInfo clusterInnerInfo)
    {
        JkeConfig jkeConfig = clusterInnerInfo.getJkeConfig();
        NetworkAddonInfo networkAddonInfo = jkeConfig.getSystemAddons().getNetwork();
        if (null == networkAddonInfo)
        {
            networkAddonInfo = new NetworkAddonInfo();
            networkAddonInfo.setProvider(NetworkProvider.CalicoNetworkPlugin);
            jkeConfig.getSystemAddons().setNetwork(networkAddonInfo);
        }
        
        Map<String, String> networkOptions = new HashMap<>();
        if (null == networkOptions)
        {
            networkOptions = new HashMap<>();
        }
        
        switch (networkAddonInfo.getProvider())
        {
            case NetworkProvider.CalicoNetworkPlugin:
                networkOptions.put("calico_cloud_provider",         K8sDefaultValue.DefaultNetworkCloudProvider);
                networkOptions.put("calico_flex_volume_plugin_dir", K8sDefaultValue.DefaultCalicoFlexVolPluginDirectory);
                break;
            case NetworkProvider.FlannelNetworkPlugin:
                networkOptions.put("flannel_backend_type",         K8sDefaultValue.DefaultNetworkCloudProvider);
                networkOptions.put("flannel_backend_port",         K8sDefaultValue.DefaultFlannelBackendVxLanPort);
                networkOptions.put("flannel_backend_vni",          K8sDefaultValue.DefaultFlannelBackendVxLanPort);
                break;
            case NetworkProvider.CanalNetworkPlugin:
                networkOptions.put("canal_flannel_backend_type",   K8sDefaultValue.DefaultFlannelBackendVxLan);
                networkOptions.put("canal_flannel_backend_port",   K8sDefaultValue.DefaultFlannelBackendVxLanPort);
                networkOptions.put("canal_flannel_backend_vni",    K8sDefaultValue.DefaultFlannelBackendVxLanPort);
                networkOptions.put("canal_flex_volume_plugin_dir", K8sDefaultValue.DefaultCanalFlexVolPluginDirectory);
            default:
                break;
        }
        
        if (networkAddonInfo.getCalicoNetworkProvider() != null)
        {
            CalicoNetworkProvider calicoNetworkProvider = networkAddonInfo.getCalicoNetworkProvider();
            if (StringUtils.isEmpty(calicoNetworkProvider.getCloudProvider()))
            {
                calicoNetworkProvider.setCloudProvider(K8sDefaultValue.DefaultNetworkCloudProvider);
            }
        }
        
        if (networkAddonInfo.getFlannelNetworkProvider() != null)
        {
            if (StringUtils.isNotBlank(networkAddonInfo.getFlannelNetworkProvider().getIface()))
            {
                networkOptions.put("flannel_iface", networkAddonInfo.getFlannelNetworkProvider().getIface());
            }
        }
        
        if (networkAddonInfo.getCanalNetworkProvider() != null)
        {
            if (StringUtils.isNotBlank(networkAddonInfo.getCanalNetworkProvider().getIface()))
            {
                networkOptions.put("canal_iface", networkAddonInfo.getCanalNetworkProvider().getIface());
            }
        }
        
        if (networkAddonInfo.getWeaveNetworkProvider() != null)
        {
            if (StringUtils.isNotBlank(networkAddonInfo.getWeaveNetworkProvider().getPassword()))
            {
                networkOptions.put("WeavePassword", networkAddonInfo.getWeaveNetworkProvider().getPassword());
            }
        }
        
        if (networkAddonInfo.getOptions() == null)
        {
            networkAddonInfo.setOptions(networkOptions);
        }
        else
        {
            for (Map.Entry<String, String> entry : networkOptions.entrySet())
            {
                String vtemp = networkAddonInfo.getOptions().get(entry.getKey());
                if (StringUtils.isEmpty(vtemp))
                {
                    networkAddonInfo.getOptions().put(entry.getKey(), entry.getValue());
                }
            }
        }
    }
    
    String DefaultEtcdElectionTimeoutName    = "election-timeout";
    String DefaultEtcdHeartbeatIntervalName  = "heartbeat-interval";
    String KubeAPIArgAdmissionControlConfigFile             = "admission-control-config-file";
    String AuthnWebhookProvider   = "webhook";
    String RollingUpdateDaemonSetStrategyType = "RollingUpdate";
    String OnDeleteDaemonSetStrategyType = "OnDelete";
    String RollingUpdateDeploymentStrategyType = "RollingUpdate";
    String RecreateDeploymentStrategyType = "Recreate";
    
    Configuration newDefaultEventRateLimitConfig()
    {
        Configuration configuration = new Configuration();
        TypeMeta typeMeta = TypeMeta.builder().apiVersion("eventratelimit.admission.k8s.io/v1alpha1").kind("Configuration").build();
        Limit limit = Limit.builder().burst(20000).qps(500).type(K8sDefaultValue.ServerLimitType).build();
        configuration.setTypeMeta(typeMeta);
        configuration.setLimits(Arrays.asList(limit));
        return configuration;
    }
    
    boolean checkVersionNeedAuditLog(String version)
    {
        if (ClsUtils.versionCompare(version, "v1.15.11") >= 0 && ClsUtils.versionCompare(version, "v1.15.99") < 0)
        {
            return true;
        }
        
        if (ClsUtils.versionCompare(version, "v1.16.8") >= 0 && ClsUtils.versionCompare(version, "v1.16.99") < 0)
        {
            return true;
        }
        
        if (ClsUtils.versionCompare(version, "v1.17.4") >= 0)
        {
            return true;
        }
        return false;
    }
    
    /**
     * @Description:
     * @Author: Regulus
     * @Date: 1/6/22 9:03 PM
     * @Param: 
     * @Return: com.lnjoying.justice.cluster.manager.domain.dto.model.cluster.AuditPolicy
     */
    AuditPolicy newDefaultAuditPolicy()
    {
        String apiVersion = GroupVersion.builder().group("audit.k8s.io").version("v1").build().toString();
        TypeMeta typeMeta = TypeMeta.builder().kind("Policy").apiVersion(apiVersion).build();
        AuditPolicyRule rule = AuditPolicyRule.builder().level("Metadata").build();
        AuditPolicy auditPolicy = AuditPolicy.builder().typeMeta(typeMeta).rules(Arrays.asList(rule)).build();
        return auditPolicy;
    }
    
    AuditLogConfig newDefaultAuditLogConfig()
    {
        return AuditLogConfig.builder().maxAge(30).maxBackup(10).maxSize(100).policy(newDefaultAuditPolicy())
                .path(K8sDefaultValue.DefaultKubeAPIArgAuditLogPathValue).format("json").build();
    }
    
    
    void initExtraParams(BaseService svc)
    {
        if (null == svc.getExtraArgs())
        {
            svc.setExtraArgs(new HashMap<>());
        }
        
        if (null == svc.getExtraEnvs())
        {
            svc.setExtraEnvs(new ArrayList<>());
        }
        
        if (null == svc.getExtraBinds())
        {
            svc.setExtraBinds(new ArrayList<>());
        }
    }
    
    Object  getServiceObj(Object e, Class clazz) throws ClassNotFoundException, IllegalAccessException, InstantiationException
    {
        if (e == null)
        {
            return clazz.newInstance();
        }
        return e;
    }
    
    void initServiceExtraParmas(K8sServiceInfo k8sServiceInfo)
    {
        try
        {
            KubeletServiceInfo kubelet = (KubeletServiceInfo) getServiceObj(k8sServiceInfo.getKubeletServiceInfo(), KubeletServiceInfo.class);
            KubeApiServiceInfo api = (KubeApiServiceInfo) getServiceObj(k8sServiceInfo.getKubeApiService(), KubeApiServiceInfo.class);
            KubeControllerServiceInfo controller = (KubeControllerServiceInfo) getServiceObj(k8sServiceInfo.getKubeControllerService(), KubeControllerServiceInfo.class);
            EtcdServiceInfo etcd = (EtcdServiceInfo) getServiceObj(k8sServiceInfo.getEtcdServiceInfo(), EtcdServiceInfo.class);
            initExtraParams(kubelet);
            initExtraParams(api);
            initExtraParams(controller);
            initExtraParams(etcd);
            k8sServiceInfo.setKubeletServiceInfo(kubelet);
            k8sServiceInfo.setKubeApiService(api);
            k8sServiceInfo.setEtcdServiceInfo(etcd);
            k8sServiceInfo.setKubeControllerService(controller);
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
        catch (InstantiationException e)
        {
            e.printStackTrace();
        }
    }
    
    public void setClusterServiceDefault(ClusterInnerInfo clusterInnerInfo)
    {
        K8sServiceInfo k8sServiceInfo = clusterInnerInfo.getJkeConfig().getServices();
        if (null == k8sServiceInfo)
        {
            k8sServiceInfo =  new K8sServiceInfo();
        }
    
        initServiceExtraParmas(k8sServiceInfo);
        
        KubeApiServiceInfo api = k8sServiceInfo.getKubeApiService();
        EtcdServiceInfo etcd = k8sServiceInfo.getEtcdServiceInfo();
        
        
        if (StringUtils.isEmpty(etcd.getExtraArgs().get(DefaultEtcdElectionTimeoutName)))
        {
            etcd.getExtraArgs().put(DefaultEtcdElectionTimeoutName, K8sDefaultValue.DefaultEtcdElectionTimeoutValue);
        }
        
        if (StringUtils.isEmpty(etcd.getExtraArgs().get(DefaultEtcdHeartbeatIntervalName)))
        {
            etcd.getExtraArgs().put(DefaultEtcdHeartbeatIntervalName, K8sDefaultValue.DefaultEtcdHeartbeatIntervalValue);
        }
        
        if (StringUtils.isEmpty(api.getExtraArgs().get(KubeAPIArgAdmissionControlConfigFile)))
        {
            EventRateLimit eventRateLimit = api.getEventRateLimit();
            if (null != eventRateLimit)
            {
                if (eventRateLimit.getEnabled() && (eventRateLimit.getConfiguration() == null))
                {
                    eventRateLimit.setConfiguration(newDefaultEventRateLimitConfig());
                }
            }
        }
        
        if (checkVersionNeedAuditLog(clusterInnerInfo.getJkeConfig().getK8sVersion()))
        {
            if (api.getAuditLog() == null)
            {
                api.setAuditLog(AuditLog.builder().enable(true).build());
            }
        }
        
        if (api.getAuditLog() != null && api.getAuditLog().getEnable())
        {
            if (api.getAuditLog().getConfiguration() == null)
            {
                api.getAuditLog().setConfiguration(newDefaultAuditLogConfig());
            }
            else
            {
                if (api.getAuditLog().getConfiguration().getPolicy() == null)
                {
                    api.getAuditLog().getConfiguration().setPolicy(newDefaultAuditPolicy());
                }
            }
        }
    }
    
    public void setClusterAuthnDefaults(ClusterInnerInfo clusterInnerInfo)
    {
        JkeConfig jkeConfig = clusterInnerInfo.getJkeConfig();
        Map<String, Boolean> authnStrategies = clusterInnerInfo.getAuthnStrategies();
    
        for (String strategy : jkeConfig.getAuthentication().getStrategy().split("\\|"))
        {
            String temp = strategy.trim().toLowerCase();
            clusterInnerInfo.putAuthnStrategies(temp, true);
        }
        
        Authentication authentication = jkeConfig.getAuthentication();
    
        if (authnStrategies.get(AuthnWebhookProvider) != null && authentication.getWebhook() == null)
        {
            AuthWebhookConfig authWebhookConfig = new AuthWebhookConfig();
            authWebhookConfig.setCacheTimeout(DefaultAuthnCacheTimeout);
    
            try
            {
                String config = clusterDataService.getTemplate(K8sDefaultValue.DefaultAuthnWebhookFile, null);
                authWebhookConfig.setConfigFile(config);
                authentication.setWebhook(authWebhookConfig);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            catch (TemplateException e)
            {
                e.printStackTrace();
            }
          
        }
        
        if (authentication.getWebhook() != null)
        {
            if (StringUtils.isBlank(authentication.getWebhook().getCacheTimeout()))
            {
                authentication.getWebhook().setCacheTimeout(K8sDefaultValue.DefaultAuthnCacheTimeout);
            }
            
            if (StringUtils.isBlank(authentication.getWebhook().getConfigFile()))
            {
                try
                {
                    String config = clusterDataService.getTemplate(K8sDefaultValue.DefaultAuthnWebhookFile, null);
                    authentication.getWebhook().setConfigFile(config);
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
                catch (TemplateException e)
                {
                    e.printStackTrace();
                }
    
            }
        }
    }
    
    public DaemonSetUpdateStrategy setDaemonsetAddonDefaults(DaemonSetUpdateStrategy updateStrategy)
    {
        if (updateStrategy != null && ! updateStrategy.getStrategy().equals(RollingUpdateDaemonSetStrategyType))
        {
            return updateStrategy;
        }
        
        if (updateStrategy == null || updateStrategy.getRollingUpdate() == null
                || updateStrategy.getRollingUpdate().getMaxUnavailable() == null)
        {
            RollingUpdateDaemonSet rollingUpdateDaemonSet = RollingUpdateDaemonSet.builder().maxUnavailable(1).build();
            return DaemonSetUpdateStrategy.builder()
                    .strategy(RollingUpdateDaemonSetStrategyType).rollingUpdate(rollingUpdateDaemonSet).build();
        }
        return updateStrategy;
    }
    
    public DeploymentStrategy setDeploymentAddonDefaults(DeploymentStrategy updateStrategy)
    {
        if (updateStrategy != null && ! updateStrategy.getStrategy().equals(RollingUpdateDaemonSetStrategyType))
        {
            return updateStrategy;
        }
        
        if (updateStrategy == null || updateStrategy.getRollingUpdate() == null
                || updateStrategy.getRollingUpdate().getMaxUnavailable() == null)
        {
            RollingUpdateDeployment rollingUpdateSet = RollingUpdateDeployment.builder().maxUnavailable(1).build();
            return DeploymentStrategy.builder()
                    .strategy(RollingUpdateDeploymentStrategyType).rollingUpdate(rollingUpdateSet).build();
        }
        return updateStrategy;
    }
    
    
    public void setAddonDefault(ClusterInnerInfo clusterInnerInfo)
    {
        JkeConfig jkeConfig = clusterInnerInfo.getJkeConfig();
        IngressAddonInfo ingressAddonInfo = jkeConfig.getSystemAddons().getIngress();
        ingressAddonInfo.setUpdateStrategy(setDaemonsetAddonDefaults(ingressAddonInfo.getUpdateStrategy()));
        NetworkAddonInfo networkAddonInfo = jkeConfig.getSystemAddons().getNetwork();
        networkAddonInfo.setUpdateStrategy(setDaemonsetAddonDefaults(networkAddonInfo.getUpdateStrategy()));
        DNSAddonInfo dnsAddonInfo = jkeConfig.getSystemAddons().getDns();
        dnsAddonInfo.setUpdateStrategy(setDeploymentAddonDefaults(dnsAddonInfo.getUpdateStrategy()));
        
        if (dnsAddonInfo.getLinearAutoscalerParams() == null)
        {
            LinearAutoscalerParams linearAutoscalerParams = LinearAutoscalerParams.builder()
                    .coresPerReplica(128).nodesPerReplica(4).min(1).preventSinglePointFailure(true).build();
            dnsAddonInfo.setLinearAutoscalerParams(linearAutoscalerParams);
        }
        
        MonitorAddonInfo monitorAddonInfo = jkeConfig.getSystemAddons().getMonitor();
        monitorAddonInfo.setUpdateStrategy(setDeploymentAddonDefaults(monitorAddonInfo.getUpdateStrategy()));
        
        if (StringUtils.isBlank(ingressAddonInfo.getNetworkMode()))
        {
            String networkMode = K8sDefaultValue.DefaultNetworkMode;
            if (ClsUtils.versionCompare(jkeConfig.getK8sVersion(), "v1.21.0") > 0)
            {
                networkMode = K8sDefaultValue.DefaultNetworkModeV121;
            }
            ingressAddonInfo.setNetworkMode(networkMode);
        }
        
        if (ingressAddonInfo.getHttpPort() == 0)
        {
            ingressAddonInfo.setHttpPort(K8sDefaultValue.DefaultHTTPPort);
        }
        
        if (ingressAddonInfo.getHttpsPort() == 0)
        {
            ingressAddonInfo.setHttpPort(K8sDefaultValue.DefaultHTTPSPort);
        }
        
        if (ingressAddonInfo.getDefaultBackend() == null)
        {
            Boolean defaultBackend = true;
            if (ClsUtils.versionCompare(jkeConfig.getK8sVersion(), "v1.21.0") >= 0)
            {
                defaultBackend = false;
            }
            ingressAddonInfo.setDefaultBackend(defaultBackend);
        }
        
        if (ingressAddonInfo.getDefaultIngressClass() == null)
        {
            ingressAddonInfo.setDefaultIngressClass(true);
        }
    }
    
    public void setUpdateStrategy(ClusterInnerInfo clusterInnerInfo)
    {
        JkeConfig jkeConfig = clusterInnerInfo.getJkeConfig();
        NodeUpgradeStrategy upgradeStrategy = jkeConfig.getUpgradeStrategy();
        if (upgradeStrategy == null)
        {
            upgradeStrategy =  NodeUpgradeStrategy.builder()
                    .maxUnavailableWorker(K8sDefaultValue.DefaultMaxUnavailableWorker)
                    .maxUnavailableController(K8sDefaultValue.DefaultMaxUnavailableController).build();
            jkeConfig.setUpgradeStrategy(upgradeStrategy);
            return;
        }
        
        if (StringUtils.isBlank(upgradeStrategy.getMaxUnavailableController()))
        {
            upgradeStrategy.setMaxUnavailableController(K8sDefaultValue.DefaultMaxUnavailableController);
        }
        
        if (StringUtils.isBlank(upgradeStrategy.getMaxUnavailableWorker()))
        {
            upgradeStrategy.setMaxUnavailableWorker(K8sDefaultValue.DefaultMaxUnavailableWorker);
        }
        
        if (upgradeStrategy.getDrain() != null && upgradeStrategy.getDrain())
        {
            return;
        }
        
        if (upgradeStrategy.getNodeDrainInput() == null)
        {
            NodeDrainInput input = NodeDrainInput.builder().timeout(K8sDefaultValue.DefaultNodeDrainTimeout)
                    .gracePeriod(K8sDefaultValue.DefaultNodeDrainGracePeriod)
                    .ignoreDaemonsets(K8sDefaultValue.DefaultNodeDrainIgnoreDaemonsets).build();
            upgradeStrategy.setNodeDrainInput(input);
        }
    }

    Yaml yaml = new Yaml();
    
    String generateKeyName(String typeName)
    {
        return Utils.buildStr(typeName, Utils.getRandomStr(6));
    }
    
    private String readEncryptionCustomConfig(EncryptionConfiguration customConfig) throws IOException, TemplateException
    {
        String custom = yaml.dump(customConfig);
        Map<String,Object> configMap = new HashMap<>();
        configMap.put("CustomConfig", custom);
        return clusterDataService.getTemplate("CUSTOM_SECRET_CONFIG", configMap);
    }
    
    EncryptionKey generateEncryptionKey() throws UnsupportedEncodingException
    {
        EncryptionKey encryptionKey = new EncryptionKey();
        byte[] nonce = new byte[16];
        new SecureRandom().nextBytes(nonce);
        
        encryptionKey.setName(generateKeyName("key"));
        encryptionKey.setSecret(ClsUtils.getBase64(nonce));
        return encryptionKey;
        
    }
    
    final String MultiKeyEncryptionProviderFile = "";
    private String getEncryptionProviderFile() throws IOException, TemplateException
    {
        EncryptionKey encryptionKey = generateEncryptionKey();
        Map<String, Object> params = new HashMap<>();
        params.put("keyNames", encryptionKey);
        return clusterDataService.getTemplate(MultiKeyEncryptionProviderFile, params);
    }
    
    private void setEncryptionInfo(ClusterInnerInfo clusterInnerInfo) throws IOException, TemplateException
    {
        JkeConfig jkeConfig = clusterInnerInfo.getJkeConfig();
        KubeApiServiceInfo apiService = jkeConfig.getServices().getKubeApiService();
        if (apiService.getSecretsEncryptionConfig() != null)
        {
            if (apiService.getSecretsEncryptionConfig().getEnabled())
            {
                if (jkeConfig.getEncryptionConfig() == null)
                {
                    jkeConfig.setEncryptionConfig(new EncryptionConfig());
                }
                jkeConfig.getEncryptionConfig().setRewriteSecrets(true);
         
            
                if (apiService.getSecretsEncryptionConfig().getCustomConfig() != null)
                {
                    String custom = readEncryptionCustomConfig(apiService.getSecretsEncryptionConfig().getCustomConfig());
                    jkeConfig.getEncryptionConfig().setEncryptionProviderFile(custom);
                }
                else
                {
                    String encryptionFile = getEncryptionProviderFile();
                    jkeConfig.getEncryptionConfig().setEncryptionProviderFile(encryptionFile);
                }
            }
        }
    }
    
    public void setDefaults(ClusterInnerInfo clusterInnerInfo) throws IOException, TemplateException
    {
        JkeConfig jkeConfig = clusterInnerInfo.getJkeConfig();
        
        if (! jkeConfig.getAuthorization().getMode().equals(K8sDefaultValue.RBACAuthorizationMode))
        {
            jkeConfig.getServices().getKubeApiService().setPodSecurityPolicy(false);
        }
        
        if (StringUtils.isEmpty(jkeConfig.getSystemAddons().getIngress().getProvider()))
        {
            jkeConfig.getSystemAddons().getIngress().setProvider(K8sDefaultValue.NginxIngressController);
        }
        
        if (StringUtils.isEmpty(jkeConfig.getSystemAddons().getMonitor().getProvider()))
        {
            jkeConfig.getSystemAddons().getMonitor().setProvider(K8sDefaultValue.DefaultMonitoringProvider);
        }
        
        if (jkeConfig.getDeployJobTime() == 0)
        {
            jkeConfig.setDeployJobTime(K8sDefaultValue.DeployJobTime);
        }
        
        setDnsDefaults(clusterInnerInfo);
        setClusterServiceDefault(clusterInnerInfo);
        setNetworkDefault(clusterInnerInfo);
        setClusterAuthnDefaults(clusterInnerInfo);
        setUpdateStrategy(clusterInnerInfo);
        setAddonDefault(clusterInnerInfo);
        setEncryptionInfo(clusterInnerInfo);
    }
}
