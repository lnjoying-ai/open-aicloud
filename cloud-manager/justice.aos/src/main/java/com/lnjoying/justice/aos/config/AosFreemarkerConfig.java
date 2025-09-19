package com.lnjoying.justice.aos.config;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.TemplateException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.IOException;
import java.util.Locale;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2022/1/13 16:57
 */
@Configuration
public class AosFreemarkerConfig
{
    private static Logger LOGGER = LogManager.getLogger();

    private static final StringTemplateLoader STRING_TEMPLATE_LOADER = new StringTemplateLoader();

    @Bean(name="freeMarkerConfig")
    public freemarker.template.Configuration freeMarkerConfigurer() throws IOException, TemplateException
    {
        LOGGER.info("Custom Freemarker configurer pre.");
        FreeMarkerConfigurer freeMarkerConfigurer = new FreeMarkerConfigurer();
        freeMarkerConfigurer.setPreTemplateLoaders(STRING_TEMPLATE_LOADER);
        // Default encoding of the template files
        freeMarkerConfigurer.setDefaultEncoding("UTF-8");
        freemarker.template.Configuration configuration = freeMarkerConfigurer.createConfiguration();
        configuration.setLocale(Locale.ROOT);
        return configuration;
    }
}
