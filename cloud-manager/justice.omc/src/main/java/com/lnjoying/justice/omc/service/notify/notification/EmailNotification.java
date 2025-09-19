package com.lnjoying.justice.omc.service.notify.notification;

import com.lnjoying.justice.omc.service.notify.Notification;
import com.lnjoying.justice.omc.service.notify.model.AlertInfo;
import com.lnjoying.justice.omc.service.notify.sender.SendResult;
import com.lnjoying.justice.omc.service.notify.sender.email.MailSender;
import com.lnjoying.justice.schema.common.TemplateID;
import com.lnjoying.justice.schema.service.apiserver.TipMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/11/10 19:24
 */

@Slf4j
@Service
public class EmailNotification implements Notification
{
    @Autowired
    private MailSender mailSender;

    @Override
    public SendResult send(String[] address, List<TipMessageService.NotifyParam> notifyParams) {

        log.info("Email send to " + String.join(", ", address) + " with content: " + notifyParams);

        Map<String, String> paramMap = new HashMap<>();
        notifyParams.forEach(notifyParam -> paramMap.put(notifyParam.getKey(), notifyParam.getValue()));

        //简易内容格式
        Integer code = mailSender.sendBatchEmail(address, TemplateID.MONITOR_ALERT_CODE, paramMap);

        if (code == 0) {
            log.info("Email send to " + Arrays.asList(address) + " Success！");
            return SendResult.success("");
        } else {
            log.info("Email send to " + Arrays.asList(address) + " Fail！");
            return SendResult.fail("");
        }

    }



    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
