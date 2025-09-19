package com.lnjoying.justice.servicegw.controller;

import com.lnjoying.justice.commonweb.controller.RestWebController;
import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;

@RestSchema(schemaId = "health")
@RequestMapping(path = "/servicegw/v1")
public class HealthCheck extends RestWebController
{
    @GetMapping(value = "/health", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> healthCheck1()
    {
        Map<String, String> ret = new HashMap<>();
        ret.put("health", "ok");
        return ResponseEntity.status(200).body(ret);
    }
}
