package com.lnjoying.justice.cluster.manager.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.lnjoying.justice.cluster.manager.util.template.RedisTemplateLoader;
import com.micro.core.common.Utils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.yaml.snakeyaml.Yaml;

import javax.annotation.PostConstruct;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

@Configuration
public class K8sConfigYaml
{
    private static Logger LOGGER = LogManager.getLogger();
    
    private Map<String,Map<String,Map<String,String>>> k8sServiceOptions;
    
    private Map<String,Map<String,String>>        k8sAddonTemplatesIndex;
    private Map<String,Map<String,String>>            k8sJkeSystemImages;
    
    private K8sBasicConfig k8sBasicConfig;
    
    @PostConstruct
    void loadK8sTemplateInfo()
    {
        LOGGER.info("load k8s template info");
        String bastConfigPath = System.getProperty("lj_config");
        String serviceOptinPath = Utils.buildStr(bastConfigPath, "/k8s/k8s_version_services_options.yaml");
    
        Yaml yaml = new Yaml();
        FileReader fileReader;
        try
        {
            fileReader = new FileReader(serviceOptinPath);
    
            k8sServiceOptions = yaml.load(fileReader);
            
            String servieImagePath = Utils.buildStr(bastConfigPath, "/k8s/k8s_version_jke_system_images.yaml");
    
            fileReader = new FileReader(servieImagePath);
            k8sJkeSystemImages = yaml.load(fileReader);
    
            String addonTemplatePath = Utils.buildStr(bastConfigPath, "/k8s/k8s_version_addon_templates_index.yaml");
            fileReader = new FileReader(addonTemplatePath);
            k8sAddonTemplatesIndex = yaml.load(fileReader);
    
            String basicConfig = Utils.buildStr(bastConfigPath, "/k8s/k8s_version_basic_config.yaml");
            fileReader = new FileReader(basicConfig);
            k8sBasicConfig = yaml.loadAs(fileReader, K8sBasicConfig.class);
        
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
    
    @Bean(name="k8sConfigBean")
    public K8sConfigBean createConfigBean()
    {
        K8sConfigBean k8sConfigBean = new K8sConfigBean();
        k8sConfigBean.setK8sAddonTemplatesIndex(k8sAddonTemplatesIndex);
        k8sConfigBean.setK8sJkeSystemImages(k8sJkeSystemImages);
        k8sConfigBean.setK8sServiceOptions(k8sServiceOptions);
        k8sConfigBean.setK8sBasicConfig(k8sBasicConfig);
        return k8sConfigBean;
    }
}
