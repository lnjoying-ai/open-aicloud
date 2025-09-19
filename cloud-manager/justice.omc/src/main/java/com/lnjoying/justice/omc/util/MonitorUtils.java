package com.lnjoying.justice.omc.util;

import com.lnjoying.justice.omc.config.OmcConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/12/11 10:16
 */

@Slf4j
@Component
public class MonitorUtils
{

    private static final String MONITOR_ADMIN_USER = "MONITOR_ADMIN_USER";

    private static final String MONITOR_ADMIN_PASSWORD = "MONITOR_ADMIN_PASSWORD";

    private static OmcConfig omcConfig;


    public static String getMonitorAdminUser()
    {
        String monitorAdminUsername = omcConfig.getMonitorAdminUsername();
        return setValueInTurn(monitorAdminUsername, MONITOR_ADMIN_USER);

    }


    public static String getMonitorAdminPassword()
    {
        String monitorAdminPassword = omcConfig.getMonitorAdminPassword();
        return setValueInTurn(monitorAdminPassword, MONITOR_ADMIN_PASSWORD);
    }


    public static String setValueInTurn(String defaultValue, String envName)
    {
        if (StringUtils.hasText(defaultValue))
        {
            return defaultValue;
        }

        // env
        String val = System.getenv(envName);
        if (StringUtils.hasText(val)) {
            return val;
        }
        // properties
        val = System.getProperty(envName);
        if (StringUtils.hasText(val)) {
            return val;
        }
        return "";
    }



    public void setOmcConfig(OmcConfig omcConfig)
    {
        MonitorUtils.omcConfig = omcConfig;
    }
}
