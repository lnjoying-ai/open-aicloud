package com.lnjoying.justice.omc.util;

import com.lnjoying.justice.commonweb.util.CollectionUtils;
import com.lnjoying.justice.omc.db.model.TblOmcAlertLog;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.lnjoying.justice.commonweb.util.ServiceCombRequestUtils.isAdmin;
import static com.lnjoying.justice.commonweb.util.ServiceCombRequestUtils.isBpAdmin;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2024/5/22 14:37
 */

@Slf4j
public class AlarmUtils
{
    public static String modifyAlertExpr(String expr, String bpId, String userId)
    {
        try
        {
            if (StringUtils.isNotBlank(expr))
            {
                List<AlarmUtils.MetricLabel> metricLabels = new ArrayList<>();

                if (isAdmin())
                {
                    return expr;
                }

                if (!isBpAdmin())
                {
                    AlarmUtils.MetricLabel userIdLabel = new AlarmUtils.MetricLabel("user_id", "=~", userId);
                    metricLabels.add(userIdLabel);
                }

                AlarmUtils.MetricLabel bpIdLabel = new AlarmUtils.MetricLabel("bp_id", "=~", bpId);
                metricLabels.add(bpIdLabel);
                String modifiedExpr = AlarmUtils.modifyExpression(expr, metricLabels);
                return modifiedExpr;


            }

        }
        catch (Exception e)
        {
            log.error("modify alert expr error", e);
            return expr;
        }
        return expr;
    }

    public static String modifyExpression(String expression, List<MetricLabel> metricLabelList) {
        if (CollectionUtils.isEmpty(metricLabelList))
        {
            return expression;
        }
        String additionalLabels  = buildLabelsString(metricLabelList);
        Pattern pattern = Pattern.compile("\\}");
        Matcher matcher = pattern.matcher(expression);
        StringBuffer stringBuffer = new StringBuffer();
        while (matcher.find()) {
            if (matcher.start() > 0 && !hasCommaBefore(expression, matcher.start())) {
                matcher.appendReplacement(stringBuffer, "," + additionalLabels + "}");
            } else {
                matcher.appendReplacement(stringBuffer, additionalLabels + "}");
            }
        }
        matcher.appendTail(stringBuffer);
        return stringBuffer.toString();
    }

    public static boolean hasCommaBefore(String input, int position) {
        for (int i = position - 1; i >= 0; i--) {
            char c = input.charAt(i);
            if (c == ',') {
                return true;
            }
            else if (c == '{') {
                return true;
            }
            else if (c != ' ') {
                break;
            }
        }
        return false;
    }

    private static String buildLabelsString(List<MetricLabel> labels) {

        StringBuilder builder = new StringBuilder();
        for (MetricLabel label : labels) {
            builder.append(label.toString()).append(",");
        }
        builder.deleteCharAt(builder.length() - 1);
        return builder.toString();
    }

    public static class MetricLabel
    {
        private String labelKey;
        private String operator;
        private String labelValue;

        public MetricLabel(String labelKey, String operator, String labelValue)
        {
            this.labelKey = labelKey;
            this.operator = operator;
            this.labelValue = labelValue;
        }


        public String toString()
        {
            return labelKey + operator + "\"" + labelValue + "\"";
        }
    }

    public static int generateAlertUniqueHash(TblOmcAlertLog alertLog)
    {
        Object alertLogLabels = alertLog.getLabels();

        String labels = Objects.nonNull(alertLogLabels) ? ((Map<String, String>) alertLogLabels).entrySet().stream().map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.joining(",")) : "";
        String combined = alertLog.getAlarmId() + alertLog.getLevel() + labels;

        return combined.hashCode();
    }
}
