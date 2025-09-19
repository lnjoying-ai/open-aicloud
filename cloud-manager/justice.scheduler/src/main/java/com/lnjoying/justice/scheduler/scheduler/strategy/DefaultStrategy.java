package com.lnjoying.justice.scheduler.scheduler.strategy;

import com.lnjoying.justice.scheduler.common.constant.SchedulerElement;
import com.lnjoying.justice.scheduler.common.constant.Strategy;
import com.lnjoying.justice.scheduler.domain.model.SchedulerBean;
import com.lnjoying.justice.scheduler.scheduler.framework.ElementInterface;
import com.lnjoying.justice.scheduler.scheduler.framework.bindrelations.BindRelations;
import com.lnjoying.justice.scheduler.scheduler.framework.directtarget.DirectTarget;
import com.lnjoying.justice.scheduler.scheduler.framework.imagelocality.ImageLocality;
import com.lnjoying.justice.scheduler.scheduler.framework.labelcheck.LabelCheck;
import com.lnjoying.justice.scheduler.scheduler.framework.nodeaffinity.NodeAffinity;
import com.lnjoying.justice.scheduler.scheduler.framework.nodeports.NodePorts;
import com.lnjoying.justice.scheduler.scheduler.framework.noderesources.NodeResources;
import com.lnjoying.justice.scheduler.scheduler.framework.ononenode.OnOneNode;
import com.lnjoying.justice.scheduler.scheduler.framework.regionaffinity.RegionAffinity;
import com.lnjoying.justice.scheduler.scheduler.framework.siteaffinity.SiteAffinity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class DefaultStrategy implements StrategyInterface
{
    private static Logger LOGGER = LogManager.getLogger();

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public String getStrategyName()
    {
        return Strategy.DEFAULT_STRATEGY.getValue();
    }

    @Override
    public Integer getStrategyMask()
    {
        int mask = 0;
        mask = mask | SchedulerElement.CHECK_LABEL.getMask() | SchedulerElement.DIRECT_TARGET.getMask()
                | SchedulerElement.ON_ONE_NODE.getMask() | SchedulerElement.REGION_AFFINITY.getMask()
                | SchedulerElement.SITE_AFFINITY.getMask() | SchedulerElement.NODE_AFFINITY.getMask()
                | SchedulerElement.NODE_RESOURCES.getMask() | SchedulerElement.NODE_PORTS.getMask()
                | SchedulerElement.IMAGE_LOCALITY.getMask() | SchedulerElement.BIND_LEAST.getMask()
                | SchedulerElement.BIND_RELATIONS.getMask();
        return mask;
    }

    @Override
    public void doScheduler(SchedulerBean schedulerBean)
    {
        Integer strategyMask = getStrategyMask();
        LOGGER.info("[step 2]do scheduler, strategy: {}, mask: {}.", getStrategyName(), Integer.toBinaryString(strategyMask));
        schedulerBean.setStrategyMask(strategyMask);

        List<ElementInterface> elementInterfaceBeanList = applicationContext.getBeansOfType(ElementInterface.class).values().stream().sorted(Comparator.comparing(ElementInterface::getElementMask)).collect(Collectors.toList());;
        elementInterfaceBeanList.forEach(elementInterface -> elementInterface.doScheduler(schedulerBean));
    }
}
