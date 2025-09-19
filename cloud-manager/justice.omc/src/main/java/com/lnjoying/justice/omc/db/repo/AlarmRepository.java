package com.lnjoying.justice.omc.db.repo;

import com.lnjoying.justice.omc.db.mapper.*;
import com.lnjoying.justice.omc.db.model.*;
import com.lnjoying.justice.omc.domain.model.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/11/1 16:33
 */

@Repository
public class AlarmRepository
{
    @Autowired
    private TblOmcAlertGroupMapper alertGroupMapper;

    @Autowired
    private TblOmcAlertMetricMapper alertMetricMapper;

    @Autowired
    private TblOmcAlarmMapper alarmMapper;

    @Autowired
    private TblOmcRuleMapper ruleMapper;

    @Autowired
    private TblOmcReceiverMapper receiverMapper;

    @Autowired
    private TblOmcNotifyRuleMapper notifyRuleMapper;

    @Autowired
    private TblOmcNotifyObjectMapper notifyObjectMapper;

    @Autowired
    private TblOmcAlertLogMapper alertLogMapper;

    @Autowired
    private TblOmcAlertLogProcessMapper alertLogProcessMapper;

    @Autowired
    private TblOmcAlertSendLogMapper alertSendLogMapper;


    public List<TblOmcAlertGroup> selectAlarmGroups(String name)
    {
        return alertGroupMapper.selectAllByName(name);
    }


    public List<TblOmcAlertMetric> selectAlarmMetrics(String groupId, String metricName)
    {
        return alertMetricMapper.selectAll(groupId, metricName);
    }

    public int insertAlarmSelective(TblOmcAlarm record)
    {
        return alarmMapper.insertSelective(record);
    }

    public int insertRuleSelective(TblOmcRule record)
    {
        return ruleMapper.insertSelective(record);
    }

    public int insertReceiverSelective(TblOmcReceiver record)
    {
        return receiverMapper.insertSelective(record);
    }

    public int insertNotifyRuleSelective(TblOmcNotifyRule record)
    {
        return notifyRuleMapper.insertSelective(record);
    }

    public int batchInsertNotifyObjects(List<TblOmcNotifyObject> notifyObjects)
    {
        return notifyObjectMapper.batchInsert(notifyObjects);
    }

    public int updateAlarm(TblOmcAlarm record)
    {
        return alarmMapper.updateByPrimaryKeySelective(record);
    }

    public TblOmcRule selectRuleByAlarmId(String alarmId)
    {
        return ruleMapper.selectOneByAlarmId(alarmId);
    }

    public int updateRule(TblOmcRule record)
    {
        return ruleMapper.updateByPrimaryKeySelective(record);
    }

    public List<TblOmcNotifyRule> selectNotifyRules(String alarmId)
    {
        return notifyRuleMapper.selectAllByAlarmId(alarmId);
    }

    public List<String> selectNotifyRuleIdsByAlarmId(String alarmId)
    {
        return notifyRuleMapper.selectIdsByAlarmId(alarmId);
    }

    public int deleteNotifyRule(String notifyRuleId)
    {
        if (StringUtils.hasText(notifyRuleId))
        {
            return notifyRuleMapper.deleteByPrimaryKey(notifyRuleId);
        }

        return 0;
    }

    public List<TblOmcNotifyObject> selectNotifyObjectsByAlarmId(String alarmId)
    {
        return notifyObjectMapper.selectByAlarmId(alarmId);
    }

    public List<TblOmcNotifyObject> selectByNotifyRuleId(String notifyRuleId)
    {
        return notifyObjectMapper.selectByNotifyRuleId(notifyRuleId);
    }


    public List<Alarm> selectAll(String alertGroupId, String bpId, String userId, String name, Integer ruleType)
    {
        return alarmMapper.selectAll(alertGroupId, bpId, userId, name, ruleType);
    }

    public int deleteRule(String ruleId)
    {
        if (StringUtils.hasText(ruleId))
        {
            return ruleMapper.deleteByPrimaryKey(ruleId);
        }

        return 0;
    }

