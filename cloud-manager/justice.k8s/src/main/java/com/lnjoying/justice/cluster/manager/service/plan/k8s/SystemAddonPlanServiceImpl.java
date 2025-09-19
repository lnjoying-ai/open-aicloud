package com.lnjoying.justice.cluster.manager.service.plan.k8s;

import com.lnjoying.justice.cluster.manager.common.*;
import com.lnjoying.justice.cluster.manager.config.EnvCfg;
import com.lnjoying.justice.cluster.manager.db.model.TblClusterInfo;
import com.lnjoying.justice.cluster.manager.db.repo.ClusterRepo;
import com.lnjoying.justice.cluster.manager.domain.dto.model.cluster.*;
import com.lnjoying.justice.cluster.manager.domain.model.ClusterInnerInfo;
import com.lnjoying.justice.cluster.manager.domain.model.ClusterNodePlanInfo;
import com.lnjoying.justice.cluster.manager.domain.model.NetworkOption;
import com.lnjoying.justice.commonweb.util.CollectionUtils;
import com.lnjoying.justice.commonweb.util.JsonUtils;
import com.lnjoying.justice.schema.constant.WorkerType;
import com.lnjoying.justice.schema.entity.docker.DockerInstParams;
import com.lnjoying.justice.cluster.manager.service.data.ClusterDataService;
import com.lnjoying.justice.cluster.manager.service.plan.ContainerPlan;
import com.lnjoying.justice.cluster.manager.util.ClsUtils;
import com.lnjoying.justice.cluster.manager.util.EnvUtil;
import com.lnjoying.justice.commonweb.exception.WebSystemException;
import com.lnjoying.justice.schema.common.ErrorCode;
import com.lnjoying.justice.schema.common.ErrorLevel;
import com.micro.core.common.Utils;
import freemarker.template.TemplateException;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.util.*;

import static com.lnjoying.justice.cluster.manager.common.NetworkProvider.*;

/**
 * @Author: Regulus
 * @Date: 11/25/21 3:22 PM
 * @Description: system addon plan
 */
@Service
public class SystemAddonPlanServiceImpl extends AbstractK8sPlanService
{
    private static Logger LOGGER = LogManager.getLogger();
    
    @Autowired
    private ClusterDataService clusterDataService;

    @Autowired
    private ClusterRepo clusterRepo;
    
    @Autowired
    EnvCfg envCfg;
    
    private String CoreDNSPriorityClassNameKey           = "coredns_priority_class_name";
    private String CoreDNSAutoscalerPriorityClassNameKey = "coredns_autoscaler_priority_class_name";
    private String KubeDNSPriorityClassNameKey           = "kube_dns_priority_class_name";
    private  String KubeDNSAutoscalerPriorityClassNameKey = "kube_dns_autoscaler_priority_class_name";
    
    private Yaml yaml = new Yaml();
    
    final String DNS_ADDON_FILENAME     = "dns.yaml";
    final String METRICS_ADDON_FILENAME = "metrics.yaml";
    final String INGRESS_ADDON_FILENAME = "ingress.yaml";
    final String NETWORK_ADDON_FILENAME = "network.yaml";
    final String AGENT_ADDON_FILENAME   = "agent.yaml";
    final String TERMINAL_ADDON_FILENAME   = "terminal.yaml";

    /**
     * @description entry for build system addon plan
     * @author Regulus
     * @date 11/30/21 9:03 PM
     * @param jkeConfig
     * @return com.lnjoying.justice.cluster.manager.service.plan.ContainerPlan
     */
    private ContainerPlan buildSystemPlan(JkeConfig jkeConfig, ClusterNodePlanInfo clusterNodePlanInfo, NetworkOption networkOption) throws Exception
    {
        ContainerPlan containerPlan = initSystemAddon(jkeConfig);
        String dnsParams = buildDnsAddon(jkeConfig, networkOption);
        String metricsParams = buildMetricsAddon(jkeConfig);
        String ingressParams = buildIngressAddon(jkeConfig);
        String networkParams = buildNetworkAddon(jkeConfig, networkOption);
        String clusterAgentParams = buildClusterAgentAddon(jkeConfig, clusterNodePlanInfo);
        String joyTerminalParams = buildJoyTerminalAddon(jkeConfig);

        String dstPath = Utils.buildStr(envCfg.getAddonFilePath(), "/", clusterNodePlanInfo.getClusterNodeInfo().getNodeId());
        if (CollectionUtils.hasContent(dnsParams))
        {
            ClsUtils.writeFile(dnsParams, dstPath, DNS_ADDON_FILENAME);
        }
    
        if (CollectionUtils.hasContent(metricsParams))
        {
            ClsUtils.writeFile(metricsParams, dstPath, METRICS_ADDON_FILENAME);
        }
    
        if (CollectionUtils.hasContent(ingressParams))
        {
            ClsUtils.writeFile(ingressParams, dstPath, INGRESS_ADDON_FILENAME);
        }
    
        if (CollectionUtils.hasContent(networkParams))
        {
            ClsUtils.writeFile(networkParams, dstPath, NETWORK_ADDON_FILENAME);
        }

        if (CollectionUtils.hasContent(clusterAgentParams))
        {
            ClsUtils.writeFile(clusterAgentParams, dstPath, AGENT_ADDON_FILENAME);
        }

        if (CollectionUtils.hasContent(joyTerminalParams))
        {
            ClsUtils.writeFile(joyTerminalParams, dstPath, TERMINAL_ADDON_FILENAME);
        }
        
        List<String> envList = new ArrayList<>();
        
        
        String addonUrl        = String.format("KUBE_ADDONCFG_URL=%s", envCfg.getAddonFileUrl());
        String nodeEnv        = String.format("NODE_ID=%s", clusterNodePlanInfo.getClusterNodeInfo().getNodeId());
        envList.add(addonUrl);
        envList.add(nodeEnv);
        
        DockerInstParams dockerInstParams =  containerPlan.getDockerInstParams();
        dockerInstParams.setEnv(envList);
        List<String> binds = new ArrayList<>();
        binds.add("/etc/kubernetes:/etc/kubernetes:z");
        dockerInstParams.getHostConfig().setBinds(binds);
        dockerInstParams.setCmd(new String[] {NETWORK_ADDON_FILENAME, DNS_ADDON_FILENAME,METRICS_ADDON_FILENAME,INGRESS_ADDON_FILENAME, AGENT_ADDON_FILENAME, TERMINAL_ADDON_FILENAME});
    
        containerPlan.setContainerName(Utils.buildStr(containerPlan.getContainerName(), "_", clusterNodePlanInfo.getClusterId().substring(0,4)));
        return containerPlan;
    }
    
