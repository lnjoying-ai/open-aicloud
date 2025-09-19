package com.lnjoying.justice.omc.service.notify;

import com.lnjoying.justice.omc.service.notify.model.AlertInfo;
import com.lnjoying.justice.omc.service.notify.sender.SendResult;
import com.lnjoying.justice.schema.service.apiserver.TipMessageService;
import org.springframework.beans.factory.InitializingBean;

import java.util.List;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/11/10 19:24
 */

public interface Notification extends InitializingBean
{
    SendResult send(String[] address, List<TipMessageService.NotifyParam> notifyParams);
}
