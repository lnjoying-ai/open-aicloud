package com.lnjoying.justice.cis.controller;

import com.lnjoying.justice.commonweb.controller.RestWebController;
import io.swagger.annotations.ApiOperation;
import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestSchema(schemaId = "health")
@RequestMapping("/cis/v1/health")
public class HealthCheck extends RestWebController
{

    /**
     * Queries liveness & readiness.
     *
     * @return status code 200 when ready
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "health check", response = String.class)
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("cis ok");
    }
}
