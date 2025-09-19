package com.lnjoying.justice.omc.prometheus.model;

import lombok.Data;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/10/26 14:27
 */

@Data
public class PrometheusRuleGroups
{
    private List<PrometheusRuleGroup> groups = new ArrayList<>();

    public void addPrometheusRuleGroup(PrometheusRuleGroup group)
    {
        groups.add(group);
    }
}
