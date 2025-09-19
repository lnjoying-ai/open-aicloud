package com.lnjoying.justice.omc.domain.model;

import java.util.Arrays;
import java.util.Objects;

/**
 * @Description: deployment status:PENDING-->PROGRESSING--->PORT_MAPPING_SUCCEEDED--->DELETED
 *               uninstall status:PORT_MAPPING_REMOVED-->SCRAPE_TARGET_REMOVED--->DELETED
 * @Author: Merak
 * @Date: 2023/10/25 15:37
 */

public enum ExporterStackDeploymentStatusEnum
{
    PENDING(1, "pending"),

    CREATE_DAEMONSET_STACK_SUCCEEDED(2, "create daemon set stack succeeded"),

    RUNNING(3, "running"),

    SUCCEEDED(4, "succeeded"),

    FAILED(5, "failed"),



    PROGRESSING(6, "progressing"),

    PORT_MAPPING_SUCCEEDED(11, "port mapping succeeded"),

    PORT_MAPPING_FAILED(12, "port mapping failed"),

    PORT_MAPPING_REMOVED(13, "port mapping removed"),



    SCRAPE_TARGET_ADDED(21, "scrape target added"),

    SCRAPE_TARGET_REMOVED(22, "scrape target removed"),



    TERMINATING(30, "terminating "),

    TERMINATED(31, "terminated "),

    DELETED(32, "Deleted ");

    private final int value;

    private final String name;

    ExporterStackDeploymentStatusEnum(int value, String name)
    {
        this.value = value;
        this.name = name;
    }

    public int value()
    {
        return this.value;
    }

    public static ExporterStackDeploymentStatusEnum fromValue(Integer code)
    {
        return Arrays.stream(ExporterStackDeploymentStatusEnum.values()).filter(x -> Objects.equals(x.value(), code)).findFirst().orElse(null);
    }

}
