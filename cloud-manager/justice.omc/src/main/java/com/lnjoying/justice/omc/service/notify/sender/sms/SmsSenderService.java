package com.lnjoying.justice.omc.service.notify.sender.sms;

import com.lnjoying.justice.commonweb.util.CollectionUtils;
import com.lnjoying.justice.omc.service.CombRpcService;
import com.lnjoying.justice.schema.common.ErrorCode;
import com.lnjoying.justice.schema.service.apiserver.TipMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/11/10 20:08
 */

@Slf4j
@Service("smsSender")
public class SmsSenderService implements SmsSender
{

    @Autowired
    CombRpcService combRpcService;

    public Integer sendBatchSms(List<TipMessageService.NotifyParam> notifyParams, List<String> mobiles, String template)
    {
        if (CollectionUtils.isEmpty(mobiles)) {
            return 0;
        }
        log.info("send sms. to: {}", mobiles);
        try
        {
            Integer integer = combRpcService.getTipMessageService().sendBatchSms(notifyParams, mobiles,  template);
            return ErrorCode.SUCCESS.getCode();
        }
        catch (Exception e)
        {
            log.info("send error." + e);
            return ErrorCode.SystemError.getCode();
         }
    }


}
