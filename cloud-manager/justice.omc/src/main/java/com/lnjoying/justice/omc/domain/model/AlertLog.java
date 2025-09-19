package com.lnjoying.justice.omc.domain.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.lnjoying.justice.omc.db.model.*;
import com.lnjoying.justice.omc.db.repo.AlarmRepository;
import com.lnjoying.justice.omc.db.repo.ReceiverRepository;
import com.lnjoying.justice.omc.domain.dto.req.AlertLogProcessReq;
import com.lnjoying.justice.omc.domain.dto.rsp.BaseRsp;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/11/2 16:13
 */

@Slf4j
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AlertLog extends BaseRsp
{
    @ApiModelProperty(value = "")
    private String id;

    @ApiModelProperty(value = "")
    private String alarmId;

    @ApiModelProperty(value = "")
    private String alarmName;

    @ApiModelProperty(value = "")
    private String ruleId;

    @ApiModelProperty(value = "")
    private String alertGroupId;

    /**
     * alert type(1:event;2:monitor)
     */
    @ApiModelProperty(value = "alert type(1:event;2:monitor)")
    private Integer alertType;

    /**
     * alert_status(1:firing;2:resolved;3pending;4:inhibited;5:silenced;6:expired;7:unknown)
     */
    @ApiModelProperty(value = "alert_status(1:firing;2:resolved;3pending;4:inhibited;5:silenced;6:expired;7:unknown)")
    private Integer alertStatus;

    /**
     * level(1:info;2:warning;3:critical)
     */
    @ApiModelProperty(value = "level(1:info;2:warning;3:critical)")
    private Integer level;

    /**
     * way(1:sms;2:email)
     */
    @ApiModelProperty(value = "way(1:sms;2:email)")
    private Integer way;

    @ApiModelProperty(value = "")
    private Object labels;

    @ApiModelProperty(value = "")
    private String summary;

    @ApiModelProperty(value = "")
    private String description;

    @ApiModelProperty(value = "")
    private Boolean inSilence;

    /**
     * create time
     */

    @ApiModelProperty(value = "create time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * update time
     */
    @ApiModelProperty(value = "update time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    @ApiModelProperty(value = "")
    private String bpId;

    @ApiModelProperty(value = "")
    private String userId;

    private List<AlertLogProcess> alertLogProcessList;

    private AlertRule alertRule;

    private List<AlertNotifyObject> notifyObjects = new ArrayList<>();


    public static AlertLog of(TblOmcAlertLog tblOmcAlertLog, AlarmRepository alarmRepository, ReceiverRepository receiverRepository)
    {
        try
        {
            AlertLog alertLog = new AlertLog();
            BeanUtils.copyProperties(tblOmcAlertLog, alertLog);
            String alertLogId = alertLog.getId();
            if (StringUtils.isNotBlank(alertLogId))
            {
                List<TblOmcAlertLogProcess> tblOmcAlertLogProcesses = alarmRepository.selectAlertLogProcess(alertLogId);
                if (!CollectionUtils.isEmpty(tblOmcAlertLogProcesses))
                {
                    List<AlertLogProcess> alertLogProcesses = tblOmcAlertLogProcesses.stream().map(alertLogProcess ->
                    {
                        AlertLogProcess alertLogProcess1 = new AlertLogProcess();
                        BeanUtils.copyProperties(alertLogProcess, alertLogProcess1);
                        return alertLogProcess1;
                    }).collect(Collectors.toList());
                    alertLog.setAlertLogProcessList(alertLogProcesses);
                }

            }

            String alarmId = alertLog.getAlarmId();
            if (StringUtils.isNotBlank(alarmId))
            {
                AlertRule alertRule = new AlertRule();
                TblOmcRule tblOmcRule = alarmRepository.selectRuleByAlarmId(alarmId);
                if (Objects.nonNull(tblOmcRule))
                {
                    BeanUtils.copyProperties(tblOmcRule, alertRule);
                    alertLog.setAlertRule(alertRule);
                }

                TblOmcAlarm tblOmcAlarm = alarmRepository.selectAlarmByPrimaryKey(alarmId);
                if (Objects.nonNull(tblOmcAlarm))
                {
                    alertLog.setAlarmName(tblOmcAlarm.getName());
                }

                List<TblOmcNotifyObject> tblOmcNotifyObjects = alarmRepository.selectNotifyObjectsByAlarmId(alarmId);
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
                        return alertNotifyObject;
                    }).collect(Collectors.toList());
                    alertLog.setNotifyObjects(alertNotifyObjects);
                }
            }

            return alertLog;
        }
        catch (Exception e)
        {
            log.error("alert log error:{}", e);
            return null;
        }

    }

    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static final class AlertLogProcess
    {
        @ApiModelProperty(value = "")
        private String id;

        @ApiModelProperty(value = "")
        private String alertLogId;

        @ApiModelProperty(value = "")
        private String processStatus;

        @ApiModelProperty(value = "")
        private Date processTime;

        @ApiModelProperty(value = "")
        private String processor;

        @ApiModelProperty(value = "")
        private String bpId;

        @ApiModelProperty(value = "")
        private String userId;

        /**
         * create time
         */
        @ApiModelProperty(value = "create time")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
        private Date createTime;

        /**
         * update time
         */
        @ApiModelProperty(value = "update time")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
        private Date updateTime;

        @ApiModelProperty(value = "")
        private String message;
    }

}
