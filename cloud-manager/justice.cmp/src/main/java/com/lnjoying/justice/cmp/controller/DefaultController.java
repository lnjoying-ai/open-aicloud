package com.lnjoying.justice.cmp.controller;

import com.lnjoying.justice.cmp.config.DescriptionConfig;
import com.lnjoying.justice.cmp.service.cloud.CloudService;
import com.lnjoying.justice.commonweb.controller.RestWebController;
import io.swagger.annotations.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

import static com.lnjoying.justice.commonweb.common.SwaggerConstants.*;

@RestSchema(schemaId = "default")
@RequestMapping("/cmp/v1/{vendor}/clouds/{cloud_id}")
@Api(value = "Default Controller",tags = "Default Controller")
@ApiOperation(value = "", extensions = {@Extension(name = SWAGGER_X_RESOURCE,
        properties = {@ExtensionProperty(name = SWAGGER_RESOURCE_SINGULAR_NAME, value = "cmpdefault"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_PLURAL_NAME, value = "cmpdefaults"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_DESCRIPTION, value = "多云默认操作"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_MODEL, value = "TblCloudInfo")})})
public class DefaultController extends RestWebController
{
    private static Logger LOGGER = LogManager.getLogger();

    @Autowired
    private CloudService cloudService;

    @RequestMapping (value = "/{path: .+}", method = RequestMethod.GET)
    @ApiOperation(value = "default get request",notes = DescriptionConfig.DEFAULT)
    public ResponseEntity<Object> defaultGetRequest(@ApiParam(value = "", required = true, name = "vendor") @PathVariable(value = "vendor") String vendor,
                                                    @ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId)
    {
        LOGGER.info("Unmapped get request handling!");

        return cloudService.sendHttpRequestToCloud(vendor, cloudId);
    }

    @RequestMapping (value = "/{path: .+}", method = RequestMethod.POST)
    @ApiOperation(value = "default post request",notes = DescriptionConfig.DEFAULT)
    public ResponseEntity<Object> defaultPostRequest(@ApiParam(value = "", required = true, name = "vendor") @PathVariable(value = "vendor") String vendor,
                                                     @ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                     @ApiParam(value = "", required = true, name = "map") @RequestBody HashMap<String, Object> map)
    {
        LOGGER.info("Unmapped post request handling!");

        return cloudService.sendHttpRequestToCloud(vendor, cloudId, map);
    }

    @RequestMapping (value = "/{path: .+}", method = RequestMethod.PUT)
    @ApiOperation(value = "default put request",notes = DescriptionConfig.DEFAULT)
    public ResponseEntity<Object> defaultPutRequest(@ApiParam(value = "", required = true, name = "vendor") @PathVariable(value = "vendor") String vendor,
                                                    @ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                    @ApiParam(value = "", required = true, name = "map") @RequestBody HashMap<String, Object> map)
    {
        LOGGER.info("Unmapped put request handling!");

        return cloudService.sendHttpRequestToCloud(vendor, cloudId, map);
    }

    @RequestMapping (value = "/{path: .+}", method = RequestMethod.DELETE)
    @ApiOperation(value = "default delete request",notes = DescriptionConfig.DEFAULT)
    public ResponseEntity<Object> defaultDeleteRequest(@ApiParam(value = "", required = true, name = "vendor") @PathVariable(value = "vendor") String vendor,
                                                       @ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId)
    {
        LOGGER.info("Unmapped delete request handling!");

        return cloudService.sendHttpRequestToCloud(vendor, cloudId);
    }
}
