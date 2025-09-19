package com.lnjoying.justice.omc.domain.model;

import java.util.Arrays;
import java.util.Objects;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/10/25 15:37
 */

public enum IntegrationTaskStatusEnum
{
    PENDING(1, "pending"),

    RUNNING(2, "running"),

    SUCCEEDED(3, "succeeded"),

    FAILED(4, "failed"),

    PROGRESSING(5, "progressing");

    private final int value;

    private final String name;

    IntegrationTaskStatusEnum(int value, String name)
    {
        this.value = value;
        this.name = name;
    }

    public int value()
    {
        return this.value;
    }

    public static IntegrationTaskStatusEnum fromValue(Integer code)
    {
        return Arrays.stream(IntegrationTaskStatusEnum.values()).filter(x -> Objects.equals(x.value(), code)).findFirst().orElse(null);
    }

}
