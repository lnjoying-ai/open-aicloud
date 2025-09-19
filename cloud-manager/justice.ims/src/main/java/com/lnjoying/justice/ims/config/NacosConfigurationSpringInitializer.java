package com.lnjoying.justice.ims.config;

import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2022/6/1 13:24
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@Configuration
public class NacosConfigurationSpringInitializer extends PropertySourcesPlaceholderConfigurer implements EnvironmentAware
{

    public static final String NACOS_CONFIG_PROPERTIES = "globalNacosProperties";

    @Override
    public void setEnvironment(Environment environment)
    {
        super.setEnvironment(environment);
        syncNacosConfigToSpring(environment);
    }

    private void syncNacosConfigToSpring(Environment environment)
    {
        if (!(environment instanceof ConfigurableEnvironment)) {
            return;
        }

        MutablePropertySources propertySources = ((ConfigurableEnvironment) environment).getPropertySources();
        if (propertySources.contains(NACOS_CONFIG_PROPERTIES)) {
            return;
        }

        propertySources.addLast(new EnumerablePropertySource<NacosConfigurationSpringInitializer.NacosConfigLoader>(NACOS_CONFIG_PROPERTIES)
        {
            private final Map<String, Object> values = new HashMap<>();

            private final String[] propertyNames;

            {
                propertyNames = values.keySet().toArray(new String[values.size()]);
            }

            @Override
            public Object getProperty(String name)
            {
                Object value = this.values.get(name);

                // spring will not resolve nested placeholder of list, so try to fix the problem
                if (value instanceof List) {
                    value = ((List<Object>) value).stream()
                            .filter(item -> item instanceof String)
                            .map(item -> environment.resolvePlaceholders((String) item))
                            .collect(Collectors.toList());
                }
                return value;
            }

            @Override
            public String[] getPropertyNames()
            {
                return propertyNames;
            }
        });
    }

    private final class NacosConfigLoader
    {

    }
}
