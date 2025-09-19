package com.lnjoying.justice.servicemanager.common;

import com.lnjoying.justice.schema.entity.StatusCode;

import java.util.HashMap;
import java.util.Map;

public enum ServiceGatewayStatus
{
    UP(0, "在线", "UP"),
    DOWN(1, "离线", "DOWN"),
    REMOVED(99, "已删除", "REMOVED");

    ServiceGatewayStatus(int code, String cnDesc, String enDesc)
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
