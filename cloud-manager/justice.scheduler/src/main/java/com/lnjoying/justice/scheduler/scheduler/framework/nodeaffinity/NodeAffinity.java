package com.lnjoying.justice.scheduler.scheduler.framework.nodeaffinity;

import com.lnjoying.justice.scheduler.common.constant.*;
import com.lnjoying.justice.scheduler.domain.model.SchedulerBean;
import com.lnjoying.justice.scheduler.scheduler.framework.AffinityElement;
import com.lnjoying.justice.schema.common.scheduler.LabelType;
import com.lnjoying.justice.schema.common.scheduler.SchedulerState;
import com.lnjoying.justice.schema.entity.dev.LabelSelector;
import com.micro.core.persistence.redis.RedisUtil;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class NodeAffinity extends AffinityElement
{
    NodeAffinity()
    {
        schedulerElement = SchedulerElement.NODE_AFFINITY;
    }

    @Override
    public void doScheduler(SchedulerBean schedulerBean)
    {
        if (!isScheduling(schedulerBean.getSchedulerState()) || isSkipElement(schedulerBean.getStrategyMask()))
        {
            return;
        }

        LOGGER.info("[element]name: {} mask: {}.", getElementName(), Integer.toBinaryString(getElementMask()));

        String middleKey = RedisCache.SCHED_TEMP_EDGE + schedulerBean.getReq().getWaitAssignId();
        String tempKey = RedisCache.SCHED_TEMP + schedulerBean.getReq().getWaitAssignId();

        RedisUtil.delete(middleKey);
        RedisUtil.delete(tempKey);

        if ((schedulerBean.getAffinityState() & AffinityState.SITE_AFFINITY.getCode()) != 0)
        {
            Set<String> sites = RedisUtil.smembers(schedulerBean.getSiteRedisKey());
            if (null == sites || sites.size()==0)
            {
                schedulerBean.setSchedulerState(SchedulerState.NO_MATCHED_SITE.getCode());
                return;
            }
            List<String> labelKeys = new ArrayList<>();
            for (String site:sites)
            {
                String labelKey = RedisCache.SITE_ONLINE_EDGE + site;
                labelKeys.add(labelKey);
            }
            RedisUtil.sunionstore(middleKey,labelKeys.toArray(new String[0]));
            schedulerBean.setNodeRedisKey(middleKey);
        }
        else if ((schedulerBean.getAffinityState() & AffinityState.TARGET_SITE.getCode()) != 0)
        {
            List<String> labelKeys = new ArrayList<>();
            for (String site:schedulerBean.getSites())
            {
                String labelKey = RedisCache.SITE_ONLINE_EDGE + site;
                labelKeys.add(labelKey);
            }
            RedisUtil.sunionstore(middleKey,labelKeys.toArray(new String[0]));
            schedulerBean.setNodeRedisKey(middleKey);
        }
        else if ((schedulerBean.getAffinityState() & AffinityState.SITE_ALL.getCode()) != 0)
        {
            if ((schedulerBean.getAffinityState() & AffinityState.TARGET_REGION.getCode()) != 0)
            {
                List<String> labelKeys = new ArrayList<>();
                for (String region:schedulerBean.getRegions())
                {
                    String labelKey = RedisCache.REGION_ONLINE_EDGE + region;
                    labelKeys.add(labelKey);
                }
                RedisUtil.sunionstore(middleKey,labelKeys.toArray(new String[0]));
                schedulerBean.setNodeRedisKey(middleKey);
            }
            if ((schedulerBean.getAffinityState() & AffinityState.REGION_AFFINITY.getCode()) != 0)
            {
                Set<String> regions = RedisUtil.smembers(schedulerBean.getRegionRedisKey());
                if (null == regions || regions.size()==0)
                {
                    schedulerBean.setSchedulerState(SchedulerState.NO_MATCHED_REGION.getCode());
                    return;
                }
                List<String> labelKeys = new ArrayList<>();
                for (String region:regions)
                {
                    String labelKey = RedisCache.REGION_ONLINE_EDGE + region;
                    labelKeys.add(labelKey);
                }
                RedisUtil.sunionstore(middleKey,labelKeys.toArray(new String[0]));
                schedulerBean.setNodeRedisKey(middleKey);
            }
        }

        List<LabelSelector> nodeLabelSelector;

        if (schedulerBean.getReq().getLabelSelectorMap().containsKey(LabelType.NODE.getValue())
                && !(nodeLabelSelector = schedulerBean.getReq().getLabelSelectorMap().get(LabelType.NODE.getValue())).isEmpty())
        {
            schedulerBean.setAffinityState(schedulerBean.getAffinityState() | AffinityState.NODE_AFFINITY.getCode());

            String allNodeKey = RedisCache.ALL_ONLINE_EDGE;

            for (LabelSelector labelSelector : nodeLabelSelector)
            {
                if (!labelSelector.getType().equals(LabelSelectorType.MUST.getValue()))
                {
                    continue;
                }
                LabelSelectorOperator labelSelectorOperator = LabelSelectorOperator.fromValue(labelSelector.getOperator());
                if (null == labelSelectorOperator)
                {
                    continue;
                }

                boolean first = (schedulerBean.getAffinityState() & AffinityState.REGION_ALL.getCode()) != 0 && (schedulerBean.getAffinityState() & AffinityState.SITE_ALL.getCode()) != 0;

                switch (labelSelectorOperator)
                {
                    case IN:
                        processOperatorIn(labelSelector, first, RedisCache.LABEL_EDGE, middleKey, tempKey);
                        break;
                    case NOT_IN:
                        processOperatorNotIn(labelSelector, first, RedisCache.LABEL_EDGE, middleKey, allNodeKey);
                        break;
                    case EXISTS:
                        processOperatorExists(labelSelector, first, RedisCache.LABEL_EDGE, middleKey, allNodeKey);
                        break;
                    case NOT_EXISTS:
                        processOperatorNotExists(labelSelector, first, RedisCache.LABEL_EDGE, middleKey, allNodeKey);
                        break;
                    case GT:
                        if (!processOperatorGt(schedulerBean, labelSelector, first, RedisCache.LABEL_EDGE, middleKey, tempKey))
                        {
                            return;
                        }
                        break;
                    case LT:
                        if (!processOperatorLt(schedulerBean, labelSelector, first, RedisCache.LABEL_EDGE, middleKey, tempKey))
                        {
                            return;
                        }
                        break;
                    case GTE:
                        if (!processOperatorGte(schedulerBean, labelSelector, first, RedisCache.LABEL_EDGE, middleKey, tempKey))
                        {
                            return;
                        }
                        break;
                    case LTE:
                        if (!processOperatorLte(schedulerBean, labelSelector, first, RedisCache.LABEL_EDGE, middleKey, tempKey))
                        {
                            return;
                        }
                        break;
                    default:
                        schedulerBean.setSchedulerState(SchedulerState.LABEL_SELECTOR_ERROR.getCode());
                        return;
                }
            }
            schedulerBean.setNodeRedisKey(middleKey);
        }
        else
        {
            schedulerBean.setAffinityState(schedulerBean.getAffinityState() | AffinityState.NODE_ALL.getCode());
            if ((schedulerBean.getAffinityState() & AffinityState.REGION_ALL.getCode()) != 0 && (schedulerBean.getAffinityState() & AffinityState.SITE_ALL.getCode()) != 0)
            {
                String allNodeKey = RedisCache.ALL_ONLINE_EDGE;
                schedulerBean.setNodeRedisKey(allNodeKey);
            }
            else
            {
                schedulerBean.setNodeRedisKey(middleKey);
            }
        }
    }
}
