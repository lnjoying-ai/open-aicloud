package com.lnjoying.justice.scheduler.scheduler.framework.regionaffinity;

import com.lnjoying.justice.scheduler.common.constant.*;
import com.lnjoying.justice.scheduler.db.mapper.DevOperator;
import com.lnjoying.justice.scheduler.domain.model.SchedulerBean;
import com.lnjoying.justice.scheduler.scheduler.framework.AffinityElement;
import com.lnjoying.justice.schema.common.scheduler.LabelType;
import com.lnjoying.justice.schema.common.scheduler.SchedulerState;
import com.lnjoying.justice.schema.entity.dev.LabelSelector;
import com.lnjoying.justice.schema.entity.dev.TargetNode;
import com.micro.core.persistence.redis.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RegionAffinity extends AffinityElement
{
    RegionAffinity()
    {
        schedulerElement = SchedulerElement.REGION_AFFINITY;
    }

    @Autowired
    DevOperator devOperator;

    @Override
    public void doScheduler(SchedulerBean schedulerBean)
    {
        if (!isScheduling(schedulerBean.getSchedulerState()) || isSkipElement(schedulerBean.getStrategyMask()))
        {
            return;
        }

        LOGGER.info("[element]name: {} mask: {}.", getElementName(), Integer.toBinaryString(getElementMask()));

        if (!isHaveTargetRegion(schedulerBean))
        {
            if (!isScheduling(schedulerBean.getSchedulerState()))
            {
                return;
            }

            List<LabelSelector> regionLabelSelector;

            if (schedulerBean.getReq().getLabelSelectorMap().containsKey(LabelType.REGION.getValue())
                    && !(regionLabelSelector = schedulerBean.getReq().getLabelSelectorMap().get(LabelType.REGION.getValue())).isEmpty())
            {
                schedulerBean.setAffinityState(schedulerBean.getAffinityState() | AffinityState.REGION_AFFINITY.getCode());

                String middleKey = RedisCache.SCHED_TEMP_REGION + schedulerBean.getReq().getWaitAssignId();
                String tempKey = RedisCache.SCHED_TEMP + schedulerBean.getReq().getWaitAssignId();

                RedisUtil.delete(middleKey);
                RedisUtil.delete(tempKey);

                String allRegionKey = RedisCache.SCHED_ALL_REGION;
                Long allRegionKeyExists = RedisUtil.exists(allRegionKey);
                if (null == allRegionKeyExists)
                {
                    schedulerBean.setSchedulerState(SchedulerState.REDIS_ERROR.getCode());
                    return;
                }
                else if (0 == allRegionKeyExists)
                {
                    List<String> regions = devOperator.getAllRegionId();
                    if (null != regions && !regions.isEmpty())
                    {
                        RedisUtil.sadd(allRegionKey, "", regions.toArray(new String[0]));
                    }
                }

                boolean first = true;
                for (LabelSelector labelSelector : regionLabelSelector)
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
                    switch (labelSelectorOperator)
                    {
                        case IN:
                            processOperatorIn(labelSelector, first, RedisCache.LABEL_REGION, middleKey, tempKey);
                            break;
                        case NOT_IN:
                            processOperatorNotIn(labelSelector, first, RedisCache.LABEL_REGION, middleKey, allRegionKey);
                            break;
                        case EXISTS:
                            processOperatorExists(labelSelector, first, RedisCache.LABEL_REGION, middleKey, allRegionKey);
                            break;
                        case NOT_EXISTS:
                            processOperatorNotExists(labelSelector, first, RedisCache.LABEL_REGION, middleKey, allRegionKey);
                            break;
                        case GT:
                            if (!processOperatorGt(schedulerBean, labelSelector, first, RedisCache.LABEL_REGION, middleKey, tempKey))
                            {
                                return;
                            }
                            break;
                        case LT:
                            if (!processOperatorLt(schedulerBean, labelSelector, first, RedisCache.LABEL_REGION, middleKey, tempKey))
                            {
                                return;
                            }
                            break;
                        case GTE:
                            if (!processOperatorGte(schedulerBean, labelSelector, first, RedisCache.LABEL_REGION, middleKey, tempKey))
                            {
                                return;
                            }
                            break;
                        case LTE:
                            if (!processOperatorLte(schedulerBean, labelSelector, first, RedisCache.LABEL_REGION, middleKey, tempKey))
                            {
                                return;
                            }
                            break;
                        default:
                            schedulerBean.setSchedulerState(SchedulerState.LABEL_SELECTOR_ERROR.getCode());
                            return;
                    }
                }
                schedulerBean.setRegionRedisKey(middleKey);
            }
            else
            {
                schedulerBean.setAffinityState(schedulerBean.getAffinityState() | AffinityState.REGION_ALL.getCode());
            }
        }
    }

    private boolean isHaveTargetRegion(SchedulerBean schedulerBean)
    {
        if(schedulerBean.getReq().getTargetNodes().size() == 0)
        {
            return false;
        }

        for (TargetNode targetNode:schedulerBean.getReq().getTargetNodes())
        {
            if (StringUtils.isEmpty(targetNode.getDstRegionId()))
            {
                LOGGER.info("[RegionAffinity]error, target: {}.", schedulerBean.getReq().getTargetNodes());
                schedulerBean.setSchedulerState(SchedulerState.TARGET_NODES_ERROR.getCode());
                return false;
            }
            else
            {
                schedulerBean.getRegions().add(targetNode.getDstRegionId());
            }
        }

        schedulerBean.setAffinityState(schedulerBean.getAffinityState() | AffinityState.TARGET_REGION.getCode());
        LOGGER.info("[RegionAffinity]target region: {}.", schedulerBean.getRegions());
        return true;
    }
}
