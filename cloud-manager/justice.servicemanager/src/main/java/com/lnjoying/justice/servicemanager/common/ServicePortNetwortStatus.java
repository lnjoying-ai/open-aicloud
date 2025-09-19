package com.lnjoying.justice.servicemanager.common;

import com.lnjoying.justice.schema.entity.StatusCode;

import java.util.HashMap;
import java.util.Map;

public enum ServicePortNetwortStatus
{
    CONNECT_SUCCESS(0, "连接成功", "connect success"),
    SERVICE_GATEWAY_UNREACHABLE(1, "service gateway不可达", "service gateway unreachable"),
    SERVICE_AGENT_UNREACHABLE(2, "service agent不可达", "service agent unreachable"),
    TARGET_SERVICE_UNREACHABLE(3, "目标服务不可达", "target service unreachable");

    ServicePortNetwortStatus(int code, String cnDesc, String enDesc)
    {
        this.code = code;
        this.desc = new HashMap<String, String>()
        {
            {
                put("cn", cnDesc);
                put("en", enDesc);
            }
        };
    }

    public static TargetServiceStatus fromCode(int code)
    {
        for (TargetServiceStatus current : TargetServiceStatus.values())
        {
            if (current.getCode() == code)
            {
                return current;
            }
        }
        throw new RuntimeException("Invalid error code: " + code);
    }

    private int code;

    private Map<String, String> desc;

    public int getCode() {
        return code;
    }

    public Map<String, String> getDesc()
    {
        return desc;
    }

    public StatusCode toStatusCode()
    {
        return new StatusCode(this.code, this.desc);
    }
}
