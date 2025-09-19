package com.lnjoying.justice.commonweb.util;

import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.ConfigType;
import com.alibaba.nacos.api.exception.NacosException;
import com.lnjoying.justice.schema.service.sys.SysService;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2022/6/1 13:58
 */
@Slf4j
public class NacosConfigLoaderUtils
{
    private static final RestTemplate REST_TEMPLATE = new RestTemplateBuilder().build();

    private static final String SYS_REST_ADDRESS = "sys.rest.address";

    private static final String DEFAULT_SYS_REST_ADDRESS = "127.0.0.1:8090";

    public static void loadAndSyncNacosConfigToSpring()
    {

        try {
            SysService.NacosConfig nacosConfig = REST_TEMPLATE.postForObject("http://" + getSysRestAddr() + "/SysServiceImpl/getNacos", null, SysService.NacosConfig.class);
            if (Objects.nonNull(nacosConfig))
            {
                System.setProperty("nacos.config.server-addr", nacosConfig.getServerAddr());
                System.setProperty("nacos.config.username", nacosConfig.getUserName());
                System.setProperty("nacos.config.password", nacosConfig.getPassword());
                System.setProperty("nacos.config.configLongPollTimeout", Long.toString(nacosConfig.getTimeout()));
                System.setProperty("nacos.common.processors", "2");

            }
            log.info("nacos connect info: {}", nacosConfig);
        }
        catch (Exception e)
        {
            log.error("get nacos connect info failed: {}", e);
        }

    }

    public static boolean doPublish(ConfigService configService, String dataId, String group, String type)
    {
        try
        {
            String oldTemplate = configService.getConfig(dataId, group, 30000);
            if (StringUtils.isNotBlank(oldTemplate))
            {
                return true;
            }

            String template = FileUtils.getNacosContentFromNacosConfigPathResource(dataId + "." + type);
            boolean published = configService.publishConfig(dataId, group, template, type);
            if (published)
            {
                if (log.isDebugEnabled())
                {
                    log.info("publish config succeed, dataId: {}, group: {}, content: {}", dataId, group, template);
                }
                else
                {
                    log.info("publish config succeed, dataId: {}, group: {}", dataId, group);
                }

            }
            else
            {
                if (log.isDebugEnabled())
                {
                    log.error("publish config failed, dataId: {}, group: {}, content: {}", dataId, group, template);
                }
                else
                {
                    log.error("publish config failed, dataId: {}, group: {}", dataId, group);
                }
                return false;
            }
        }
        catch (NacosException e)
        {
            log.error("publish config error: {}", e);
            return false;
        }

        return true;
    }



    private static String getSysRestAddr()
    {
        String sysRestAddress = getConfig("SYS_REST_ADDRESS");
        if (StringUtils.isBlank(sysRestAddress))
        {
            return DEFAULT_SYS_REST_ADDRESS;
        }

        return sysRestAddress;
    }

    private static String getConfig(String name) {
        // env
        String val = System.getenv(name);
        if (StringUtils.isNotEmpty(val)) {
            return val;
        }
        // properties
        val = System.getProperty(name);
        if (StringUtils.isNotEmpty(val)) {
            return val;
        }
        return "";
    }
}
