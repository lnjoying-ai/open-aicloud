package com.lnjoying.justice.omc.domain.model;

import java.util.Arrays;
import java.util.Objects;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/11/10 16:46
 */

public enum PrometheusTypeEnum
{

    SERVER(0),

    AGENT(1);

    private final int value;


    PrometheusTypeEnum(int value)
    {
        this.value = value;
    }

    public int value()
    {
        return this.value;
    }

    public static PrometheusTypeEnum fromValue(Integer code)
    {
        return Arrays.stream(PrometheusTypeEnum.values()).filter(x -> Objects.equals(x.value(), code)).findFirst().orElse(null);
    }


}