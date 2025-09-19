package com.lnjoying.justice.ims.harbor.model;

/**
 * role type in harbor
 *
 * @author merak
 **/

public enum RoleType
{

    /**
     * projectAdmin
     */
    PROJECTADMIN(1, "projectAdmin"),

    /**
     * developer
     */
    DEVELOPER(2, "developer"),

    /**
     * guest
     */
    GUEST(3, "guest"),

    /**
     * limitedGuest
     */
    LIMITEDGUEST(4, "limitedGuest"),

    /**
     * maintainer
     */
    MAINTAINER(5, "maintainer");

    private final int value;

    private final String name;

    RoleType(int value, String name)
    {
        this.value = value;
        this.name = name;
    }

    public int getValue()
    {
        return value;
    }

    public String getName()
    {
        return name;
    }
}
