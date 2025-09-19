package com.lnjoying.justice.iam;

//import org.apache.servicecomb.springboot2.starter.EnableServiceComb;
//import org.apache.servicecomb.springboot2.starter.EnableServiceComb;
import com.lnjoying.justice.iam.config.SecurityModeConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.servicecomb.springboot2.starter.EnableServiceComb;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.context.request.RequestContextListener;

import static com.lnjoying.justice.commonweb.common.CommonWebScanPath.*;
import static com.lnjoying.justice.commonweb.util.NacosConfigLoaderUtils.loadAndSyncNacosConfigToSpring;

@SpringBootApplication(scanBasePackages = {"com.lnjoying.justice.iam", DEFAULT_MAPPER_PATH, DEFAULT_SERVICE_PATH}, exclude = {SecurityAutoConfiguration.class})
@MapperScan(basePackages = {"com.lnjoying.justice.iam.db.mapper"})
@EnableScheduling
@EnableServiceComb
@ImportResource(locations = DEFAULT_COMMON_WEB_BEAN_RESOURCE)
public class IamApplication
{
    private static Logger LOGGER = LogManager.getLogger();

    @Bean
    public RequestContextListener requestContextListener(){
        return new RequestContextListener();
    }


    /**
     * Main.
     */
    public static void main(String[] args)
    {
        LOGGER.info("iam starting");
        SecurityModeConfig.loadSecurityModeConfig();
        loadAndSyncNacosConfigToSpring();
        SpringApplication.run(IamApplication.class, args);
    }
}
