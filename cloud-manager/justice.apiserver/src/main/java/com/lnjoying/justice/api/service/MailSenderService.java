package com.lnjoying.justice.api.service;

import com.alibaba.nacos.api.annotation.NacosInjected;
import com.alibaba.nacos.api.config.ConfigService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.lnjoying.justice.api.config.ApiConfig;
import com.lnjoying.justice.api.config.MailConfig;
import com.lnjoying.justice.api.config.NotifyTemplate;
import com.lnjoying.justice.commonweb.util.JacksonUtils;
import com.lnjoying.justice.commonweb.util.TemplateEngineUtils;
import com.lnjoying.justice.schema.common.ErrorCode;
import com.lnjoying.justice.schema.common.TemplateID;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.io.*;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class MailSenderService
{
    private static Logger LOGGER = LogManager.getLogger();

    @Autowired
    private MailConfig mailConfig;

    @Autowired
    ApiConfig apiConfig;

    @Autowired
    private JavaMailSender mailSender;

    @NacosInjected
    private ConfigService configService;

    public MailSenderService()
    {
        LOGGER.info("start mail service");
    }

    public Integer sendEmail(String destination, Map<String, String> paramMap, String template)
    {
        if (destination == null)
        {
            return 0;
        }

        try
        {
            LOGGER.info("send email. to: {}", destination);
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(mailConfig.getSendFrom());
            message.setTo(destination);
            message.setSubject(getSubject(template));
            String text = assembleContent(paramMap, template);
            if (StringUtils.isEmpty(text))
            {
                LOGGER.warn("send content is empty");
                return ErrorCode.SystemError.getCode();
            }
            message.setText(text);
            mailSender.send(message);
            return ErrorCode.SUCCESS.getCode();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.info("send error:{}" , e);
            return ErrorCode.SystemError.getCode();
        }
    }

    public Integer sendBatchEmail(String [] destination, Map<String, String> paramMap, String template)
    {
        if (destination == null)
        {
            return 0;
        }
        LOGGER.info("send email. to: {}", destination);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(mailConfig.getSendFrom());
        message.setTo(destination);
        message.setSubject(getSubject(template));
        message.setText(assembleContent(paramMap, template));
        try
        {
            mailSender.send(message);
            return ErrorCode.SUCCESS.getCode();
        }
        catch (Exception e)
        {
            LOGGER.info("send error." + e);
            return ErrorCode.SystemError.getCode();
        }
    }

    String assembleContent(Map<String, String> paramMap, String template)
    {
        String retContent = assembleEmailMessageByFreemarker(paramMap, template);
        return retContent;
    }

    String assembleEmailMessage(String content, String templateId)
    {
        String [] infoArray = content.split(",");
        String format = templatContent.get(templateId);
        String ret = String.format(format, infoArray);
        return ret;
    }

    String assembleEmailMessageByDollar(String content, String templateId)
    {
        String [] infoArray = content.split("\\$\\$\\$\\$");
        String format = templatContent.get(templateId);
        String ret = String.format(format, infoArray);
        return ret;
    }

    String assembleEmailMessageByFreemarker(Map<String, String> paramMap, String templateId)
    {
        String template = apiConfig.getNotifyTemplate().get(templateId);
        if (!CollectionUtils.isEmpty(paramMap))
        {
            try
            {
                String afterRender = TemplateEngineUtils.render(template, paramMap);
                return afterRender;
            }
            catch (Exception e)
            {
                LOGGER.error("template paramMap:{}, error:{}", paramMap, e);
            }

        }

        return "";
    }


    private Map<String, String> templatContent = new ConcurrentHashMap<>();

    @PostConstruct
    void initTemplateContentFromNacos()
    {
        try
        {
            String authVerificationCode = configService.getConfig("com.justice.api.config.notify.auth_verification_code", "api", 30000);
            String patchVerificationCode = configService.getConfig("com.justice.api.config.notify.patch_verification_code", "api", 30000);
            String regVerificationCode = configService.getConfig("com.justice.api.config.notify.reg_verification_code", "api", 30000);
            String monitorAlertCode = configService.getConfig("com.justice.api.config.notify.monitor_alert_code", "api", 30000);
            String billBalanceInsufficientAlertCode = configService.getConfig("com.justice.api.config.notify.bill_balance_insufficient_alert_code", "api", 30000);
            String billBalanceOverdueAlertCode = configService.getConfig("com.justice.api.config.notify.bill_balance_overdue_alert_code", "api", 30000);
            String billOrderBalanceInsufficientAlertCode = configService.getConfig("com.justice.api.config.notify.bill_order_balance_insufficient_alert_code", "api", 30000);
            String billBalanceOverdueReleaseResourcesAlertCode = configService.getConfig("com.justice.api.config.notify.bill_balance_overdue_release_resources_alert_code", "api", 30000);
            if (StringUtils.hasText(authVerificationCode))
            {
                authVerificationCode.replace("\\n" ,"\n");
            }
            if (StringUtils.hasText(patchVerificationCode))
            {
                patchVerificationCode.replace("\\n" ,"\n");
            }
            if (StringUtils.hasText(regVerificationCode))
            {
                regVerificationCode.replace("\\n" ,"\n");
            }
            if (StringUtils.hasText(monitorAlertCode))
            {
                monitorAlertCode.replace("\\n" ,"\n");
            }
            if (StringUtils.hasText(billBalanceInsufficientAlertCode))
            {
                billBalanceInsufficientAlertCode.replace("\\n" ,"\n");
            }
            if (StringUtils.hasText(billBalanceOverdueAlertCode))
            {
                billBalanceOverdueAlertCode.replace("\\n" ,"\n");
            }
            if (StringUtils.hasText(billOrderBalanceInsufficientAlertCode))
            {
                billOrderBalanceInsufficientAlertCode.replace("\\n" ,"\n");
            }
            if (StringUtils.hasText(billBalanceOverdueReleaseResourcesAlertCode))
            {
                billBalanceOverdueReleaseResourcesAlertCode.replace("\\n" ,"\n");
            }
            templatContent.putIfAbsent("auth_verification_code", authVerificationCode);
            templatContent.putIfAbsent("patch_verification_code", patchVerificationCode);
            templatContent.putIfAbsent("patch_verification_code", regVerificationCode);
            templatContent.putIfAbsent("monitor_alert_code", monitorAlertCode);
            templatContent.putIfAbsent("bill_balance_insufficient_alert_code", billBalanceInsufficientAlertCode);
            templatContent.putIfAbsent("bill_order_balance_insufficient_alert_code", billOrderBalanceInsufficientAlertCode);
            templatContent.putIfAbsent("bill_balance_overdue_alert_code", billBalanceOverdueAlertCode);
            templatContent.putIfAbsent("bill_balance_overdue_release_resources_alert_code", billBalanceOverdueReleaseResourcesAlertCode);
        }
        catch (Exception e)
        {
            LOGGER.error("init template context failed:{}", e);
        }
    }

    //@PostConstruct
    void initTemplateContent()
    {
        if (templatContent == null)
        {
            templatContent = new HashMap<>();
        }
        Field[] fields = TemplateID.class.getFields();
        for (Field f : fields)
        {
            String templateId = null;
            try {
                templateId = (String)f.get(f.getName());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                continue;
            }

            loadTemplateContent(templateId);
        }

        LOGGER.info("succcess to init template context");
    }

    void loadTemplateContent(String templateId)
    {
        if (templateId == null)
        {
            return;
        }
        String template="";
        String templateFileName = new StringBuilder(System.getProperty("lj_config")).append("/")
                                  .append( "email_template").append("/").append(templateId).append(".template").toString();
        try
        {
            FileInputStream fs = new FileInputStream(new File(templateFileName));
            template = IOUtils.toString(fs, StandardCharsets.UTF_8);
            template=template.replace("\\n" ,"\n");
            templatContent.put(templateId, template);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public String getSubject(String templateId)
    {
        if (null == apiConfig.getNotifyTemplateCode())
        {
            return "";
        }

        NotifyTemplate notifyTemplate = apiConfig.getNotifyTemplateCode().get(templateId);
        if (notifyTemplate == null) return "";

        return notifyTemplate.getEmail();
    }

    public Map<String, String> getTemplatContent()
    {
        return templatContent;
    }

    public void setTemplatContent(Map<String, String> templatContent)
    {
        this.templatContent = templatContent;
    }
}
