package com.lnjoying.justice.omc.prometheus.model;

import lombok.Data;

import java.util.Map;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/10/26 14:14
 */

@Data
public class PrometheusAlertingRule
{
    public static final String ANNOTATION_SUMMARY = "summary";

    public static final String ANNOTATION_DESCRIPTION = "description";

    public static final String ANNOTATION_ALARM_UID = "x_alarm_uid";

    private String alert;

    private String expr;

    private String forValue;

    private Map<String, String> labels;

    private Map<String, String> annotations;
}
