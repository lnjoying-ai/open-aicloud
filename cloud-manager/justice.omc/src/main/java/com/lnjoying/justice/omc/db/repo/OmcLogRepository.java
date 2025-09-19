package com.lnjoying.justice.omc.db.repo;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lnjoying.justice.commonweb.util.CollectionUtils;
import com.lnjoying.justice.omc.db.mapper.TblOmcLogMapper;
import com.lnjoying.justice.omc.db.model.TblOmcLog;
import com.lnjoying.justice.omc.db.model.TblOmcLogExample;
import com.lnjoying.justice.omc.domain.dto.response.QueryApiLogRsp;
import com.lnjoying.justice.omc.domain.model.HourlyOperationLog;
import com.lnjoying.justice.omc.domain.model.search.ApiLogSearchCritical;
import com.lnjoying.justice.schema.entity.omc.BpLastLoginTimeDTO;
import com.lnjoying.justice.schema.entity.omc.BpLastLoginTimeRsp;
import com.lnjoying.justice.schema.entity.omc.BpLoginDetailDTO;
import com.lnjoying.justice.schema.entity.omc.BpLoginDetailRsp;
import com.lnjoying.justice.schema.service.omc.OmcService;
import com.micro.core.common.Utils;
import io.jsonwebtoken.lang.Collections;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import static org.springframework.util.StringUtils.isEmpty;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 功能描述
 *
 * @author: Robin
 * @date: 2023年10月20日 14:22
 */
@Repository
public class OmcLogRepository
{
    @Autowired
    private TblOmcLogMapper omcLogMapper;

    public int insertLog(OmcService.OperationLog operationLog)
    {
        TblOmcLog logEntity = new TblOmcLog();
        BeanUtils.copyProperties(operationLog, logEntity);
        if (logEntity.getId() == null)
        {
            logEntity.setId(Utils.assignUUId());
        }
        logEntity.setCreatedAt(new Date());
        return omcLogMapper.insert(logEntity);
    }

    public QueryApiLogRsp getLogs(ApiLogSearchCritical critical)
    {
        TblOmcLogExample example = new TblOmcLogExample();
        TblOmcLogExample.Criteria criteria = example.createCriteria();
        if (!isEmpty(critical.getBpId()))
        {
            criteria.andBpIdEqualTo(critical.getBpId());
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
        if (!isEmpty(critical.getUserId()))
        {
            criteria.andUserIdEqualTo(critical.getUserId());
        }
        if (!isEmpty(critical.getLogLevel()))
        {
            criteria.andLevelEqualTo(critical.getLogLevel());
        }
        if (!isEmpty(critical.getHttpMethod()))
        {
            criteria.andHttpMethodEqualTo(critical.getHttpMethod().toUpperCase());
        }
        if (critical.getStartTriggerTime() != null)
        {
            criteria.andTriggerTimeGreaterThanOrEqualTo(critical.getStartTriggerTime());
        }
        if (critical.getEndTriggerTime() != null)
        {
            criteria.andTriggerTimeLessThanOrEqualTo(critical.getEndTriggerTime());
        }
        long count = omcLogMapper.countByExample(example);
        QueryApiLogRsp queryApiLogRsp = new QueryApiLogRsp();
        if (count == 0)
        {
            return queryApiLogRsp;
        }
        queryApiLogRsp.setTotal_num(count);
        int startRow = ((critical.getPageNum()-1) * critical.getPageSize());
        example.setOrderByClause("created_at desc");
        example.setStartRow(startRow);
        example.setPageSize(critical.getPageSize());
        List<TblOmcLog> tblOmcLogs = omcLogMapper.selectByExample(example);
        if (Collections.isEmpty(tblOmcLogs))
        {
            return null;
        }
        queryApiLogRsp.setLogs(tblOmcLogs.stream().map(x -> x.toDto()).collect(Collectors.toList()));
        return queryApiLogRsp;
    }

    public List<HourlyOperationLog> selectDailyTrendOperationLogs(String bpId, String userId, Date minCreateTime, Date maxCreateTime)
    {
        return omcLogMapper.selectAllByCreatedAtBetweenEqualAndLevelAndBpIdAndUserId(minCreateTime, maxCreateTime, null, bpId, userId);
    }

    public BpLastLoginTimeRsp getLastLoginTimeGroupByBpId(List<String> bpIds, Date startDate, Date endDate, Integer pageSize, Integer pageNum, String sortDirection, String userId, String userName)
    {
        PageHelper.startPage(pageNum, pageSize);
        List<BpLastLoginTimeDTO> lastLoginTimeDTOS = omcLogMapper.selectLoginLogsGroupByBpIdAndCreateTimeBetween(bpIds, startDate, endDate, sortDirection, userId, userName);
        if (!Collections.isEmpty(lastLoginTimeDTOS))
        {
            PageInfo<BpLastLoginTimeDTO> pageInfo = new PageInfo<>(lastLoginTimeDTOS);
            return BpLastLoginTimeRsp.builder().totalNum(pageInfo.getTotal()).lastLoginTimeDTOS(pageInfo.getList()).build();
        }

        return BpLastLoginTimeRsp.builder().totalNum(0).lastLoginTimeDTOS(lastLoginTimeDTOS).build();
    }

    public long deleteApiLogByDateAfterOrEqual(Date date)
    {
        TblOmcLogExample example = new TblOmcLogExample();
        TblOmcLogExample.Criteria criteria = example.createCriteria();
        criteria.andCreatedAtLessThanOrEqualTo(date);
        return omcLogMapper.deleteByExample(example);
    }

    public BpLoginDetailRsp getLoginDetailByBpId(String bpId, String userId, Date startDate, Date endDate, Integer pageSize, Integer pageNum, String sortDirection)
    {
        PageHelper.startPage(pageNum, pageSize);
        List<BpLoginDetailDTO> bpLoginDetailDTOS = omcLogMapper.selectLoginLogsByBpIdAndCreateTimeBetween(bpId, userId, startDate, endDate, sortDirection);
        if (!CollectionUtils.isEmpty(bpLoginDetailDTOS))
        {
            PageInfo<BpLoginDetailDTO> pageInfo = new PageInfo<>(bpLoginDetailDTOS);
            return BpLoginDetailRsp.builder().totalNum(pageInfo.getTotal()).userDatas(pageInfo.getList()).build();
        }

        return BpLoginDetailRsp.builder().totalNum(0).userDatas(bpLoginDetailDTOS).build();
    }
}
