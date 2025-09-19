package com.lnjoying.justice.omc.service.alert;

import com.lnjoying.justice.commonweb.model.TagItem;
import com.lnjoying.justice.omc.db.model.TblOmcAlarm;
import com.lnjoying.justice.omc.db.model.TblOmcAlertLog;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2024/9/24 10:08
 */

public abstract class AlertFilter
{
    public abstract boolean filter(TblOmcAlertLog alertLog);

    protected  Boolean matchByTagAndPriorities(TblOmcAlertLog alertLog, Object silenceRuleTags, Object silenceRulePriorities)
    {
        Boolean match;
        if (Objects.isNull(silenceRuleTags))
        {
            match = true;
        }
        List<TagItem> tags = (List<TagItem>) silenceRuleTags;

        Object alertLogLabels = alertLog.getLabels();
        if (Objects.isNull(alertLogLabels))
        {
            match = true;
        }

        Map<String, String> labels = (Map<String, String>) alertLogLabels;
        match = tags.stream().anyMatch(tagItem -> {
            if (labels.containsKey(tagItem.getName()))
            {
                String labelValue = labels.get(tagItem.getName());
                return Objects.equals(labelValue, tagItem.getValue());
            }
            else
            {
                return false;
            }
        });

        if (match && Objects.nonNull(silenceRulePriorities))
        {
            List<String> priorities = (List<String>) silenceRulePriorities;
            match  = priorities.stream().anyMatch(priority -> Objects.equals(priority, alertLog.getLevel().toString()));
        }
        return match;
    }

}
