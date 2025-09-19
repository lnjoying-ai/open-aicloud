package com.lnjoying.justice.omc.domain.model;

import com.alibaba.druid.util.StringUtils;

import java.util.Arrays;
import java.util.Objects;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/10/25 15:39
 */

public enum AlertStatusEnum
{
    FIRING(1),

    RESOLVED(2),

    PENDING(3),

    INHIBITED(4),

    SILENCED(5),

    EXPIRED(6),

    UNKNOWN(7);



    private final int value;


    AlertStatusEnum(int value)
    {
        this.value = value;
    }

    public int value()
    {
        return this.value;
    }

    public static AlertStatusEnum fromValue(Integer code)
    {
        return Arrays.stream(AlertStatusEnum.values()).filter(x -> Objects.equals(x.value(), code)).findFirst().orElse(null);
    }

    public static AlertStatusEnum fromName(String name)
    {
        return Arrays.stream(AlertStatusEnum.values()).filter(x -> StringUtils.equalsIgnoreCase(x.name(), name)).findFirst().orElse(UNKNOWN);
    }
}
