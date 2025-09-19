package com.lnjoying.justice.iam.config;


import cn.binarywang.wx.miniapp.api.WxMaService;
import com.alibaba.nacos.api.annotation.NacosInjected;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.ConfigType;
import com.alibaba.nacos.api.config.annotation.NacosConfigListener;
import com.alibaba.nacos.api.exception.NacosException;
import com.binarywang.spring.starter.wxjava.miniapp.properties.WxMaProperties;
import com.lnjoying.justice.commonweb.util.ApplicationUtils;
import com.lnjoying.justice.iam.config.wx.LettuceWxRedisOps;
import com.lnjoying.justice.iam.config.wx.WxMaInLettuceConfigStorageConfiguration;
import com.lnjoying.justice.iam.config.wx.WxMaLettuceConfigImpl;
import com.lnjoying.justice.iam.handler.CommonPropertiesConverter;
import com.lnjoying.justice.iam.handler.WxMaPropertiesConverter;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.redis.WxRedisOps;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.lang.reflect.Field;
import java.util.Arrays;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2022/6/7 14:20
 */

@Slf4j
@Configuration
@DependsOn("nacosConfiguration")
public class NacosListener  implements ApplicationListener<ContextRefreshedEvent>
{

    @Autowired
    private WxMaService wxMaService;

    @NacosInjected
    private ConfigService configService;

    @Autowired
    private WxMaInLettuceConfigStorageConfiguration storageConfig;

    @NacosConfigListener(dataId = "com.justice.ums.wx.config", groupId = "ums", type = ConfigType.YAML, converter = WxMaPropertiesConverter.class)
    public void onChange(WxMaProperties wxMaProperties) throws Exception
    {
        changeWxConfig(wxMaProperties);

    }

    @NacosConfigListener(dataId = "com.justice.iam.common.config", groupId = "iam", type = ConfigType.YAML, converter = CommonPropertiesConverter.class)
    public void onCommonPropertiesChange(CommonProperties commonProperties) throws Exception
    {
        changeCorsConfig(commonProperties);

    }



    @Override
    public void onApplicationEvent(ContextRefreshedEvent event)
    {
        try
        {
            String wxConfig = configService.getConfig("com.justice.ums.wx.config", "ums", 30000);
            WxMaProperties wxMaProperties = new WxMaPropertiesConverter().convert(wxConfig);
            changeWxConfig(wxMaProperties);
        }
        catch (NacosException e)
        {
            log.error("wx ma properties update error on context refresh:{}", e);
        }
    }


    private void changeCorsConfig(CommonProperties commonProperties)
    {
        try
        {
            String corsAllowOrigins = commonProperties.getCorsAllowOrigins();
            if (StringUtils.isBlank(corsAllowOrigins))
            {
                corsAllowOrigins = "*";
            }

            CorsFilter corsFilter = ApplicationUtils.getBean(CorsFilter.class);
            Field nameField = ReflectionUtils.findField(corsFilter.getClass(), "configSource");
            nameField.setAccessible(true);

            CorsConfiguration corsConfiguration = new CorsConfiguration();
            corsConfiguration.setAllowCredentials(true);
            corsConfiguration.setAllowedOrigins(Arrays.asList(corsAllowOrigins.split(",")));
            corsConfiguration.addAllowedHeader("*");
            corsConfiguration.addAllowedMethod("*");
            UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
            urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
            ReflectionUtils.setField(nameField, corsFilter, urlBasedCorsConfigurationSource);
        }
        catch (Exception e)
        {
            log.error("common  properties update error:{}", e);
        }
    }

    private void changeWxConfig(WxMaProperties wxMaProperties)
    {
        try
        {
            String appId = wxMaProperties.getAppid();
            WxRedisOps redisOps = new LettuceWxRedisOps();
            WxMaLettuceConfigImpl wxMaLettuceConfig = new WxMaLettuceConfigImpl(redisOps, wxMaProperties.getConfigStorage().getKeyPrefix());
            wxMaLettuceConfig.setAppid(appId);
            wxMaLettuceConfig.setSecret(wxMaProperties.getSecret());
            wxMaService.addConfig(appId, wxMaLettuceConfig);
            wxMaService.switchover(appId);
            wxMaService.removeConfig("xxxx");
        }
        catch (Exception e)
        {
            log.error("wx ma properties update error:{}", e);
        }
    }
}
