package com.lnjoying.justice.aos.helm.util;

import org.apache.commons.lang3.StringUtils;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2022/7/29 19:27
 */

public class EnvUtils
{

    public static String getConfig(String name) {
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
