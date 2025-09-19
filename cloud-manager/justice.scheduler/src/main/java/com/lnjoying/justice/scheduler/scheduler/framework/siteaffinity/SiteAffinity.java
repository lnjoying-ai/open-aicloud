package com.lnjoying.justice.scheduler.scheduler.framework.siteaffinity;

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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class SiteAffinity extends AffinityElement
{
    SiteAffinity()
    {
        schedulerElement = SchedulerElement.SITE_AFFINITY;
    }

    @Autowired
    DevOperator devOperator;

    @Override
    public void doScheduler(SchedulerBean schedulerBean)
    {
        if (!schedulerBean.getSchedulerState().equals(SchedulerState.SCHEDULING.getCode()) || (schedulerBean.getStrategyMask()&getElementMask()) == 0)
        {
            return;
        }

        LOGGER.info("[element]name: {} mask: {}.", getElementName(), Integer.toBinaryString(getElementMask()));

        if (!isHaveTargetSite(schedulerBean))
        {
            String middleKey = RedisCache.SCHED_TEMP_SITE + schedulerBean.getReq().getWaitAssignId();
            String tempKey = RedisCache.SCHED_TEMP + schedulerBean.getReq().getWaitAssignId();

            RedisUtil.delete(middleKey);
            RedisUtil.delete(tempKey);

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
                    String labelKey = RedisCache.SCHED_REGION_SITE + region;
                    labelKeys.add(labelKey);

                    Long labelKeyExists = RedisUtil.exists(labelKey);
                    if (null == labelKeyExists)
                    {
                        schedulerBean.setSchedulerState(SchedulerState.REDIS_ERROR.getCode());
                        return;
                    }
                    else if (0 == labelKeyExists)
                    {
                        List<String> sites = devOperator.getSiteByRegionId(region);
                        if (null != sites && !sites.isEmpty())
                        {
                            RedisUtil.sadd(labelKey, "", sites.toArray(new String[0]));
                        }
                    }
                }
                RedisUtil.sunionstore(middleKey, labelKeys.toArray(new String[0]));
                schedulerBean.setSiteRedisKey(middleKey);
            }
            else if ((schedulerBean.getAffinityState() & AffinityState.TARGET_REGION.getCode()) != 0)
            {
                List<String> labelKeys = new ArrayList<>();
                for (String region:schedulerBean.getRegions())
                {
                    String labelKey = RedisCache.SCHED_REGION_SITE + region;
                    labelKeys.add(labelKey);

                    Long labelKeyExists = RedisUtil.exists(labelKey);
                    if (null == labelKeyExists)
                    {
                        schedulerBean.setSchedulerState(SchedulerState.REDIS_ERROR.getCode());
                        return;
                    }
                    else if (0 == labelKeyExists)
                    {
                        List<String> sites = devOperator.getSiteByRegionId(region);
                        if (null != sites && !sites.isEmpty())
                        {
                            RedisUtil.sadd(labelKey, "", sites.toArray(new String[0]));
                        }
                    }
                }
                RedisUtil.sunionstore(middleKey, labelKeys.toArray(new String[0]));
                schedulerBean.setSiteRedisKey(middleKey);
            }

            if (!schedulerBean.getSchedulerState().equals(SchedulerState.SCHEDULING.getCode()))
            {
                return;
            }

            List<LabelSelector> siteLabelSelector;

            if (schedulerBean.getReq().getLabelSelectorMap().containsKey(LabelType.SITE.getValue())
                    && !(siteLabelSelector = schedulerBean.getReq().getLabelSelectorMap().get(LabelType.SITE.getValue())).isEmpty())
            {
                schedulerBean.setAffinityState(schedulerBean.getAffinityState() | AffinityState.SITE_AFFINITY.getCode());

                String allSiteKey = RedisCache.SCHED_ALL_SITE;
                Long allSiteKeyExists = RedisUtil.exists(allSiteKey);
                if (null == allSiteKeyExists)
                {
                    schedulerBean.setSchedulerState(SchedulerState.REDIS_ERROR.getCode());
                    return;
                }
                else if (0 == allSiteKeyExists)
                {
                    List<String> sites = devOperator.getAllSiteId();
                    if (null != sites && !sites.isEmpty())
                    {
                        RedisUtil.sadd(allSiteKey, "", sites.toArray(new String[0]));
                    }
                }

                for (LabelSelector labelSelector : siteLabelSelector)
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

                    boolean first = (schedulerBean.getAffinityState() & AffinityState.REGION_ALL.getCode()) != 0;

                    switch (labelSelectorOperator)
                    {
                        case IN:
                            processOperatorIn(labelSelector, first, RedisCache.LABEL_SITE, middleKey, tempKey);
                            break;
                        case NOT_IN:
                            processOperatorNotIn(labelSelector, first, RedisCache.LABEL_SITE, middleKey, allSiteKey);
                            break;
                        case EXISTS:
                            processOperatorExists(labelSelector, first, RedisCache.LABEL_SITE, middleKey, allSiteKey);
                            break;
                        case NOT_EXISTS:
                            processOperatorNotExists(labelSelector, first, RedisCache.LABEL_SITE, middleKey, allSiteKey);
                            break;
                        case GT:
                            if (!processOperatorGt(schedulerBean, labelSelector, first, RedisCache.LABEL_SITE, middleKey, tempKey))
                            {
                                return;
                            }
                            break;
                        case LT:
                            if (!processOperatorGt(schedulerBean, labelSelector, first, RedisCache.LABEL_SITE, middleKey, tempKey))
                            {
                                return;
                            }
                            break;
                        case GTE:
                            if (!processOperatorGte(schedulerBean, labelSelector, first, RedisCache.LABEL_SITE, middleKey, tempKey))
                            {
                                return;
                            }
                            break;
                        case LTE:
                            if (!processOperatorLte(schedulerBean, labelSelector, first, RedisCache.LABEL_SITE, middleKey, tempKey))
                            {
                                return;
                            }
                            break;
                        default:
                            schedulerBean.setSchedulerState(SchedulerState.LABEL_SELECTOR_ERROR.getCode());
                            return;
                    }
                }
                schedulerBean.setSiteRedisKey(middleKey);
            }
            else
            {
                schedulerBean.setAffinityState(schedulerBean.getAffinityState() | AffinityState.SITE_ALL.getCode());
            }
        }
    }

    private boolean isHaveTargetSite(SchedulerBean schedulerBean)
    {
        if(schedulerBean.getReq().getTargetNodes().size() == 0)
        {
            return false;
        }
        boolean isHave = false;
        int state = 0;
        for (TargetNode targetNode:schedulerBean.getReq().getTargetNodes())
        {
            if (StringUtils.isEmpty(targetNode.getDstSiteId()))
            {
                if (state == 0)
                {
                    state = 1;
                    isHave = false;
                }
                else if (state != 1)
                {
                    schedulerBean.setSchedulerState(SchedulerState.TARGET_NODES_ERROR.getCode());
                    return false;
                }
            }
            else
            {
                if (state == 0)
                {
                    state = 2;
                    isHave = true;
                }
                else if (state != 2)
                {
                    schedulerBean.setSchedulerState(SchedulerState.TARGET_NODES_ERROR.getCode());
                    return false;
                }
                schedulerBean.getSites().add(targetNode.getDstSiteId());

            }
        }
        schedulerBean.setAffinityState(schedulerBean.getAffinityState() | AffinityState.TARGET_SITE.getCode());
        LOGGER.info("[SiteAffinity]target site: {}.", schedulerBean.getSites());
        return isHave;
    }
}
