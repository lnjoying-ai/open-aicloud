package com.lnjoying.justice.omc.service;

import com.lnjoying.justice.omc.domain.model.ResourceTypeEnum;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/11/16 10:18
 */

public interface ResourceStatusStatsService
{
    void performStatusDateCollection(ResourceTypeEnum type);
}
