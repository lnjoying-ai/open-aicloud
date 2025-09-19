package com.lnjoying.justice.omc.service.impl;

import com.lnjoying.justice.omc.domain.model.ResourceTypeEnum;
import com.lnjoying.justice.omc.service.CombRpcService;
import com.lnjoying.justice.omc.service.ResourceStatusStatsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/11/16 10:18
 */

@Service
@Slf4j
public class ResourceStatusStatsServiceImpl implements ResourceStatusStatsService
{

    @Autowired
    private CombRpcService combRpcService;
    @Override
    public void performStatusDateCollection(ResourceTypeEnum type)
    {
        switch(type) {
            case NODE:
                combRpcService.getRegionResourceService().getEdgeStatusMetrics(null, null);
                log.info("perform node status date collection");
                break;
            case CLUSTER:
                log.info("perform cluster status date collection");
                break;
            case CONTAINER:
                log.info("perform container status date collection");
                break;
            case VM:
                log.info("perform vm status date collection");
                break;
            default:
                log.info("perform status date collection");
                break;
        }
    }
}
