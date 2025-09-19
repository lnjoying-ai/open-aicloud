package com.lnjoying.justice.ims.handler;

import com.lnjoying.justice.ims.db.repo.ImsRegistry3rdRepository;
import com.lnjoying.justice.ims.service.CombRpcService;
import org.apache.servicecomb.core.BootListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * custom listener
 *
 * @author merak
 **/

@Component
public class ImsBootListener implements BootListener
{
    @Autowired
    private ImsRegistry3rdRepository registry3rdRepository;

    @Autowired
    private CombRpcService combRpcService;

    @Override
    public void onBootEvent(BootEvent event)
    {
        switch (event.getEventType()) {
            case BEFORE_PRODUCER_PROVIDER:
                registry3rdRepository.addDefaultRegistry3rd();
                break;
            case AFTER_TRANSPORT:
             /*   SysService.NacosConfig nacos = combRpcService.getSysService().getNacos();
                System.out.println(nacos);*/
                break;
            default:
                break;
        }
    }


}
