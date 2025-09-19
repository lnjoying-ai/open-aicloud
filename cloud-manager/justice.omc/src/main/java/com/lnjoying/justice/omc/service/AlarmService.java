package com.lnjoying.justice.omc.service;

import com.lnjoying.justice.omc.domain.dto.req.AddAlarmReq;
import com.lnjoying.justice.omc.domain.dto.req.AlertLogProcessReq;
import com.lnjoying.justice.omc.domain.dto.req.UpdateAlarmReq;
import com.lnjoying.justice.omc.domain.dto.rsp.*;
import com.lnjoying.justice.omc.domain.model.DailyAlertLogTrendRsp;

import java.util.Date;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/11/1 16:30
 */

public interface AlarmService
{

    AlertGroupsRsp getAlarmGroupList(String name, Integer pageNum, Integer pageSize);

    AlertMetricsRsp getAlarmMetricList(String metricName, String groupId, Integer pageNum, Integer pageSize);

    Object addAlarm(AddAlarmReq addAlarmReq);

    void updateAlarm(String alarmId, UpdateAlarmReq req);

    AlarmsRsp getAlarmList(String queryBpId, String queryUserId, Integer ruleType, String groupId, String name, Integer pageNum, Integer pageSize);

    void deleteAlarm(String alarmId);

    Object getAlarm(String alarmId);

    void action(String alarmId, String action);

    AlertLogsRsp getAlertLogsList(String queryBpId, String queryUserId, Integer status, Integer alertStatus, String processStatus, Integer level, String groupId,
                                  String resourceTypeId, String resourceId,
                                  Integer pageNum,
                                  Integer pageSize);

    void handleAlertLog(String alertLogId, AlertLogProcessReq req);

    AlertLogCountsRsp getAlertLogCounts(Date left, Date right);

    DailyAlertLogTrendRsp getAlertLogDailyTrend(Date startDate, Date endDate);
}
