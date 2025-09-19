package com.lnjoying.justice.omc.domain.model;

import java.util.Arrays;
import java.util.Objects;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/11/10 16:46
 */

public enum NotifyChannelEnum
{

    EMAIL(0),

    SMS(1);

    private final int value;


    NotifyChannelEnum(int value)
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


}