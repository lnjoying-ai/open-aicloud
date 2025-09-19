package com.lnjoying.justice.omc.db.repo;

import com.lnjoying.justice.omc.db.mapper.TblOmcEventMapper;
import com.lnjoying.justice.omc.db.model.TblOmcEvent;
import com.lnjoying.justice.omc.db.model.TblOmcEventExample;
import com.lnjoying.justice.omc.domain.dto.response.QueryOperationEventRsp;
import com.lnjoying.justice.omc.domain.model.search.ApiEventSearchCritical;
import com.lnjoying.justice.schema.service.omc.OmcService;
import com.micro.core.common.Utils;
import io.jsonwebtoken.lang.Collections;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.util.StringUtils.isEmpty;

/**
 * 功能描述
 *
 * @author: Robin
 * @date: 2023年10月25日 09:58
 */
@Repository
public class OmcEventRepository
{
    @Autowired
    private TblOmcEventMapper omcEventMapper;

    public int insertEvent(OmcService.OperationEvent operationEvent)
    {
        if (operationEvent == null)
        {
            return 0;
        }

        TblOmcEvent logEntity = new TblOmcEvent();
        BeanUtils.copyProperties(operationEvent, logEntity);
        if (logEntity.getId() == null)
        {
            logEntity.setId(Utils.assignUUId());
        }
        logEntity.setResourceId(operationEvent.getResourceInstId());
        logEntity.setResourceInstName(operationEvent.getResourceInstName());
        logEntity.setType(operationEvent.getType());
        logEntity.setCreatedAt(new Date());
        return omcEventMapper.insert(logEntity);
    }

    public QueryOperationEventRsp getEvents(ApiEventSearchCritical critical)
    {
        TblOmcEventExample example = new TblOmcEventExample();
        TblOmcEventExample.Criteria criteria = example.createCriteria();
        if (!isEmpty(critical.getBpId()))
        {
            criteria.andBpIdEqualTo(critical.getBpId());
        }
        if (!isEmpty(critical.getEventName()))
        {
            criteria.andNameLike("%" + critical.getEventName() + "%");
        }
        if (!isEmpty(critical.getServiceName()))
        {
            criteria.andServiceEqualTo(critical.getServiceName());
        }
        if (!isEmpty(critical.getResourceName()))
        {
            criteria.andResourceEqualTo(critical.getResourceName());
        }
        if (!isEmpty(critical.getResourceId()))
        {
            criteria.andResourceIdEqualTo(critical.getResourceId());
        }
        if (!isEmpty(critical.getResourceInstName()))
        {
            criteria.andResourceInstNameLike("%" + critical.getResourceInstName() + "%");
        }
        if (!isEmpty(critical.getType()))
        {
            criteria.andTypeEqualTo(critical.getType());
        }
        if (!isEmpty(critical.getUserId()))
        {
            criteria.andUserIdEqualTo(critical.getUserId());
        }
        if (!isEmpty(critical.getLogLevel()))
        {
            criteria.andLevelEqualTo(critical.getLogLevel());
        }
        if (critical.getStartTriggerTime() != null)
        {
            criteria.andTriggerTimeGreaterThanOrEqualTo(critical.getStartTriggerTime());
        }
        if (critical.getEndTriggerTime() != null)
        {
            criteria.andTriggerTimeLessThanOrEqualTo(critical.getEndTriggerTime());
        }
        long count = omcEventMapper.countByExample(example);
        QueryOperationEventRsp queryOperationEventRsp = new QueryOperationEventRsp();
        if (count == 0)
        {
            return queryOperationEventRsp;
        }
        queryOperationEventRsp.setTotal_num(count);
        int startRow = ((critical.getPageNum()-1) * critical.getPageSize());
        example.setOrderByClause("created_at desc");
        example.setStartRow(startRow);
        example.setPageSize(critical.getPageSize());
        List<TblOmcEvent> tblOmcEvents = omcEventMapper.selectByExample(example);
        if (Collections.isEmpty(tblOmcEvents))
        {
            return null;
        }
        queryOperationEventRsp.setEvents(tblOmcEvents.stream().map(x -> x.toDto()).collect(Collectors.toList()));
        return queryOperationEventRsp;
    }

    public long deleteEventLogByDateAfterOrEqual(Date date)
    {
        TblOmcEventExample example = new TblOmcEventExample();
        TblOmcEventExample.Criteria criteria = example.createCriteria();
        criteria.andCreatedAtLessThanOrEqualTo(date);
        return omcEventMapper.deleteByExample(example);
    }
}
