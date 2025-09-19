package com.lnjoying.justice.omc.domain.model;

import java.util.Arrays;
import java.util.Objects;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/11/2 19:32
 */

public enum AlertSilenceTypeEnum
{

    ONCE(0),

    DAY(1),

    WEEK(2);


    private final int value;


    AlertSilenceTypeEnum(int value)
    {
        this.value = value;
    }

    public int value()
    {
        return this.value;
    }

    public static AlertSilenceTypeEnum fromValue(Integer code)
    {
        return Arrays.stream(AlertSilenceTypeEnum.values()).filter(x -> Objects.equals(x.value(), code)).findFirst().orElse(ONCE);
    }
}
