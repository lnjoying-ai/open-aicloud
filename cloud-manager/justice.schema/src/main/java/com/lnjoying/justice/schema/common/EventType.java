package com.lnjoying.justice.schema.common;

public enum EventType
{
    DEFAULT("default",0, "default"),
    LOST_STATUS_GT_10M_LT_2H("lost_status_gt_10m",1, "Status not updated for more than %s"),
    LOST_STATUS_GT_2H("lost_status_gt_2h",2, "Status not updated for more than %s"),
    ;

    private final String name;
    private final int code;
    private final String description;

    EventType(String name, int code, String description)
    {
        this.name = name;
        this.code = code;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static EventType fromCode(int code)
    {
        for (EventType value : EventType.values())
        {
            if(value.getCode() == code)
            {
                return value;
            }
        }
        return null;
    }
}