    private void buildSystemPlan(ClusterInnerInfo clusterInnerInfo, ClusterNodePlanInfo clusterNodePlanInfo) throws Exception
    {
        buildDnsAddon2(clusterInnerInfo, clusterNodePlanInfo);
        buildMetricsAddon2(clusterInnerInfo, clusterNodePlanInfo);
        buildIngressAddon2(clusterInnerInfo, clusterNodePlanInfo);
        buildNetworkAddon2(clusterInnerInfo, clusterNodePlanInfo);
    }
    
    /**
     * @description build dns addon
     * @author Regulus
     * @date 11/30/21 9:03 PM
     * @param jkeConfig
     * @return java.lang.String
     */
    String buildDnsAddon(JkeConfig jkeConfig, NetworkOption networkOption) throws IOException, TemplateException
    {
        DNSAddonInfo dnsAddonInfo = jkeConfig.getSystemAddons().getDns();
    
        Map<String, Object> dnsMap;
        switch (dnsAddonInfo.getProvider())
        {
            case DnsProvider.CoreDNSProvider:
                dnsMap = buildCoreDns(dnsAddonInfo, jkeConfig, networkOption);
                break;
            case DnsProvider.KubeDNSProvider:
                dnsMap = buildKubeDns(dnsAddonInfo, jkeConfig, networkOption);
                break;
            default:
                throw new WebSystemException(ErrorCode.CLUSTER_K8S_UNKOWN_DNS_PROVIDER, ErrorLevel.INFO);
        }
        
        String index = clusterDataService.getTemplateIndex(jkeConfig.getK8sVersion(), dnsAddonInfo.getProvider());
        return clusterDataService.getTemplate(index, dnsMap);
    }
    
    void buildDnsAddon2(ClusterInnerInfo clusterInnerInfo, ClusterNodePlanInfo clusterNodePlanInfo) throws IOException, TemplateException
    {
        JkeConfig jkeConfig = clusterInnerInfo.getJkeConfig();
        ContainerPlan containerPlan = initSystemAddon(jkeConfig);
        DNSAddonInfo dnsAddonInfo = jkeConfig.getSystemAddons().getDns();
        
        Map<String, Object> dnsMap;
        switch (dnsAddonInfo.getProvider())
        {
            case DnsProvider.CoreDNSProvider:
                dnsMap = buildCoreDns(dnsAddonInfo, jkeConfig, clusterInnerInfo.getNetworkOption());
                break;
            case DnsProvider.KubeDNSProvider:
                dnsMap = buildKubeDns(dnsAddonInfo, jkeConfig, clusterInnerInfo.getNetworkOption());
                break;
            default:
                throw new WebSystemException(ErrorCode.CLUSTER_K8S_UNKOWN_DNS_PROVIDER, ErrorLevel.INFO);
        }
        
        String index = clusterDataService.getTemplateIndex(jkeConfig.getK8sVersion(), dnsAddonInfo.getProvider());
        String dnsCfg =  clusterDataService.getTemplate(index, dnsMap);
        buildAddonContainer(dnsCfg, containerPlan);
        DockerInstParams dockerInstParams = containerPlan.getDockerInstParams();
        dockerInstParams.setCmd(new String[] {"dns"});
        clusterNodePlanInfo.addContainerPlan(containerPlan);
    }
    
    void buildAddonContainer(String addonCfg, ContainerPlan containerPlan)
    {
        List<String> envList = new ArrayList<>();
        String dnsCfg  = String.format("KUBE_ADDONCFG_DNS=%s", addonCfg);
        envList.add(dnsCfg);
        DockerInstParams dockerInstParams =  containerPlan.getDockerInstParams();
        dockerInstParams.setEnv(envList);
        List<String> binds = new ArrayList<>();
        binds.add("/etc/kubernetes:/etc/kubernetes:z");
        dockerInstParams.getHostConfig().setBinds(binds);
    }
    
