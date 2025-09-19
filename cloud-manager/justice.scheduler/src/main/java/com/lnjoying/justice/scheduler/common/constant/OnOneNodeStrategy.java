package com.lnjoying.justice.scheduler.common.constant;

public enum OnOneNodeStrategy
{
    DEFAULT("Default", 0, "no deployment requirements"),
    ON_ONE_NODE("OnOneNode", 1, "all replica on one node"),
    DISPERSE("Disperse", 2, "only one replica can deployed on one node, other application also can deployed on this node"),
    MONOPOLY("Monopoly", 3, "only one container or service or role can deployed on one node");

    private String name;
    private int code;
    private String desc;

    OnOneNodeStrategy(String name, int code, String desc)
    {
        this.name = name;
        this.code = code;
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public int getCode() {
        return code;
    }

    public static OnOneNodeStrategy fromName(String name)
    {
        for (OnOneNodeStrategy s : OnOneNodeStrategy.values())
        {
            if (s.getName().equals(name))
            {
                return s;
            }
        }
        return null;
    }

    public static OnOneNodeStrategy fromCode(int code)
    {
        for (OnOneNodeStrategy value : OnOneNodeStrategy.values())
        {
            if(value.getCode() == code)
            {
                return value;
            }
        }
        return null;
    }
}
