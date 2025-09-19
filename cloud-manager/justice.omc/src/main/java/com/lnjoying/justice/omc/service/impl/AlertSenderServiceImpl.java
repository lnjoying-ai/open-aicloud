package com.lnjoying.justice.omc.service.impl;

import com.lnjoying.justice.commonweb.util.JacksonUtils;
import com.lnjoying.justice.omc.config.OmcConfig;
import com.lnjoying.justice.omc.db.model.TblOmcAlarm;
import com.lnjoying.justice.omc.db.model.TblOmcAlertLog;
import com.lnjoying.justice.omc.db.model.TblOmcAlertSendLog;
import com.lnjoying.justice.omc.db.model.TblOmcNotifyRule;
import com.lnjoying.justice.omc.db.repo.AlarmRepository;
import com.lnjoying.justice.omc.domain.model.AlertLevelEnum;
import com.lnjoying.justice.omc.domain.model.AlertSendStatusEnum;
import com.lnjoying.justice.omc.domain.model.AlertStatusEnum;
import com.lnjoying.justice.omc.domain.model.ReceiverDetail;
import com.lnjoying.justice.omc.service.AlertSenderService;
import com.lnjoying.justice.omc.service.notify.Notification;
import com.lnjoying.justice.omc.service.notify.NotificationFactory;
import com.lnjoying.justice.omc.service.notify.model.AlertData;
import com.lnjoying.justice.omc.service.notify.model.AlertInfo;
import com.lnjoying.justice.omc.service.notify.sender.SendResult;
import com.lnjoying.justice.omc.util.DateUtils;
import com.lnjoying.justice.schema.service.apiserver.TipMessageService;
import com.micro.core.common.Pair;
import com.micro.core.common.Utils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;

import static com.lnjoying.justice.omc.domain.model.NotifyChannelEnum.EMAIL;
import static com.lnjoying.justice.omc.domain.model.NotifyChannelEnum.SMS;
import static com.lnjoying.justice.omc.util.DateUtils.isInAlarmPeriod;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/11/10 16:19
 */

@Slf4j
@Service
public class AlertSenderServiceImpl implements AlertSenderService
{

    @Autowired
    private AlarmRepository alarmRepository;

    @Autowired
    private NotificationFactory notificationFactory;

    @Autowired
    private OmcConfig omcConfig;

    @Override
    public void processAlertSend(Pair<String, CompletableFuture<Void>> alertLogPair)
    {
        try
        {
            String alertLogId = alertLogPair.getLeft();
            TblOmcAlertLog tblOmcAlertLog = alarmRepository.selectAlertLog(alertLogId);
            if (Objects.isNull(tblOmcAlertLog))
            {
                return;
            }

            if (!(AlertStatusEnum.FIRING.value() == tblOmcAlertLog.getAlertStatus() ||
                    AlertStatusEnum.RESOLVED.value() == tblOmcAlertLog.getAlertStatus()))
            {
                return;
            }

            String alarmId = tblOmcAlertLog.getAlarmId();
            if (StringUtils.isBlank(alarmId))
            {
                return;
            }

            TblOmcAlarm tblOmcAlarm = alarmRepository.selectAlarmByPrimaryKey(alarmId);
            if (Objects.isNull(tblOmcAlarm))
            {
                return;
            }

            List<TblOmcNotifyRule> tblOmcNotifyRules = alarmRepository.selectNotifyRules(alarmId);
            LocalDateTime now = LocalDateTime.now();
            if (com.alibaba.nacos.common.utils.CollectionUtils.isNotEmpty(tblOmcNotifyRules))
            {
                TblOmcNotifyRule tblOmcNotifyRule = tblOmcNotifyRules.get(0);
                // Determine whether it is within the notification period
                if(!isInAlarmPeriod(tblOmcNotifyRule.getNotifyStartTime(), tblOmcNotifyRule.getNotifyEndTime()))
                {
                    return;
                }
            }

            List<String> receivers = alarmRepository.selectReceiversByAlarmId(alarmId);
            if (CollectionUtils.isEmpty(receivers))
            {
                return;
            }

            receivers.stream().forEach(receiverId -> CompletableFuture.runAsync(() ->
            {
                ReceiverDetail receiverDetail = alarmRepository.selectReceiverById(receiverId, alarmId);
                if (Objects.nonNull(receiverDetail))
                {
                    String notifyChannels = receiverDetail.getNotifyChannels();
                    if (StringUtils.isNotBlank(notifyChannels))
                    {
                        String[] notifyChannelArr = notifyChannels.split(",");
                        if (notifyChannelArr != null && notifyChannelArr.length > 0)
                        {
                            for (String channel : notifyChannelArr)
                            {
                                try
                                {

                                    int channelCode = Integer.parseInt(channel);
                                    List<String> contactList = new ArrayList<>();
                                    List<TipMessageService.NotifyParam> notifyParams;
                                    if (EMAIL.value() == channelCode)
                                    {
                                        if (!Boolean.parseBoolean(omcConfig.getSendEmail()))
                                        {
                                            return;
                                        }
                                        contactList.add(receiverDetail.getEmail());
                                        notifyParams = buildEmailContent(tblOmcAlertLog, tblOmcAlarm);
                                    }
                                    else if (SMS.value() == channelCode)
                                    {
                                        if (!Boolean.parseBoolean(omcConfig.getSendSms()))
                                        {
                                            return;
                                        }
                                        contactList.add(receiverDetail.getPhone());
                                        notifyParams = buildSmsContent(tblOmcAlertLog, tblOmcAlarm);
                                    }
                                    else
                                    {
                                        log.warn("unknown notify channel:{}", channel);
                                        notifyParams = new ArrayList<>();
                                    }

                                    Notification notification = notificationFactory.createNotificationService(channelCode);

                                    SendResult sendResult = notification.send(contactList.toArray(new String[0]), notifyParams);
                                    if (Objects.nonNull(sendResult))
                                    {
                                        if (sendResult.getStatus().equals(SendResult.SendStatus.SUCCESS))
                                        {
                                            addAlertSendStatus(sendResult, AlertSendStatusEnum.SUCCESS.value(), alertLogId);
                                        }
                                        else if(sendResult.getStatus().equals(SendResult.SendStatus.FAILED))
                                        {
                                            addAlertSendStatus(sendResult, AlertSendStatusEnum.FAILED.value(), alertLogId);
                                        }
                                    }
                                }
                                catch (Exception e)
                                {
                                    log.error("send notify message error:{}", e);
                                    addAlertSendStatus(null, AlertSendStatusEnum.UNKNOWN.value(), alertLogId);
                                }
                            }
                        }
                    }
                }

            }).exceptionally(ex -> {
                log.error("do notify error:{}", ex);
                return null;
            }));

            alertLogPair.getRight().complete(null);
        }
        catch (Exception e)
        {
            log.error("processAlertSend error:{}", e);
            alertLogPair.getRight().completeExceptionally(e);
        }


    }

