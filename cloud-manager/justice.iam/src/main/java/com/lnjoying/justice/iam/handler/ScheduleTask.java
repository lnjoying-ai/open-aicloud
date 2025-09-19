package com.lnjoying.justice.iam.handler;

import com.lnjoying.justice.iam.authz.opa.client.OpaClient;
import com.lnjoying.justice.iam.authz.opa.client.health.OpaHealthResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * Schedule task
 *
 * @author merak
 **/

@Component
@Lazy(false)
@Slf4j
public class ScheduleTask
{

    @Autowired
    private OpaClient opaClient;

    /**
     * run after a fixed delay 30 seconds
     * check opa server status
     */
    @Scheduled(fixedDelayString = "30000", initialDelay = 5 * 1000)
    public void checkOpaServerHealth()
    {

        OpaHealthResponse health = opaClient.health();
        if (Objects.nonNull(health))
        {
            String error = health.getError();
            if (StringUtils.isNotBlank(error))
            {
                log.error("opa server in a non healthy state，error:{}", error);
            }
        }
    }

}
