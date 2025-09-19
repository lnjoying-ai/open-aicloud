package com.lnjoying.justice.servicemanager.common;

import com.lnjoying.justice.schema.entity.StatusCode;

import java.util.HashMap;
import java.util.Map;

public enum TargetServiceStatus
{
    CREATING(0, "创建中", "Creating"),
    UPDATING(1, "升级中", "Updating"),
    READY(2, "就绪", "Ready"),

    NO_RESOURCE(50, "无可用资源", "No resource"),
    NOT_SATISFIED(51, "不满足", "Not satisfied"),
    CREATE_FILED(52, "创建失败", "Create filed"),

    REMOVING(90, "删除中", "Removing"),
    REMOVED(99, "已删除", "Removed");

    TargetServiceStatus(int code, String cnDesc, String enDesc)
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
