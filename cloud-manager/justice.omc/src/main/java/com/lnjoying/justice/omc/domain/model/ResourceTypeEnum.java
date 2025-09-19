package com.lnjoying.justice.omc.domain.model;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/11/16 10:14
 */

@Slf4j
public enum ResourceTypeEnum
{
    VM(1),

    NODE(2),

    CLUSTER(3),

    CONTAINER(4);

    private final int value;

    ResourceTypeEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
