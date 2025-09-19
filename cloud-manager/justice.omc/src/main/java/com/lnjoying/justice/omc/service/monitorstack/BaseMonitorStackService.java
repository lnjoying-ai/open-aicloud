package com.lnjoying.justice.omc.service.monitorstack;

import com.lnjoying.justice.omc.domain.model.IntegrationTaskTypeEnum;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/10/30 14:43
 */

public interface BaseMonitorStackService
{

    public static final String ADMIN_USER_ID = "39937079-99fe-4cd8-881f-04ca8c4fe09d";

    String getMonitorStackTemplateName();

    IntegrationTaskTypeEnum getIntegrationTaskType();
}
