package com.lnjoying.justice.omc.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.lnjoying.justice.commonweb.exception.WebSystemException;
import com.lnjoying.justice.commonweb.model.RecordStatus;
import com.lnjoying.justice.commonweb.model.ScopeEnum;
import com.lnjoying.justice.omc.db.model.*;
import com.lnjoying.justice.omc.db.repo.AlarmRepository;
import com.lnjoying.justice.omc.db.repo.PrometheusInstanceRepository;
import com.lnjoying.justice.omc.db.repo.ReceiverRepository;
import com.lnjoying.justice.omc.domain.dto.req.AddAlarmReq;
import com.lnjoying.justice.omc.domain.dto.req.AlertLogProcessReq;
import com.lnjoying.justice.omc.domain.dto.req.AlertRuleReq;
import com.lnjoying.justice.omc.domain.dto.req.UpdateAlarmReq;
import com.lnjoying.justice.omc.domain.dto.rsp.*;
import com.lnjoying.justice.omc.domain.model.*;
import com.lnjoying.justice.omc.prometheus.model.PrometheusAlertingRule;
import com.lnjoying.justice.omc.prometheus.model.PrometheusRuleGroup;
import com.lnjoying.justice.omc.prometheus.model.PrometheusRuleGroups;
import com.lnjoying.justice.omc.service.AlarmService;
import com.lnjoying.justice.omc.service.CombRpcService;
import com.lnjoying.justice.omc.service.PrometheusService;
import com.lnjoying.justice.omc.util.AlarmUtils;
import com.lnjoying.justice.omc.util.DateUtils;
import com.lnjoying.justice.omc.util.ServiceResourceUtils;
import com.lnjoying.justice.schema.common.ErrorCode;
import com.lnjoying.justice.schema.common.ErrorLevel;
import com.micro.core.common.Utils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.lnjoying.justice.commonweb.util.ServiceCombRequestUtils.*;
import static com.lnjoying.justice.omc.prometheus.model.PrometheusAlertingRule.*;
import static com.lnjoying.justice.omc.prometheus.util.PrometheusUtils.removeEscapeCharacters;
import static com.lnjoying.justice.schema.common.ErrorCode.*;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/11/1 16:31
 */

@Slf4j
@Service
public class AlarmServiceImpl implements AlarmService
{

    @Autowired
    private AlarmRepository alarmRepository;

    @Autowired
    private PrometheusService prometheusService;

    @Autowired
    private CombRpcService combRpcService;

    @Autowired
    private PrometheusInstanceRepository prometheusInstanceRepository;

    @Autowired
    private ReceiverRepository receiverRepository;

    @Override
    public AlertGroupsRsp getAlarmGroupList(String name, Integer pageNum, Integer pageSize)
    {
        PageHelper.startPage(pageNum, pageSize);
        List<TblOmcAlertGroup> tblOmcAlertGroups = alarmRepository.selectAlarmGroups(name);
        if (CollectionUtils.isEmpty(tblOmcAlertGroups))
        {
            return  AlertGroupsRsp.builder().totalNum(0).groups(Collections.emptyList()).build();
        }

        String bpId = queryBpId();
        String userId = queryUserId();
        List<AlertGroup> alertGroups = tblOmcAlertGroups.stream().filter(group ->
        {
            return checkPermit(bpId, userId, group);
        }).map(AlertGroup::of).collect(Collectors.toList());

        PageInfo<TblOmcAlertGroup> pageInfo = new PageInfo<>(tblOmcAlertGroups);
        return AlertGroupsRsp.builder().totalNum(alertGroups.size())
                .groups(alertGroups).build();
    }


