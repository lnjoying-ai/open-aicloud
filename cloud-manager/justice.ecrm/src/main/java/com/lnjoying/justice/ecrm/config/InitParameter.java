package com.lnjoying.justice.ecrm.config;

import com.lnjoying.justice.ecrm.facade.RedisCacheFacade;
import com.lnjoying.justice.ecrm.facade.RegionServiceFacade;
import com.lnjoying.justice.ecrm.facade.SiteServiceFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

@Service
public class InitParameter implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private RedisCacheFacade redisCacheFacade;

    @Autowired
    private RegionServiceFacade regionServiceFacade;

    @Autowired
    private SiteServiceFacade siteServiceFacade;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent){
        if (contextRefreshedEvent.getApplicationContext().getParent() == null)
        {
            redisCacheFacade.initRedisData();
            regionServiceFacade.checkDefaultRegion();
            siteServiceFacade.checkDefaultSite();
        }
    }
}
