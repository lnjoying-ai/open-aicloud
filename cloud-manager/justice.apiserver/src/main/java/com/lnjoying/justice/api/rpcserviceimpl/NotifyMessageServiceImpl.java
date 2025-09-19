package com.lnjoying.justice.api.rpcserviceimpl;

import com.lnjoying.justice.api.config.ContactAdmin;
import com.lnjoying.justice.api.config.MailConfig;
import com.lnjoying.justice.api.config.SmsConfig;
import com.lnjoying.justice.api.service.MailSenderService;
import com.lnjoying.justice.api.service.SmsSenderService;
import com.lnjoying.justice.commonweb.util.JsonUtils;
import com.lnjoying.justice.schema.service.apiserver.NotifyMessageService;
import com.lnjoying.justice.schema.service.apiserver.TipMessageService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.servicecomb.provider.pojo.RpcSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RpcSchema(schemaId = "notifyMessage")
public class NotifyMessageServiceImpl implements NotifyMessageService
{
    private static Logger LOGGER = LogManager.getLogger();
    @Autowired
    MailSenderService mailSenderService;

    @Autowired
    MailConfig mailConfig;

    @Autowired
    SmsConfig smsConfig;

    @Autowired
    SmsSenderService smsSenderService;

    @Autowired
    JavaMailSender mailSender;

    @Autowired
    ContactAdmin contactAdmin;

    @Override
    public Integer notifyAdmin(List<TipMessageService.NotifyParam> notifyParams, String template)
    {
        try
        {
            LOGGER.info("notify message. {}", template);
            if ((contactAdmin.getPhones() != null && contactAdmin.getPhones().size() > 0))
            {
                smsSenderService.getSmsSender().sendBatchSms(notifyParams, contactAdmin.getPhones(), template);
            }

            if ((contactAdmin.getEmails() != null && contactAdmin.getEmails().size() > 0))
            {
                Map<String, String> paramMap = new HashMap<>();
                notifyParams.forEach(notifyParam -> paramMap.put(notifyParam.getKey(), notifyParam.getValue()));
                List<String> eamils = contactAdmin.getEmails();
                mailSenderService.sendBatchEmail(eamils.toArray(new String[eamils.size()]), paramMap, template);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("notify error. {}", e.getMessage());
            return 0;
        }
        return 0;
    }
}