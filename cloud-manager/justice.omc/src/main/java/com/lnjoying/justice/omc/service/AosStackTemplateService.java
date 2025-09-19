package com.lnjoying.justice.omc.service;

import com.lnjoying.justice.schema.service.aos.AosTemplateService;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/10/27 19:20
 */

public interface AosStackTemplateService
{

    int countStackByName(String nodeMonitorStackTemplate);

    int addStackTemplate(AosTemplateService.StackTemplateReq addTemplateReq);
}
