package com.lnjoying.justice.commonweb.swagger;

import org.apache.servicecomb.registry.RegistrationManager;

import java.util.Map;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/3/24 10:18
 */

public final class ScbSchemaUtils
{

    public static Map<String, String> getSchemaMap()
    {
        return RegistrationManager.INSTANCE.getMicroservice().getSchemaMap();
    }

    public static String getMicrosoftServiceName(){
        return RegistrationManager.INSTANCE.getMicroservice().getServiceName();
    }
}
