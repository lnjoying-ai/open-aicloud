package com.lnjoying.justice.scheduler.common.constant;

public enum SchedulerElement
{
    CHECK_LABEL             ("CheckLabel",            0b1),
    DIRECT_TARGET           ("DirectTarget",          0b10),
    ON_ONE_NODE             ("OnOneNode",             0b100),
    REGION_AFFINITY         ("RegionAffinity",        0b1000),
    SITE_AFFINITY           ("SiteAffinity",          0b10000),
    NODE_AFFINITY           ("NodeAffinity",          0b100000),
    NODE_RESOURCES          ("NodeResources",         0b1000000),
    NODE_PORTS              ("NodePorts",             0b10000000),
    BIND_LEAST              ("BindLeast",             0b100000000),
    IMAGE_LOCALITY          ("ImageLocality",         0b1000000000),
    BIND_RELATIONS          ("BindRelations",         0b10000000000);

    private String name;
    private int mask;

    SchedulerElement(String name, int mask)
    {
        this.name = name;
        this.mask = mask;
    }

    public String getName()
    {
        return name;
    }

    public int getMask()
    {
        return mask;
    }

    public static SchedulerElement fromName(String name)
    {
        for (SchedulerElement s : SchedulerElement.values())
        {
            if (s.getName().equals(name))
            {
                return s;
            }
        }
        return null;
    }
}
