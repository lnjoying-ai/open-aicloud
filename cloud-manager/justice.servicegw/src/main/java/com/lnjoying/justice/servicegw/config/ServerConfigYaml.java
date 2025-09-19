package com.lnjoying.justice.servicegw.config;

import com.lnjoying.justice.commonweb.util.CollectionUtils;
import com.micro.core.common.Utils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.yaml.snakeyaml.Yaml;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: Regulus
 * @Date: 2/28/22 5:41 PM
 */
@Configuration
public class ServerConfigYaml
{
    private static Logger LOGGER = LogManager.getLogger();
    
    private ServerConfigBean serverConfigBean;
    private Map<String, Map<String,List<String>>> proxyFwdBean;
    
    @PostConstruct
    void loadConfig()
    {
        loadServerConfig();
        loadProxyConfig();
    }
    
    void loadServerConfig()
    {
        LOGGER.info("load sevice  gateway cert info");
        String baseConfigPath = System.getProperty("lj_config");
        String svrConfigPath = Utils.buildStr(baseConfigPath, "/cluster_server.yaml");
        File svrConfigFile = new File(svrConfigPath);
        if (! svrConfigFile.exists())
        {
            return;
        }
        
        Yaml yaml = new Yaml();
        FileReader fileReader;
        try
        {
            fileReader = new FileReader(svrConfigPath);
    
            serverConfigBean = yaml.load(fileReader);
            
            fileReader.close();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    
    void loadProxyConfig()
    {
        LOGGER.info("load cluster proxy config info");
        String baseConfigPath = System.getProperty("lj_config");
        String proxyFwd = Utils.buildStr(baseConfigPath, "/proxy_tunnel.yaml");
        File fwdConfig = new File(proxyFwd);
        if (! fwdConfig.exists())
        {
            return;
        }
    
        Yaml yaml = new Yaml();
        FileReader fileReader;
        try
        {
            fileReader = new FileReader(fwdConfig);
    
            proxyFwdBean = yaml.load(fileReader);
        
            fileReader.close();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    
    @Bean(name="serverConfigBean")
    public ServerConfigBean createConfigBean()
    {
        if (serverConfigBean == null)
        {
            serverConfigBean = new ServerConfigBean();
        }
        
        String svrPortStr = System.getenv("CLUSTER_SERVER_PORT");
        if (CollectionUtils.hasContent(svrPortStr))
        {
            serverConfigBean.setSvrPort(Integer.parseInt(svrPortStr));
        }
        
        return serverConfigBean;
    }
    
    @Bean(name="proxyFwdBean")
    public Map<String, Map<String,List<String>>> createFwdConfigBean()
    {
        return proxyFwdBean;
    }
}
