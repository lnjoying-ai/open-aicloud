package com.lnjoying.justice.scheduler.scheduler.framework.labelcheck;

import com.google.common.collect.Lists;
import com.lnjoying.justice.scheduler.common.constant.*;
import com.lnjoying.justice.scheduler.config.LabelProperty;
import com.lnjoying.justice.scheduler.domain.model.AssignEdgeReq;
import com.lnjoying.justice.scheduler.domain.model.SchedulerBean;
import com.lnjoying.justice.scheduler.scheduler.framework.BaseElement;
import com.lnjoying.justice.schema.common.scheduler.LabelType;
import com.lnjoying.justice.schema.entity.dev.LabelSelector;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.*;

@Component
public class LabelCheck extends BaseElement
{
    @Autowired
    LabelProperty labelProperty;

    @PostConstruct
    public void init(){
        this.systemLabelSet.add(labelProperty.getRegionGpu());
        this.systemLabelSet.add(labelProperty.getSiteGpu());
        this.systemLabelSet.add(labelProperty.getNodeGpu());
        this.labelPatternMap.put(LabelType.NODE.getValue(), labelProperty.getNodeProjectName());
        this.labelPatternMap.put(LabelType.SITE.getValue(), labelProperty.getSiteProjectName());
        this.labelPatternMap.put(LabelType.REGION.getValue(), labelProperty.getRegionProjectName());
    }

    LabelCheck()
    {
        schedulerElement = SchedulerElement.CHECK_LABEL;
    }

    private Set<String> systemLabelSet = new HashSet<>();

    private Map<String, String> labelPatternMap = new HashMap<>();

    @Override
    public void doScheduler(SchedulerBean schedulerBean)
    {
        if (!isScheduling(schedulerBean.getSchedulerState()))
        {
            return;
        }

        boolean isSkipLabelCheck = isSkipElement(schedulerBean.getStrategyMask());

        LOGGER.info("[element]name: {} mask: {}, skip label check: {}.", getElementName(), Integer.toBinaryString(getElementMask()), isSkipLabelCheck);

        checkLabelSelector(schedulerBean.getReq(), isSkipLabelCheck);
    }

    private void checkLabelSelector(AssignEdgeReq assignEdgeReq, boolean skipLabelCheck)
    {
        if (!assignEdgeReq.getLabelSelectorMap().isEmpty())
        {
            for (Map.Entry<String, List<LabelSelector>> entry : assignEdgeReq.getLabelSelectorMap().entrySet())
            {
                String pattern = labelPatternMap.get(entry.getKey());
                if (null == pattern)
                {
                    continue;
                }

                entry.getValue().removeIf(labelSelector -> systemLabelSet.contains(labelSelector.getKey())
                        || (!skipLabelCheck && (!checkLabelSelectorKeyPattern(labelSelector, pattern) || !checkLabelSelectorType(labelSelector))));
            }
        }
        genGpuLabelSelector(assignEdgeReq);

        genNodeOwnerLabelSelector(assignEdgeReq);
    }

    private boolean checkLabelSelectorKeyPattern(LabelSelector labelSelector, String pattern)
    {
        return labelSelector.getKey().startsWith(pattern);
    }

    private boolean checkLabelSelectorType(LabelSelector labelSelector)
    {
        LabelSelectorType labelSelectorType = LabelSelectorType.fromValue(labelSelector.getType());
        if (null == labelSelectorType)
        {
            return false;
        }
        switch (labelSelectorType)
        {
            case MUST:
                return checkLabelSelectorOperator(labelSelector);
            case MUST_NOT:
                labelSelector.setType(LabelSelectorType.MUST.getValue());
                return convertNot(labelSelector);
            case PREFER:
                return false;
            case PREFER_NOT:
                return false;
            default:
                return false;
        }
    }

    private boolean checkLabelSelectorOperator(LabelSelector labelSelector)
    {
        LabelSelectorOperator labelSelectorOperator = LabelSelectorOperator.fromValue(labelSelector.getOperator());
        if (null == labelSelectorOperator)
        {
            return false;
        }
        switch (labelSelectorOperator)
        {
            case IN:
            case NOT_IN:
                if (StringUtils.isEmpty(labelSelector.getValue())) return false;
                break;
            case EXISTS:
            case NOT_EXISTS:
                break;
            case GT:
            case LT:
            case GTE:
            case LTE:
                if (!isNumeric(labelSelector.getValue())) return false;
                break;
            default:
                return false;
        }
        return true;
    }

    private boolean convertNot(LabelSelector labelSelector)
    {
        LabelSelectorOperator labelSelectorOperator = LabelSelectorOperator.fromValue(labelSelector.getOperator());
        if (null == labelSelectorOperator)
        {
            return false;
        }
        switch (labelSelectorOperator)
        {
            case IN:
                labelSelector.setOperator(LabelSelectorOperator.NOT_IN.getValue());
                if (StringUtils.isEmpty(labelSelector.getKey())) return false;
                break;
            case NOT_IN:
                labelSelector.setOperator(LabelSelectorOperator.IN.getValue());
                if (StringUtils.isEmpty(labelSelector.getKey())) return false;
                break;
            case EXISTS:
                labelSelector.setOperator(LabelSelectorOperator.NOT_EXISTS.getValue());
                break;
            case NOT_EXISTS:
                labelSelector.setOperator(LabelSelectorOperator.EXISTS.getValue());
                break;
            case GT:
                labelSelector.setOperator(LabelSelectorOperator.LTE.getValue());
                if (!isNumeric(labelSelector.getKey())) return false;
                break;
            case LT:
                labelSelector.setOperator(LabelSelectorOperator.GTE.getValue());
                if (!isNumeric(labelSelector.getKey())) return false;
                break;
            case GTE:
                labelSelector.setOperator(LabelSelectorOperator.LT.getValue());
                if (!isNumeric(labelSelector.getKey())) return false;
                break;
            case LTE:
                labelSelector.setOperator(LabelSelectorOperator.GT.getValue());
                if (!isNumeric(labelSelector.getKey())) return false;
                break;
            default:
                return false;
        }
        return true;
    }

