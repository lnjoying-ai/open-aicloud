package com.lnjoying.justice.cluster.agent.config;

import com.lnjoying.justice.cluster.agent.common.GlobalArgsInfo;
import com.lnjoying.justice.cluster.agent.model.NodeConfig;
import com.lnjoying.justice.cluster.agent.util.CipherUtil;
import com.lnjoying.justice.schema.constant.WorkerType;
import com.micro.core.nework.entity.NetEntity;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1ConfigMap;
import io.kubernetes.client.openapi.models.V1ObjectMeta;
import io.kubernetes.client.openapi.models.V1Secret;
import io.kubernetes.client.openapi.models.V1ServiceAccount;
import io.kubernetes.client.util.ClientBuilder;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.yaml.snakeyaml.Yaml;

import javax.annotation.PostConstruct;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Configuration("agentConfigYaml")
public class AgentConfigYaml
{
    NodeConfig nodeConfig = null;
    String configPath;
    String gwCandidatesPath;
    CoreV1Api coreApi = null;
    Yaml yaml = new Yaml();
    
    String configMapName = "joy-cls-agent-config";
    String configGwCandiate = "joy-cls-gw-candidate";
    
    String configMapNameSpace = "default";
    private static Logger LOGGER = LogManager.getLogger();
    
    private String workerType = WorkerType.K8S_AGENT;
    
    boolean loadCfgFromK8s = true;
    @PostConstruct
    void loadNodeConfigYaml()
    {
        LOGGER.info("load config from config map");
    
        setConfigPath();
        
        String tmpWorkerType = System.getenv("WORKER_TYPE");
        if (StringUtils.isNotEmpty(tmpWorkerType))
        {
            workerType = tmpWorkerType;
        }
        
        readNodeConfigFromConfigMap();
        if (nodeConfig == null)
        {
            LOGGER.info("load config from local node map");
            loadLocalNodeConfigYaml();
        }
    }
    
    void setConfigPath()
    {
        if (StringUtils.isNotBlank(configPath))
        {
            return;
        }
        
        String runDir = System.getProperty("user.dir");
        LOGGER.info("userdir:"  +  runDir);
        configPath = runDir + "/" + "agent_config.yaml";
        gwCandidatesPath = runDir + "/" + "gw_candidates.yaml";
        LOGGER.info(configPath);
    }
    
