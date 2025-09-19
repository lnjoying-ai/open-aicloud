package com.lnjoying.justice.api.handler;


import com.alibaba.nacos.api.annotation.NacosInjected;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.ConfigType;
import com.alibaba.nacos.api.config.annotation.NacosConfigListener;
import com.alibaba.nacos.api.config.annotation.NacosConfigurationProperties;
import com.alibaba.nacos.api.exception.NacosException;
import com.lnjoying.justice.api.config.MailConfig;
import com.lnjoying.justice.api.config.SmsConfig;
import com.lnjoying.justice.api.service.MailSenderService;
import com.lnjoying.justice.commonweb.util.ApplicationUtils;
import com.lnjoying.justice.commonweb.util.FileUtils;
import com.lnjoying.justice.commonweb.util.NacosConfigLoaderUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.*;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2022/6/1 11:20
 */

@Slf4j
@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
public class NacosListener
{

    @NacosInjected
    private ConfigService configService;

    @Autowired
    @Lazy
    private MailSenderService mailSenderService;

    @NacosConfigListener(dataId = "com.justice.api.config.notify", groupId = "api", type = ConfigType.YAML, converter = JavaMailSenderConverter.class)
    public void onChangeNotify(JavaMailSender javaMailSender) throws Exception {
        log.info("notify config change: {}", javaMailSender);
    }

    @PostConstruct
    public void publishConfig() {
        NacosConfigLoaderUtils.doPublish(configService,"com.justice.api.config.notify", "api", ConfigType.YAML.getType());
    }
}