    /**
     * @description build kube dns params
     * @author Regulus
     * @date 11/30/21 9:04 PM
     * @param dnsAddonInfo
     * @param jkeConfig 
     * @return java.util.Map<java.lang.String,java.lang.Object>
     */
    Map<String, Object> buildKubeDns(DNSAddonInfo dnsAddonInfo, JkeConfig jkeConfig, NetworkOption networkOption) throws WebSystemException
    {
        Map<String, Object> dnsMap = new HashMap<>();
        dnsMap.put("KubeDNSImage", clusterDataService.getImage(jkeConfig.getK8sVersion(), "coredns"));
        dnsMap.put("KubeDNSSidecarImage", clusterDataService.getImage(jkeConfig.getK8sVersion(), "coredns-autoscaler"));
        dnsMap.put("KubeDNSAutoScalerImage", clusterDataService.getImage(jkeConfig.getK8sVersion(), "coredns-autoscaler"));
        dnsMap.put("DNSMasqImage", jkeConfig.getAuthorization().getMode());
        dnsMap.put("RBACConfig", jkeConfig.getAuthorization().getMode());
        dnsMap.put("ClusterDomain", networkOption.getClusterDomain());
        dnsMap.put("ClusterDNSServer", networkOption.getClusterDnsServer());
        dnsMap.put("UpstreamNameservers", dnsAddonInfo.getUpstreamNameServers());
        dnsMap.put("ReverseCIDRs", dnsAddonInfo.getReverseCidrs());
        dnsMap.put("StubDomains", dnsAddonInfo.getReverseCidrs());
    
        dnsMap.put("NodeSelector", dnsAddonInfo.getNodeSelector());
        Map<String, Object> updateStrategyMap = new HashMap<>();
        updateStrategyMap.put("type", dnsAddonInfo.getUpdateStrategy().getStrategy());
        updateStrategyMap.put("rollingUpdate", ClsUtils.objectToMap(dnsAddonInfo.getUpdateStrategy().getRollingUpdate()));
        dnsMap.put("UpdateStrategy", updateStrategyMap);
        dnsMap.put("Tolerations", ClsUtils.listObjToMap(dnsAddonInfo.getTolerations()));
        dnsMap.put("KubeDNSPriorityClassName", dnsAddonInfo.getOptions().get(KubeDNSPriorityClassNameKey));
        dnsMap.put("KubeDNSAutoscalerPriorityClassNameKey", dnsAddonInfo.getOptions().get(KubeDNSAutoscalerPriorityClassNameKey));
        LinearAutoscalerParams linearAutoscalerParams = dnsAddonInfo.getLinearAutoscalerParams();
        if (linearAutoscalerParams != null)
        {
            dnsMap.put("LinearAutoscalerParams", JsonUtils.toJson(linearAutoscalerParams));
        }
        
        return dnsMap;
    }
    
    /**
     * @description build core dns params
     * @author Regulus
     * @date 11/30/21 9:04 PM
     * @param dnsAddonInfo
     * @param jkeConfig
     * @return java.util.Map<java.lang.String,java.lang.Object>
     */
    Map<String, Object> buildCoreDns(DNSAddonInfo dnsAddonInfo, JkeConfig jkeConfig, NetworkOption networkOption) throws WebSystemException
    {
        Map<String, Object> dnsMap = new HashMap<>();
        dnsMap.put("CoreDNSImage", clusterDataService.getImage(jkeConfig.getK8sVersion(), "coredns"));
        dnsMap.put("CoreDNSAutoScalerImage", clusterDataService.getImage(jkeConfig.getK8sVersion(), "coredns-autoscaler"));
        dnsMap.put("RBACConfig", jkeConfig.getAuthorization().getMode());
        dnsMap.put("ClusterDomain", networkOption.getClusterDomain());
        dnsMap.put("ClusterDNSServer", networkOption.getClusterDnsServer());
        dnsMap.put("UpstreamNameservers", dnsAddonInfo.getUpstreamNameServers());
        dnsMap.put("ReverseCIDRs", dnsAddonInfo.getReverseCidrs());
        dnsMap.put("NodeSelector", dnsAddonInfo.getNodeSelector());
        Map<String, Object> updateStrategyMap = new HashMap<>();
        updateStrategyMap.put("type", dnsAddonInfo.getUpdateStrategy().getStrategy());
        updateStrategyMap.put("rollingUpdate", ClsUtils.objectToMap(dnsAddonInfo.getUpdateStrategy().getRollingUpdate()));
        dnsMap.put("UpdateStrategy", updateStrategyMap);
        dnsMap.put("Tolerations", ClsUtils.listObjToMap(dnsAddonInfo.getTolerations()));
        if (! CollectionUtils.isEmpty(dnsAddonInfo.getOptions()))
        {
            dnsMap.put("CoreDNSPriorityClassName", dnsAddonInfo.getOptions().get(CoreDNSPriorityClassNameKey));
            dnsMap.put("CoreDNSAutoscalerPriorityClassName", dnsAddonInfo.getOptions().get(CoreDNSAutoscalerPriorityClassNameKey));
        }
        
        LinearAutoscalerParams linearAutoscalerParams = dnsAddonInfo.getLinearAutoscalerParams();
        if (linearAutoscalerParams != null)
        {
            
            dnsMap.put("LinearAutoscalerParams", JsonUtils.toJson(ClsUtils.objectToMap(linearAutoscalerParams)));
        }
        
        return dnsMap;
    }
    
