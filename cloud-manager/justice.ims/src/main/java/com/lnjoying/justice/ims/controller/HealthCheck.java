package com.lnjoying.justice.ims.controller;

import com.lnjoying.justice.commonweb.controller.RestWebController;
import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;

@RestSchema(schemaId = "health")
@RequestMapping(path = "/ims/v1")
public class HealthCheck extends RestWebController
{
    @GetMapping(value = "/health", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<Map<String, String>> healthCheck1()
    {
        Map<String, String> ret = new HashMap<>();
        ret.put("abc", "ok");
        return ResponseEntity.status(200).body(ret);
    }
}
