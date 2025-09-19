package com.lnjoying.justice.omc.handler;

import com.lnjoying.justice.omc.domain.model.ResourceTypeEnum;
import com.lnjoying.justice.omc.service.ResourceStatusStatsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/11/16 10:05
 */

//@Component
//@Lazy(false)
//@Slf4j
//@EnableScheduling
@Deprecated
public class ResourceStatusStatsJob
{
    @Autowired
    private ResourceStatusStatsService resourceStatusStatsService;

    @Scheduled(fixedDelay = 180000)
    public void performDataCollection()
    {
        Arrays.stream(ResourceTypeEnum.values()).parallel().forEach(type -> {
            resourceStatusStatsService.performStatusDateCollection(type);
        });
    }
}
