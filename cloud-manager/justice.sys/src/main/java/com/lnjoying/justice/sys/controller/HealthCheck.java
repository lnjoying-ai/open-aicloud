package com.lnjoying.justice.sys.controller;

import com.lnjoying.justice.commonweb.controller.RestWebController;
import com.lnjoying.justice.sys.handler.NacosListener;
import lombok.extern.slf4j.Slf4j;
import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;

@RestSchema(schemaId = "health")
@RequestMapping(path = "/sys/v1")
@Slf4j
public class HealthCheck extends RestWebController
{
    @Autowired
    private NacosListener nasListener;

    @GetMapping(value = "/health", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> healthCheck1()
    {
        boolean published = nasListener.isPublished();
        if (!published)
        {
            log.warn("nacos config not published");
            return ResponseEntity.status(500).body(null);
        }
        Map<String, String> ret = new HashMap<>();
        ret.put("health", "ok");
        return ResponseEntity.status(200).body(ret);
    }
}
