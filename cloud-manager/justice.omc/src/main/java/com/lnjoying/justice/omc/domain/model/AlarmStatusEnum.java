package com.lnjoying.justice.omc.domain.model;

import java.util.Arrays;
import java.util.Objects;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/11/2 19:32
 */

public enum AlarmStatusEnum
{

    /**
     * normal
     */
    NORMAL(1),

    /**
     * deleted
     */
    DELETED(-1),

    ACTIVE(2),

    DEACTIVE(3);


    private final int value;


    AlarmStatusEnum(int value)
    {
        this.value = value;
    }

    public int value()
    {
        return this.value;
    }

    public static AlarmStatusEnum fromValue(Integer code)
    {
        return Arrays.stream(AlarmStatusEnum.values()).filter(x -> Objects.equals(x.value(), code)).findFirst().orElse(null);
    }
}
