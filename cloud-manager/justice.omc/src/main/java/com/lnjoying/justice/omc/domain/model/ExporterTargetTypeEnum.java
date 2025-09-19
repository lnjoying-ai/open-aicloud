package com.lnjoying.justice.omc.domain.model;

import java.util.Arrays;
import java.util.Objects;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/10/25 15:39
 */

public enum ExporterTargetTypeEnum
{
    LIGHTWEIGHT_NODE(1, "轻量级节点"),

    NEXTSTACK(2, "nextstack"),

    OPENSTACK(3, "openstack"),

    SITE(4, "站点");

    private final int value;

    private final String name;

    ExporterTargetTypeEnum(int value, String name)
    {
        this.value = value;
        this.name = name;
    }

    public int value()
    {
        return this.value;
    }

    public static ExporterTargetTypeEnum fromValue(Integer code)
    {
        return Arrays.stream(ExporterTargetTypeEnum.values()).filter(x -> Objects.equals(x.value(), code)).findFirst().orElse(null);
    }

    public String getName()
    {
        return name;
    }
}
