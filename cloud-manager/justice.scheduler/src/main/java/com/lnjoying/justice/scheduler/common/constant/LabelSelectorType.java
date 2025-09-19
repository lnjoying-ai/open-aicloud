package com.lnjoying.justice.scheduler.common.constant;

public enum LabelSelectorType
{
    MUST("Must"),
    PREFER("Prefer"),
    MUST_NOT("MustNot"),
    PREFER_NOT("PreferNot");

    private String value;

    LabelSelectorType(String value)
    {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static LabelSelectorType fromValue(String value)
    {
        for (LabelSelectorType s : LabelSelectorType.values())
        {
            if (s.getValue().equals(value))
            {
                return s;
            }
        }
        return null;
    }
}
