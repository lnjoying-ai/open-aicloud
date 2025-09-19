package com.lnjoying.justice.omc.service.notify.sender.email;

import java.util.Map;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/11/10 20:10
 */

public interface MailSender
{
    Integer sendBatchEmail(String[] address, String template, Map<String, String> paramMap);
}
