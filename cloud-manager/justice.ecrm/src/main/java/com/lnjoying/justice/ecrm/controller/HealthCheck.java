package com.lnjoying.justice.ecrm.controller;

import com.lnjoying.justice.commonweb.controller.RestWebController;
import io.swagger.annotations.ApiParam;
import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestSchema(schemaId = "health")
@RequestMapping(path = "/ecrm/v1")
@Controller
public class HealthCheck extends RestWebController
{
    @GetMapping(value = "/health", produces = MediaType.APPLICATION_JSON_VALUE)
//    @RequestMapping(path = "/health1", method = RequestMethod.GET)
    public ResponseEntity<Map<String,String>> healthCheck1()
    {
        Map<String, String> ret = new HashMap<>();
        ret.put("health","ok");
        return ResponseEntity.status(200).body(ret);
    }

    @RequestMapping(path = "/health/{region_id}", method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> healthCheck22(@ApiParam(value = "region_id", required = true, name = "region_id")  @PathVariable String region_id)
    {
		Map<String, String> ret = new HashMap<>();
		ret.put("abc","ok");
        return ResponseEntity.ok(ret);
    }

    @RequestMapping(path = "/health/{region_id}", method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody @ResponseStatus(HttpStatus.OK)
    public Map<String, String> healthCheck23(@ApiParam(value = "region_id", required = true, name = "region_id") @PathVariable String region_id)
    {
        Map<String, String> ret = new HashMap<>();
        ret.put("abc","ok");
        return ret;
    }
}
