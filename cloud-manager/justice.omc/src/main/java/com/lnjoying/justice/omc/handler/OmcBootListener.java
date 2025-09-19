package com.lnjoying.justice.omc.handler;

import com.lnjoying.justice.omc.db.repo.AlertReduceRepository;
import com.lnjoying.justice.omc.prometheus.sd.PrometheusTargetsStoreManager;
import com.lnjoying.justice.omc.service.PrometheusService;
import com.lnjoying.justice.omc.service.monitorstack.impl.NodeMonitorStackService;
import lombok.extern.slf4j.Slf4j;
import org.apache.servicecomb.core.BootListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/10/27 19:18
 */

@Slf4j
@Component
public class OmcBootListener implements BootListener
{

    @Autowired
    private NodeMonitorStackService nodeMonitorStackService;

    @Autowired
    private PrometheusService prometheusService;

    @Autowired
    private PrometheusTargetsStoreManager storeManager;

    @Autowired
    private AlertReduceRepository alertReduceRepository;

    @Override
    public void onBootEvent(BootEvent event)
    {
        try
        {
            log.info("omc boot listener start");
            switch (event.getEventType())
            {
                case AFTER_REGISTRY:
                    log.info("after registry event");
                    //nodeMonitorStackService.checkMonitorStackTemplate();
                    prometheusService.loadPrometheusServer();
                    storeManager.init();
                    break;
                case BEFORE_CONSUMER_PROVIDER:
                    log.info("after producer provider event");
                    break;
                default:
                    break;
            }
        }
        catch (Exception e)
        {
            log.error("omc boot listener error:{}", e);
        }

    }

}
