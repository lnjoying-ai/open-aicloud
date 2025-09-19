package com.lnjoying.justice.servicemanager.common;

import com.lnjoying.justice.schema.entity.StatusCode;

import java.util.HashMap;
import java.util.Map;

public enum ServicePortStatus
{
    CREATING(0, "创建中", "Creating"),
    UPDATING(1, "升级中", "Updating"),
    READY(2, "就绪", "Ready"),
    PARTIAL_READY(3, "部分就绪", "Partial Ready"),

    FILED(50, "失败", "Filed"),

    REMOVING(90, "删除中", "Removing"),
    REMOVED(99, "已删除", "Removed");

    ServicePortStatus(int code, String cnDesc, String enDesc)
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
    
    public static ServicePortStatus fromCode(int code)
    {
        for (ServicePortStatus current : ServicePortStatus.values())
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
