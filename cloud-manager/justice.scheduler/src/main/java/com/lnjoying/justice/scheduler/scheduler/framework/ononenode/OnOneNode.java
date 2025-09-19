package com.lnjoying.justice.scheduler.scheduler.framework.ononenode;

import com.lnjoying.justice.scheduler.common.constant.SchedulerElement;
import com.lnjoying.justice.scheduler.domain.model.SchedulerBean;
import com.lnjoying.justice.scheduler.scheduler.framework.BaseElement;
import com.lnjoying.justice.schema.entity.dev.*;
import org.springframework.stereotype.Component;

@Component
public class OnOneNode extends BaseElement
{
    OnOneNode()
    {
        schedulerElement = SchedulerElement.ON_ONE_NODE;
    }

    @Override
    public void doScheduler(SchedulerBean schedulerBean)
    {
        LOGGER.info("[element]name: {} mask: {}.", getElementName(), Integer.toBinaryString(getElementMask()));
    }

    public DevNeedInfo onOneNode(SchedulerBean schedulerBean)
    {
        if (!isScheduling(schedulerBean.getSchedulerState()) || isSkipElement(schedulerBean.getStrategyMask()))
        {
            return schedulerBean.getReq().getDevNeedInfo();
        }

        int replicaNum = schedulerBean.getReq().getReplicaNum();
        if (replicaNum > 1)
        {
            DevNeedInfo devNeedInfo = new DevNeedInfo();
            CpuNeed cpuNeed = new CpuNeed();
            MemNeed memNeed = new MemNeed();
            DiskNeed diskNeed = new DiskNeed();
            devNeedInfo.setCpu(cpuNeed);
            devNeedInfo.setMem(memNeed);
            devNeedInfo.setDisk(diskNeed);

            devNeedInfo.getCpu().setCpuNum(schedulerBean.getReq().getDevNeedInfo().getCpu().getCpuNum() * replicaNum);
            devNeedInfo.getMem().setMemLimit(schedulerBean.getReq().getDevNeedInfo().getMem().getMemLimit() * replicaNum);
            devNeedInfo.getDisk().setDiskLimit(schedulerBean.getReq().getDevNeedInfo().getDisk().getDiskLimit() * replicaNum);
            if (null != schedulerBean.getReq().getDevNeedInfo().getGpu() && null != schedulerBean.getReq().getDevNeedInfo().getGpu().getGpuNum())
            {
                GpuNeed gpuNeed = new GpuNeed();
                devNeedInfo.setGpu(gpuNeed);
                devNeedInfo.getGpu().setGpuNum(schedulerBean.getReq().getDevNeedInfo().getGpu().getGpuNum() * replicaNum);
            }


            return devNeedInfo;
        }
        return schedulerBean.getReq().getDevNeedInfo();
    }
}
