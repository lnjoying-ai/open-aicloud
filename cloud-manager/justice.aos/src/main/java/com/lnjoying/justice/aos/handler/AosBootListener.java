package com.lnjoying.justice.aos.handler;

import com.alibaba.nacos.api.annotation.NacosInjected;
import com.alibaba.nacos.api.config.ConfigService;
import com.lnjoying.justice.aos.facade.TemplateServiceFacade;
import com.lnjoying.justice.aos.util.TtyStackUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.servicecomb.core.BootListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2022/4/13 14:59
 */
@Slf4j
@Component
public class AosBootListener implements BootListener
{
    @Autowired
    private TemplateServiceFacade templateServiceFacade;

    @NacosInjected
    private ConfigService configService;

    @Override
    public void onBootEvent(BootEvent event)
    {
        switch (event.getEventType())
        {
            case BEFORE_PRODUCER_PROVIDER:
                TtyStackUtils.getInstance().checkTtyStackTemplate(templateServiceFacade, configService);
                break;
            default:
                break;
        }
    }


}
