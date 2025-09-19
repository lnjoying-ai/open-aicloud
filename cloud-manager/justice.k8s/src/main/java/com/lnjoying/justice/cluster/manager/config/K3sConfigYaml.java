package com.lnjoying.justice.cluster.manager.config;

import com.lnjoying.justice.cluster.manager.util.YamlConfigUtils;
import com.lnjoying.justice.commonweb.util.YamlParserUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2022/8/12 16:16
 */

@Configuration
public class K3sConfigYaml
{
    private static Logger LOGGER = LogManager.getLogger();

    private static final String SERVICES_OPTIONS_PATH = "k3s/k3s_version_services_options.yaml";

    private static final String ADDON_TEMPLATES_INDEX_PATH = "/k3s/k3s_version_addon_templates_index.yaml";

    private static final String JKE_SYSTEM_IMAGES_PATH = "k3s/k3s_version_jke_system_images.yaml";

    private static final String BASIC_CONFIG_PATH = "k3s/k3s_version_basic_config.yaml";

    private Map<String,Map<String,Map<String,String>>> k3sServiceOptions;

    private Map<String,Map<String,String>>        k3sAddonTemplatesIndex;
    private Map<String,Map<String,String>>            k3sJkeSystemImages;

    private K3sConfigBean.K3sBasicConfig k3sBasicConfig;

    @PostConstruct
    void loadK3sTemplateInfo()
    {
        LOGGER.info("load k3s template info");
        k3sServiceOptions = YamlConfigUtils.readYamlConfig(SERVICES_OPTIONS_PATH, Map.class);
        k3sJkeSystemImages = YamlConfigUtils.readYamlConfig(JKE_SYSTEM_IMAGES_PATH, Map.class);
        k3sAddonTemplatesIndex = YamlConfigUtils.readYamlConfig(ADDON_TEMPLATES_INDEX_PATH, Map.class);
        k3sBasicConfig = YamlConfigUtils.readYamlConfig(BASIC_CONFIG_PATH, K3sConfigBean.K3sBasicConfig.class);
    }

    @Bean(name="k3sConfigBean")
    public K3sConfigBean k3sConfigBean()
    {
        K3sConfigBean k3sConfigBean = new K3sConfigBean();
        k3sConfigBean.setK3sBasicConfig(k3sBasicConfig);
        k3sConfigBean.setK3sAddonTemplatesIndex(k3sAddonTemplatesIndex);
        k3sConfigBean.setK3sJkeSystemImages(k3sJkeSystemImages);
        k3sConfigBean.setK3sServiceOptions(k3sServiceOptions);
        return k3sConfigBean;
    }

}
