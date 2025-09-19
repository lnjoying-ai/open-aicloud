package com.lnjoying.justice.iam.handler;

import com.alibaba.nacos.api.config.convert.NacosConfigConverter;
import com.binarywang.spring.starter.wxjava.miniapp.enums.StorageType;
import com.binarywang.spring.starter.wxjava.miniapp.properties.WxMaProperties;
import com.lnjoying.justice.commonweb.util.ApplicationUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.yaml.snakeyaml.Yaml;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2022/7/12 19:42
 */

@Slf4j
public class WxMaPropertiesConverter implements NacosConfigConverter<WxMaProperties>
{

    @Override
    public boolean canConvert(Class<WxMaProperties> targetType)
    {
        return WxMaProperties.class.equals(targetType);
    }

    @Override
    public WxMaProperties convert(String config)
    {
        return wxMaProperties(config);
    }


    private WxMaProperties wxMaProperties(String config)
    {
        if (StringUtils.isNotBlank(config))
        {
            try
            {
                Yaml yaml = new Yaml();
                Map<String, Object> load = yaml.loadAs(config, Map.class);
                Map<String, Object> wx = (LinkedHashMap<String, Object>)load.get("wx");
                Map<String, Object> miniapp = (LinkedHashMap<String, Object>)wx.get("miniapp");
                String appId = (String)miniapp.get("appid");
                String secret = (String)miniapp.get("secret");
                Map<String, Object> configStorage = (LinkedHashMap<String, Object>)miniapp.get("config-storage");
                String type = (String) configStorage.get("type");
                WxMaProperties wxMaProperties = ApplicationUtils.getBean(WxMaProperties.class);
                wxMaProperties.setAppid(appId);
                wxMaProperties.setSecret(secret);
                wxMaProperties.getConfigStorage().setType(StorageType.valueOf(type));
                return wxMaProperties;
            }
            catch (Exception e)
            {
                log.error("parse wx ma properties failed:{}", e);
            }

        }
       return null;
    }
}
