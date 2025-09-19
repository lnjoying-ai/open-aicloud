package com.lnjoying.justice.omc.service.alert;

import com.alibaba.nacos.common.utils.CollectionUtils;
import com.lnjoying.justice.commonweb.model.TagItem;
import com.lnjoying.justice.omc.common.cache.CacheFactory;
import com.lnjoying.justice.omc.common.cache.CommonCacheService;
import com.lnjoying.justice.omc.db.model.TblOmcAlertLog;
import com.lnjoying.justice.omc.db.model.TblOmcAlertSilenceRule;
import com.lnjoying.justice.omc.db.repo.AlertReduceRepository;
import com.lnjoying.justice.omc.domain.model.AlertSilenceTypeEnum;
import com.micro.core.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.lnjoying.justice.omc.common.cache.CacheFactory.CACHE_KEY_ALERT_SILENCE_RULE;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2024/9/26 10:15
 */

@Component
public class SilenceFilter extends AlertFilter
{
    private boolean isSilenced;

    public SilenceFilter()
    {
        this.isSilenced = true;
    }

    @Autowired
    private AlertReduceRepository reduceRepository;

    public SilenceFilter(boolean isSilenced)
    {
        this.isSilenced = isSilenced;
    }

    @Override
    public boolean filter(TblOmcAlertLog alertLog)
    {
        if (isSilenced)
        {
            CommonCacheService<String, Object> alertSilenceRuleCache = CacheFactory.getAlertSilenceRuleCache();
            List<TblOmcAlertSilenceRule> silenceRules = reduceRepository.selectAllAlertSilenceRules();
            if (CollectionUtils.isEmpty(silenceRules))
            {
                silenceRules = reduceRepository.selectAllAlertSilenceRules();
                silenceRules.sort((o1, o2) -> Boolean.compare(o1.getMatchAll() , o1.getMatchAll()));

                alertSilenceRuleCache.put(CACHE_KEY_ALERT_SILENCE_RULE, silenceRules);
            }

            for (TblOmcAlertSilenceRule silenceRule : silenceRules)
            {
                if (!silenceRule.getEnable())
                {
                    continue;
                }

                Boolean match = silenceRule.getMatchAll();
                if (!match)
                {
                    match = matchByTagAndPriorities(alertLog, silenceRule.getTags(), silenceRule.getPriorities());
                }

                if (match)
                {
                    return !isInSilentPeriod(silenceRule);
                }

            }
        }
        return true;
    }



    public static boolean isInSilentPeriod(TblOmcAlertSilenceRule silenceRule) {
        LocalDateTime now = LocalDateTime.now();
        LocalTime nowTime = LocalTime.now();
        Integer silenceType = silenceRule.getType();
        AlertSilenceTypeEnum alertSilenceTypeEnum = AlertSilenceTypeEnum.fromValue(silenceType);

        LocalDateTime localDateStartTime = DateUtils.of(silenceRule.getStartTime());
        LocalDateTime localDateEndTime = DateUtils.of(silenceRule.getEndTime());
        LocalTime startTime = localDateStartTime.toLocalTime();
        LocalTime endTime = localDateEndTime.toLocalTime();

        switch (alertSilenceTypeEnum) {
            case DAY:
                return isInSilencePeriodByDays(silenceRule, now, nowTime, startTime, endTime);
            case WEEK:
                return isInSilencePeriodByWeeks(silenceRule, now, nowTime, startTime, endTime);
            case ONCE:
            default:
                return  !nowTime.isBefore(startTime) && !nowTime.isAfter(endTime);
        }
    }

    private static boolean isInSilencePeriodByWeeks(TblOmcAlertSilenceRule silenceRule, LocalDateTime now, LocalTime nowTime, LocalTime startTime, LocalTime endTime)
    {
        List<String> weeks = (List<String>) silenceRule.getWeeks();
        return weeks.contains(String.valueOf(now.getDayOfWeek().getValue())) && !nowTime.isBefore(startTime) && !nowTime.isAfter(endTime);
    }

    private static boolean isInSilencePeriodByDays(TblOmcAlertSilenceRule silenceRule, LocalDateTime now, LocalTime nowTime, LocalTime startTime, LocalTime endTime)
    {
        List<String> days = (List<String>) silenceRule.getDays();
        return days.contains(String.valueOf(now.getDayOfMonth())) && !nowTime.isBefore(startTime) && !nowTime.isAfter(endTime);
    }

}
