package com.lnjoying.justice.omc.service.notify.sender.sms;

import com.lnjoying.justice.schema.service.apiserver.TipMessageService;

import java.util.List;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/11/10 19:27
 */

public interface SmsSender {
    Integer sendBatchSms(List<TipMessageService.NotifyParam> notifyParams, List<String> mobiles, String template);
}