package com.lnjoying.justice.sys.config;

import com.alibaba.nacos.api.config.annotation.NacosConfigurationProperties;
import com.micro.core.config.JdbcConfig;
import lombok.Data;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.PropertySource;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2022/5/28 12:46
 */
@Data
@NacosConfigurationProperties(dataId = "com.justice.db.config", groupId = "db", autoRefreshed = true)
public class DbConfig extends JdbcConfig
{
}
