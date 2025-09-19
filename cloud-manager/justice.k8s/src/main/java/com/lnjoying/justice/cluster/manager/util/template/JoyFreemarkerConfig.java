package com.lnjoying.justice.cluster.manager.util.template;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.IOException;
import java.util.Locale;

@Component
public class JoyFreemarkerConfig
{
    private static Logger LOGGER = LogManager.getLogger();
    
    public JoyFreemarkerConfig()
    {
    }
    
    @Autowired
    private  RedisTemplateLoader loader;
    
    @Bean(name="freeMarkerConfig")
    public freemarker.template.Configuration freeMarkerConfigurer() throws IOException, TemplateException
    {
        LOGGER.info("Custom Freemarker configurer pre.");
        FreeMarkerConfigurer freeMarkerConfigurer = new FreeMarkerConfigurer();
        freeMarkerConfigurer.setPreTemplateLoaders(loader);
        
        // Default encoding of the template files
        freeMarkerConfigurer.setDefaultEncoding("UTF-8");
        Configuration configuration = freeMarkerConfigurer.createConfiguration();
        configuration.setLocale(Locale.ROOT);
        return configuration;
    }
}