    void loadLocalNodeConfigYaml()
    {
    
        setConfigPath();
        
        try
        {
            FileReader reader = new FileReader(configPath);
            nodeConfig = yaml.loadAs(reader, NodeConfig.class);
            if (nodeConfig != null && nodeConfig.getPrivate_key() != null && nodeConfig.getNode_id() != null)
            {
                CipherUtil.import_private_key(nodeConfig.getPrivate_key());
            }
            reader.close();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
			if (nodeConfig != null) nodeConfig.reset();
        }
        catch (IllegalArgumentException e)
		{
			e.printStackTrace();
            if (nodeConfig != null) nodeConfig.reset();
        }
        catch (IOException e)
        {
            if (nodeConfig != null) nodeConfig.reset();
        }

		if (nodeConfig == null || nodeConfig.getPrivate_key() == null && nodeConfig.getNode_id() == null
                || nodeConfig.getPrivate_key().isEmpty() || nodeConfig.getNode_id().isEmpty())
        {
            nodeConfig  = CipherUtil.gen_node();
            try
            {
                FileWriter writer = new FileWriter(configPath);
                yaml.dump(nodeConfig, writer);
                writer.flush();
                writer.close();
                saveNodeConfigToConfigMap();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
    
    public CoreV1Api getCoreApi()
    {
        if (coreApi == null && loadCfgFromK8s)
        {
            try
            {
                coreApi = new CoreV1Api(ClientBuilder.standard().build());
                coreApi.readNamespace(configMapNameSpace,"true");
                LOGGER.info("build read core api success");
            }
            catch (IOException e)
            {
                loadCfgFromK8s = false;
                coreApi = null;
                e.printStackTrace();
            }
            catch (ApiException e)
            {
                loadCfgFromK8s = false;
                coreApi = null;
                e.printStackTrace();
            }
        }
        
        return coreApi;
    }
    
    public void readNodeConfigFromConfigMap()
    {
        LOGGER.info("begin read node config to config map");
        try
        {
            CoreV1Api coreclient = getCoreApi();
            if (coreclient != null)
            {
                V1ConfigMap agentconfigMap = coreclient.readNamespacedConfigMap(configMapName, configMapNameSpace, "true");
                String configstr = agentconfigMap.getData().get("data");
                LOGGER.info("load data from config map {}", configstr);
                nodeConfig = yaml.loadAs(configstr, NodeConfig.class);
                if (nodeConfig != null)
                {
                    if (StringUtils.isNotBlank(GlobalArgsInfo.cmdArgs.getToken())
                            && StringUtils.isNotBlank(nodeConfig.getToken())
                            && !nodeConfig.getToken().equals(GlobalArgsInfo.cmdArgs.getToken()))
                    {
                        coreclient.deleteNamespacedConfigMap(configMapName, configMapNameSpace, null, null, null, null, null, null);
                        nodeConfig = null;
                    }
                    CipherUtil.import_private_key(nodeConfig.getPrivate_key());
                }
            }
            
         }

        catch (ApiException e)
        {
            e.printStackTrace();
            LOGGER.error("read node config map error {}", e.getMessage());
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("read node config map error {}", e.getMessage());
        }
    }
    
    public void saveNodeConfigToConfigMap()
    {
        V1ConfigMap agentconfigMap = new V1ConfigMap();
    
        try
        {
            LOGGER.info("begin save node config to config map");
            CoreV1Api coreClient = getCoreApi();
            if (coreClient != null)
            {
    
                V1ObjectMeta v1ObjectMeta = new V1ObjectMeta();
                v1ObjectMeta.setName(configMapName);
                v1ObjectMeta.setNamespace(configMapNameSpace);
                agentconfigMap.setMetadata(v1ObjectMeta);
                agentconfigMap.putDataItem("data", yaml.dumpAsMap(nodeConfig));
                coreClient.createNamespacedConfigMap(configMapNameSpace, agentconfigMap, "true", null, null, null);
                LOGGER.info("save config map success");
                LOGGER.info("core api build success");
            }
        }
        catch (ApiException e)
        {
            LOGGER.error("save node config map error. code {} message {}", e.getCode(), e.getMessage());
            if (e.getCode() == 409)
            {
                replaceConfigMap(agentconfigMap);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("save node config map error {}", e.getMessage());
        }
    }
    
    void replaceConfigMap(V1ConfigMap agentconfigMap)
    {
        try
        {
            CoreV1Api coreClient = getCoreApi();
            if (coreClient != null)
            {
                coreClient.replaceNamespacedConfigMap(configMapName, configMapNameSpace, agentconfigMap, "true", null, null,null);
                LOGGER.info("replace config map success");
            }
        }
        catch (ApiException e)
        {
            e.printStackTrace();
            LOGGER.error("save node config map error. code {} message {}", e.getCode(), e.getMessage());
        }
    }
    
    public void dumpNodeConfig(NodeConfig config)
    {
        LOGGER.info("dump node config");
        
        try
        {
            FileWriter writer = new FileWriter(configPath);
            yaml.dump(config, writer);
            writer.flush();
            writer.close();
            nodeConfig = config;
            saveNodeConfigToConfigMap();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            LOGGER.error("dump node config error {}", e.getMessage());
        }
    }
    
    public void dumpGWCandidates(List<NetEntity> gwList)
    {
        LOGGER.info("dump node config");
        try
        {
            FileWriter writer = new FileWriter(gwCandidatesPath);
            yaml.dump(gwList, writer);
            writer.flush();
            writer.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            LOGGER.error("dump node config error {}", e.getMessage());
        }
    }
    
    
    public  NodeConfig getNodeConfig()
    {
        return nodeConfig;
    }
    
    public List<NetEntity> getGwCandidates()
    {
        LOGGER.info("begin read candidate config to config map");
        try
        {
            CoreV1Api coreClient = getCoreApi();
            String configstr = "";
            List<NetEntity> candidate = null;
            if (coreClient != null)
            {
                V1ConfigMap agentconfigMap = coreClient.readNamespacedConfigMap(configGwCandiate, configMapNameSpace, "true");
                configstr = agentconfigMap.getData().get("data");
    
                LOGGER.info("load candiddate data from config map {}", configstr);
                candidate = yaml.loadAs(configstr, new ArrayList<>().getClass());
            }
            else
            {
                File file = new File(gwCandidatesPath);
                if (! file.exists())
                {
                    return null;
                }
                
                FileReader reader = new FileReader(file);
                try
                {
                    candidate = yaml.loadAs(reader, new ArrayList<>().getClass());
                    reader.close();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    if (reader != null)
                    {
                        reader.close();
                    }
                }
            }
            
            if (candidate != null)
            {
                return candidate;
            }
        }
        catch (ApiException e)
        {
            e.printStackTrace();
            LOGGER.error("read node config map error {}", e.getMessage());
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("read node config map error {}", e.getMessage());
        }
        return null;
    }
    
    void replaceGwCandidate(V1ConfigMap candidate)
    {
        try
        {
            CoreV1Api coreClient = getCoreApi();
            if (coreClient != null)
            {
                coreClient.replaceNamespacedConfigMap(configGwCandiate, configMapNameSpace, candidate, "true", null, null,null);
                LOGGER.info("replace candidate success");
            }
        }
        catch (ApiException e)
        {
            e.printStackTrace();
            LOGGER.error("save candidate error. code {} message {}", e.getCode(), e.getMessage());
        }
    }
    
    public void saveGwCandidate(List<NetEntity> gwAddrlist)
    {
        V1ConfigMap candidate = new V1ConfigMap();
    
        try
        {
            LOGGER.info("begin save candidate config to config map");
            CoreV1Api coreClient = getCoreApi();
            V1ObjectMeta v1ObjectMeta = new V1ObjectMeta();
            v1ObjectMeta.setName(configGwCandiate);
            v1ObjectMeta.setNamespace(configMapNameSpace);
            candidate.setMetadata(v1ObjectMeta);
            candidate.putDataItem("data", yaml.dumpAsMap(gwAddrlist));
            if (coreClient != null)
            {
                coreClient.createNamespacedConfigMap(configMapNameSpace, candidate, "true", null, null, null);
            }
            else
            {
                dumpGWCandidates(gwAddrlist);
            }
            
            LOGGER.info("save config map success");
        }
        catch (ApiException e)
        {
            LOGGER.error("save candidate error. code {} message {}", e.getCode(), e.getMessage());
            if (e.getCode() == 409)
            {
                replaceGwCandidate(candidate);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("save candidate error {}", e.getMessage());
        }
    }

    @Bean("nodeConfig")
    public NodeConfig createNodeConfig()
    {
        if (StringUtils.isNotBlank(GlobalArgsInfo.cmdArgs.getGateway()))
        {
            String [] gatewayParmas = StringUtils.split(GlobalArgsInfo.cmdArgs.getGateway(), ":");
            if (gatewayParmas.length == 2)
            {
                nodeConfig.setPeer_ip(gatewayParmas[0]);
                nodeConfig.setPeer_port(Integer.parseInt(gatewayParmas[1]));
            }
        }
        
        if (StringUtils.isNotBlank(GlobalArgsInfo.cmdArgs.getRegion()))
        {
            nodeConfig.setRegion_id(GlobalArgsInfo.cmdArgs.getRegion());
        }
        
    
        if (StringUtils.isNotBlank(GlobalArgsInfo.cmdArgs.getToken()))
        {
            nodeConfig.setToken(GlobalArgsInfo.cmdArgs.getToken());
        }
    
        if (StringUtils.isNotBlank(GlobalArgsInfo.cmdArgs.getWorkerType()))
        {
            nodeConfig.setWorkerType(GlobalArgsInfo.cmdArgs.getWorkerType());
        }
        else
        {
            nodeConfig.setWorkerType(workerType);
        }
        
        if (nodeConfig.getWorkerType().equals(WorkerType.K8S_AGENT))
        {
            loadClusterServerAddr();
            loadClusterSaToken();
        }
        
        dumpNodeConfig(nodeConfig);
        
        return nodeConfig;
    }
    
    void loadClusterServerAddr()
    {
        String serviceIp = System.getenv("KUBERNETES_PORT_443_TCP_ADDR");
        nodeConfig.setK8sServiceIP(serviceIp);
        String portStr = System.getenv("KUBERNETES_PORT_443_TCP_PORT");
        if (StringUtils.isNotBlank(portStr))
        {
            nodeConfig.setK8sServicePort(Integer.valueOf(portStr));
        }
        LOGGER.info("k8s server addr info {}:{}", serviceIp, portStr);
    }

    void loadClusterSaToken()
    {
        V1Secret v1Secret = null;
        try
        {
            LOGGER.info("load cluster sa token from file");
            File tokenFile = new File("/var/run/secrets/kubernetes.io/serviceaccount/token");
            if (tokenFile.exists())
            {
                BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(tokenFile),"UTF-8"));
                String tempStr;
                StringBuilder saToken = new StringBuilder();
                while((tempStr = reader.readLine()) != null){
                    saToken.append(tempStr);
                }
                nodeConfig.setSaToken(saToken.toString());
                reader.close();
            }
            else
            {
                v1Secret = getJoySecret();
                if (v1Secret != null)
                {
                    nodeConfig.setSaToken(new String(v1Secret.getData().get("token")));
                }
            }

            File caFile = new File("/var/run/secrets/kubernetes.io/serviceaccount/ca.crt");
            if (caFile.exists())
            {
                BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(caFile),"UTF-8"));
                String tempStr;
                StringBuilder kubeCa = new StringBuilder();
                while((tempStr = reader.readLine()) != null){
                    kubeCa.append(tempStr).append("\n");
                }
                nodeConfig.setKubeCa(kubeCa.toString());
                reader.close();
            }
            else
            {
                if (v1Secret == null) v1Secret = getJoySecret();
                if (v1Secret != null)
                {
                    nodeConfig.setKubeCa(new String(v1Secret.getData().get("ca.crt")));
                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
            LOGGER.info("load sa token error");
        }
    }

    private V1Secret getJoySecret()
    {
        V1Secret v1Secret = null;
        try
        {
            CoreV1Api coreClient = getCoreApi();
            if (coreClient != null)
            {
                V1ServiceAccount v1ServiceAccount = coreClient.readNamespacedServiceAccount("joy", "joy-system", "true");
                v1Secret = coreClient.readNamespacedSecret(v1ServiceAccount.getSecrets().get(0).getName(),"joy-system", "true");
            }

        }
        catch (ApiException ioException)
        {
            ioException.printStackTrace();
        }
        return v1Secret;
    }
}
