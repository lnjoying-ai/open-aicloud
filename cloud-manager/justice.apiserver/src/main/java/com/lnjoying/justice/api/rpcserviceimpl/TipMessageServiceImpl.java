package com.lnjoying.justice.api.rpcserviceimpl;

import com.lnjoying.justice.api.config.MailConfig;
import com.lnjoying.justice.api.config.SmsConfig;
import com.lnjoying.justice.api.entity.BatchSmsRsp;
import com.lnjoying.justice.api.service.MailSenderService;
import com.lnjoying.justice.api.service.SmsSenderService;
import com.lnjoying.justice.schema.common.ErrorCode;
import com.lnjoying.justice.schema.service.apiserver.TipMessageService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.servicecomb.provider.pojo.RpcSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RpcSchema(schemaId = "tipMessage")
public class TipMessageServiceImpl implements TipMessageService
{
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
    private static Logger LOGGER = LogManager.getLogger();

    public Integer sendEmail(String dst, Map<String, String> paramMap, String template)
    {
        LOGGER.info("send email");
        return mailSenderService.sendEmail(dst, paramMap, template);
    }

    public Integer sendBatchEmail(String [] dst, Map<String, String> paramMap, String template)
    {
        LOGGER.info("send email");
        return mailSenderService.sendBatchEmail(dst, paramMap, template);
    }

    public Integer sendSingleSms(List<NotifyParam> notifyParams, String mobile, String template)
    {
        BatchSmsRsp smsRsp = smsSenderService.getSmsSender().sendBatchSms(notifyParams, Arrays.asList(mobile), template);
        if (null != smsRsp && smsRsp.getCode().equals("0"))
        {
            return ErrorCode.SUCCESS.getCode();
        }
        return ErrorCode.SMSError.getCode();
    }

    public Integer sendBatchSms(List<NotifyParam> notifyParams, List<String> mobiles, String template)
    {
        BatchSmsRsp smsRsp = smsSenderService.getSmsSender().sendBatchSms(notifyParams, mobiles, template);
        if (null != smsRsp && smsRsp.getCode().equals("0"))
        {
            return ErrorCode.SUCCESS.getCode();
        }
        return ErrorCode.SMSError.getCode();
    }
}