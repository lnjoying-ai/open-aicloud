package com.lnjoying.justice.commonweb.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import static com.lnjoying.justice.commonweb.util.FileUtils.FILE_SEPARATOR;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2022/7/2 15:18
 */

@Slf4j
public class BaseConfigPathUtils
{
    private static  String baseConfigPath;

    static {
        baseConfigPath = System.getProperty("lj_config");
        if (StringUtils.isNotBlank(baseConfigPath))
        {
            if (!baseConfigPath.endsWith("/"))
            {
                baseConfigPath = baseConfigPath + "/";
            }
        }
        else
        {
            log.error("need set base config to env");
        }
    }

    public static String getBaseConfigPath()
    {
        return baseConfigPath;
    }

    public static String getNacosConfigPath()
    {
        return getBaseConfigPath() + "nacos" + FILE_SEPARATOR;
    }

    public static String getRegoConfigPath()
    {
        return getBaseConfigPath() + "rego" + FILE_SEPARATOR;
    }
}