    private void genGpuLabelSelector(AssignEdgeReq assignEdgeReq)
    {
        LabelSelector regionLabelSelector = new LabelSelector();
        regionLabelSelector.setKey(labelProperty.getRegionGpu());

        LabelSelector siteLabelSelector = new LabelSelector();
        siteLabelSelector.setKey(labelProperty.getSiteGpu());

        LabelSelector nodeLabelSelector = new LabelSelector();
        nodeLabelSelector.setKey(labelProperty.getNodeGpu());

        if (null != assignEdgeReq.getDevNeedInfo().getGpu() && assignEdgeReq.getDevNeedInfo().getGpu().getGpuNum() >= 0)
        {
            regionLabelSelector.setType(LabelSelectorType.PREFER.getValue());
            siteLabelSelector.setType(LabelSelectorType.PREFER.getValue());
            nodeLabelSelector.setType(LabelSelectorType.MUST.getValue());

            if (null != assignEdgeReq.getDevNeedInfo().getGpu() && null != assignEdgeReq.getDevNeedInfo().getGpu().getGpuType())
            {
                List<String> gpuList = new ArrayList<>();
                gpuList.add(assignEdgeReq.getDevNeedInfo().getGpu().getGpuType());
                String gpuListJson = StringUtils.join(gpuList.toArray(), ",");
                regionLabelSelector.setOperator(LabelSelectorOperator.IN.getValue());
                regionLabelSelector.setValue(gpuListJson);
                siteLabelSelector.setOperator(LabelSelectorOperator.IN.getValue());
                siteLabelSelector.setValue(gpuListJson);
                nodeLabelSelector.setOperator(LabelSelectorOperator.IN.getValue());
                nodeLabelSelector.setValue(gpuListJson);
            }
            else
            {
                regionLabelSelector.setOperator(LabelSelectorOperator.EXISTS.getValue());
                siteLabelSelector.setOperator(LabelSelectorOperator.EXISTS.getValue());
                nodeLabelSelector.setOperator(LabelSelectorOperator.EXISTS.getValue());
            }
        }
        else
        {
            regionLabelSelector.setType(LabelSelectorType.PREFER.getValue());
            regionLabelSelector.setOperator(LabelSelectorOperator.NOT_EXISTS.getValue());
            siteLabelSelector.setType(LabelSelectorType.PREFER.getValue());
            siteLabelSelector.setOperator(LabelSelectorOperator.NOT_EXISTS.getValue());
            nodeLabelSelector.setType(LabelSelectorType.PREFER.getValue());
            nodeLabelSelector.setOperator(LabelSelectorOperator.NOT_EXISTS.getValue());
        }

        if (assignEdgeReq.getLabelSelectorMap().containsKey(LabelType.NODE.getValue()))
        {
            assignEdgeReq.getLabelSelectorMap().get(LabelType.NODE.getValue()).add(nodeLabelSelector);
        }
        else
        {
            assignEdgeReq.getLabelSelectorMap().put(LabelType.NODE.getValue(), Lists.newArrayList(nodeLabelSelector));
        }

        if (assignEdgeReq.getLabelSelectorMap().containsKey(LabelType.SITE.getValue()))
        {
            assignEdgeReq.getLabelSelectorMap().get(LabelType.SITE.getValue()).add(siteLabelSelector);
        }
        else
        {
            assignEdgeReq.getLabelSelectorMap().put(LabelType.SITE.getValue(), Lists.newArrayList(siteLabelSelector));
        }

        if (assignEdgeReq.getLabelSelectorMap().containsKey(LabelType.REGION.getValue()))
        {
            assignEdgeReq.getLabelSelectorMap().get(LabelType.REGION.getValue()).add(regionLabelSelector);
        }
        else
        {
            assignEdgeReq.getLabelSelectorMap().put(LabelType.REGION.getValue(), Lists.newArrayList(regionLabelSelector));
        }
    }

    private void genNodeOwnerLabelSelector(AssignEdgeReq assignEdgeReq)
    {
        if (StringUtils.isNotEmpty(assignEdgeReq.getBpId()))
        {
            LabelSelector nodeLabelSelector = new LabelSelector();
            nodeLabelSelector.setKey(labelProperty.getNodeBpId());
            nodeLabelSelector.setType(LabelSelectorType.MUST.getValue());
            nodeLabelSelector.setOperator(LabelSelectorOperator.IN.getValue());
            nodeLabelSelector.setValue(assignEdgeReq.getBpId());

            if (assignEdgeReq.getLabelSelectorMap().containsKey(LabelType.NODE.getValue()))
            {
                List<LabelSelector> nodeLabelSelectors = assignEdgeReq.getLabelSelectorMap().get(LabelType.NODE.getValue());
                if (!nodeLabelSelectors.stream().anyMatch(m->m.getKey().equals(labelProperty.getNodeBpId())))
                {
                    assignEdgeReq.getLabelSelectorMap().get(LabelType.NODE.getValue()).add(nodeLabelSelector);
                }
            }
            else
            {
                assignEdgeReq.getLabelSelectorMap().put(LabelType.NODE.getValue(), Lists.newArrayList(nodeLabelSelector));
            }
        }
    }

    public static boolean isNumeric(String str)
    {
        try
        {
            new BigDecimal(str);
        }
        catch (Exception e)
        {
            return false;
        }
        return true;
    }
}