    /**
     * @description build metric addon
     * @author Regulus
     * @date 11/30/21 9:04 PM
     * @param jkeConfig
     * @return java.lang.String
     */
    String buildMetricsAddon(JkeConfig jkeConfig) throws IOException, TemplateException
    {
        MonitorAddonInfo monitorAddonInfo = jkeConfig.getSystemAddons().getMonitor();
        if (monitorAddonInfo == null || StringUtils.isEmpty(monitorAddonInfo.getProvider()))
        {
            return "";
        }
        if (!monitorAddonInfo.getProvider().equals("metrics-server"))
        {
            return "";
        }
        String image = clusterDataService.getImage(jkeConfig.getK8sVersion(), "metrics-server");
        String[] imageArray = image.split(":");
        String tag = imageArray[imageArray.length - 1];
        Map<String, Object> monitorMap = new HashMap<>();
        monitorMap.put("MetricsServerImage", image);
        monitorMap.put("RBACConfig", jkeConfig.getAuthorization().getMode());
        monitorMap.put("Options", monitorAddonInfo.getOptions());
        monitorMap.put("NodeSelector", monitorAddonInfo.getNodeSelector());
        monitorMap.put("Version", tag);
        Map<String, Object> updateStrategyMap = new HashMap<>();
        updateStrategyMap.put("type", monitorAddonInfo.getUpdateStrategy().getStrategy());
        updateStrategyMap.put("rollingUpdate", ClsUtils.objectToMap(monitorAddonInfo.getUpdateStrategy().getRollingUpdate()));
        monitorMap.put("UpdateStrategy", updateStrategyMap);
        
        monitorMap.put("Replicas", monitorAddonInfo.getReplicas());
        monitorMap.put("Tolerations", ClsUtils.listObjToMap(monitorAddonInfo.getTolerations()));
        monitorMap.put("MetricsServerPriorityClassName", monitorAddonInfo.getMetricsServerPriorityClassName());
    
        String index = clusterDataService.getTemplateIndex(jkeConfig.getK8sVersion(), monitorAddonInfo.getProvider());
        return clusterDataService.getTemplate(index, monitorMap);
    }
    
