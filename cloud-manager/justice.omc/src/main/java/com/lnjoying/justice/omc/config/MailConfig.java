package com.lnjoying.justice.omc.config;

import com.alibaba.nacos.api.config.ConfigType;
import com.alibaba.nacos.api.config.annotation.NacosConfigurationProperties;
import lombok.Data;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Data
@NacosConfigurationProperties(dataId = "com.justice.api.config.notify", groupId = "api", autoRefreshed = true, prefix = "mail", type = ConfigType.YAML)
public class MailConfig
{
    private static Logger LOGGER = LogManager.getLogger();
    public MailConfig()
    {
        LOGGER.info("config email");
    }
    //@Value(" ${mail.sendFrom}")
    //@NacosProperty(value = "${mail.sendFrom}")
    private String sendFrom;

    //@Value("${mail.host}")
    //@NacosProperty(value = "${mail.host}")
    private String host;

    //@Value("${mail.username}")
    //@NacosProperty(value = "${mail.username}")
    private String username;

    //@Value("${mail.password}")
    //@NacosProperty(value = "${mail.password}")
    private String password;

    //@Value("${mail.auth}")
    // @NacosProperty(value = "${mail.auth}")
    private String auth;

    //@Value("${mail.endableStarttls}")
    //@NacosProperty(value = "${mail.endableStarttls}")
    private String endableStarttls;

    //@Value("${mail.requiredStarttls}")
    //@NacosProperty(value = "${mail.requiredStarttls}")
    private String requiredStarttls;

//    @Value("${spring.mail.template.subject}")
//    private String templateSubject;


}
