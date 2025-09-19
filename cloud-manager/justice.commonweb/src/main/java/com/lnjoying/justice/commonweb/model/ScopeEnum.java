package com.lnjoying.justice.commonweb.model;

import com.lnjoying.justice.commonweb.exception.WebSystemException;
import com.lnjoying.justice.schema.common.ErrorCode;
import com.lnjoying.justice.schema.common.ErrorLevel;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2024/9/25 14:49
 */

public enum ScopeEnum
{
    /**
     * private： Only available for creating user
     */
    PRIVATE(0),

    /**
     * bp: bp user visible
     */
    BP(1),

    /**
     * public: All users can access
     */
    PUBLIC(2);
    private final int value;

    ScopeEnum(int value)
    {
        this.value = value;
    }

    public int value()
    {
        return this.value;
    }

    public static ScopeEnum fromValue(int value)
    {
        switch (value)
        {
            case 1:
                return BP;
            case 2:
                return PUBLIC;
            case 0:
            default:
                return PRIVATE;
        }
    }
}
