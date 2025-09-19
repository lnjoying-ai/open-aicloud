package com.lnjoying.justice.aos.handler;

import com.lnjoying.justice.aos.facade.HelmFacade;
import com.lnjoying.justice.aos.facade.NodeStackFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Schedule task
 *
 * @author merak
 **/

@Component
@Lazy(false)
@EnableAsync
public class ScheduleTask
{
    @Autowired
    private NodeStackFacade nodeStackFacade;

    @Autowired
    private HelmFacade helmFacade;

    /**
     * run after a fixed delay 5 seconds
     * check tty stack on node is  in use
     */
    @Async("ttyStackExecutor")
    @Scheduled(fixedDelayString = "30000", initialDelay = 5 * 1000)
    public void checkNodeStackIsInUse()
    {
        nodeStackFacade.checkNodeStackIsInUse();
    }


    /**
     * check if stack has been deleted
     */
    @Async("ttyStackExecutor")
    @Scheduled(fixedDelayString = "10000", initialDelay = 5 * 1000)
    public void checkNodeStackIsDeleted()
    {
        nodeStackFacade.checkNodeStackIsDeleted();
    }



    @Async("repoTaskExecutor")
    @Scheduled(fixedDelayString = "10000", initialDelay = 5 * 1000)
    public void validRepo()
    {
        helmFacade.validRepo();
    }

    @Async("repoTaskExecutor")
    @Scheduled(fixedDelayString = "10000", initialDelay = 5 * 1000)
    public void parseRepoIndex()
    {
        helmFacade.parseRepoIndex();
    }

    @Async("repoTaskExecutor")
    @Scheduled(fixedDelayString = "300000", initialDelay = 5 * 1000)
    public void checkHelmRepoUpdate()
    {
        helmFacade.checkHelmRepoUpdate();
    }

    @Async("repoTaskExecutor")
    @Scheduled(fixedDelayString = "60000", initialDelay = 5 * 1000)
    public void checkHelmStackStatus()
    {
        helmFacade.checkHelmStackStatus();
    }

    /**
     * check if stack has been deleted
     */
    @Async("repoTaskExecutor")
    @Scheduled(fixedDelayString = "10000", initialDelay = 5 * 1000)
    public void checkHelmStackIsDeleted()
    {
        helmFacade.checkHelmStackIsDeleted();
    }
}
