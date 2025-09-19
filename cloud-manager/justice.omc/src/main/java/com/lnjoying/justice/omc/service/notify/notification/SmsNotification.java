package com.lnjoying.justice.omc.service.notify.notification;

import com.lnjoying.justice.omc.service.notify.Notification;
import com.lnjoying.justice.omc.service.notify.model.AlertInfo;
import com.lnjoying.justice.omc.service.notify.sender.SendResult;
import com.lnjoying.justice.omc.service.notify.sender.sms.SmsSender;
import com.lnjoying.justice.schema.common.TemplateID;
import com.lnjoying.justice.schema.service.apiserver.TipMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/11/10 19:25
 */

@Slf4j
@Service
public class SmsNotification implements Notification
{

    @Autowired
    private SmsSender smsSender;

    @Override
    public SendResult send(String[] address, List<TipMessageService.NotifyParam> notifyParams) {

        String template = TemplateID.MONITOR_ALERT_CODE;

        List<String> mobiles = Arrays.asList(address);

        Integer smsRsp = smsSender.sendBatchSms(notifyParams, mobiles, template);

        log.info("Sms send to " + mobiles + " with content: " + notifyParams);
        if (null != smsRsp)
        {
            log.info("Sms send to " + mobiles + " Success！");
        }

        return null;

    }


    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
