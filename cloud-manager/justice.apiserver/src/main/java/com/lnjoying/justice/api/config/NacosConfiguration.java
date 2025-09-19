package com.lnjoying.justice.api.config;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2022/5/23 17:56
 */

import com.alibaba.boot.nacos.config.autoconfigure.NacosConfigBootBeanDefinitionRegistrar;
import com.alibaba.nacos.api.config.annotation.NacosConfigurationProperties;
import com.alibaba.nacos.api.config.annotation.NacosProperty;
import com.alibaba.nacos.spring.context.annotation.config.EnableNacosConfig;
import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource;
import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySources;
import com.lnjoying.justice.commonweb.handler.ConfigBootListener;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Import;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Objects;
import java.util.Properties;


@Configuration
@EnableNacosConfig()
@NacosPropertySources({
        @NacosPropertySource(dataId = "com.justice.db.config", groupId = "db", autoRefreshed = true),
        @NacosPropertySource(dataId = "com.justice.redis.config", groupId = "redis", autoRefreshed = true)/*,
        @NacosPropertySource(dataId = "com.justice.api.config.notify", groupId = "api", autoRefreshed = true)*/
})
//@Import({ConfigBootListener.class})
@Import(value = { NacosConfigBootBeanDefinitionRegistrar.class })
public class NacosConfiguration
{
    @Bean
    MailConfig mailConfig()
    {
        return new MailConfig();
    }

    @Bean
    SmsConfig smsConfig()
    {
        return new SmsConfig();
    }

    @Bean(name = "mailSender")
    @DependsOn("nacosListener")
    JavaMailSender createJavaMailSender(MailConfig mailConfig)
    {

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        if (StringUtils.isBlank(mailConfig.getHost())
                || StringUtils.isBlank(mailConfig.getUsername())
                || StringUtils.isBlank(mailConfig.getPassword()))
        {
            return mailSender;
        }
        mailSender.setHost(mailConfig.getHost());
        mailSender.setUsername(mailConfig.getUsername());
        mailSender.setPassword(mailConfig.getPassword());
        Properties props = mailSender.getJavaMailProperties();
        props.setProperty("mail.smtp.auth", mailConfig.getAuth());
        props.setProperty("smtp.starttls.enable", mailConfig.getEndableStarttls());
        props.setProperty("smtp.starttls.required", mailConfig.getRequiredStarttls());
        mailSender.setJavaMailProperties(props);
        return mailSender;
    }
}

