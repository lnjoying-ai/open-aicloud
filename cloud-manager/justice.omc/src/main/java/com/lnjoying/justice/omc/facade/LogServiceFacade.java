package com.lnjoying.justice.omc.facade;

import com.lnjoying.justice.omc.db.repo.OmcEventRepository;
import com.lnjoying.justice.omc.db.repo.OmcLogRepository;
import com.lnjoying.justice.omc.domain.dto.model.ApiLogDto;
import com.lnjoying.justice.omc.domain.dto.model.OperationEventDto;
import com.lnjoying.justice.omc.domain.dto.response.QueryApiLogRsp;
import com.lnjoying.justice.omc.domain.dto.response.QueryOperationEventRsp;
import com.lnjoying.justice.omc.domain.model.DailyAlertLogTrendRsp;
import com.lnjoying.justice.omc.domain.model.HourlyOperationEventTrendRsp;
import com.lnjoying.justice.omc.domain.model.HourlyOperationLog;
import com.lnjoying.justice.omc.domain.model.HourlyOperationLogTrendRsp;
import com.lnjoying.justice.omc.domain.model.search.ApiEventSearchCritical;
import com.lnjoying.justice.omc.domain.model.search.ApiLogSearchCritical;
import com.lnjoying.justice.omc.service.CombRpcService;
import com.lnjoying.justice.omc.util.DateUtils;
import com.lnjoying.justice.schema.common.OperationEventType;
import com.lnjoying.justice.schema.entity.RpcResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.lnjoying.justice.commonweb.util.ServiceCombRequestUtils.queryBpId;
import static com.lnjoying.justice.commonweb.util.ServiceCombRequestUtils.queryUserId;
import static org.springframework.util.StringUtils.isEmpty;

/**
 * 功能描述
 *
 * @author: Robin
 * @date: 2023年11月01日 16:30
 */
@Service
public class LogServiceFacade
{
    private static Logger LOGGER = LogManager.getLogger();

    @Autowired
    private OmcLogRepository   omcLogRepository;
    @Autowired
    private OmcEventRepository omcEventRepository;
    @Autowired
    private CombRpcService     combRpcService;

    public QueryApiLogRsp getLogs(ApiLogSearchCritical critical)
    {
        QueryApiLogRsp queryApiLogRsp = omcLogRepository.getLogs(critical);
        if (queryApiLogRsp == null || CollectionUtils.isEmpty(queryApiLogRsp.getLogs()))
        {
            return queryApiLogRsp;
        }

        fillOrTransformFieldForLog(queryApiLogRsp);
        return queryApiLogRsp;
    }

    public QueryOperationEventRsp getEvents(ApiEventSearchCritical critical)
    {
        QueryOperationEventRsp queryOperationEventRsp = omcEventRepository.getEvents(critical);
        if (queryOperationEventRsp == null || CollectionUtils.isEmpty(queryOperationEventRsp.getEvents()))
        {
            return queryOperationEventRsp;
        }

        fillOrTransformFieldForEvent(queryOperationEventRsp);
        return queryOperationEventRsp;
    }

    public HourlyOperationEventTrendRsp getOperationEventHourlyTrend(Date left, Date right)
    {
        return null;
    }

    public HourlyOperationLogTrendRsp getOperationLogHourlyTrend(Date startDate, Date endDate)
    {
        HourlyOperationLogTrendRsp rsp = HourlyOperationLogTrendRsp.builder().hourlyTrends(Collections.emptyList()).build();
        List<HourlyOperationLog> hourlyOperationLogs = omcLogRepository.selectDailyTrendOperationLogs(queryBpId(), queryUserId(), startDate, endDate);
        if (!CollectionUtils.isEmpty(hourlyOperationLogs))
        {
            List<HourlyOperationLogTrendRsp.HourlyTrend> hourlyTrends =
                    hourlyOperationLogs.stream().map(operationLog -> new HourlyOperationLogTrendRsp.HourlyTrend(operationLog.getLogHour(), operationLog.getLogCount())).collect(Collectors.toList());
            List<HourlyOperationLogTrendRsp.HourlyTrend> fillDailyTrends = generateDailyTrendData(DateUtils.of(startDate), DateUtils.of(endDate), hourlyTrends);
            rsp.setHourlyTrends(fillDailyTrends);
        }

        return rsp;
    }

