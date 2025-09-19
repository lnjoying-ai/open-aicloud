package com.lnjoying.justice.scheduler.scheduler;

import com.lnjoying.justice.scheduler.scheduler.clean.RedisCacheCleaner;
import com.lnjoying.justice.scheduler.scheduler.framework.noderesources.ResourcesUtil;
import com.lnjoying.justice.schema.common.scheduler.SchedulerState;
import com.lnjoying.justice.scheduler.common.constant.Strategy;
import com.lnjoying.justice.scheduler.domain.model.SchedulerBean;
import com.lnjoying.justice.scheduler.scheduler.bind.BindNode;
import com.lnjoying.justice.scheduler.scheduler.preprocess.PreProcess;
import com.lnjoying.justice.scheduler.scheduler.strategy.DefaultStrategy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Scheduler
{
    protected static Logger LOGGER = LogManager.getLogger();

    @Autowired
    DefaultStrategy defaultScheduler;

    @Autowired
    PreProcess preProcess;

    @Autowired
    BindNode bindNode;

    @Autowired
    RedisCacheCleaner redisCacheCleaner;

    @Autowired
    ResourcesUtil resourcesUtil;

    public void scheduler(SchedulerBean schedulerBean)
    {
        LOGGER.info("Scheduling start, product type: {}.", schedulerBean.getSchedulerMsgType());

        preProcess(schedulerBean);

        try
        {
            if (schedulerBean.getSchedulerState() == SchedulerState.SCHEDULING.getCode())
            {
                doScheduler(schedulerBean);

                checkResult(schedulerBean);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            schedulerBean.setSchedulerState(SchedulerState.SCHEDULER_ERROR.getCode());
        }

        bindNode(schedulerBean);
        doClean(schedulerBean);

        LOGGER.info("Scheduling end, result: {}.", schedulerBean.getSchedulerState());
    }

    protected void preProcess(SchedulerBean schedulerBean)
    {
        preProcess.preProcess(schedulerBean);
    }

    protected void doScheduler(SchedulerBean schedulerBean)
    {
        Strategy strategy = Strategy.fromValue(schedulerBean.getReq().getStrategy());
        if (null == strategy)
        {
            schedulerBean.setSchedulerState(SchedulerState.STRATEGY_NOT_EXISTS.getCode());
        }
        else
        {
            switch(strategy)
            {
                case DEFAULT_STRATEGY:
                    defaultScheduler.doScheduler(schedulerBean);
                    break;
                default:
                    defaultScheduler.doScheduler(schedulerBean);
            }
        }
    }

    protected void checkResult(SchedulerBean schedulerBean)
    {
        if(schedulerBean.getSchedulerState() == SchedulerState.SUCCESS.getCode() && schedulerBean.getReq().getReplicaNum() > 0 && schedulerBean.getBindRelations().size() != schedulerBean.getReq().getReplicaNum())
        {
            releaseResourceWhileScheduleFail(schedulerBean);
            schedulerBean.setSchedulerState(SchedulerState.RESULT_NOT_MATCH_REQUIRE.getCode());
        }
    }

    protected void releaseResourceWhileScheduleFail(SchedulerBean schedulerBean)
    {
        resourcesUtil.releaseResourceWhileScheduleFail(schedulerBean.getBindRelations());
    }

    protected void bindNode(SchedulerBean schedulerBean)
    {
        bindNode.bindNode(schedulerBean);
    }

    protected void doClean(SchedulerBean schedulerBean)
    {
        redisCacheCleaner.doClean(schedulerBean);
    }
}
