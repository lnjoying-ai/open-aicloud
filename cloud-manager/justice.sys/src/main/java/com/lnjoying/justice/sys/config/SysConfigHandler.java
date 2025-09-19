package com.lnjoying.justice.sys.config;

import com.lnjoying.justice.schema.entity.sys.NodeConfig;
import com.lnjoying.justice.sys.util.CipherUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.yaml.snakeyaml.Yaml;

import javax.annotation.PostConstruct;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @Description:
 * @Author: Regulus
 * @Date: 3/4/23 6:42 PM
 */
@Configuration
public class SysConfigHandler
{
    NodeConfig nodeConfig = null;
    private static Logger LOGGER = LogManager.getLogger();
    
    @PostConstruct
    void loadNodeConfigYaml()
    {
        String path = System.getProperty("lj_config") + "/" + "node_config.yaml";
        Yaml yaml = new Yaml();
        
        try
        {
            FileReader reader = new FileReader(path);
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
                FileWriter writer = new FileWriter(path);
                yaml.dump(nodeConfig, writer);
                writer.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
    
    @Bean("nodeConfig")
    public NodeConfig createNodeConfig()
    {
        return nodeConfig;
    }
}
