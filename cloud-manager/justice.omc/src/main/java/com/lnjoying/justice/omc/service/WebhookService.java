package com.lnjoying.justice.omc.service;

import com.lnjoying.justice.omc.domain.dto.req.WebhookInfoReq;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/11/8 15:30
 */

public interface WebhookService
{
    void handleAlerts(WebhookInfoReq req);
}
