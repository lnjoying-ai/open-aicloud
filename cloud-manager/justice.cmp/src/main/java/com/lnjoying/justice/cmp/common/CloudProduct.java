package com.lnjoying.justice.cmp.common;

public enum CloudProduct
{
    NEXTSTACK("NextStack", "NSK"),
    OPENSTACK("OpenStack", "OSK"),
    EASYSTACK("EasyStack", "ESK");

    CloudProduct(String name, String shortName)
    {
        this.name = name;
        this.shortName = shortName;
    }

    private String name;

    private String shortName;

    public String getName() {
        return name;
    }

    public String getShortName() {
        return shortName;
    }

    public static CloudProduct fromName(String name)
    {
        for (CloudProduct current : CloudProduct.values())
        {
            if (current.getName().equalsIgnoreCase(name))
            {
                return current;
            }
        }
        throw new RuntimeException("Invalid error name: " + name);
    }
}
