package com.lnjoying.justice.api.config;

import com.alibaba.nacos.api.annotation.NacosInjected;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import com.lnjoying.justice.api.rpcserviceimpl.CombRpcService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;

import javax.annotation.PostConstruct;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

@Component
@Slf4j
public class ApiConfigYaml
{
    private static Logger LOGGER = LogManager.getLogger();
    
    ApiConfig apiConfig = null;
    ContactAdmin contactAdmin = null;

    @Autowired
    CombRpcService combRpcService;
    
    @NacosInjected
    private ConfigService configService;

    @PostConstruct
    void createServiceParamBean()
    {
        loadContactYaml();
    }


    @Bean
    ApiConfig apiConfig()
    {
        return new ApiConfig();
    }

    void loadApiYaml()
    {
        String path = System.getProperty("lj_config") + "/" + "api.yaml";
        Yaml yaml = new Yaml();
        try
        {
            String api = configService.getConfig("com.justice.api.config.notify.template", "api", 30000);
            if (StringUtils.isNotBlank(api))
            {
                apiConfig = yaml.loadAs(api, ApiConfig.class);
            }
        }
        catch (NacosException e)
        {
           log.error("get notify template from nacos failed: {}", e);
        }
    }

    void loadContactYaml()
    {
        String path = System.getProperty("lj_config") + "/" + "contact.yaml";
        Yaml yaml = new Yaml();

        try
        {
            FileReader reader = new FileReader(path);
            contactAdmin = yaml.loadAs(reader, ContactAdmin.class);
            reader.close();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e) {
			e.printStackTrace();
		}
	}

    @Bean("apiConfig")
    public ApiConfig createApiConfig()
    {
        //return apiConfig;
        return new ApiConfig();
    }

    @Bean("contactAdmin")
    public ContactAdmin createContactAdminConfig()
    {
        return contactAdmin;
    }
}
