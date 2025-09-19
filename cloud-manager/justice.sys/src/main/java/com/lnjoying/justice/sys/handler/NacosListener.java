package com.lnjoying.justice.sys.handler;

import com.alibaba.nacos.api.annotation.NacosInjected;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.ConfigType;
import com.alibaba.nacos.api.config.annotation.NacosConfigListener;
import com.lnjoying.justice.commonweb.util.NacosConfigLoaderUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2022/6/1 11:20
 */

@Slf4j
@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
public class NacosListener
{

    private volatile boolean published = false;

    @NacosInjected
    private ConfigService configService;

    @PostConstruct
    public void publishConfig() {
        try
        {
            boolean isSuccess = true;
            isSuccess = isSuccess && (NacosConfigLoaderUtils.doPublish(configService, "com.justice.db.config", "db", ConfigType.PROPERTIES.getType()));
            isSuccess = isSuccess && NacosConfigLoaderUtils.doPublish(configService,"com.justice.redis.config", "redis", ConfigType.PROPERTIES.getType());
            isSuccess = isSuccess && NacosConfigLoaderUtils.doPublish(configService,"com.justice.api.config.notify", "api", ConfigType.YAML.getType());
            isSuccess = isSuccess && NacosConfigLoaderUtils.doPublish(configService,"com.justice.ums.wx.config", "ums", ConfigType.YAML.getType());
            isSuccess = isSuccess && NacosConfigLoaderUtils.doPublish(configService,"com.justice.iam.opa.config", "iam", ConfigType.YAML.getType());
            isSuccess = isSuccess && NacosConfigLoaderUtils.doPublish(configService,"com.justice.iam.common.config", "iam", ConfigType.YAML.getType());
            //isSuccess = isSuccess && NacosConfigLoaderUtils.doPublish(configService,"com.justice.lic", "sys", ConfigType.JSON.getType());
            if (isSuccess)
            {
                published = true;
            }

        }
        catch (Exception e)
        {
            log.error("publish config error", e);
        }

    }

    public boolean isPublished()
    {
        return published;
    }

}
