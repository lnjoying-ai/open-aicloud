package com.lnjoying.justice.omc.domain.model.stat;

import com.lnjoying.justice.commonweb.exception.WebSystemException;
import com.lnjoying.justice.schema.common.ErrorLevel;

import java.util.Arrays;

import static com.lnjoying.justice.schema.common.ErrorCode.AUTH_METHOD_NOT_CORRECT;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2024/1/18 19:37
 */

public enum MetricType
{
    CPU("cpu"),

    MEM("mem"),

    DISK_READ("disk_read"),

    DISK_WRITE("disk_write"),

    NETWORK_IN("network_in"),

    NETWORK_OUT("network_out");

    public String getName()
    {
        return name;
    }

    private String name;

    MetricType(String name)
    {
        this.name = name;
    }

    public static MetricType fromName(String name)
    {
        return Arrays.stream(MetricType.values()).filter(x -> x.name().equalsIgnoreCase(name)).findFirst().orElse(CPU);
    }
}