    @Override
    public AlertMetricsRsp getAlarmMetricList(String metricName, String groupId, Integer pageNum, Integer pageSize)
    {
        PageHelper.startPage(pageNum, pageSize);
        List<TblOmcAlertMetric> tblOmcAlertMetrics = alarmRepository.selectAlarmMetrics(groupId, metricName);
        PageInfo<TblOmcAlertMetric> pageInfo = new PageInfo<>(tblOmcAlertMetrics);
        return AlertMetricsRsp.builder().totalNum(pageInfo.getTotal())
                .metrics(tblOmcAlertMetrics.stream().map(AlertMetric::of).collect(Collectors.toList())).build();
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public Object addAlarm(AddAlarmReq addAlarmReq)
    {

        checkParams(addAlarmReq);
        assembleAlertRule(addAlarmReq);
        if (!isAdmin())
        {
            String modifyAlertExpr = AlarmUtils.modifyAlertExpr(addAlarmReq.getExpr(), addAlarmReq.getBpId(), addAlarmReq.getUserId());
            addAlarmReq.setExpr(modifyAlertExpr);
        }

        String alarmId = Utils.assignUUId();
        generateRuleFile(alarmId ,addAlarmReq.getName(), addAlarmReq.getExpr(), addAlarmReq.getDurationTime(), addAlarmReq.getLabels(),
                addAlarmReq.getBpId(), addAlarmReq.getUserId(), addAlarmReq.getAlertMessage(), addAlarmReq.getDescription());

        TblOmcAlarm tblOmcAlarm = new TblOmcAlarm();
        BeanUtils.copyProperties(addAlarmReq, tblOmcAlarm);

        tblOmcAlarm.setId(alarmId);
        tblOmcAlarm.setCreateTime(new Date());
        tblOmcAlarm.setUpdateTime(tblOmcAlarm.getCreateTime());
        tblOmcAlarm.setStatus(RecordStatus.NORMAL.value());
        alarmRepository.insertAlarmSelective(tblOmcAlarm);

        doCreateRule(addAlarmReq.getAlertRule(), alarmId);

        List<NotifyRule> notifyRules = addAlarmReq.getNotifyRules();
        if (!CollectionUtils.isEmpty(notifyRules))
        {
            doCreateNotifyRules(alarmId, notifyRules);
        }

        return alarmId;
    }


    @Override
    public void updateAlarm(String alarmId, UpdateAlarmReq req)
    {
        checkAlarmExists(alarmId);
        assembleUpdateAlertRule(req);
        if (!isAdmin())
        {
            String modifyAlertExpr = AlarmUtils.modifyAlertExpr(req.getExpr(), req.getBpId(), req.getUserId());
            req.setExpr(modifyAlertExpr);
        }

        deleteRuleFile(req.getBpId(), req.getUserId(), alarmId);
        generateRuleFile(alarmId, req.getName(), req.getExpr(), req.getDurationTime(), req.getLabels(),
                req.getBpId(), req.getUserId(), req.getAlertMessage(), req.getDescription());
        TblOmcAlarm tblOmcAlarm = new TblOmcAlarm();
        BeanUtils.copyProperties(req, tblOmcAlarm);
        tblOmcAlarm.setId(alarmId);
        tblOmcAlarm.setUpdateTime(tblOmcAlarm.getCreateTime());
        alarmRepository.updateAlarm(tblOmcAlarm);

        TblOmcRule tblOmcRule = alarmRepository.selectRuleByAlarmId(alarmId);
        String ruleId = tblOmcRule.getId();
        AlertRuleReq alertRule = req.getAlertRule();
        if (Objects.nonNull(tblOmcRule))
        {
            BeanUtils.copyProperties(req, tblOmcRule);
            BeanUtils.copyProperties(alertRule, tblOmcRule);

            tblOmcRule.setUpdateTime(new Date());
            tblOmcRule.setId(ruleId);
            tblOmcRule.setAlarmId(alarmId);
            alarmRepository.updateRule(tblOmcRule);
        }
        else
        {
            doCreateRule(alertRule, alarmId);
        }

        List<NotifyRule> notifyRules = req.getNotifyRules();
        if (!CollectionUtils.isEmpty(notifyRules))
        {
            // delete first, then batch insert
            List<String> notifyRuleIds = alarmRepository.selectNotifyRuleIdsByAlarmId(alarmId);
            if (!CollectionUtils.isEmpty(notifyRuleIds))
            {
                notifyRuleIds.parallelStream().forEach(notifyRuleId -> {
                    alarmRepository.deleteNotifyRule(notifyRuleId);
                    alarmRepository.deleteNotifyObjectByNotifyRuleId(notifyRuleId);
                });
            }

            doCreateNotifyRules(alarmId, notifyRules);
        }

    }

    @Override
    public AlarmsRsp getAlarmList(String queryBpId, String queryUserId, Integer ruleType, String groupId, String name, Integer pageNum, Integer pageSize)
    {
        PageHelper.startPage(pageNum, pageSize);
        List<Alarm> tblOmcAlarms = alarmRepository.selectAll(groupId, queryBpId, queryUserId, name, ruleType);
        
        if (CollectionUtils.isEmpty(tblOmcAlarms))
        {
            return AlarmsRsp.builder().totalNum(0).alarms(new ArrayList<>()).build();
        }

        List<Alarm> alarmList = tblOmcAlarms.parallelStream()
                .map(alarm ->
                {
                    assembleAlertGroupInfo(alarm);
                    alarm.setAlertMetricName(assembleAlertMetricInfo(alarm.getAlertMetricId()));
                    return alarm;
                }).collect(Collectors.toList());

        ServiceResourceUtils.fillAlarmResourceInfo(alarmList);

        PageInfo<Alarm> pageInfo = new PageInfo<>(tblOmcAlarms);
        return AlarmsRsp.builder()
                .totalNum(pageInfo.getTotal())
                .alarms(alarmList.stream().map(alarm -> BaseRsp.assembleUserInfo(alarm.getBpId(), alarm.getUserId(), alarm, combRpcService.getUmsService())).collect(Collectors.toList()))
                .build();
    }


    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void deleteAlarm(String alarmId)
    {
        checkAlarmExists(alarmId);

        TblOmcAlarm tblOmcAlarm = alarmRepository.selectAlarmByPrimaryKey(alarmId);
        deleteRuleFile(tblOmcAlarm.getBpId(), tblOmcAlarm.getUserId(), alarmId);

        TblOmcRule tblOmcRule = alarmRepository.selectRuleByAlarmId(alarmId);
        if (Objects.nonNull(tblOmcRule))
        {
            alarmRepository.deleteRule(tblOmcRule.getId());
        }

        List<String> notifyRuleIds = alarmRepository.selectNotifyRuleIdsByAlarmId(alarmId);
        if (!CollectionUtils.isEmpty(notifyRuleIds))
        {
            notifyRuleIds.parallelStream().forEach(notifyRuleId -> {
                alarmRepository.deleteNotifyRule(notifyRuleId);
                alarmRepository.deleteNotifyObjectByNotifyRuleId(notifyRuleId);
            });
        }


        if (Objects.nonNull(tblOmcAlarm))
        {
            alarmRepository.deleteAlarmByPrimaryKey(tblOmcAlarm.getId());
        }
    }

    private void deleteRuleFile(String bpId, String userId, String alarmId)
    {
        try
        {
            prometheusService.deleteAlertRule(bpId, userId, alarmId);
        }
        catch (Exception e)
        {
            throw new WebSystemException(OMC_ALARM_DELETE_FAILED, ErrorLevel.ERROR);
        }
    }

    @Override
    public Object getAlarm(String alarmId)
    {

        AlarmDetail alarmDetail = alarmRepository.selectAlarmById(alarmId);
        if (Objects.nonNull(alarmDetail))
        {
            String dataSourceId = alarmDetail.getDataSourceId();
            if (StringUtils.isNotBlank(dataSourceId))
            {
                TblOmcPrometheus tblOmcPrometheus = prometheusInstanceRepository.selectByPrimaryKey(dataSourceId);
                if (Objects.nonNull(tblOmcPrometheus))
                {
                    alarmDetail.setDataSourceName(tblOmcPrometheus.getName());
                }
            }
        }

        assembleAlertGroupInfo(alarmDetail);
        alarmDetail.setAlertMetricName(assembleAlertMetricInfo(alarmDetail.getAlertMetricId()));

        List<TblOmcNotifyRule> tblOmcNotifyRules = alarmRepository.selectNotifyRules(alarmId);
        if (!CollectionUtils.isEmpty(tblOmcNotifyRules))
        {
            List<AlertNotifyRule> alertNotifyRules = tblOmcNotifyRules.stream().map(tblOmcNotifyRule ->
            {
                AlertNotifyRule alertNotifyRule = new AlertNotifyRule();
                BeanUtils.copyProperties(tblOmcNotifyRule, alertNotifyRule);
                String notifyRuleId = tblOmcNotifyRule.getId();
                List<TblOmcNotifyObject> tblOmcNotifyObjects = alarmRepository.selectByNotifyRuleId(notifyRuleId);
                assembleNotifyObjects(alertNotifyRule, tblOmcNotifyObjects);
                return alertNotifyRule;
            }).collect(Collectors.toList());
            alarmDetail.setAlertNotifyRules(alertNotifyRules);
        }
        ServiceResourceUtils.fillAlarmDetailResourceInfo(Lists.newArrayList(alarmDetail));
        return alarmDetail;
    }



    @Override
    public void action(String alarmId, String action)
    {
        checkAlarmExists(alarmId);

        // todo update

        TblOmcAlarm tblOmcAlarm = new TblOmcAlarm();
        tblOmcAlarm.setId(alarmId);

        if (AlarmStatusEnum.ACTIVE.name().equalsIgnoreCase(action))
        {
            tblOmcAlarm.setStatus(AlarmStatusEnum.ACTIVE.value());
        }

        if (AlarmStatusEnum.DEACTIVE.name().equalsIgnoreCase(action))
        {
            tblOmcAlarm.setStatus(AlarmStatusEnum.DEACTIVE.value());
        }

        tblOmcAlarm.setUpdateTime(tblOmcAlarm.getCreateTime());
        alarmRepository.updateAlarm(tblOmcAlarm);

    }

    @Override
    public AlertLogsRsp getAlertLogsList(String queryBpId, String queryUserId, Integer status, Integer alertStatus, String processStatus, Integer level, String groupId, String resourceTypeId, String resourceId, Integer pageNum, Integer pageSize)
    {
        PageHelper.startPage(pageNum, pageSize);
        List<TblOmcAlertLog> tblOmcAlertLogs = alarmRepository.selectAllAlertLogs(queryBpId, queryUserId, groupId, status, alertStatus, processStatus, level, resourceTypeId,
                resourceId);
        PageInfo<TblOmcAlertLog> pageInfo = new PageInfo<>(tblOmcAlertLogs);
        return AlertLogsRsp.builder()
                .totalNum(pageInfo.getTotal())
                .alertLogs(tblOmcAlertLogs.parallelStream().map(alertLog -> AlertLog.of(alertLog, alarmRepository, receiverRepository))
                        .map(alertLog -> BaseRsp.assembleUserInfo(alertLog.getBpId(), alertLog.getUserId(), alertLog, combRpcService.getUmsService())).collect(Collectors.toList())).build();
    }

    @Override
    public void handleAlertLog(String alertLogId, AlertLogProcessReq req)
    {
        TblOmcAlertLogProcess alertLogProcess = new TblOmcAlertLogProcess();
        BeanUtils.copyProperties(req, alertLogProcess);
        alertLogProcess.setAlertLogId(alertLogId);
        alertLogProcess.setId(Utils.assignUUId());
        alertLogProcess.setCreateTime(new Date());
        alertLogProcess.setUpdateTime(alertLogProcess.getCreateTime());
        alarmRepository.insertAlertLogProcessSelective(alertLogProcess);
    }

    @Override
    public AlertLogCountsRsp getAlertLogCounts(Date startDate, Date endDate)
    {

        int totalInfos = alarmRepository.selectAlertLogCounts(startDate, endDate, AlertLevelEnum.INFO.value(),queryBpId(), queryUserId());
        int totalWarnings = alarmRepository.selectAlertLogCounts(startDate, endDate, AlertLevelEnum.WARNING.value(), queryBpId(), queryUserId());
        int totalCriticals = alarmRepository.selectAlertLogCounts(startDate, endDate, AlertLevelEnum.CRITICAL.value(), queryBpId(), queryUserId());
        return AlertLogCountsRsp.builder().totalInfos(totalInfos).totalWarnings(totalWarnings).totalCriticals(totalCriticals).build();

    }

    @Override
    public DailyAlertLogTrendRsp getAlertLogDailyTrend(Date startDate, Date endDate)
    {
        DailyAlertLogTrendRsp rsp = DailyAlertLogTrendRsp.builder().dailyTrends(Collections.emptyList()).build();
        List<DailyAlertLog> dailyAlertLogs = alarmRepository.selectDailyTrendAlertLogs(queryBpId(), queryUserId(), startDate, endDate);
        if (!CollectionUtils.isEmpty(dailyAlertLogs))
        {
            Map<Date, List<DailyAlertLog>> groupByDate = dailyAlertLogs.stream().collect(Collectors.groupingBy(DailyAlertLog::getAlertDay));
            List<DailyAlertLogTrendRsp.DailyTrend> dailyTrends = groupByDate.entrySet().stream().map(entry ->
            {
                DailyAlertLogTrendRsp.DailyTrend dailyTrend = new DailyAlertLogTrendRsp.DailyTrend();
                dailyTrend.setAlertDay(entry.getKey());
                List<DailyAlertLog> logs = entry.getValue();
                if (!CollectionUtils.isEmpty(logs))
                {
                    DailyAlertLogTrendRsp.AlertLogLevelCount alertLogLevelCount = new DailyAlertLogTrendRsp.AlertLogLevelCount();
                    logs.stream().collect(Collectors.groupingBy(DailyAlertLog::getLevel)).forEach((level, alertLogs) ->
                    {
                        if (level == AlertLevelEnum.INFO.value())
                        {
                            alertLogLevelCount.setInfoCount(alertLogs.stream().mapToLong(DailyAlertLog::getAlertCount).sum());
                        }
                        else if (level == AlertLevelEnum.WARNING.value())
                        {
                            alertLogLevelCount.setWarnCount(alertLogs.stream().mapToLong(DailyAlertLog::getAlertCount).sum());
                        }
                        else if (level == AlertLevelEnum.CRITICAL.value())
                        {
                            alertLogLevelCount.setCriticalCount(alertLogs.stream().mapToLong(DailyAlertLog::getAlertCount).sum());
                        }

                    });

                    alertLogLevelCount.setAllCount(alertLogLevelCount.getInfoCount() + alertLogLevelCount.getWarnCount() + alertLogLevelCount.getCriticalCount());
                    dailyTrend.setAlertLogLevelCount(alertLogLevelCount);
                }
                return dailyTrend;
            }).collect(Collectors.toList());

            List<DailyAlertLogTrendRsp.DailyTrend> fillDailyTrends = generateDailyTrendData(DateUtils.of(startDate), DateUtils.of(endDate), dailyTrends);
            rsp.setDailyTrends(fillDailyTrends);
        }
        else
        {
            List<DailyAlertLogTrendRsp.DailyTrend> fillDailyTrends = generateDailyTrendData(DateUtils.of(startDate), DateUtils.of(endDate), null);
            rsp.setDailyTrends(fillDailyTrends);
        }
        return rsp;
    }

    private List<DailyAlertLogTrendRsp.DailyTrend> generateDailyTrendData(LocalDateTime startDate, LocalDateTime endDate, List<DailyAlertLogTrendRsp.DailyTrend> dailyTrends)
    {
        List<DailyAlertLogTrendRsp.DailyTrend> result = new ArrayList<>();
        while (!(startDate.isAfter(endDate) || startDate.isEqual(endDate)))
        {
            if (!CollectionUtils.isEmpty(dailyTrends))
            {
                LocalDateTime finalStartDate = startDate;
                DailyAlertLogTrendRsp.DailyTrend dailyTrend =
                        dailyTrends.stream().filter(trend -> DateUtils.of(trend.getAlertDay()).isEqual(finalStartDate)).findFirst().orElse(new DailyAlertLogTrendRsp.DailyTrend(DateUtils.of(startDate), new DailyAlertLogTrendRsp.AlertLogLevelCount()));
                result.add(dailyTrend);
            }
            else
            {
                result.add(new DailyAlertLogTrendRsp.DailyTrend(DateUtils.of(startDate), new DailyAlertLogTrendRsp.AlertLogLevelCount()));
            }
            startDate  = startDate.plusDays(1);
        }

        return result;
    }

    private void checkParams(AddAlarmReq addAlarmReq)
    {
        AlertRuleReq alertRule = addAlarmReq.getAlertRule();
        if (Objects.isNull(alertRule))
        {
            throw new WebSystemException(ErrorCode.OMC_RULE_CANNOT_BE_EMPTY, ErrorLevel.ERROR);

        }

        if (!isAdmin())
        {
            String alertGroupId = addAlarmReq.getAlertGroupId();
            if (StringUtils.isNotBlank(alertGroupId))
            {
                TblOmcAlertGroup tblOmcAlertGroup = alarmRepository.selectAlertGroup(alertGroupId);
                if (Objects.nonNull(tblOmcAlertGroup))
                {
                    String bpId = queryBpId();
                    String userId = queryUserId();
                    if (!checkPermit(bpId, userId, tblOmcAlertGroup))
                    {
                        throw new WebSystemException(OMC_ALARM_CREATE_NOT_ALLOWED, ErrorLevel.ERROR);
                    }
                }
            }
        }

    }


    private void checkAlarmExists(String alarmId)
    {
        TblOmcAlarm tblOmcAlarm = alarmRepository.selectAlarmByPrimaryKey(alarmId);
        if (Objects.isNull(tblOmcAlarm))
        {
            throw new WebSystemException(ErrorCode.OMC_ALARM_NOT_EXIST, ErrorLevel.ERROR);
        }

    }


    private void doCreateRule(AlertRuleReq alertRule, String alarmId)
    {
        TblOmcRule tblOmcRule = new TblOmcRule();
        BeanUtils.copyProperties(alertRule, tblOmcRule);
        String ruleId = Utils.assignUUId();
        tblOmcRule.setId(ruleId);
        tblOmcRule.setAlarmId(alarmId);
        tblOmcRule.setCreateTime(new Date());
        tblOmcRule.setUpdateTime(tblOmcRule.getCreateTime());
        alarmRepository.insertRuleSelective(tblOmcRule);
    }

    private void doCreateNotifyRules(String alarmId, List<NotifyRule> notifyRules)
    {
        notifyRules.stream().forEach(notifyRule -> {
            TblOmcNotifyRule tblOmcNotifyRule = new TblOmcNotifyRule();
            BeanUtils.copyProperties(notifyRule, tblOmcNotifyRule);
            String notifyRuleId = Utils.assignUUId();
            tblOmcNotifyRule.setId(notifyRuleId);
            tblOmcNotifyRule.setAlarmId(alarmId);
            alarmRepository.insertNotifyRuleSelective(tblOmcNotifyRule);

            List<NotifyRule.NotifyObject> notifyObjects = notifyRule.getNotifyObjects();
            if (!CollectionUtils.isEmpty(notifyObjects))
            {
                List<TblOmcNotifyObject> tblNotifyObjects = notifyObjects.stream().map(notifyObject ->
                {
                    TblOmcNotifyObject tblOmcNotifyObject = new TblOmcNotifyObject();
                    String notifyObjectId = Utils.assignUUId();
                    tblOmcNotifyObject.setId(notifyObjectId);
                    tblOmcNotifyObject.setNotifyChannels(notifyObject.getNotifyChannels());
                    tblOmcNotifyObject.setNotifyRuleId(notifyRuleId);
                    tblOmcNotifyObject.setReceiverId(notifyObject.getReceiverId());
                    return tblOmcNotifyObject;
                }).collect(Collectors.toList());
                alarmRepository.batchInsertNotifyObjects(tblNotifyObjects);
            }
        });
    }


    private void generateRuleFile(String alarmId, String alertName, String expr, Integer durationTime, Map<String, String> alarmLabels, String bpId, String userId,
                                  String alertMessage, String description)
    {
        try
        {
            PrometheusRuleGroups groups = new PrometheusRuleGroups();
            PrometheusRuleGroup prometheusRuleGroup = new PrometheusRuleGroup();
            prometheusRuleGroup.setName(alarmId);
            PrometheusAlertingRule prometheusAlertingRule = new PrometheusAlertingRule();
            prometheusAlertingRule.setAlert(alertName);
            String ruleExpr = removeEscapeCharacters(expr);
            prometheusAlertingRule.setExpr(ruleExpr);
            prometheusAlertingRule.setForValue(String.valueOf(durationTime)+"m");
            Map<String, String> labels = new HashMap<>();
            labels.putAll(alarmLabels);
            if (StringUtils.isNotBlank(bpId))
            {
                labels.put("bpId", bpId);
            }

            if (StringUtils.isNotBlank(userId))
            {
                labels.put("userId", userId);
            }

            prometheusAlertingRule.setLabels(labels);
            Map<String, String> annotations = new HashMap<>();
            annotations.put(ANNOTATION_SUMMARY, alertMessage);
            annotations.put(ANNOTATION_DESCRIPTION, description);
            annotations.put(ANNOTATION_ALARM_UID, alarmId);
            prometheusAlertingRule.setAnnotations(annotations);

            prometheusRuleGroup.addPrometheusAlertingRule(prometheusAlertingRule);
            groups.addPrometheusRuleGroup(prometheusRuleGroup);
            prometheusService.addAlertRule(groups, bpId, userId, alarmId);
        }
        catch (Exception e)
        {
            log.error("generate rule file error:{}", e);
            throw new WebSystemException(OMC_ALARM_ADD_FAILED, ErrorLevel.ERROR);
        }

    }

    private void assembleNotifyObjects(AlertNotifyRule alertNotifyRule, List<TblOmcNotifyObject> tblOmcNotifyObjects)
    {
        if (!CollectionUtils.isEmpty(tblOmcNotifyObjects))
        {
            List<AlertNotifyObject> alertNotifyObjects = tblOmcNotifyObjects.stream().map(tblOmcNotifyObject ->
            {
                AlertNotifyObject alertNotifyObject = new AlertNotifyObject();
                BeanUtils.copyProperties(tblOmcNotifyObject, alertNotifyObject);
                TblOmcReceiver tblOmcReceiver = receiverRepository.selectReceiverByPrimaryKey(alertNotifyObject.getReceiverId());
                if (Objects.nonNull(tblOmcReceiver))
                {
                    alertNotifyObject.setReceiverName(tblOmcReceiver.getName());
                }
                StringBuilder optionalNotifyChannels = new StringBuilder();
                if (StringUtils.isNotBlank(tblOmcReceiver.getEmail()))
                {
                    optionalNotifyChannels.append(NotifyChannelEnum.EMAIL.value());
                }
                if (StringUtils.isNotBlank(tblOmcReceiver.getPhone()))
                {
                    if (optionalNotifyChannels.length() > 0)
                    {
                        optionalNotifyChannels.append(",");
                    }
                    optionalNotifyChannels.append(NotifyChannelEnum.SMS.value());

                }
                alertNotifyObject.setOptionalNotifyChannels(optionalNotifyChannels.toString());
                return alertNotifyObject;
            }).collect(Collectors.toList());
            alertNotifyRule.setNotifyObjects(alertNotifyObjects);
        }
    }

    private void assembleAlertGroupInfo(Alarm alarm)
    {
        String alertGroupId = alarm.getAlertGroupId();
        if (StringUtils.isNotBlank(alertGroupId))
        {
            TblOmcAlertGroup tblOmcAlertGroup = alarmRepository.selectAlertGroup(alertGroupId);
            if (Objects.nonNull(tblOmcAlertGroup))
            {
                alarm.setAlertGroupName(tblOmcAlertGroup.getName());
                alarm.setTargetResourceType(tblOmcAlertGroup.getResourceType());
                alarm.setTargetServiceType(tblOmcAlertGroup.getServiceType());
            }
        }

    }

    private void assembleAlertGroupInfo(AlarmDetail alarm)
    {
        String alertGroupId = alarm.getAlertGroupId();
        if (StringUtils.isNotBlank(alertGroupId))
        {
            TblOmcAlertGroup tblOmcAlertGroup = alarmRepository.selectAlertGroup(alertGroupId);
            if (Objects.nonNull(tblOmcAlertGroup))
            {
                alarm.setAlertGroupName(tblOmcAlertGroup.getName());
                alarm.setTargetResourceType(tblOmcAlertGroup.getResourceType());
                alarm.setTargetServiceType(tblOmcAlertGroup.getServiceType());
            }
        }

    }

    private String assembleAlertMetricInfo(String alertMetricId)
    {
        if (StringUtils.isNotBlank(alertMetricId))
        {
            TblOmcAlertMetric tblOmcAlertMetric = alarmRepository.selectAlertMetric(alertMetricId);
            if (Objects.nonNull(tblOmcAlertMetric))
            {
                return tblOmcAlertMetric.getMetricName();
            }
        }

        return "";
    }


    private void assembleAlertRule(AddAlarmReq addAlarmReq)
    {
        AlertRuleReq alertRule = addAlarmReq.getAlertRule();
        if (Objects.isNull(alertRule))
        {
            alertRule = new AlertRuleReq();
        }

        BeanUtils.copyProperties(addAlarmReq, alertRule);
        addAlarmReq.setAlertRule(alertRule);
    }

    private void assembleUpdateAlertRule(UpdateAlarmReq updateAlarmReq)
    {
        AlertRuleReq alertRule = updateAlarmReq.getAlertRule();
        if (Objects.isNull(alertRule))
        {
            alertRule = new AlertRuleReq();
        }

        BeanUtils.copyProperties(updateAlarmReq, alertRule);
        updateAlarmReq.setAlertRule(alertRule);
    }


    private static boolean checkPermit(String bpId, String userId, TblOmcAlertGroup group)
    {
        if (isAdmin())
        {
            return true;
        }
        ScopeEnum scope = ScopeEnum.fromValue(group.getScope());
        boolean permit = false;
        switch (scope)
        {
            case PRIVATE:
                permit = Objects.equals(bpId, group.getBpId()) && Objects.equals(userId, group.getUserId());
                break;
            case BP:
                permit = Objects.equals(bpId, group.getBpId());
                break;
            case PUBLIC:
            default:
                permit  = true;
        }
        return permit;
    }
}