    void buildMetricsAddon2(ClusterInnerInfo clusterInnerInfo, ClusterNodePlanInfo clusterNodePlanInfo) throws IOException, TemplateException
    {
        JkeConfig jkeConfig = clusterInnerInfo.getJkeConfig();
        MonitorAddonInfo monitorAddonInfo = jkeConfig.getSystemAddons().getMonitor();
        if (monitorAddonInfo == null || StringUtils.isEmpty(monitorAddonInfo.getProvider()))
        {
            return;
        }
        if (!monitorAddonInfo.getProvider().equals("metrics-server"))
        {
            return;
        }
        String image = clusterDataService.getImage(jkeConfig.getK8sVersion(), "metrics-server");
        String[] imageArray = image.split(":");
        String tag = imageArray[imageArray.length - 1];
        Map<String, Object> monitorMap = new HashMap<>();
        monitorMap.put("MetricsServerImage", image);
        monitorMap.put("RBACConfig", jkeConfig.getAuthorization().getMode());
        monitorMap.put("Options", monitorAddonInfo.getOptions());
        monitorMap.put("NodeSelector", monitorAddonInfo.getNodeSelector());
        monitorMap.put("Version", tag);
        Map<String, Object> updateStrategyMap = new HashMap<>();
        updateStrategyMap.put("type", monitorAddonInfo.getUpdateStrategy().getStrategy());
        updateStrategyMap.put("rollingUpdate", ClsUtils.objectToMap(monitorAddonInfo.getUpdateStrategy().getRollingUpdate()));
        monitorMap.put("UpdateStrategy", updateStrategyMap);
        
        monitorMap.put("Replicas", monitorAddonInfo.getReplicas());
        monitorMap.put("Tolerations", ClsUtils.listObjToMap(monitorAddonInfo.getTolerations()));
        monitorMap.put("MetricsServerPriorityClassName", monitorAddonInfo.getMetricsServerPriorityClassName());
        
        String index = clusterDataService.getTemplateIndex(jkeConfig.getK8sVersion(), monitorAddonInfo.getProvider());
    
        ContainerPlan containerPlan = initSystemAddon(jkeConfig);
        String cfg =  clusterDataService.getTemplate(index, monitorMap);
        DockerInstParams dockerInstParams = containerPlan.getDockerInstParams();
        dockerInstParams.setCmd(new String[] {"metrics"});
        
        buildAddonContainer(cfg, containerPlan);
        clusterNodePlanInfo.addContainerPlan(containerPlan);
    }
    /**
     * @description build ingress addon params
     * @author Regulus
     * @date 11/30/21 9:05 PM
     * @param jkeConfig
     * @return java.lang.String
     */
    String buildIngressAddon(JkeConfig jkeConfig) throws IOException, TemplateException
    {
        IngressAddonInfo ingressAddonInfo = jkeConfig.getSystemAddons().getIngress();
        if (ingressAddonInfo == null || StringUtils.isEmpty(ingressAddonInfo.getProvider()))
        {
            return "";
        }
        
        String ingressService = "nginx-ingress";
        if (! ingressAddonInfo.getProvider().equals("nginx") && ! ingressAddonInfo.getProvider().equals("nginx-ingress"))
        {
            ingressService = ingressAddonInfo.getProvider();
        }
        
        String image = clusterDataService.getImage(jkeConfig.getK8sVersion(), ingressService);
        if (StringUtils.isEmpty(image))
        {
            throw new WebSystemException(ErrorCode.CLUSTER_K8S_IMAGE_NOT_CFG, ErrorLevel.INFO);
        }
    
        Map<String, Object> ingressMap = new HashMap<>();
        ingressMap.put("Options", ingressAddonInfo.getOptions());
        ingressMap.put("RBACConfig", jkeConfig.getAuthorization().getMode());
        ingressMap.put("NodeSelector", ingressAddonInfo.getNodeSelector());
        ingressMap.put("ExtraArgs", ingressAddonInfo.getExtraArgs());
        ingressMap.put("DNSPolicy", ingressAddonInfo.getDnsPolicy());
        ingressMap.put("IngressImage", image);
        String  ingressBackImage= clusterDataService.getImage(jkeConfig.getK8sVersion(), "ingress-backend");
        ingressMap.put("IngressBackend", ingressBackImage);
        String ingressWebhook = clusterDataService.getImage(jkeConfig.getK8sVersion(), "ingress-webhook");
        ingressMap.put("IngressWebhook", ingressWebhook);
        ingressMap.put("ExtraEnvs", ingressAddonInfo.getExtraArgs());
        ingressMap.put("ExtraVolumes", ingressAddonInfo.getExtraVolumes());
        ingressMap.put("ExtraVolumeMounts", ingressAddonInfo.getExtraVolumeMounts());
        ingressMap.put("HTTPPort", ingressAddonInfo.getHttpPort());
        ingressMap.put("HTTPSPort", ingressAddonInfo.getHttpsPort());
        ingressMap.put("NetworkMode", ingressAddonInfo.getNetworkMode());
        ingressMap.put("DefaultBackend", ingressAddonInfo.getDefaultBackend());
        if (ingressAddonInfo.getUpdateStrategy() != null)
        {
            Map<String, Object> updateStrategyMap = new HashMap<>();
            updateStrategyMap.put("type", ingressAddonInfo.getUpdateStrategy().getStrategy());
            Map<String, Integer> updateValue = new HashMap<>();
            updateValue.put("maxUnavailable",ingressAddonInfo.getUpdateStrategy().getRollingUpdate().getMaxUnavailable());
            updateStrategyMap.put("rollingUpdate", updateValue);
            ingressMap.put("UpdateStrategy", updateStrategyMap);
        }
        
        ingressMap.put("Tolerations", ClsUtils.listObjToMap(ingressAddonInfo.getTolerations()));
        ingressMap.put("NginxIngressControllerPriorityClassName", ingressAddonInfo.getNginxIngressControllerPriorityClassName());
        ingressMap.put("DefaultIngressClass", ingressAddonInfo.getDefaultHttpBackendPriorityClassName());
    
        String index = clusterDataService.getTemplateIndex(jkeConfig.getK8sVersion(), ingressAddonInfo.getProvider());
        return clusterDataService.getTemplate(index, ingressMap);
    }
    
    void buildIngressAddon2(ClusterInnerInfo clusterInnerInfo, ClusterNodePlanInfo clusterNodePlanInfo) throws IOException, TemplateException
    {
        JkeConfig jkeConfig = clusterInnerInfo.getJkeConfig();
        IngressAddonInfo ingressAddonInfo = jkeConfig.getSystemAddons().getIngress();
        if (ingressAddonInfo == null || StringUtils.isEmpty(ingressAddonInfo.getProvider()))
        {
            return;
        }
        
        String ingressService = "nginx-ingress";
        if (! ingressAddonInfo.getProvider().equals("nginx") && ! ingressAddonInfo.getProvider().equals("nginx-ingress"))
        {
            ingressService = ingressAddonInfo.getProvider();
        }
        
        String image = clusterDataService.getImage(jkeConfig.getK8sVersion(), ingressService);
        if (StringUtils.isEmpty(image))
        {
            throw new WebSystemException(ErrorCode.CLUSTER_K8S_IMAGE_NOT_CFG, ErrorLevel.INFO);
        }
        
        Map<String, Object> ingressMap = new HashMap<>();
        ingressMap.put("Options", ingressAddonInfo.getOptions());
        ingressMap.put("RBACConfig", jkeConfig.getAuthorization().getMode());
        ingressMap.put("NodeSelector", ingressAddonInfo.getNodeSelector());
        ingressMap.put("ExtraArgs", ingressAddonInfo.getExtraArgs());
        ingressMap.put("DNSPolicy", ingressAddonInfo.getDnsPolicy());
        ingressMap.put("IngressImage", image);
        String  ingressBackImage= clusterDataService.getImage(jkeConfig.getK8sVersion(), "ingress-backend");
        ingressMap.put("IngressBackend", ingressBackImage);
        String ingressWebhook = clusterDataService.getImage(jkeConfig.getK8sVersion(), "ingress-webhook");
        ingressMap.put("IngressWebhook", ingressWebhook);
        ingressMap.put("ExtraEnvs", ingressAddonInfo.getExtraArgs());
        ingressMap.put("ExtraVolumes", ingressAddonInfo.getExtraVolumes());
        ingressMap.put("ExtraVolumeMounts", ingressAddonInfo.getExtraVolumeMounts());
        ingressMap.put("HTTPPort", ingressAddonInfo.getHttpPort());
        ingressMap.put("HTTPSPort", ingressAddonInfo.getHttpsPort());
        ingressMap.put("NetworkMode", ingressAddonInfo.getNetworkMode());
        ingressMap.put("DefaultBackend", ingressAddonInfo.getDefaultBackend());
        Map<String, Object> updateStrategyMap = new HashMap<>();
        updateStrategyMap.put("type", ingressAddonInfo.getUpdateStrategy().getStrategy());
        updateStrategyMap.put(" ", ClsUtils.objectToMap(ingressAddonInfo.getUpdateStrategy().getRollingUpdate()));
        ingressMap.put("UpdateStrategy", updateStrategyMap);
        ingressMap.put("Tolerations", ClsUtils.listObjToMap(ingressAddonInfo.getTolerations()));
        ingressMap.put("NginxIngressControllerPriorityClassName", ingressAddonInfo.getNginxIngressControllerPriorityClassName());
        ingressMap.put("DefaultIngressClass", ingressAddonInfo.getDefaultHttpBackendPriorityClassName());
        
        String index = clusterDataService.getTemplateIndex(jkeConfig.getK8sVersion(), ingressAddonInfo.getProvider());
        String cfg = clusterDataService.getTemplate(index, ingressMap);
        ContainerPlan containerPlan = initSystemAddon(jkeConfig);
        DockerInstParams dockerInstParams = containerPlan.getDockerInstParams();
        dockerInstParams.setCmd(new String[] {"ingress"});
        buildAddonContainer(cfg, containerPlan);
        clusterNodePlanInfo.addContainerPlan(containerPlan);
    }
    
