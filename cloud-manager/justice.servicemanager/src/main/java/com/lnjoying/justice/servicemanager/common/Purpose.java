package com.lnjoying.justice.servicemanager.common;

import com.lnjoying.justice.schema.entity.StatusCode;

import java.util.HashMap;
import java.util.Map;

public enum Purpose
{
    USER                        ("service_user",           0,    "用户配置"    ,"user"),
    MONITOR                     ("inner_monitor",         1,    "资源监控"    ,"monitor");

    private String name;
    private int code;
    private Map<String, String> desc;

    Purpose(String name, int code, String cnDesc, String enDesc)
    {
        this.name = name;
        this.code = code;
        this.desc = new HashMap<String, String>()
        {
            {
                put("cn", cnDesc);
                put("en", enDesc);
            }
        };
    }

    public String getName() {
        return name;
    }

    public int getCode() {
        return code;
    }

    public Map<String, String> getDesc() {
        return desc;
    }

    public static Purpose fromName(String name)
    {
        for (Purpose s : Purpose.values())
        {
            if (s.getName().equals(name))
            {
                return s;
            }
        }
        return null;
    }

    public static Purpose fromCode(int code)
    {
        for (Purpose value : Purpose.values())
        {
            if(value.getCode() == code)
            {
                return value;
            }
        }
        return null;
    }

    public StatusCode toStatusCode()
    {
        return new StatusCode(this.code, this.desc);
    }
}
