package com.lnjoying.justice.api.handler;

import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertySourceFactory;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Properties;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2022/6/1 11:07
 */

public class ApiYamlPropertySourceFactory implements PropertySourceFactory
{
    @Override
    public PropertySource<?> createPropertySource(String name, EncodedResource encodedResource)
            throws IOException
    {
        if (encodedResource.getResource().getFilename().equals("com.justice.api.config.notify.template.yaml"))
        {
            //ApiConfig apiConfig = YamlParserUtil.readYamlConfig(encodedResource.getResource().getFilename(), ApiConfig.class);
            ResourceUtils.getFile(encodedResource.getResource().getFilename());
            File resource = new ClassPathResource(
                    encodedResource.getResource().getFilename()).getFile();
            String template = new String(
                    Files.readAllBytes(resource.toPath()));

            Properties properties = new Properties();
            properties.setProperty("notifyTemplate", template);
            return new PropertiesPropertySource(encodedResource.getResource().getFilename(), properties);
        }

        Properties properties = new Properties();

        return new PropertiesPropertySource(encodedResource.getResource().getFilename(), properties);
    }
}
