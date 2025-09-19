package com.lnjoying.justice.schema.common.scheduler;

public enum LabelType
{
    NODE("node"),
    SITE("site"),
    REGION("region");

    private String value;

    LabelType(String value)
    {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static LabelType fromValue(String value)
    {
        for (LabelType s : LabelType.values())
        {
            if (s.getValue().equals(value))
            {
                return s;
            }
        }
        return null;
    }
}
