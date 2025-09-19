package com.lnjoying.justice.omc.prometheus.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/10/26 14:24
 */

@Data
public class PrometheusRuleGroup
{
    private String name;

    private int interval;

    private int limit;

    private List<PrometheusAlertingRule> rules = new ArrayList<>();

    public void addPrometheusAlertingRule(PrometheusAlertingRule alertingRule)
    {
        this.rules.add(alertingRule);
    }
}
