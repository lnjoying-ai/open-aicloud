package com.lnjoying.justice.scheduler.scheduler.framework.directtarget;

import com.lnjoying.justice.scheduler.common.constant.SchedulerElement;
import com.lnjoying.justice.scheduler.scheduler.framework.BaseElement;
import com.lnjoying.justice.schema.common.scheduler.SchedulerState;
import com.lnjoying.justice.scheduler.db.mapper.TblEdgeComputeInfoMapper;
import com.lnjoying.justice.scheduler.db.model.TblEdgeComputeInfo;
import com.lnjoying.justice.schema.entity.scheduler.BindRelation;
import com.lnjoying.justice.scheduler.domain.model.SchedulerBean;
import com.lnjoying.justice.schema.entity.dev.TargetNode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DirectTarget extends BaseElement
{
    DirectTarget()
    {
        schedulerElement = SchedulerElement.DIRECT_TARGET;
    }

    @Autowired
    TblEdgeComputeInfoMapper tblEdgeComputeInfoMapper;

    @Override
    public void doScheduler(SchedulerBean schedulerBean)
    {
        if (!isScheduling(schedulerBean.getSchedulerState()) || isSkipElement(schedulerBean.getStrategyMask()))
        {
            return;
        }

        LOGGER.info("[element]name: {} mask: {}.", getElementName(), Integer.toBinaryString(getElementMask()));

        getDirectTarget(schedulerBean);
    }

    private void getDirectTarget(SchedulerBean schedulerBean)
    {
        if (null == schedulerBean.getReq().getTargetNodes() || schedulerBean.getReq().getTargetNodes().size() == 0)
        {
            LOGGER.info("[DirectTarget]target nodes is null or empty.");
            return;
        }
        if (schedulerBean.getReq().getTargetNodes().size() == 1)
        {
            TargetNode targetNode = schedulerBean.getReq().getTargetNodes().get(0);

            if (StringUtils.isNotEmpty(targetNode.getDstNodeId())
                    && StringUtils.isNotEmpty(targetNode.getDstSiteId())
                    && StringUtils.isNotEmpty(targetNode.getDstRegionId()))
            {
                for (String refId : schedulerBean.getReq().getRefIdList())
                {
                    BindRelation bindRelation = getEdgeNodeByNodeId(targetNode.getDstNodeId(), refId);
                    if (null == bindRelation || !bindRelation.getDstRegionId().equals(targetNode.getDstRegionId())
                            || !bindRelation.getDstSiteId().equals(targetNode.getDstSiteId()))
                    {
                        LOGGER.info("[DirectTarget]direct target node not exists or info mismatch. submit node: {}, edge node: {}", targetNode, bindRelation);
                        schedulerBean.setSchedulerState(SchedulerState.DIRECT_TARGET_ERROR.getCode());
                        return;
                    }
                    schedulerBean.getBindRelations().add(bindRelation);
                }
                schedulerBean.setSchedulerState(SchedulerState.SUCCESS.getCode());
                LOGGER.info("[DirectTarget]direct target set success, skip left scheduler element.");
            }
        }
    }

    public BindRelation getEdgeNodeByNodeId(String nodeId, String refId){
        try
        {
            TblEdgeComputeInfo tblEdgeComputeInfo = tblEdgeComputeInfoMapper.selectByPrimaryKey(nodeId);
            if (tblEdgeComputeInfo != null)
            {
                BindRelation bindRelation = new BindRelation();
                bindRelation.setDstNodeId(nodeId);
                bindRelation.setDstRegionId(tblEdgeComputeInfo.getRegionId());
                bindRelation.setDstSiteId(tblEdgeComputeInfo.getSiteId());
                bindRelation.setWaitAssignId(refId);
                return bindRelation;
            }
            return null;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
}
