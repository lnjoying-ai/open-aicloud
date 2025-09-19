package com.lnjoying.justice.aos.handler;


import com.alibaba.nacos.api.annotation.NacosInjected;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.ConfigType;
import com.alibaba.nacos.api.config.annotation.NacosConfigListener;
import com.lnjoying.justice.aos.facade.TemplateServiceFacade;
import com.lnjoying.justice.aos.util.TtyStackUtils;
import com.lnjoying.justice.commonweb.util.NacosConfigLoaderUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.annotation.PostConstruct;
import java.util.concurrent.*;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2022/6/7 14:20
 */

@Slf4j
@Configuration
@DependsOn("nacosConfiguration")
public class NacosListener
{

    @Autowired
    private TemplateServiceFacade templateServiceFacade;

    @NacosInjected
    private ConfigService configService;

    private Executor executor = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(),
            new ThreadFactory() {
                @Override
                public Thread newThread(Runnable r) {
                    Thread thread = new Thread(r);
                    thread.setName("tty-template-update-thread");
                    return thread;
                }
            });

    @NacosConfigListener(dataId = "com.justice.aos.config.ttytmpl", groupId = "aos")
    public void onChange(String newContent) throws Exception {
       log.info("tty tmpl change: {}", newContent);
       executor.execute(() ->  TtyStackUtils.getInstance().updateTtyStackTemplate(templateServiceFacade, newContent));
    }

    @PostConstruct
    public void publishConfig() {
        NacosConfigLoaderUtils.doPublish(configService,"com.justice.aos.config.ttytmpl", "aos", ConfigType.YAML.getType());
        NacosConfigLoaderUtils.doPublish(configService,"com.justice.aos.config.frp", "aos", ConfigType.YAML.getType());
        NacosConfigLoaderUtils.doPublish(configService,"com.justice.aos.config.helm", "aos", ConfigType.YAML.getType());
    }
}