    public int deleteNotifyObjectByPrimaryKey(String notifyObjectId)
    {
        if (StringUtils.hasText(notifyObjectId))
        {
            return notifyObjectMapper.deleteByPrimaryKey(notifyObjectId);
        }

        return 0;
    }

    public int deleteNotifyObjectByNotifyRuleId(String notifyRuleId)
    {
        if (StringUtils.hasText(notifyRuleId))
        {
            return notifyObjectMapper.deleteByNotifyRuleId(notifyRuleId);
        }

        return 0;
    }


    public List<TblOmcAlertLog> selectAllAlertLogs(String queryBpId, String queryUserId, String alertGroupId, Integer status,  Integer alertStatus, String processStatus,
                                                   Integer level, String resourceTypeId,
                                                   String resourceId)
    {
        return alertLogMapper.selectAll(queryBpId, queryUserId, alertGroupId, alertStatus, processStatus, level, resourceTypeId, resourceId);
    }

    public AlarmDetail selectAlarmById(String id)
    {
        AlarmDetail alarmDetail = alarmMapper.selectById(id);
        if (Objects.nonNull(alarmDetail))
        {
            TblOmcRule tblOmcRule = selectRuleByAlarmId(id);
            if (Objects.nonNull(tblOmcRule))
            {
                AlertRule alertRule = new AlertRule();
                BeanUtils.copyProperties(tblOmcRule, alertRule);
                alarmDetail.setAlertRule(alertRule);
            }
        }

        return alarmDetail;
    }

    public TblOmcAlarm selectAlarmByPrimaryKey(String id)
    {
        return alarmMapper.selectByPrimaryKey(id);
    }

    public int deleteAlarmByPrimaryKey(String id)
    {
        return alarmMapper.deleteByPrimaryKey(id);
    }

    public int insertAlertLogProcessSelective(TblOmcAlertLogProcess record)
    {
        return alertLogProcessMapper.insertSelective(record);
    }

    public List<TblOmcAlertLogProcess> selectAlertLogProcess(String alertLogId)
    {
        return alertLogProcessMapper.selectByAlertLogId(alertLogId);
    }

    public int insertAlertLog(TblOmcAlertLog record)
    {
        return alertLogMapper.insertSelective(record);
    }

    public List<TblOmcAlertLog> selectAllByAlertStatus(Integer alertStatus)
    {
        return alertLogMapper.selectAllByAlertStatus(alertStatus);
    }

    public List<String> selectIdByAlertStatus(Integer alertStatus)
    {
        return alertLogMapper.selectIdByAlertStatus(alertStatus);
    }

    public int insertAlertSendLogSelective(TblOmcAlertSendLog record)
    {
        return alertSendLogMapper.insertSelective(record);
    }

    public TblOmcAlertLog selectAlertLog(String id)
    {
       return alertLogMapper.selectByPrimaryKey(id);
    }

    public List<String> selectReceiversByAlarmId(String alarmId)
    {
        return notifyRuleMapper.selectReceiversByAlarmId(alarmId);
    }

    public ReceiverDetail selectReceiverById(String receiverId, String alarmId)
    {
        return receiverMapper.selectById(receiverId, alarmId);
    }

    public int selectAlertLogCounts(Date startDate, Date endDate, int value, String bpId, String userId)
    {
        return alertLogMapper.selectAllByCreateTimeBetweenEqualAndLevel(startDate, endDate, value, bpId, userId);
    }


    public List<DailyAlertLog> selectDailyTrendAlertLogs(String bpId, String userId, Date minCreateTime, Date maxCreateTime)
    {
        return alertLogMapper.selectByCreateTimeBetweenEqualAndLevel(minCreateTime, maxCreateTime, null, bpId, userId);
    }

    public TblOmcAlertGroup selectAlertGroup(String alertGroupId)
    {
        return alertGroupMapper.selectByPrimaryKey(alertGroupId);
    }

    public TblOmcAlertMetric selectAlertMetric(String alertMetricId)
    {
        return alertMetricMapper.selectByPrimaryKey(alertMetricId);
    }

    public TblOmcAlertLog selectOneByUniqueHash(Integer uniqueHash)
    {
        return alertLogMapper.selectOneByUniqueHash(uniqueHash);
    }
}
