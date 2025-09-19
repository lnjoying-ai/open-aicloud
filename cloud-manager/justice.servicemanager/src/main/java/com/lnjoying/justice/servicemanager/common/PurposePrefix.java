package com.lnjoying.justice.servicemanager.common;

import com.lnjoying.justice.schema.entity.StatusCode;

import java.util.HashMap;
import java.util.Map;

public enum PurposePrefix
{
    INNER                       ("inner_",           0,    "系统内部"    ,"inner"),
    SERVICE                     ("service_",         1,    "应用服务"    ,"service");

    private String name;
    private int code;
    private Map<String, String> desc;

    PurposePrefix(String name, int code, String cnDesc, String enDesc)
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

    public static PurposePrefix fromName(String name)
    {
        for (PurposePrefix s : PurposePrefix.values())
        {
            if (s.getName().equals(name))
            {
                return s;
            }
        }
        return null;
    }

    public static PurposePrefix fromCode(int code)
    {
        for (PurposePrefix value : PurposePrefix.values())
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
