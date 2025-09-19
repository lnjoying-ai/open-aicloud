package com.lnjoying.justice.omc.domain.model;

import com.alibaba.druid.util.StringUtils;

import java.util.Arrays;
import java.util.Objects;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/10/25 15:39
 */

public enum AlertLevelEnum
{
    INFO(1),

    WARNING(2),

    CRITICAL(3);


    private final int value;


    AlertLevelEnum(int value)
    {
        this.value = value;
    }

    public int value()
    {
        return this.value;
    }

    public static AlertLevelEnum fromValue(Integer code)
    {
        return Arrays.stream(AlertLevelEnum.values()).filter(x -> Objects.equals(x.value(), code)).findFirst().orElse(null);
    }

    public static AlertLevelEnum fromName(String name)
    {
        return Arrays.stream(AlertLevelEnum.values()).filter(x -> StringUtils.equalsIgnoreCase(x.name(), name)).findFirst().orElse(CRITICAL);
    }
}
