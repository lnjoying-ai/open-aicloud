package com.lnjoying.justice.aos.controller;

import com.lnjoying.justice.commonweb.controller.RestWebController;
import io.swagger.annotations.ApiParam;
import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestSchema(schemaId = "health")
@RequestMapping(path = "/aos/v1")
public class HealthCheck extends RestWebController
{
    @GetMapping(value = "/health", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> healthCheck()
    {
        return ResponseEntity.status(200).body("ok");
    }
}
