package com.lnjoying.justice.commonweb.util;

import org.apache.commons.lang3.StringUtils;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2024/1/3 10:42
 */

public class SystemUtils
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