    /**
     * @description build network addon
     * @author Regulus
     * @date 11/30/21 9:06 PM
     * @param jkeConfig
     * @param networkOption
     * @return java.lang.String
     */
    String buildNetworkAddon(JkeConfig jkeConfig, NetworkOption networkOption) throws IOException, TemplateException
    {
        NetworkAddonInfo networkAddonInfo = jkeConfig.getSystemAddons().getNetwork();
        Map<String, Object> netMaps = null;
        switch (networkAddonInfo.getProvider())
        {
            case FlannelNetworkPlugin:
                netMaps = buildFlanelNetworkPlugin(networkAddonInfo, jkeConfig, networkOption);
                break;
            case CalicoNetworkPlugin:
                netMaps = buildCalicoNetworkPlugin(networkAddonInfo, jkeConfig, networkOption);
                break;
            default:
                throw new WebSystemException(ErrorCode.CLUSTER_K8S_IMAGE_NOT_CFG, ErrorLevel.INFO);
        }
        
        String index = clusterDataService.getTemplateIndex(jkeConfig.getK8sVersion(), networkAddonInfo.getProvider());
        return clusterDataService.getTemplate(index, netMaps);
    }
    
    void buildNetworkAddon2(ClusterInnerInfo clusterInnerInfo, ClusterNodePlanInfo clusterNodePlanInfo) throws IOException, TemplateException
    {
        JkeConfig jkeConfig = clusterInnerInfo.getJkeConfig();
        NetworkOption networkOption = clusterInnerInfo.getNetworkOption();
        NetworkAddonInfo networkAddonInfo = jkeConfig.getSystemAddons().getNetwork();
        Map<String, Object> netMaps = null;
        switch (networkAddonInfo.getProvider())
        {
            case FlannelNetworkPlugin:
                netMaps = buildFlanelNetworkPlugin(networkAddonInfo, jkeConfig, networkOption);
                break;
            case CalicoNetworkPlugin:
                netMaps = buildCalicoNetworkPlugin(networkAddonInfo, jkeConfig, networkOption);
                break;
            default:
                throw new WebSystemException(ErrorCode.CLUSTER_K8S_IMAGE_NOT_CFG, ErrorLevel.INFO);
        }
        
        String index = clusterDataService.getTemplateIndex(jkeConfig.getK8sVersion(), networkAddonInfo.getProvider());
        String cfg = clusterDataService.getTemplate(index, netMaps);
        ContainerPlan containerPlan = initSystemAddon(jkeConfig);
        DockerInstParams dockerInstParams = containerPlan.getDockerInstParams();
        dockerInstParams.setCmd(new String[] {"network"});
        buildAddonContainer(cfg, containerPlan);
        
        buildAddonContainer(cfg, containerPlan);
        clusterNodePlanInfo.addContainerPlan(containerPlan);
    }
    
