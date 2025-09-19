package com.lnjoying.justice.api.handler;

import com.alibaba.nacos.api.config.convert.NacosConfigConverter;
import com.lnjoying.justice.api.config.MailConfig;
import com.lnjoying.justice.commonweb.util.ApplicationUtils;
import com.lnjoying.justice.commonweb.util.YamlParserUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.yaml.snakeyaml.Yaml;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2022/7/12 19:42
 */

@Slf4j
public class JavaMailSenderConverter implements NacosConfigConverter<JavaMailSender>
{

    @Override
    public boolean canConvert(Class<JavaMailSender> targetType)
    {
        return JavaMailSender.class.equals(targetType);
    }

    @Override
    public JavaMailSender convert(String config)
    {
        return javaMailSender(config);
    }


    private JavaMailSenderImpl javaMailSender(String config)
    {
        if (StringUtils.isNotBlank(config))
        {
            try
            {
                Yaml yaml = new Yaml();
                Map<String, Object> load = yaml.loadAs(config, Map.class);
                Map<String, Object> mail = (LinkedHashMap<String, Object>)load.get("mail");
                String host = (String)mail.get("host");
                String username = (String)mail.get("username");
                String password = (String)mail.get("password");
                String auth = (String)mail.get("auth");
                String endableStarttls = (String)mail.get("endableStarttls");
                String requiredStarttls = (String)mail.get("requiredStarttls");
                String sendFrom = (String)mail.get("sendFrom");

                JavaMailSenderImpl mailSender = ApplicationUtils.getBean("mailSender", JavaMailSenderImpl.class);
                mailSender.setHost(host );
                mailSender.setUsername(username);
                mailSender.setPassword(password);
                Properties props = mailSender.getJavaMailProperties();
                props.setProperty("mail.smtp.auth", auth);
                props.setProperty("smtp.starttls.enable", endableStarttls);
                props.setProperty("smtp.starttls.required", requiredStarttls);
                mailSender.setJavaMailProperties(props);
                return mailSender;
            }
            catch (Exception e)
            {
                log.error("parse java mail properties failed:{}", e);
            }

        }
       return null;
    }
}
