package com.lnjoying.justice.omc.service.impl;

import com.lnjoying.justice.omc.db.model.TblOmcAlertLog;
import com.lnjoying.justice.omc.db.repo.AlarmRepository;
import com.lnjoying.justice.omc.domain.dto.req.WebhookInfoReq;
import com.lnjoying.justice.omc.domain.model.AlarmDetail;
import com.lnjoying.justice.omc.domain.model.AlarmStatusEnum;
import com.lnjoying.justice.omc.domain.model.AlertStatusEnum;
import com.lnjoying.justice.omc.domain.model.AlertTypeEnum;
import com.lnjoying.justice.omc.service.WebhookService;
import com.lnjoying.justice.omc.service.alert.AlertReduceHandler;
import com.micro.core.common.Utils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.Objects;

import static com.lnjoying.justice.omc.prometheus.model.PrometheusAlertingRule.*;
import static com.lnjoying.justice.omc.util.AlarmUtils.generateAlertUniqueHash;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/11/8 15:31
 */

@Slf4j
@Service
public class WebhookServiceImpl implements WebhookService
{

    @Autowired
    private AlarmRepository alarmRepository;

    @Autowired
    private AlertReduceHandler alertReduceHandler;

    @Override
    public void handleAlerts(WebhookInfoReq req)
    {
        if (CollectionUtils.isEmpty(req.getAlerts()))
        {
            log.info("alert webhook, no alerts");
            return ;
        }

        for ( WebhookInfoReq.Alert alert : req.getAlerts())
        {
            doHandleAlert(alert);

        }

    }

    private void doHandleAlert(WebhookInfoReq.Alert alert)
    {
        try
        {
            String alarmId = alert.getAnnotations().get(ANNOTATION_ALARM_UID);
            if (StringUtils.isBlank(alarmId))
            {
                return;
            }

            AlarmDetail alarmDetail = alarmRepository.selectAlarmById(alarmId);
            if (Objects.isNull(alarmDetail))
            {
                log.warn("no suitable alarm found, illegal alarm id:{}", alarmId);
            }

            Integer status = alarmDetail.getStatus();
            if (AlarmStatusEnum.DEACTIVE.value() == status || AlarmStatusEnum.DELETED.value() == status)
            {
                log.warn("alarm is not active, alarm id:{}", alarmId);
                return;
            }

            TblOmcAlertLog tblOmcAlertLog = new TblOmcAlertLog();
            tblOmcAlertLog.setId(Utils.assignUUId());
            tblOmcAlertLog.setAlarmId(alarmId);
            tblOmcAlertLog.setRuleId(alarmDetail.getAlertRule().getId());
            tblOmcAlertLog.setAlertGroupId(alarmDetail.getAlertGroupId());
            tblOmcAlertLog.setAlertType(AlertTypeEnum.MONITOR.value());

            AlertStatusEnum alertStatusEnum = AlertStatusEnum.fromName(alert.getStatus());
            tblOmcAlertLog.setAlertStatus(alertStatusEnum.value());
            tblOmcAlertLog.setLevel(alarmDetail.getLevel());
            tblOmcAlertLog.setLabels(alert.getLabels());
            tblOmcAlertLog.setSummary(alert.getAnnotations().get(ANNOTATION_SUMMARY));
            tblOmcAlertLog.setDescription(alert.getAnnotations().get(ANNOTATION_DESCRIPTION));
            // todo
            tblOmcAlertLog.setInSilence(false);
            tblOmcAlertLog.setCreateTime(new Date());
            tblOmcAlertLog.setUpdateTime(tblOmcAlertLog.getCreateTime());
            tblOmcAlertLog.setBpId(alarmDetail.getBpId());
            tblOmcAlertLog.setUserId(alarmDetail.getUserId());
            int alertUniqueHash = generateAlertUniqueHash(tblOmcAlertLog);
            tblOmcAlertLog.setUniqueHash(alertUniqueHash);

            if (alertReduceHandler.reduceAlarm(tblOmcAlertLog))
            {
                alarmRepository.insertAlertLog(tblOmcAlertLog);
            }

        }
        catch (Exception e)
        {
            log.error("alert log save failed:{}", e);

        }

    }
}
