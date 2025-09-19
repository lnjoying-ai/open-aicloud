package com.lnjoying.justice.iam.controller;

import com.lnjoying.justice.commonweb.controller.RestWebController;
import io.swagger.annotations.ApiOperation;
import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RestSchema(schemaId = "health")
@RequestMapping("/iam/v1")
@Controller
public class HealthCheck extends RestWebController
{

    /**
     * Queries liveness & readiness.
     *
     * @return status code 200 when ready
     */
    @GetMapping(value = "/health", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "health check", response = String.class)
    public ResponseEntity<String> healthCheck()
    {
        return ResponseEntity.ok(null);
    }
}
