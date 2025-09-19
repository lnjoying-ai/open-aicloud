package com.lnjoying.justice.scheduler.common.constant;

public enum AffinityState
{
    DEFAULT             ("Default",          0),
    TARGET_REGION       ("TargetRegion",     0b1),
    REGION_AFFINITY     ("RegionAffinity",   0b10),
    REGION_ALL          ("RegionAll",        0b100),
    TARGET_SITE         ("TargetSite",       0b1000),
    SITE_AFFINITY       ("SiteAffinity",     0b10000),
    SITE_ALL            ("SiteAll",          0b100000),
    NODE_AFFINITY       ("NodeAffinity",     0b1000000),
    NODE_ALL            ("NodeAll",          0b10000000);


    private String name;
    private int code;

    AffinityState(String name, int code)
    {
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public int getCode() {
        return code;
    }

    public static AffinityState fromName(String name)
    {
        for (AffinityState s : AffinityState.values())
        {
            if (s.getName().equals(name))
            {
                return s;
            }
        }
        return null;
    }

    public static AffinityState fromCode(int code)
    {
        for (AffinityState s : AffinityState.values())
        {
            if (s.getCode()==code)
            {
                return s;
            }
        }
        return null;
    }
}
