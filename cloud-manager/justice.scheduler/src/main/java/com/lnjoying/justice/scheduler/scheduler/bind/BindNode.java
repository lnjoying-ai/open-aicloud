package com.lnjoying.justice.scheduler.scheduler.bind;

import com.lnjoying.justice.scheduler.common.constant.EdgeMonopolyState;
import com.lnjoying.justice.scheduler.common.constant.OnOneNodeStrategy;
import com.lnjoying.justice.scheduler.common.constant.RedisCache;
import com.lnjoying.justice.scheduler.common.constant.SchedulerMsgType;
import com.lnjoying.justice.scheduler.db.mapper.EdgeMonopolyOperator;
import com.lnjoying.justice.scheduler.db.model.TblSchedEdgeMonopoly;
import com.lnjoying.justice.scheduler.domain.model.SchedulerBean;
import com.lnjoying.justice.scheduler.scheduler.framework.noderesources.ResourcesUtil;
import com.lnjoying.justice.schema.entity.scheduler.BindRelation;
import com.lnjoying.justice.schema.entity.scheduler.BindResourcesResult;
import com.lnjoying.justice.schema.entity.scheduler.SchedulerResult;
import com.lnjoying.justice.schema.service.aos.AosService;
import com.lnjoying.justice.schema.service.cis.ContainerInstService;
import com.lnjoying.justice.schema.service.cmp.CloudManagerService;
import com.lnjoying.justice.schema.service.ecrm.EdgeResourceService;
import com.micro.core.persistence.redis.RedisUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.servicecomb.provider.pojo.RpcReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class BindNode
{
    protected static Logger LOGGER = LogManager.getLogger();

    @Autowired
    EdgeMonopolyOperator edgeMonopolyOperator;

    @Autowired
    ResourcesUtil resourcesUtil;

    @RpcReference(microserviceName = "cis", schemaId = "containerService")
    private ContainerInstService cisService;

    @RpcReference(microserviceName = "aos", schemaId = "aosService")
    private AosService aosService;

    @RpcReference(microserviceName = "cmp", schemaId = "cloudManagerService")
    private CloudManagerService cloudManagerService;

    @RpcReference(microserviceName = "ecrm", schemaId = "edgeResourceService")
    private EdgeResourceService edgeResourceService;

    public void bindNode(SchedulerBean schedulerBean)
    {
        LOGGER.info("[step 3]bind node, ref id: {}, scheduler state: {}.", schedulerBean.getReq().getWaitAssignId(), schedulerBean.getSchedulerState());
        switch (schedulerBean.getSchedulerMsgType())
        {
            case SchedulerMsgType.INST:
                bindNodeToInst(schedulerBean);
                break;
            case SchedulerMsgType.STACK:
                bindNodeToStack(schedulerBean);
                break;
            default:
        }
    }

    private void bindNodeToInst(SchedulerBean schedulerBean)
    {
        SchedulerResult schedulerResult = new SchedulerResult();
        schedulerResult.setSchedulerState(schedulerBean.getSchedulerState());
        schedulerResult.setBindRelations(schedulerBean.getBindRelations());
        schedulerResult.setSpecId(schedulerBean.getReq().getWaitAssignId());
        schedulerResult.setWaitAssignIdList(schedulerBean.getReq().getRefIdList());

        BindResourcesResult bindResourcesResult = null;
        try
        {
            if (schedulerResult.getSpecId().startsWith("CMP"))
            {
                bindResourcesResult = cloudManagerService.bindResources(schedulerResult);
            }
            else if (schedulerResult.getSpecId().startsWith("ECRM"))
            {
                bindResourcesResult = edgeResourceService.bindResources(schedulerResult);
            }
            else
            {
                bindResourcesResult = cisService.bindResources(schedulerResult);
            }
        }
        catch (IllegalStateException e)
        {
            LOGGER.error("microservice error, waiting for resend schedule request");
        }

        LOGGER.info("[step 3.1]bind node, scheduler result:{} , bind result: {}.", schedulerResult, bindResourcesResult);

        delResourcesCache(schedulerBean);

        if (null != bindResourcesResult)
        {
            if (null != bindResourcesResult.getSuccessBindRelations() && schedulerBean.getReq().getOnOneNode() == OnOneNodeStrategy.MONOPOLY.getCode())
            {
                bindEdgeMonopoly(bindResourcesResult.getSuccessBindRelations(), EdgeMonopolyState.INST_MONOPOLY);
            }

            if (null != bindResourcesResult.getFailBindRelations())
            {
                releaseResourceWhileScheduleFail(bindResourcesResult.getFailBindRelations());
            }
        }
        else
        {
            releaseResourceWhileScheduleFail(schedulerBean.getBindRelations());
        }
    }

    private void bindNodeToStack(SchedulerBean schedulerBean)
    {
        SchedulerResult schedulerResult = new SchedulerResult();
        schedulerResult.setSchedulerState(schedulerBean.getSchedulerState());
        schedulerResult.setBindRelations(schedulerBean.getBindRelations());
        schedulerResult.setSpecId(schedulerBean.getReq().getWaitAssignId());
        schedulerResult.setWaitAssignIdList(schedulerBean.getReq().getRefIdList());

        BindResourcesResult bindResourcesResult = null;
        try
        {
            bindResourcesResult = aosService.bindResources(schedulerResult);
        }
        catch (IllegalStateException e)
        {
            LOGGER.error("microservice error, waiting for resend schedule request");
        }

        LOGGER.info("[step 3.1]bind node, scheduler result:{} , bind result: {}.", schedulerResult, bindResourcesResult);

        delResourcesCache(schedulerBean);

        if (null != bindResourcesResult)
        {
            if (null != bindResourcesResult.getSuccessBindRelations() && schedulerBean.getReq().getOnOneNode() == OnOneNodeStrategy.MONOPOLY.getCode())
            {
                bindEdgeMonopoly(bindResourcesResult.getSuccessBindRelations(), EdgeMonopolyState.STACK_MONOPOLY);
            }

            if (null != bindResourcesResult.getFailBindRelations())
            {
                releaseResourceWhileScheduleFail(bindResourcesResult.getFailBindRelations());
            }
        }
        else
        {
            releaseResourceWhileScheduleFail(schedulerBean.getBindRelations());
        }
    }

    protected void delResourcesCache(SchedulerBean schedulerBean)
    {
        for(BindRelation bindRelation:schedulerBean.getBindRelations())
        {
            RedisUtil.delete(RedisCache.SCHED_EDGE_RESOURCES + bindRelation.getDstNodeId());
        }
    }

    protected void bindEdgeMonopoly(List<BindRelation> bindRelations, Integer monopolyStatus)
    {
        for (BindRelation bindRelation:bindRelations)
        {
            TblSchedEdgeMonopoly tblSchedEdgeMonopoly = new TblSchedEdgeMonopoly();
            tblSchedEdgeMonopoly.setNodeId(bindRelation.getDstNodeId());
            tblSchedEdgeMonopoly.setRefId(bindRelation.getWaitAssignId());
            tblSchedEdgeMonopoly.setStatus(monopolyStatus);
            tblSchedEdgeMonopoly.setCreateTime(new Date());

            edgeMonopolyOperator.insertOrUpdate(tblSchedEdgeMonopoly);
        }
    }

    protected void releaseResourceWhileScheduleFail(List<BindRelation> bindRelations)
    {
        resourcesUtil.releaseResourceWhileScheduleFail(bindRelations);
    }
}