    String CalicoCloudProvider                           = "calico_cloud_provider";
    String CalicoFlexVolPluginDirectory                  = "calico_flex_volume_plugin_dir";
    String CalicoNodePriorityClassNameKeyName            = "calico_node_priority_class_name";
    String CalicoKubeControllersPriorityClassNameKeyName = "calico_kube_controllers_priority_class_name";
    Map<String, Object> buildCalicoNetworkPlugin(NetworkAddonInfo networkAddonInfo, JkeConfig jkeConfig, NetworkOption networkOption) throws WebSystemException
    {
        Map<String, Object> netMap = new HashMap<>();
        String clientConfig        = EnvUtil.getConfigPath(KeyCertName.KubeNodeCertName);
        netMap.put("KubeCfg",clientConfig);
        netMap.put("ClusterCIDR",networkOption.getClusterCidr());
        String cniImage   = clusterDataService.getImage(jkeConfig.getK8sVersion(), "calico-cni");
        String nodeImage  = clusterDataService.getImage(jkeConfig.getK8sVersion(), "calico-node");
        String calicoctl  = clusterDataService.getImage(jkeConfig.getK8sVersion(), "calico-ctl");
        String ctrlsImage = clusterDataService.getImage(jkeConfig.getK8sVersion(), "calico-kube-controllers");
        String flexVolImg = clusterDataService.getImage(jkeConfig.getK8sVersion(), "calico-flexvol");
    
    
        netMap.put("MTU", String.valueOf(networkAddonInfo.getMtu()));
        netMap.put("CNIImage",         cniImage);
        netMap.put("NodeImage",        nodeImage);
        netMap.put("Calicoctl",        calicoctl);
        netMap.put("FlexVolImg",       flexVolImg);
        netMap.put("ControllersImage", ctrlsImage);
        netMap.put("RBACConfig",       jkeConfig.getAuthorization().getMode());
        netMap.put("NodeSelector",     networkAddonInfo.getNodeSelector());
    
        if (networkAddonInfo.getUpdateStrategy() != null)
        {
            Map<String, Object> updateStrategyMap = new HashMap<>();
            updateStrategyMap.put("type", networkAddonInfo.getUpdateStrategy().getStrategy());
            Map<String, Integer> updateValue = new HashMap<>();
            updateValue.put("maxUnavailable",networkAddonInfo.getUpdateStrategy().getRollingUpdate().getMaxUnavailable());
            updateStrategyMap.put("rollingUpdate", updateValue);
            netMap.put("UpdateStrategy", updateStrategyMap);
        }
        
        if (! CollectionUtils.isEmpty(networkAddonInfo.getTolerations()))
        {
            netMap.put("Tolerations", ClsUtils.listObjToMap(networkAddonInfo.getTolerations()));
        }
        
        if (! CollectionUtils.isEmpty(networkAddonInfo.getOptions()))
        {
            netMap.put("CloudProvider",    networkAddonInfo.getOptions().get(CalicoCloudProvider));
            netMap.put("FlexVolPluginDir", networkAddonInfo.getOptions().get(CalicoFlexVolPluginDirectory));
            netMap.put("CalicoNodePriorityClassName",networkAddonInfo.getOptions().get(CalicoNodePriorityClassNameKeyName));
            netMap.put("CalicoKubeControllersPriorityClassName",networkAddonInfo.getOptions().get(CalicoKubeControllersPriorityClassNameKeyName));
        }
        
        return netMap;
    }
    
    
    
    int    FlannelVxLanNetworkIdentify         = 1;
    String FlannelBackendVxLanNetworkIdentify  = "flannel_backend_vni";
    
    int atoiWithDefault(String val, int defaultValue)
    {
        if (StringUtils.isEmpty(val))
        {
            return defaultValue;
        }
        return Integer.parseInt(val);
    }
    
    int    FlannelVxLanPort                    = 8472;
    String FlannelBackendPort                  = "flannel_backend_port";
    String FlannelIface                        = "flannel_iface";
    String FlannelBackendType                  = "flannel_backend_type";
    String KubeFlannelPriorityClassNameKeyName = "kube_flannel_priority_class_name";
    Map<String, Object> buildFlanelNetworkPlugin(NetworkAddonInfo networkAddonInfo, JkeConfig jkeConfig, NetworkOption networkOption) throws WebSystemException
    {
       
    
        String image = clusterDataService.getImage(jkeConfig.getK8sVersion(), "flannel");
        String cniImage = clusterDataService.getImage(jkeConfig.getK8sVersion(), "flannel-cni");
        Map<String, Object> netMap = new HashMap<>();
        netMap.put("ClusterCIDR", networkOption.getClusterCidr());
        netMap.put("Image",    image);
        netMap.put("CNIImage", cniImage);
        Map<String,Object> backEndMap = new HashMap<>();
       
        netMap.put("FlannelBackend",    backEndMap);
        netMap.put("RBACConfig",        jkeConfig.getAuthorization().getMode());
        netMap.put("ClusterVersion",    ClsUtils.getTagMajorVersion(jkeConfig.getK8sVersion()));
        netMap.put("NodeSelector",      networkAddonInfo.getNodeSelector());
        
        if (! CollectionUtils.isEmpty(networkAddonInfo.getOptions()))
        {
            int vni  = atoiWithDefault(networkAddonInfo.getOptions().get(FlannelBackendVxLanNetworkIdentify), FlannelVxLanNetworkIdentify);
            int port = atoiWithDefault(networkAddonInfo.getOptions().get(FlannelBackendPort), FlannelVxLanPort);
            backEndMap.put("VNI",           vni);
            backEndMap.put("Port",          port);
            netMap.put("FlannelInterface", networkAddonInfo.getOptions().get(FlannelIface));
            backEndMap.put("Type",          networkAddonInfo.getOptions().get(FlannelBackendType));
            netMap.put("KubeFlannelPriorityClassName", networkAddonInfo.getOptions().get(KubeFlannelPriorityClassNameKeyName));
        }
    
        Map<String, Object> updateStrategyMap = new HashMap<>();
        updateStrategyMap.put("type", networkAddonInfo.getUpdateStrategy().getStrategy());
        updateStrategyMap.put("rollingUpdate",     ClsUtils.objectToMap(networkAddonInfo.getUpdateStrategy().getRollingUpdate()));
        netMap.put("UpdateStrategy",               updateStrategyMap);
        
        return netMap;
    }
    
