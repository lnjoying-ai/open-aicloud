package com.lnjoying.justice.omc.domain.model;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Objects;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/10/25 15:32
 */

@Slf4j
public enum IntegrationTaskTypeEnum
{
    LIGHTWEIGHT_NODE_DEPLOYMENT_TASK(1, "轻量级节点组件"),

    NEXTSTACK_DEPLOYMENT_TASK(2, "nextstack云监控组件"),

    OPENSTACK_DEPLOYMENT_TASK(3, "openstack云监控组件"),

    GPU_DEPLOYMENT_TASK(4, "GPU监控组件"),

    TEMPLATE_TASK(5, "模板监控组件");

    private final int value;

    private final String name;

    IntegrationTaskTypeEnum(int value, String name)
    {
        this.value = value;
        this.name = name;
    }

    public int value()
    {
        return this.value;
    }

    public static IntegrationTaskTypeEnum fromValue(Integer code)
    {
        return Arrays.stream(IntegrationTaskTypeEnum.values()).filter(x -> Objects.equals(x.value(), code)).findFirst().orElse(null);
    }

    public static ExporterTargetTypeEnum fromIntegrationTaskType(Integer code)
    {
        if (null == code)
        {
            return null;
        }

        switch (code)
        {
            case 1:
            case 4:
                return ExporterTargetTypeEnum.LIGHTWEIGHT_NODE;
            case 2:
                return ExporterTargetTypeEnum.NEXTSTACK;
            case 3:
                return ExporterTargetTypeEnum.OPENSTACK;
            case 5:
                return ExporterTargetTypeEnum.SITE;
            default:
                log.warn("illegal integration type code:{}", code);
                return null;

        }
    }

    public String getName()
    {
        return name;
    }
}
