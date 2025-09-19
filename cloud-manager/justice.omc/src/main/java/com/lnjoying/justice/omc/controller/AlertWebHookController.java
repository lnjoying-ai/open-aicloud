package com.lnjoying.justice.omc.controller;

import com.lnjoying.justice.omc.domain.dto.req.WebhookInfoReq;
import com.lnjoying.justice.omc.domain.dto.rsp.AlertGroupsRsp;
import com.lnjoying.justice.omc.service.WebhookService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.ResponseEntity.status;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/11/3 19:20
 */

@RestSchema(schemaId = "AlertWebHookController")
@RequestMapping(path = "/omc/v1/alert/webhook")
@Slf4j
public class AlertWebHookController
{
    @Autowired
    private WebhookService webhookService;

    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "alert webhook")
    public ResponseEntity<Object> alertWebhook(@ApiParam(required = true, name = "req") @RequestBody WebhookInfoReq req)
    {
        log.info("post, request:{}", req);
        webhookService.handleAlerts(req);
        return status(OK).body(null);

    }
}
