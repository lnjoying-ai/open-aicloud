package com.lnjoying.justice.omc.domain.model;

import java.util.Arrays;
import java.util.Objects;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/10/25 15:39
 */

public enum AlertTypeEnum
{
    EVENT(1, "事件"),

    MONITOR(2, "监控");


    private final int value;

    private final String name;

    AlertTypeEnum(int value, String name)
    {
        this.value = value;
        this.name = name;
    }

    public int value()
    {
        return this.value;
    }

    public static AlertTypeEnum fromValue(Integer code)
    {
        return Arrays.stream(AlertTypeEnum.values()).filter(x -> Objects.equals(x.value(), code)).findFirst().orElse(null);
    }
}
