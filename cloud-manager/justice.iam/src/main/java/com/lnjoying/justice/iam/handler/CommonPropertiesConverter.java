package com.lnjoying.justice.iam.handler;

import com.alibaba.nacos.api.config.convert.NacosConfigConverter;
import com.binarywang.spring.starter.wxjava.miniapp.enums.StorageType;
import com.binarywang.spring.starter.wxjava.miniapp.properties.WxMaProperties;
import com.lnjoying.justice.commonweb.util.ApplicationUtils;
import com.lnjoying.justice.iam.config.CommonProperties;
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
public class CommonPropertiesConverter implements NacosConfigConverter<CommonProperties>
{

    @Override
    public boolean canConvert(Class<CommonProperties> targetType)
    {
        return CommonProperties.class.equals(targetType);
    }

    @Override
    public CommonProperties convert(String config)
    {
        return commonProperties(config);
    }


    private CommonProperties commonProperties(String config)
    {
        if (StringUtils.isNotBlank(config))
        {
            try
            {
                Yaml yaml = new Yaml();
                Map<String, Object> load = yaml.loadAs(config, Map.class);
                String  corsAllowOrigins = (String)load.get("cors-allow-origins");
                CommonProperties commonProperties = ApplicationUtils.getBean(CommonProperties.class);
                commonProperties.setCorsAllowOrigins(corsAllowOrigins);
                return commonProperties;
            }
            catch (Exception e)
            {
                log.error("parse wx ma properties failed:{}", e);
            }

        }
       return null;
    }
}