    private List<TipMessageService.NotifyParam> buildEmailContent(TblOmcAlertLog tblOmcAlertLog, TblOmcAlarm tblOmcAlarm)
    {
        List<TipMessageService.NotifyParam> notifyParams = new ArrayList<>();
        String currentTime = DateUtils.formatDate(new Date());
        notifyParams.add(new TipMessageService.NotifyParam("current_time", currentTime));
        notifyParams.add(new TipMessageService.NotifyParam("alarm_id", tblOmcAlarm.getId()));
        notifyParams.add(new TipMessageService.NotifyParam("alarm_name", tblOmcAlarm.getName()));
        notifyParams.add(new TipMessageService.NotifyParam("level", AlertLevelEnum.fromValue(tblOmcAlertLog.getLevel()).name()));
        notifyParams.add(new TipMessageService.NotifyParam("summary", tblOmcAlertLog.getSummary()));
        notifyParams.add(new TipMessageService.NotifyParam("user_id", tblOmcAlertLog.getUserId()));
        notifyParams.add(new TipMessageService.NotifyParam("alert_status_message", alertStatusMessage(tblOmcAlertLog.getAlertStatus())));
        return notifyParams;
    }

    private List<TipMessageService.NotifyParam> buildSmsContent(TblOmcAlertLog tblOmcAlertLog, TblOmcAlarm tblOmcAlarm)
    {
        List<TipMessageService.NotifyParam> notifyParams = new ArrayList<>();
        String currentTime = DateUtils.formatDate(new Date());
        notifyParams.add(new TipMessageService.NotifyParam("current_time", currentTime));
        notifyParams.add(new TipMessageService.NotifyParam("alarm_id", tblOmcAlarm.getId()));
        notifyParams.add(new TipMessageService.NotifyParam("alarm_name", tblOmcAlarm.getName()));
        notifyParams.add(new TipMessageService.NotifyParam("level", AlertLevelEnum.fromValue(tblOmcAlertLog.getLevel()).name()));
        notifyParams.add(new TipMessageService.NotifyParam("summary", tblOmcAlertLog.getSummary()));
        notifyParams.add(new TipMessageService.NotifyParam("user_id", tblOmcAlertLog.getUserId()));
        notifyParams.add(new TipMessageService.NotifyParam("alert_status_message", alertStatusMessage(tblOmcAlertLog.getAlertStatus())));
        return notifyParams;
    }


    public String alertStatusMessage(int status)
    {
        if (AlertStatusEnum.FIRING.value() == status)
        {
            return "告警通知";
        }
        else if (AlertStatusEnum.RESOLVED.value() == status)
        {
            return "告警通知恢复";
        }

        return "通知";
    }

    private  String buildEmailContent(String description)
    {
        String currentTime = DateUtils.formatDate(new Date());
        // currentTime, resourceType, resourceId, description
        StringBuilder content = new StringBuilder();
        content.append(currentTime);
        content.append("$$$$");
        content.append("节点");
        content.append("$$$$");
        content.append("节点id");
        content.append("$$$$");
        content.append(description);
        return content.toString();
    }
    

    private void addAlertSendStatus(SendResult sendResult, int sendStatus, String alarmLogId)
    {
        TblOmcAlertSendLog tblOmcAlertSendLog = new TblOmcAlertSendLog();
        tblOmcAlertSendLog.setAlertLogId(alarmLogId);
        tblOmcAlertSendLog.setId(Utils.assignUUId());
        tblOmcAlertSendLog.setLog(JacksonUtils.objToStrDefault(sendResult));
        tblOmcAlertSendLog.setSendStatus(sendStatus);
        tblOmcAlertSendLog.setCreateTime(new Date());
        alarmRepository.insertAlertSendLogSelective(tblOmcAlertSendLog);
    }

}
