package com.lnjoying.justice.scheduler.scheduler.framework;

import com.lnjoying.justice.scheduler.config.LabelProperty;
import com.lnjoying.justice.scheduler.domain.model.SchedulerBean;
import com.lnjoying.justice.schema.common.scheduler.SchedulerState;
import com.lnjoying.justice.schema.entity.dev.LabelSelector;
import com.micro.core.persistence.redis.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AffinityElement extends BaseElement
{
    @Autowired
    LabelProperty labelProperty;

    protected void processOperatorIn(LabelSelector labelSelector, Boolean first, String redisKeyPrefix, String middleKey, String tempKey)
    {
        String[] inValues = labelSelector.getValue().split(",");
        List<String> inLabelKeys = new ArrayList<>();
        for (String value : inValues)
        {
            String labelKey = redisKeyPrefix + labelSelector.getKey() + ":" + value;
            inLabelKeys.add(labelKey);
        }
        if (first)
        {
            RedisUtil.sunionstore(middleKey, inLabelKeys.toArray(new String[0]));
            first = false;
        }
        else
        {
            RedisUtil.sunionstore(tempKey, inLabelKeys.toArray(new String[0]));
            RedisUtil.sinterstore(middleKey, tempKey, middleKey);
        }
    }

    protected void processOperatorNotIn(LabelSelector labelSelector, Boolean first, String redisKeyPrefix, String middleKey, String allKey)
    {
        String[] notInValues = labelSelector.getValue().split(",");
        List<String> notInLabelKeys = new ArrayList<>();
        if (first)
        {
            notInLabelKeys.add(allKey);
            first = false;
        }
        else
        {
            notInLabelKeys.add(middleKey);
        }
        for (String value : notInValues)
        {
            String labelKey = redisKeyPrefix + labelSelector.getKey() + ":" + value;
            notInLabelKeys.add(labelKey);
        }
        RedisUtil.sdiffstore(middleKey, notInLabelKeys.toArray(new String[0]));
    }

    protected void processOperatorExists(LabelSelector labelSelector, Boolean first, String redisKeyPrefix, String middleKey, String allKey)
    {
        String existsLabelKey = redisKeyPrefix + labelSelector.getKey();
        if (first)
        {
            RedisUtil.sinterstore(middleKey, allKey, existsLabelKey);
            first = false;
        }
        else
        {
            RedisUtil.sinterstore(middleKey, middleKey, existsLabelKey);
        }
    }

    protected void processOperatorNotExists(LabelSelector labelSelector, Boolean first, String redisKeyPrefix, String middleKey, String allKey)
    {
        String notExistsLabelKey = redisKeyPrefix + labelSelector.getKey();
        if (first)
        {
            RedisUtil.sdiffstore(middleKey, allKey, notExistsLabelKey);
            first = false;
        }
        else
        {
            RedisUtil.sdiffstore(middleKey, middleKey, notExistsLabelKey);
        }
    }

    protected Boolean processOperatorGt(SchedulerBean schedulerBean, LabelSelector labelSelector, boolean first, String redisKeyPrefix, String middleKey, String tempKey)
    {
        String gtKeyPattern = redisKeyPrefix + labelSelector.getKey() + ":*";
        List<String> gtKeys = RedisUtil.keys(gtKeyPattern);
        if (null == gtKeys)
        {
            schedulerBean.setSchedulerState(SchedulerState.REDIS_ERROR.getCode());
            return false;
        }
        Iterator<String> gtIt=gtKeys.iterator();
        BigDecimal gtValue = new BigDecimal(labelSelector.getValue());
        while (gtIt.hasNext())
        {
            String key=gtIt.next();
            BigDecimal num = new BigDecimal(key.replace(redisKeyPrefix + labelSelector.getKey() + ":", ""));
            if (num.compareTo(gtValue) < 1)
            {
                gtIt.remove();
            }
        }
        if (gtKeys.size() == 0)
        {
            schedulerBean.setSchedulerState(SchedulerState.NO_MATCHED_REGION.getCode());
            return false;
        }
        if (first)
        {
            RedisUtil.sunionstore(middleKey,gtKeys.toArray(new String[0]));
            first = false;
        }
        else
        {
            RedisUtil.sunionstore(tempKey,gtKeys.toArray(new String[0]));
            RedisUtil.sinterstore(middleKey,tempKey,middleKey);
        }
        return true;
    }

    protected Boolean processOperatorLt(SchedulerBean schedulerBean, LabelSelector labelSelector, Boolean first, String redisKeyPrefix, String middleKey, String tempKey)
    {
        String ltKeyPattern = redisKeyPrefix + labelSelector.getKey() + ":*";
        List<String> ltKeys = RedisUtil.keys(ltKeyPattern);
        if (null == ltKeys)
        {
            schedulerBean.setSchedulerState(SchedulerState.REDIS_ERROR.getCode());
            return false;
        }
        Iterator<String> ltIt=ltKeys.iterator();
        BigDecimal ltValue = new BigDecimal(labelSelector.getValue());
        while (ltIt.hasNext())
        {
            String key=ltIt.next();
            BigDecimal num = new BigDecimal(key.replace(redisKeyPrefix + labelSelector.getKey() + ":", ""));
            if (num.compareTo(ltValue) > -1)
            {
                ltIt.remove();
            }
        }
        if (ltKeys.size() == 0)
        {
            schedulerBean.setSchedulerState(SchedulerState.NO_MATCHED_REGION.getCode());
            return false;
        }
        if (first)
        {
            RedisUtil.sunionstore(middleKey, ltKeys.toArray(new String[0]));
            first = false;
        }
        else
        {
            RedisUtil.sunionstore(tempKey, ltKeys.toArray(new String[0]));
            RedisUtil.sinterstore(middleKey, tempKey, middleKey);
        }
        return true;
    }

    protected Boolean processOperatorGte(SchedulerBean schedulerBean, LabelSelector labelSelector, Boolean first, String redisKeyPrefix, String middleKey, String tempKey)
    {
        String gteKeyPattern = redisKeyPrefix + labelSelector.getKey() + ":*";
        List<String> gteKeys = RedisUtil.keys(gteKeyPattern);
        if (null == gteKeys)
        {
            schedulerBean.setSchedulerState(SchedulerState.REDIS_ERROR.getCode());
            return false;
        }
        Iterator<String> gteIt=gteKeys.iterator();
        BigDecimal gteValue = new BigDecimal(labelSelector.getValue());
        while (gteIt.hasNext())
        {
            String key=gteIt.next();
            BigDecimal num = new BigDecimal(key.replace(redisKeyPrefix + labelSelector.getKey() + ":", ""));
            if (num.compareTo(gteValue) < 0)
            {
                gteIt.remove();
            }
        }
        if (gteKeys.size() == 0)
        {
            schedulerBean.setSchedulerState(SchedulerState.NO_MATCHED_REGION.getCode());
            return false;
        }
        if (first)
        {
            RedisUtil.sunionstore(middleKey, gteKeys.toArray(new String[0]));
            first = false;
        }
        else
        {
            RedisUtil.sunionstore(tempKey, gteKeys.toArray(new String[0]));
            RedisUtil.sinterstore(middleKey, tempKey, middleKey);
        }
        return true;
    }

    protected Boolean processOperatorLte(SchedulerBean schedulerBean, LabelSelector labelSelector, Boolean first, String redisKeyPrefix, String middleKey, String tempKey)
    {
        String lteKeyPattern = redisKeyPrefix + labelSelector.getKey() + ":*";
        List<String> lteKeys = RedisUtil.keys(lteKeyPattern);
        if (null == lteKeys)
        {
            schedulerBean.setSchedulerState(SchedulerState.REDIS_ERROR.getCode());
            return false;
        }
        Iterator<String> lteIt=lteKeys.iterator();
        BigDecimal lteValue = new BigDecimal(labelSelector.getValue());
        while (lteIt.hasNext())
        {
            String key=lteIt.next();
            BigDecimal num = new BigDecimal(key.replace(redisKeyPrefix + labelSelector.getKey() + ":", ""));
            if (num.compareTo(lteValue) > 0)
            {
                lteIt.remove();
            }
        }
        if (lteKeys.size() == 0)
        {
            schedulerBean.setSchedulerState(SchedulerState.NO_MATCHED_REGION.getCode());
            return false;
        }
        if (first)
        {
            RedisUtil.sunionstore(middleKey, lteKeys.toArray(new String[0]));
            first = false;
        }
        else
        {
            RedisUtil.sunionstore(tempKey, lteKeys.toArray(new String[0]));
            RedisUtil.sinterstore(middleKey, tempKey, middleKey);
        }
        return true;
    }
}
