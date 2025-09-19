package com.lnjoying.justice.omc.prometheus.sd;

import java.util.*;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/11/3 19:53
 */


public class TargetsData
{

    private List<String> targets;

    private Map<String, String> labels;

    public TargetsData()
    {
        this.targets = new ArrayList<>();
        this.labels  = new HashMap<>();
    }

    public void addTarget(String target)
    {
        if (!targets.contains(target))
        {
            this.targets.add(target);
        }
    }

    public void addLabels(Map<String, String> labels)
    {
        this.labels.putAll(labels);
    }

    public void addLabel(String key, String value)
    {
        this.labels.put(key, value);
    }

    public void deleteTarget(String target)
    {
        this.targets.remove(target);
    }


    public List<String> getTargets()
    {
        return targets;
    }

    public Map<String, String> getLabels()
    {
        return labels;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof TargetsData)) return false;
        TargetsData that = (TargetsData) o;
        return targets.equals(that.targets) && labels.equals(that.labels);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(targets, labels);
    }
}
