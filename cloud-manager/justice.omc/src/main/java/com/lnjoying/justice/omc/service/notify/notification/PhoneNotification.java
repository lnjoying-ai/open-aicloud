package com.lnjoying.justice.omc.service.notify.notification;

import com.lnjoying.justice.omc.service.notify.Notification;
import com.lnjoying.justice.omc.service.notify.model.AlertInfo;
import com.lnjoying.justice.omc.service.notify.sender.SendResult;
import com.lnjoying.justice.schema.service.apiserver.TipMessageService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/11/10 19:26
 */

@Service
public class PhoneNotification implements Notification
{

    @Override
    public SendResult send(String[] address,  List<TipMessageService.NotifyParam> notifyParams)
    {
        return null;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
