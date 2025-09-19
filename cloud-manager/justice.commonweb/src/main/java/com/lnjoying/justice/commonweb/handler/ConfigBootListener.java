package com.lnjoying.justice.commonweb.handler;

import com.alibaba.nacos.api.annotation.NacosInjected;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.ConfigType;
import com.alibaba.nacos.api.config.annotation.NacosConfigurationProperties;
import com.alibaba.nacos.api.exception.NacosException;
import com.lnjoying.justice.commonweb.util.ApplicationUtils;
import com.lnjoying.justice.commonweb.util.FileUtils;
import com.lnjoying.justice.commonweb.util.NacosConfigLoaderUtils;
import com.lnjoying.justice.commonweb.util.YamlParserUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.servicecomb.core.BootListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Map;

import static com.alibaba.nacos.spring.util.NacosBeanUtils.CONFIG_GLOBAL_NACOS_PROPERTIES_BEAN_NAME;


/**
 * @Description: publish config to nacos
 * @Author: Merak
 * @Date: 2022/5/28 12:20
 */

@Slf4j
//@ConditionalOnBean(name = CONFIG_GLOBAL_NACOS_PROPERTIES_BEAN_NAME)
@Component
public class ConfigBootListener implements ApplicationListener<ContextRefreshedEvent>
{

    @NacosInjected
    @Lazy
    private ConfigService configService;


    @Override
    public void onApplicationEvent(ContextRefreshedEvent event)
    {
        loadAndPublish();
    }

    public void loadAndPublish()
    {
        try
        {
            Map<String, Object> config = ApplicationUtils.getBeansWithAnnotation(NacosConfigurationProperties.class);
            if (!CollectionUtils.isEmpty(config))
            {
                config.entrySet().stream().forEach(entry -> {
                    NacosConfigurationProperties ncp = ApplicationUtils.findAnnotationOnBean(entry.getKey(), NacosConfigurationProperties.class);
                    String dataId = ncp.dataId();
                    String group = ncp.groupId();
                    if (!checkIfPublished(dataId, group))
                    {
                        //String content = YamlParserUtil.dumpObject(entry.getValue());
                        String content = FileUtils.getNacosContentFromNacosConfigPathResource(dataId + ".yaml");
                        publishConfig(dataId, group, content);
                    }
                });
            }
        }
        catch (Exception e)
        {
            log.error("loadAndPublish error: {}", e);
        }

    }

    private boolean checkIfPublished(String dataId, String group)
    {
        try
        {
            String config = configService.getConfig(dataId, group, 300000);
            if (StringUtils.isNotBlank(config))
            {
                return true;
            }
        }
        catch (NacosException e)
        {
            log.info("get config error: {}", e);
        }

        return false;
    }

    private void publishConfig(String dataId, String group, String content)
    {
        try
        {
            boolean published = configService.publishConfig(dataId, group, content, ConfigType.YAML.getType());
            if (published)
            {
                log.info("publish config succeed, dataId: {}, group: {}, content: {}", dataId, group, content);
            }
            else
            {
                log.error("publish config failed, dataId: {}, group: {}, content: {}", dataId, group, content);
            }
        }
        catch (NacosException e)
        {
            log.error("publish config to nacos error: {}", e);
        }
    }
}
