package com.lnjoying.justice.scheduler.common.constant;

public enum Strategy
{
    DEFAULT_STRATEGY("defaultStrategy");

    private String value;

    Strategy(String value)
    {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static Strategy fromValue(String name)
    {
        for (Strategy s : Strategy.values())
        {
            if (s.getValue().equals(name))
            {
                return s;
            }
        }
        return null;
    }
}
