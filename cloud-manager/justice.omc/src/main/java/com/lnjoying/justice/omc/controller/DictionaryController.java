package com.lnjoying.justice.omc.controller;

import com.lnjoying.justice.commonweb.handler.aspect.annotation.LNJoyAuditLog;
import com.lnjoying.justice.omc.config.DescriptionConfig;
import com.lnjoying.justice.omc.domain.dto.model.OperationEventTypeDto;
import com.lnjoying.justice.schema.common.OperationEventType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 功能描述
 *
 * @author: Robin
 * @date: 2023年11月02日 15:26
 */
@RestSchema(schemaId = "dictionaryController")
@RequestMapping(path = "/omc/v1/dictionary")
@Api(value = "Dictionary Controller", tags = "Dictionary Controller")
public class DictionaryController
{
    @GetMapping(value = "/eventTypes", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get event types", notes = DescriptionConfig.GET_API_EVENT_TYPE_LIST)
    @LNJoyAuditLog(value = "get event types", logOff = true)
    public ResponseEntity<List<OperationEventTypeDto>> getEventTypes()
    {
        return new ResponseEntity<>(Arrays.stream(OperationEventType.values())
                .map(x -> OperationEventTypeDto.builder()
                        .type(x.name())
                        .enName(x.getEnName())
                        .cnName(x.getCnName())
                        .description(x.getDescription())
                        .build()).collect(Collectors.toList()), HttpStatus.OK);
    }
}
