package com.lnjoying.justice.omc.service.alert;

import com.lnjoying.justice.omc.common.cache.CacheFactory;
import com.lnjoying.justice.omc.common.cache.CommonCacheService;
import com.lnjoying.justice.omc.db.model.TblOmcAlertInhibitionRule;
import com.lnjoying.justice.omc.db.model.TblOmcAlertLog;
import com.lnjoying.justice.omc.db.repo.AlarmRepository;
import com.lnjoying.justice.omc.db.repo.AlertReduceRepository;
import com.micro.core.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import static com.lnjoying.justice.omc.common.cache.CacheFactory.CACHE_KEY_ALERT_INHIBITION_RULE;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2024/9/26 10:10
 */
@Component
public class InhibitionFilter extends AlertFilter
{

    @Autowired
    private AlertReduceRepository alertReduceRepository;

    @Autowired
    private AlarmRepository alarmRepository;

    @Override
    public boolean filter(TblOmcAlertLog currentAlertLog)
    {
        Date now = new Date();
        CommonCacheService<String, Object> alertInhibitionRuleCache = CacheFactory.getAlertInhibitionRuleCache();
        List<TblOmcAlertInhibitionRule> inhibitionRules = (List<TblOmcAlertInhibitionRule>) alertInhibitionRuleCache.get(CACHE_KEY_ALERT_INHIBITION_RULE);
        if (CollectionUtils.isEmpty(inhibitionRules))
        {
            inhibitionRules = alertReduceRepository.selectAllAlertInhibitionRules();
            inhibitionRules.sort((o1, o2) -> Boolean.compare(o1.getMatchAll() , o1.getMatchAll()));

            alertInhibitionRuleCache.put(CACHE_KEY_ALERT_INHIBITION_RULE, inhibitionRules);
        }

        for (TblOmcAlertInhibitionRule inhibitionRule : inhibitionRules)
        {
            if (!inhibitionRule.getEnable())
            {
                continue;
            }

            boolean match = inhibitionRule.getMatchAll();
            if (!match)
            {
                match = matchByTagAndPriorities(currentAlertLog, inhibitionRule.getTags(), inhibitionRule.getPriorities());
            }

            if (match)
            {
                long evalIntervalMs = inhibitionRule.getEvalInterval() * 1000L;
                if (evalIntervalMs <= 0)
                {
                    return true;
                }

                TblOmcAlertLog prevAlertLog = alarmRepository.selectOneByUniqueHash(currentAlertLog.getUniqueHash());
                if (Objects.isNull(prevAlertLog))
                {
                    currentAlertLog.setTimes(1);
                    currentAlertLog.setFirstAlertTime(now);
                    currentAlertLog.setLastAlertTime(now);
                    return true;
                }
                else
                {
                    Date firstAlertTime = prevAlertLog.getFirstAlertTime();
                    int alertTime = Objects.nonNull(prevAlertLog.getTimes()) ? prevAlertLog.getTimes() : 1 ;
                    if (Objects.nonNull(firstAlertTime) && (DateUtils.diff(now, firstAlertTime) < evalIntervalMs))
                    {
                       prevAlertLog.setTimes(alertTime + 1);
                       prevAlertLog.setLastAlertTime(now);
                       return false;
                    }
                    else
                    {
                        currentAlertLog.setTimes(alertTime);
                        currentAlertLog.setFirstAlertTime(firstAlertTime);
                        currentAlertLog.setLastAlertTime(now);
                        return true;
                    }
                }
            }
        }
        return true;
    }

}
