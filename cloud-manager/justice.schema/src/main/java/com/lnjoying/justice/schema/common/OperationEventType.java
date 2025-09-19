package com.lnjoying.justice.schema.common;

import lombok.Getter;
import org.springframework.util.StringUtils;

import java.util.Optional;

/**
 * 功能描述
 *
 * @author: Robin
 * @date: 2023年11月02日 16:04
 */
@Getter
public enum OperationEventType
{
    //事件日志(应用/服务)
    APP_OR_SERVICE_DEPLOY("APP/SERVICE DEPLOY", "应用/服务 部署", "app or service deployment is executing"),
    APP_OR_SERVICE_START("APP/SERVICE START", "应用/服务 启动", "app or service is starting"),
    APP_OR_SERVICE_STOP("APP/SERVICE STOP", "应用/服务 停止", "app or service is stopping"),
    APP_OR_SERVICE_REBOOT("APP/SERVICE REBOOT", "应用/服务 重启", "app or service is stopping"),
    RESOURCE_CREATE("RESOURCE CREATE", "资源创建", "resource is creating"),
    RESOURCE_DELETE("RESOURCE DELETE", "资源删除", "resource is deleting"),
    RESOURCE_UPDATE("RESOURCE UPDATE", "资源更新", "resource is updating"),

    ;

    private String enName;
    private String cnName;
    private String description;

    OperationEventType(String enName, String cnName, String description)
    {
        this.enName = enName;
        this.cnName = cnName;
        this.description = description;
    }

    public static Optional<String> getOperationTypeCnName(String operationType)
    {
        try
        {
            if (StringUtils.isEmpty(operationType))
            {
                return Optional.empty();
            }

            for (OperationEventType operationEventType : OperationEventType.values())
            {
                if (operationType.equals(operationEventType.name()))
                {
                    return Optional.of(operationEventType.getCnName());
                }
            }

            return Optional.empty();
        } catch (Exception e)
        {
            return Optional.empty();
        }
    }
}