    /**
     * @description init system addon params
     * @author Regulus
     * @date 11/30/21 9:08 PM
     * @param jkeConfig
     * @return com.lnjoying.justice.cluster.manager.service.plan.ContainerPlan
     */
    ContainerPlan initSystemAddon(JkeConfig jkeConfig) throws WebSystemException
    {
        ContainerPlan containerPlan = new ContainerPlan(DeployContainerName.JKE_ADDON_DEPLOYER, jkeConfig.getRegistries());
        DockerInstParams dockerInstParams = containerPlan.getDockerInstParams();
        dockerInstParams.getHostConfig().setPrivileged(true);
        dockerInstParams.getHostConfig().setNetworkMode(DockerNetworkMode.HOST);
        containerPlan.setAutoRun(true);
        String image = clusterDataService.getImage(jkeConfig.getK8sVersion(), containerPlan.getContainerName());
        
        if (StringUtils.isEmpty(image))
        {
            throw new WebSystemException(ErrorCode.CLUSTER_K8S_IMAGE_NOT_CFG, ErrorLevel.INFO);
        }
        
        dockerInstParams.setImage(image);
        Map<String, String> labels = new HashMap<>();
        labels.put("com.lnjoying.jke.container.name", containerPlan.getContainerName());
        dockerInstParams.setLabels(labels);
        dockerInstParams.setEntrypoint(new String[]{"addon-deploy"});
        return containerPlan;
    }
    
    @Override
    public int buildNodeDeployPlan(ClusterInnerInfo clusterInnerInfo, ClusterNodePlanInfo clusterNodePlanInfo)
    {
        try
        {
            LOGGER.info("build node deploy plan.");
            ContainerPlan containerPlan = buildSystemPlan(clusterInnerInfo.getJkeConfig(), clusterNodePlanInfo, clusterInnerInfo.getNetworkOption());
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

    String buildClusterAgentAddon(JkeConfig jkeConfig, ClusterNodePlanInfo clusterNodePlanInfo) throws IOException, TemplateException
    {
        String agentService = "cluster-agent";

        String image = clusterDataService.getImage(jkeConfig.getK8sVersion(), agentService);
        if (StringUtils.isEmpty(image))
        {
            throw new WebSystemException(ErrorCode.CLUSTER_K8S_IMAGE_NOT_CFG, ErrorLevel.INFO);
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
        clusterAgentMap.put("WorkerType", WorkerType.K8S_AGENT);

        return clusterDataService.getTemplate("cluster-agent", clusterAgentMap);
    }

    String buildClusterAgentAddon(String regionId, String token) throws IOException, TemplateException
    {
        String agentService = "cluster-agent";

        String image = clusterDataService.getImage(clusterDataService.getClusterVersions(ClusterType.K8S).get(ClusterType.K8S).get(0), agentService);
        if (StringUtils.isEmpty(image))
        {
            throw new WebSystemException(ErrorCode.CLUSTER_K8S_IMAGE_NOT_CFG, ErrorLevel.INFO);
        }

        List<Toleration> tolerations = null;
        String gateway = combRpcService.getEdgeResourceService().getOnlineGwByRegion(regionId);
        Map<String, Boolean> features = null;
        Map<String, String> agentEnvVars = null;

        Map<String, Object> clusterAgentMap = new HashMap<>();
        clusterAgentMap.put("PrivateRegistryConfig", null);
        clusterAgentMap.put("Tolerations", ClsUtils.listObjToMap(tolerations));
        clusterAgentMap.put("Token", token);
        clusterAgentMap.put("region_id", regionId);
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

    public void buildImportClusterAddon(String clusterId, String token, String regionId)
    {
        try
        {
            LOGGER.info("build import cluster addon file.");
            String clusterAgentParams = buildClusterAgentAddon(regionId, token);

            String dstPath = Utils.buildStr(envCfg.getAddonFilePath(), "/", clusterId);

            if (CollectionUtils.hasContent(clusterAgentParams))
            {
                ClsUtils.writeFile(clusterAgentParams, dstPath, AGENT_ADDON_FILENAME);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("build import cluster addon file failed.");
        }
    }

    public String getAgentAddonFilename()
    {
        return AGENT_ADDON_FILENAME;
    }

    String buildJoyTerminalAddon(JkeConfig jkeConfig) throws IOException, TemplateException
    {
        String image = clusterDataService.getImage(jkeConfig.getK8sVersion(), "kubelet");
        if (StringUtils.isEmpty(image))
        {
            throw new WebSystemException(ErrorCode.CLUSTER_K8S_IMAGE_NOT_CFG, ErrorLevel.INFO);
        }

        Map<String, Object> joyTerminalMap = new HashMap<>();
        joyTerminalMap.put("IsK8s", "true");
        joyTerminalMap.put("TerminalImage", image);

        return clusterDataService.getTemplate("joy-terminal", joyTerminalMap);
    }
}

