package com.lnjoying.justice.sys.controller;

import com.lnjoying.justice.commonweb.controller.RestWebController;
import com.lnjoying.justice.sys.service.SystemService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Extension;
import io.swagger.annotations.ExtensionProperty;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import static com.lnjoying.justice.commonweb.common.SwaggerConstants.*;

@RestSchema(schemaId = "system")
@RequestMapping(path = "/sys/v1")
@ApiOperation(value = "", extensions = {@Extension(name = SWAGGER_X_RESOURCE,
        properties = {@ExtensionProperty(name = SWAGGER_RESOURCE_SINGULAR_NAME, value = "system"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_PLURAL_NAME, value = "system"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_DESCRIPTION, value = "系统信息"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_MODEL, value = "")})})
public class SystemController extends RestWebController
{
    @Autowired
    SystemService systemService;
    private static Logger LOGGER = LogManager.getLogger();

    @GetMapping(value = "/anonymous/system-info", produces = MediaType.APPLICATION_JSON_VALUE)
    public Object getSystemInfo()
    {
        return systemService.getSystemInfo();
    }
}
