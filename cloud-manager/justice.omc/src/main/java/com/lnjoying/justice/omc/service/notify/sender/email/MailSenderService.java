package com.lnjoying.justice.omc.service.notify.sender.email;

import com.lnjoying.justice.omc.service.CombRpcService;
import com.lnjoying.justice.schema.common.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/11/10 20:10
 */

@Service("mailSender")
@Slf4j
public class MailSenderService implements MailSender {

    @Autowired
    private CombRpcService combRpcService;


    public MailSenderService() {
        log.info("start mail service");
    }


    public Integer sendBatchEmail(String[] address, String template, Map<String, String> paramMap) {
        if (address == null) {
            return 0;
        }
        log.info("send email. to: {}", (Object) address);
        try {
            combRpcService.getTipMessageService().sendBatchEmail(address, paramMap, template);
            return ErrorCode.SUCCESS.getCode();
        } catch (Exception e) {
            log.info("send error." + e);
            return ErrorCode.SystemError.getCode();
        }


    }

}
