package com.lnjoying.justice.omc.controller;

import com.lnjoying.justice.commonweb.handler.aspect.annotation.LNJoyAuditLog;
import com.lnjoying.justice.omc.domain.dto.rsp.AlertGroupsRsp;
import com.lnjoying.justice.omc.prometheus.sd.TargetsData;
import com.lnjoying.justice.omc.service.PrometheusService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.ResponseEntity.status;

/**
 * @Description: different kind of http discovery for prometheus
 * @Author: Merak
 * @Date: 2023/11/3 19:25
 */

@RestSchema(schemaId = "PrometheusHttpSdController")
@RequestMapping(path = "/omc/v1/job")
@Slf4j
public class PrometheusHttpSdController
{
    @Autowired
    private PrometheusService prometheusService;

    /**
     * type eg:LIGHT_NODE CADVISOR
     * @param type
     * @return
     */
    @GetMapping(value = "/prometheus", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "prometheus job")
    @LNJoyAuditLog(value = "get prometheus job", logOff = true)
    public ResponseEntity<Object> httpSdByType(@ApiParam(name = "type", required = true) @RequestParam(value = "type", required = true)String type)
    {

        List<TargetsData> targets = prometheusService.httpSdByType(type);
        return status(OK).body(targets);

    }
}