    private List<HourlyOperationLogTrendRsp.HourlyTrend> generateDailyTrendData(LocalDateTime startDate, LocalDateTime endDate, List<HourlyOperationLogTrendRsp.HourlyTrend> hourlyTrends)
    {
        List<HourlyOperationLogTrendRsp.HourlyTrend> result = new ArrayList<>();
        while (!(startDate.isAfter(endDate) || startDate.isEqual(endDate)))
        {
            if (!CollectionUtils.isEmpty(hourlyTrends))
            {
                LocalDateTime finalStartDate = startDate;
                HourlyOperationLogTrendRsp.HourlyTrend dailyTrend =
                        hourlyTrends.stream().filter(trend -> DateUtils.of(trend.getLogHour()).isEqual(finalStartDate)).findFirst().orElse(new HourlyOperationLogTrendRsp.HourlyTrend(DateUtils.of(startDate), 0));
                result.add(dailyTrend);
            }
            else
            {
                result.add(new HourlyOperationLogTrendRsp.HourlyTrend(DateUtils.of(startDate), 0));
            }
            startDate  = startDate.plusHours(1);
        }

        return result;
    }

    private void fillOrTransformFieldForLog(QueryApiLogRsp queryApiLogRsp)
    {
        try
        {
            if (queryApiLogRsp == null)
            {
                return;
            }

            Set<String> serviceResourceEnNamePairSet = queryApiLogRsp.getLogs().stream().filter(x -> !isEmpty(x.getService()))
                    .map(x -> getServiceResourcePair(x.getService(),x.getResource()))
                    .collect(Collectors.toSet());
            RpcResult<HashMap<String, String>> rpcResult = combRpcService.getCommonResourceFeederService().getServiceResourceDisplayInfo(serviceResourceEnNamePairSet);
            if (rpcResult.ok())
            {
                HashMap<String, String> serviceResourceCnNameMap = rpcResult.getData();
                for (ApiLogDto log : queryApiLogRsp.getLogs())
                {
                    String serviceResourceEnNamePair = getServiceResourcePair(log.getService(), log.getResource());
                    String serviceResourceCnNamePair = serviceResourceCnNameMap.get(serviceResourceEnNamePair);
                    if (!isEmpty(serviceResourceCnNamePair))
                    {
                        String[] splitResults = serviceResourceCnNamePair.split(":", 2);
                        log.setService(splitResults[0]);
                        String resourceCnName = splitResults[1];
                        if (StringUtils.hasText(resourceCnName))
                        {
                            log.setResource(resourceCnName);
                        }
                        else
                        {
                            String[] splitEnResults = serviceResourceEnNamePair.split(":", 2);
                            if (splitEnResults.length == 2)
                            {
                                log.setResource(splitEnResults[1]);
                            }
                        }

                    }
                }
            }
        } catch (Exception e)
        {
            LOGGER.error("填充omc log的服务和资源名称时出错! error: {}", e.getMessage());
        }
    }

    private void fillOrTransformFieldForEvent(QueryOperationEventRsp queryOperationEventRsp)
    {
        try
        {
            if (queryOperationEventRsp == null)
            {
                return;
            }

            Set<String> serviceResourceEnNamePairSet = queryOperationEventRsp.getEvents().stream().filter(x -> !isEmpty(x.getService()))
                    .map(x -> getServiceResourcePair(x.getService(),x.getResource()))
                    .collect(Collectors.toSet());
            RpcResult<HashMap<String, String>> rpcResult = combRpcService.getCommonResourceFeederService().getServiceResourceDisplayInfo(serviceResourceEnNamePairSet);
            if (rpcResult.ok())
            {
                HashMap<String, String> serviceResourceCnNameMap = rpcResult.getData();
                for (OperationEventDto operationEventDto : queryOperationEventRsp.getEvents())
                {
                    String serviceResourceEnNamePair = getServiceResourcePair(operationEventDto.getService(), operationEventDto.getResource());
                    String serviceResourceCnNamePair = serviceResourceCnNameMap.get(serviceResourceEnNamePair);
                    if (!isEmpty(serviceResourceCnNamePair))
                    {
                        String[] splitResults = serviceResourceCnNamePair.split(":", 2);
                        operationEventDto.setService(splitResults[0]);
                        operationEventDto.setResource(splitResults[1]);
                    }
                    operationEventDto.setType(OperationEventType.getOperationTypeCnName(operationEventDto.getType())
                            .orElse(operationEventDto.getType()));
                }
            }
        } catch (Exception e)
        {
            LOGGER.error("填充omc event的服务和资源名称时出错! error: {}", e.getMessage());
        }
    }

    private String getServiceResourcePair(String serviceName, String resourceName)
    {
        return String.format("%s:%s", serviceName, StringUtils.isEmpty(resourceName)
                ? ""
                : resourceName);
    }

    public long deleteSixMonthAgoApiLog()
    {
        return omcLogRepository.deleteApiLogByDateAfterOrEqual(DateUtils.getXxMonthAgoDate(6));
    }

    public long deleteSixMonthAgoEventLog()
    {
        return omcEventRepository.deleteEventLogByDateAfterOrEqual(DateUtils.getXxMonthAgoDate(6));
    }
}
