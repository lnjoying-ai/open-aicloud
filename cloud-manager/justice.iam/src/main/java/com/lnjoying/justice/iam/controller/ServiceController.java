package com.lnjoying.justice.iam.controller;

import com.lnjoying.justice.commonweb.handler.aspect.annotation.LNJoyAuditLog;
import com.lnjoying.justice.commonweb.handler.aspect.annotation.ResourceIdExtractConfiguration;
import com.lnjoying.justice.commonweb.handler.aspect.enums.ResourceIdLocationType;
import com.lnjoying.justice.iam.common.constant.IamResources;
import com.lnjoying.justice.iam.domain.dto.request.service.AddServiceReq;
import com.lnjoying.justice.iam.domain.dto.request.service.UpdateServiceReq;
import com.lnjoying.justice.iam.domain.dto.response.service.ServicesRsp;
import com.lnjoying.justice.iam.handler.actiondescription.i18n.zh_cn.ServiceActionDescriptionTemplate;
import com.lnjoying.justice.iam.service.ServiceService;
import com.lnjoying.justice.schema.constant.ResourceOperationTypeConstants;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.Map;

import static com.lnjoying.justice.commonweb.common.SwaggerConstants.*;
import static com.lnjoying.justice.iam.utils.ServiceCombRequestUtils.*;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.ResponseEntity.status;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/2/16 15:52
 */

@RestSchema(schemaId = "services-manager")
@RequestMapping("/api/iam/v1/services")
@Controller
@Api(value = "service Controller")
@ApiOperation(value = "", extensions = {@Extension(name = SWAGGER_X_RESOURCE,
        properties = {@ExtensionProperty(name = SWAGGER_RESOURCE_SINGULAR_NAME, value = "service"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_PLURAL_NAME, value = "services"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_DESCRIPTION, value = "服务"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_MODEL, value = "tbl_iam_service")})})
@Slf4j
public class ServiceController  extends IamRestWebController
{

    @Autowired
    private ServiceService serviceService;


    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "add service")
    @LNJoyAuditLog(value = "add service",
            tags = ResourceOperationTypeConstants.RESOURCE_CREATE,
            actionDescriptionTemplateClass = ServiceActionDescriptionTemplate.class,
            isResourceInstanceNameFromArgs = true,
            resourceInstanceNameFromArgsParseSpEl = "#obj?.get('displayName')?.toString()",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.METHOD_RETURN_VALUE,
                            resourceIdParseSPEL = "#obj?.body?.toString()"))
    public ResponseEntity<Object> addService(@ApiParam(required = true, name = "serviceReq") @RequestBody @Valid AddServiceReq req)
    {
        String bpId = getBpId();
        String userId = getUserId();
        log.info("add service. req: {}, userId: {}", req, userId);

        setBaseReq(req, bpId, userId);
        return status(CREATED).body(serviceService.addService(req));
    }


    @PutMapping(value = "/{service_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "update service")
    @LNJoyAuditLog(value = "update service",
            actionDescriptionTemplateClass = ServiceActionDescriptionTemplate.class,
            isResourceInstanceNameFromArgs = true,
            resourceInstanceNameFromArgsParseSpEl = "#obj?.get('displayName')?.toString()",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                            bindParameterName = "serviceId"))
    public ResponseEntity<Map<String, String>> updateService(@ApiParam(required = true, name = "service_id") @PathVariable String serviceId,
                                                             @ApiParam(required = true, name = "req") @RequestBody @Valid UpdateServiceReq req)
    {
        String bpId = getBpId();
        String userId = getUserId();
        log.info("update service. req: {}, userId: {}", req, userId);

        // update userId and bpId
        setBaseReq(req, bpId, userId);
        serviceService.updateService(serviceId, req);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get service list")
    public ResponseEntity<Object> getServiceList(@ApiParam(name = "name") @RequestParam(value = "name", required = false) String name,
                                                 @ApiParam(name = "page_size") @RequestParam(value = "page_size", required = false, defaultValue = Integer.MAX_VALUE + "") Integer pageSize,
                                                 @ApiParam(name = "page_num") @RequestParam(value = "page_num", required = false, defaultValue = "1") Integer pageNum,
                                                 @ApiParam(name = "status") @RequestParam(value = "status", required = false) Integer status)
    {

        ServicesRsp servicesRsp = serviceService.getServiceList(name, status, pageSize, pageNum);

        return status(OK).body(servicesRsp);
    }


    @GetMapping(value = "/{service_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get service info")
    @LNJoyAuditLog(value = "get service info",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                            bindParameterName = "serviceId"))
    public ResponseEntity<Object> getService(@ApiParam(required = true, name = "service_id") @PathVariable String serviceId)
    {
        return status(OK).body(serviceService.getService(serviceId));

    }

    @DeleteMapping(value = "/{service_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "delete service")
    @LNJoyAuditLog(value = "delete service",
            resourceMapperId = IamResources.SERVICE,
            actionDescriptionTemplateClass = ServiceActionDescriptionTemplate.class,
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                            bindParameterName = "serviceId"))
    public ResponseEntity<String> deleteService(@ApiParam(required = true, name = "service_id") @PathVariable String serviceId)
    {

        log.info("delete service: {}, userId: {}", serviceId, getUserId());

        serviceService.deleteService(serviceId);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}
