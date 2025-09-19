package com.lnjoying.justice.cmp.utils;

public class BoolUtil
{
    public static Boolean short2Bool(Short shortValue)
    {
        return shortValue == null ? null : shortValue != 0;
    }

    public static Short bool2Short(Boolean boolValue)
    {
        return boolValue == null ? null : boolValue ? (short)1 : (short)0;
    }

    public static Integer bool2Integer(Boolean boolValue)
    {
        return boolValue == null ? null : boolValue ? 1 : 0;
    }

    public static Boolean short2Bool(Short shortValue, Boolean defaultValue)
    {
        return shortValue == null ? defaultValue : shortValue != 0;
    }
}
