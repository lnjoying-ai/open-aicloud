package com.lnjoying.justice.scheduler.scheduler.framework.bindleast;

import com.lnjoying.justice.scheduler.common.constant.SchedulerElement;
import com.lnjoying.justice.scheduler.domain.model.NodeResourcesInfo;
import com.lnjoying.justice.scheduler.domain.model.SchedulerBean;
import com.lnjoying.justice.scheduler.scheduler.framework.BaseElement;
import com.lnjoying.justice.scheduler.scheduler.framework.noderesources.ResourcesUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class BindLeast extends BaseElement
{
    BindLeast()
    {
        schedulerElement = SchedulerElement.BIND_LEAST;
    }

    @Autowired
    ResourcesUtil resourcesUtil;

    @Override
    public void doScheduler(SchedulerBean schedulerBean)
    {
        if (!isScheduling(schedulerBean.getSchedulerState()) || isSkipElement(schedulerBean.getStrategyMask()))
        {
            return;
        }

        LOGGER.info("[element]name: {} mask: {}.", getElementName(), Integer.toBinaryString(getElementMask()));

        sortByBindLeast(schedulerBean);
    }

    private void sortByBindLeast(SchedulerBean schedulerBean)
    {
        if (!schedulerBean.getNodes().isEmpty())
        {
            List<NodeResourcesInfo> nodeResourcesInfos = new ArrayList<>();
            schedulerBean.getNodes().forEach(nodeId -> {
                nodeResourcesInfos.add(resourcesUtil.getResources(nodeId));
            });
            schedulerBean.setNodes(nodeResourcesInfos.stream().sorted(Comparator.comparing(NodeResourcesInfo::getBindStackNum).thenComparing(NodeResourcesInfo::getBindInstNum)).map(NodeResourcesInfo::getNodeId).collect(Collectors.toList()));
        }
    }
}
