package com.lnjoying.justice.scheduler.scheduler.scheduler;

import com.lnjoying.justice.scheduler.domain.model.SchedulerBean;
import com.lnjoying.justice.scheduler.scheduler.Scheduler;
import com.lnjoying.justice.scheduler.scheduler.bind.BindNodeToCluster;
import com.lnjoying.justice.scheduler.scheduler.preprocess.ClusterPreProcess;
import com.lnjoying.justice.schema.common.scheduler.SchedulerState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ClusterScheduler extends Scheduler
{
    @Autowired
    ClusterPreProcess clusterPreProcess;

    @Autowired
    BindNodeToCluster bindNodeToCluster;

    public void scheduler(SchedulerBean schedulerBean)
    {
        LOGGER.info("Scheduling start, product type: {}.", schedulerBean.getSchedulerMsgType());
        List<SchedulerBean> successSchedulerBeans = new ArrayList<>();

        List<SchedulerBean> tempSchedulerBeans = clusterPreProcess(schedulerBean);

        tempSchedulerBeans.stream().sorted(Comparator.comparing(SchedulerBean::getSchedulerState).reversed()).collect(Collectors.toList());

        for (SchedulerBean tempSchedulerBean : tempSchedulerBeans)
        {
            if (tempSchedulerBean.getSchedulerState() == SchedulerState.SCHEDULING.getCode())
            {
                doScheduler(tempSchedulerBean);

                if (tempSchedulerBean.getSchedulerState() > SchedulerState.SUCCESS.getCode())
                {
                    successSchedulerBeans.forEach(this::releaseResourceWhileScheduleFail);
                    schedulerBean.setSchedulerState(tempSchedulerBean.getSchedulerState());
                    break;
                }
                if (!clusterCheckResult(tempSchedulerBean))
                {
                    successSchedulerBeans.forEach(this::releaseResourceWhileScheduleFail);
                    schedulerBean.setSchedulerState(SchedulerState.RESULT_NOT_MATCH_REQUIRE.getCode());
                    break;
                }

                successSchedulerBeans.add(tempSchedulerBean);
            }
            else if (tempSchedulerBean.getSchedulerState() == SchedulerState.SUCCESS.getCode())
            {
                successSchedulerBeans.add(tempSchedulerBean);
            }
            else
            {
                successSchedulerBeans.forEach(this::releaseResourceWhileScheduleFail);
                schedulerBean.setSchedulerState(tempSchedulerBean.getSchedulerState());
                break;
            }
        }

        if (successSchedulerBeans.size() == tempSchedulerBeans.size())
        {
            schedulerBean.setSchedulerState(SchedulerState.SUCCESS.getCode());
        }
        bindNode(schedulerBean, successSchedulerBeans);

        successSchedulerBeans.forEach(this::doClean);

        LOGGER.info("Scheduling end, result: {}.", schedulerBean.getSchedulerState());
    }

    protected List<SchedulerBean> clusterPreProcess(SchedulerBean schedulerBean)
    {
        return clusterPreProcess.preProcess(schedulerBean);
    }

    protected boolean clusterCheckResult(SchedulerBean schedulerBean)
    {
        if(schedulerBean.getSchedulerState() == SchedulerState.SUCCESS.getCode() && schedulerBean.getReq().getReplicaNum() > 0 && schedulerBean.getBindRelations().size() != schedulerBean.getReq().getReplicaNum())
        {
            releaseResourceWhileScheduleFail(schedulerBean);
            schedulerBean.setSchedulerState(SchedulerState.RESULT_NOT_MATCH_REQUIRE.getCode());

            doClean(schedulerBean);

            return false;
        }
        return true;
    }

    protected void bindNode(SchedulerBean schedulerBean, List<SchedulerBean> successSchedulerBeans)
    {
        bindNodeToCluster.bindNode(schedulerBean, successSchedulerBeans);
    }
}
