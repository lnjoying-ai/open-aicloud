package com.lnjoying.justice.cmp.common;

import com.lnjoying.justice.schema.entity.StatusCode;

import java.util.HashMap;
import java.util.Map;

public enum HealthStatus
{
    HEALTHY(0, "健康", "Healthy"),
    UNHEALTHY(1, "不健康", "UnHealthy"),
    UNKNOWN(2, "未知", "Unknown");
    HealthStatus(int code, String cnDesc, String enDesc)
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
    
    public static HealthStatus fromCode(int code)
    {
        for (HealthStatus current : HealthStatus.values())
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

    public StatusCode toStatusCode()
    {
        return new StatusCode(this.code, this.desc);
    }
}
