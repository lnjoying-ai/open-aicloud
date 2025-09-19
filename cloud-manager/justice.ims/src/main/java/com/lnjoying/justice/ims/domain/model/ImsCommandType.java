package com.lnjoying.justice.ims.domain.model;

import java.util.Arrays;

/**
 * Enumeration of various commands
 *
 * @author merak
 **/

public enum ImsCommandType
{
    /**
     * login command
     */
    LOGIN(0),

    /**
     * pull command
     */
    PULL(1),

    /**
     * push command
     */
    PUSH(2);

    private final int value;

    ImsCommandType(int value)
    {
        this.value = value;
    }

    public int value()
    {
        return this.value;
    }

    public static ImsCommandType fromValue(int value)
    {
        return Arrays.stream(ImsCommandType.values()).filter(x -> x.value() == value).findFirst().get();
    }
}
