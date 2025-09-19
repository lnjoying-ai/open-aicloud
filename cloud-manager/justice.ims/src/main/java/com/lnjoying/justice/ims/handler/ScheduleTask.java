package com.lnjoying.justice.ims.handler;

import com.lnjoying.justice.ims.facade.ImsRegistryFacade;
import com.lnjoying.justice.ims.facade.ImsRegistryProjectFacade;
import com.lnjoying.justice.ims.facade.ImsRegistryUserFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Schedule task
 *
 * @author merak
 **/

@Component
@Lazy(false)
public class ScheduleTask
{
    @Autowired
    private ImsRegistryFacade registryFacade;

    @Autowired
    private ImsRegistryUserFacade registryUserFacade;

    @Autowired
    private ImsRegistryProjectFacade registryProjectFacade;

    /**
     * run after a fixed delay 30 seconds
     * Modify the status of the registry whose status is creating and can be pinged to succeed
     */
    @Scheduled(fixedDelayString = "${fixedDelay.ping-registry: 30000}", initialDelay = 120 * 1000)
    public void pingRegistry()
    {
        registryFacade.processPingRegistry();
    }

    /**
     * run after a fixed delay 30 seconds
     * Modify the status of the registry whose status is succeed and can get userInfo to healthy
     */
    @Scheduled(fixedDelayString = "${fixedDelay.ping-registry: 30000}", initialDelay = 180 * 1000)
    public void authenticateRegistry()
    {
        registryFacade.processAuthenticateRegistry();
    }

    /**
     * run after a fixed delay 30 seconds
     * Modify the status of the registry whose status is healthy and the registry components are healthy to enable
     */
    @Scheduled(fixedDelayString = "${fixedDelay.ping-registry: 30000}", initialDelay = 240 * 1000)
    public void enableRegistry()
    {
        registryFacade.processEnableRegistry();
    }

    /**
     * run after a fixed delay 5 minutes
     * Synchronize users to all registry
     */
    @Scheduled(fixedDelayString = "${fixedDelay.sync-registry-user: 300000}", initialDelay = 300 * 1000)
    public void syncRegistryUser()
    {
        registryUserFacade.processSyncRegistryUser();
    }

    /**
     *  run after a fixed delay 1 hour
     * Check whether there is a new bp project, if so, check whether all users under bp are added to the project
     */
    @Scheduled(fixedDelayString = "${fixedDelay.sync-registry-user-to-bp-project: 3600000}", initialDelay = 300 * 1000)
    public void syncRegistryUser2BpProject()
    {
        registryProjectFacade.processSyncRegistryUser2BpProject();
    }
}
