package com.lnjoying.justice.servicemanager.common;

import com.lnjoying.justice.schema.entity.StatusCode;

import java.util.HashMap;
import java.util.Map;

public enum PortAllocationStatus
{
    CREATING(0, "创建中", "Creating"),
    LISTENING(1, "监听中", "Listening"),
    RELEASING(90, "释放中", "Releasing"),
    RELEASED(99, "已释放", "Released");

    PortAllocationStatus(int code, String cnDesc, String enDesc)
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
