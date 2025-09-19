package com.lnjoying.justice.cluster.manager.controller;

import com.lnjoying.justice.commonweb.controller.RestWebController;
import io.swagger.annotations.ApiParam;
import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.HashMap;
import java.util.Map;

@RestSchema(schemaId = "health")
@RequestMapping(path = "/cls/v1")
public class HealthCheck extends RestWebController
{
    @GetMapping(value = "/health", produces = MediaType.TEXT_HTML_VALUE)
//    @RequestMapping(path = "/health1", method = RequestMethod.GET)
    public ResponseEntity<Map<String, String>> healthCheck1()
    {
        Map<String, String> ret = new HashMap<>();
        ret.put("abc","ok");
        return ResponseEntity.status(200).body(ret);
    }

    @RequestMapping(path = "/health/{region_id}", method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> healthCheck22(@ApiParam(value = "region_id", required = true, name = "region_id")  @PathVariable String region_id)
    {
		Map<String, String> ret = new HashMap<>();
		ret.put("abc","ok");
        return ResponseEntity.ok(ret);
    }
}
