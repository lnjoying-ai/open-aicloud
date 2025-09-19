package com.lnjoying.justice.omc.domain.model;

import com.alibaba.druid.util.StringUtils;

import java.util.Arrays;
import java.util.Objects;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/11/10 16:46
 */

public enum AlertSendStatusEnum
{

    SUCCESS(1),

    FAILED(2),

    UNKNOWN(3);

    private final int value;


    AlertSendStatusEnum(int value)
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