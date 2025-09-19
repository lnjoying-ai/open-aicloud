/*
package com.lnjoying.justice.ims.handler;

import com.lnjoying.justice.commonweb.util.ApplicationUtils;
import com.lnjoying.justice.ims.service.CombRpcService;
import com.lnjoying.justice.schema.service.sys.SysService;
import lombok.extern.slf4j.Slf4j;
import org.apache.servicecomb.provider.pojo.RpcReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.support.AbstractRefreshableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.stereotype.Component;

import java.util.Properties;

import static com.alibaba.nacos.spring.util.NacosBeanUtils.CONFIG_GLOBAL_NACOS_PROPERTIES_BEAN_NAME;

*/
/**
 * @Description:
 * @Author: Merak
 * @Date: 2022/5/28 13:18
 *//*

@Component
@ConditionalOnMissingBean(name = CONFIG_GLOBAL_NACOS_PROPERTIES_BEAN_NAME)
@Slf4j
public class NacosConfigApplicationListener  implements ApplicationListener<ContextRefreshedEvent>
{

    @RpcReference(microserviceName = "sys", schemaId = "sysService")
    private SysService sysService;

    @Autowired
    private ConfigurableEnvironment environment;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event)
    {
        SysService.NacosConfig nacos = sysService.getNacos();
        log.info("nacos config info: {}", nacos);
        MutablePropertySources propertySources = environment.getPropertySources();
        Properties properties = new Properties();
        properties.setProperty("nacos.config.password", nacos.getPassword());
        properties.setProperty("nacos.config.username", nacos.getUserName());
        properties.setProperty("nacos.config.server-addr", nacos.getServerAddr());
        properties.setProperty("nacos.config.configLongPollTimeout",String.valueOf(nacos.getTimeout()));
        PropertiesPropertySource nacosConfig = new PropertiesPropertySource("globalNacosProperties", properties);
        propertySources.addLast( nacosConfig);

        ApplicationContext applicationContext = ApplicationUtils.getApplicationContext();
        ApplicationContext parent = applicationContext.getParent();
        if (parent !=null) {
            ((AbstractRefreshableApplicationContext) parent) .refresh();
        }
        ((AbstractRefreshableApplicationContext) applicationContext).refresh();

    }
}
*/
