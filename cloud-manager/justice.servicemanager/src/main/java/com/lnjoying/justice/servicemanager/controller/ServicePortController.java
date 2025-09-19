package com.lnjoying.justice.servicemanager.controller;

import com.lnjoying.justice.commonweb.handler.aspect.annotation.LNJoyAuditLog;
import com.lnjoying.justice.commonweb.handler.aspect.annotation.ResourceIdExtractConfiguration;
import com.lnjoying.justice.commonweb.handler.aspect.enums.ResourceIdLocationType;
import com.lnjoying.justice.schema.constant.ResourceOperationTypeConstants;
import com.lnjoying.justice.servicemanager.config.DescriptionConfig;
import com.lnjoying.justice.servicemanager.domain.dto.request.AddServicePortReq;
import com.lnjoying.justice.servicemanager.domain.dto.request.UpdateServicePortReq;
import com.lnjoying.justice.servicemanager.domain.dto.response.GetServicePortRsp;
import com.lnjoying.justice.servicemanager.domain.dto.response.GetServicePortsRsp;
import com.lnjoying.justice.servicemanager.domain.model.TargetServiceTestResult;
import com.lnjoying.justice.servicemanager.entity.search.ServicePortSearchCritical;
import com.lnjoying.justice.commonweb.controller.RestWebController;
import com.lnjoying.justice.schema.constant.UserHeadInfo;
import com.lnjoying.justice.servicemanager.service.ServicePortService;
import io.swagger.annotations.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.lnjoying.justice.commonweb.common.SwaggerConstants.*;
import static com.lnjoying.justice.commonweb.util.ServiceCombRequestUtils.*;

@RestSchema(schemaId = "serviceport")
@RequestMapping("/service-manager/v1")
@Api(value = "Service Port Controller",tags = "Service Port Controller")
@ApiOperation(value = "", extensions = {@Extension(name = SWAGGER_X_RESOURCE,
        properties = {@ExtensionProperty(name = SWAGGER_RESOURCE_SINGULAR_NAME, value = "serviceport"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_PLURAL_NAME, value = "serviceports"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_DESCRIPTION, value = "服务端口"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_MODEL, value = "TblCloudInfo")})})
public class ServicePortController extends RestWebController
{
    private static Logger LOGGER = LogManager.getLogger();

    @Autowired
    ServicePortService servicePortService;

    @PostMapping(value = "/service-ports", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "add service port",notes = DescriptionConfig.ADD_SERVICE_PORT_MSG)
    @LNJoyAuditLog(value = "add service port",
            tags = ResourceOperationTypeConstants.RESOURCE_CREATE,
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.METHOD_RETURN_VALUE,
                            resourceIdParseSPEL = "#obj?.body?.toString()"))
    public ResponseEntity<String> addServicePort(@ApiParam(value = "", required = true, name = "addServicePortReq") @RequestBody @Valid AddServicePortReq addServicePortReq,
                                                 @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId,
                                                 @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
                                                 @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
    {
        try
        {

            String servicePortId = servicePortService.addServicePort(addServicePortReq, bpId, userId);
            return ResponseEntity.status(HttpStatus.CREATED).body(servicePortId);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("add service port error. {}", e.getMessage());
            throw throwWebException(e);
        }
    }

    @GetMapping(value = "/service-ports", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get service port list",notes = DescriptionConfig.SERVICE_PORT_LIST_MSG)
    public ResponseEntity<GetServicePortsRsp> getServicePorts(@ApiParam(name = "service_port_id") @RequestParam(value = "service_port_id", required = false) String servicePortId,
                                                              @ApiParam(name = "name") @RequestParam(value = "name", required = false) String name,
                                                              @ApiParam(name = "status") @RequestParam(value = "status", required = false) Integer status,
                                                              @ApiParam(name = "target_type") @RequestParam(value = "target_type", required = false) String targetType,
                                                              @ApiParam(name = "deployment") @RequestParam(value = "deployment", required = false) String deployment,
                                                              @ApiParam(name = "purpose") @RequestParam(value = "purpose", required = false) String purpose,
                                                              @ApiParam(name = "page_size") @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                              @ApiParam(name = "page_num") @RequestParam(value = "pageNum", required = false) Integer pageNum,
                                                              @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId,
                                                              @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
                                                              @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities,
                                                              @RequestHeader(name = UserHeadInfo.USERKIND,    required = false) String userKind)
    {

        try
        {
            ServicePortSearchCritical servicePortSearchCritical = new ServicePortSearchCritical();
            servicePortSearchCritical.setServicePortId(servicePortId);
            servicePortSearchCritical.setName(name);
            servicePortSearchCritical.setStatus(status);
            servicePortSearchCritical.setTargetType(targetType);
            servicePortSearchCritical.setDeployment(deployment);
            servicePortSearchCritical.setPurpose(purpose);

            if (isBpAdmin())
            {
                servicePortSearchCritical.setBp(bpId);
            }
            else if (isBpUser() || isPersonal())
            {
                servicePortSearchCritical.setUser(userId);
            }

            if (pageNum != null) servicePortSearchCritical.setPageNum(pageNum);
            if (pageSize != null) servicePortSearchCritical.setPageSize(pageSize);

            return ResponseEntity.ok().body(servicePortService.getServicePorts(servicePortSearchCritical));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("get service ports error. {}", e.getMessage());
            throw throwWebException(e);
        }
    }

    @GetMapping(value = "/service-ports/{service_port_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get service port",notes = DescriptionConfig.SERVICE_PORT_DETAIL_MSG)
    @LNJoyAuditLog(value = "get service port",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                    bindParameterName = "servicePortId"))
    public ResponseEntity<GetServicePortRsp> getServicePort(@ApiParam(value = "", required = true, name = "service_port_id") @PathVariable(value = "service_port_id") String servicePortId,
                                                            @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId,
                                                            @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
                                                            @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities,
                                                            @RequestHeader(name = UserHeadInfo.USERKIND,    required = false) String userKind)
    {

        try
        {
            return ResponseEntity.ok().body(servicePortService.getServicePort(servicePortId));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("get service port error. {}", e.getMessage());
            throw throwWebException(e);
        }
    }

    @PutMapping(value = "/service-ports/{service_port_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "update service port",notes = DescriptionConfig.UPDATE_SERVICE_PORT_MSG)
    @LNJoyAuditLog(value = "update service port",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                            bindParameterName = "servicePortId"))
    public ResponseEntity<String> updateServicePort(@ApiParam(value = "", required = true, name = "service_port_id") @PathVariable(value = "service_port_id") String servicePortId,
                                                    @ApiParam(value = "", required = true, name = "updateServicePortReq") @RequestBody @Valid UpdateServicePortReq updateServicePortReq,
                                                    @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId,
                                                    @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
    {
        try
        {

            servicePortService.updateServicePort(servicePortId, updateServicePortReq);
            return ResponseEntity.ok(null);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("update service port error. {}", e.getMessage());
            throw throwWebException(e);
        }
    }

    @DeleteMapping(value = "/service-ports/{service_port_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "delete service port",notes = DescriptionConfig.DELETE_SERVICE_PORT_MSG)
    @LNJoyAuditLog(value = "delete service port",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                            bindParameterName = "servicePortId"))
    public ResponseEntity<String> deleteServicePort(@ApiParam(value = "", required = true, name = "service_port_id") @PathVariable(value = "service_port_id") String servicePortId,
                                                    @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId,
                                                    @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
    {
        try
        {

            servicePortService.deleteServicePort(servicePortId);
            return ResponseEntity.ok(null);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("delete service port error. {}", e.getMessage());
            throw throwWebException(e);
        }
    }

    @PostMapping(value = "/service-ports/{service_port_id}/reallocate", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "reallocate service port",notes = DescriptionConfig.REALLOCATE_SERVICE_PORT_MSG)
    @LNJoyAuditLog(value = "reallocate service port",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                            bindParameterName = "servicePortId"))
    public ResponseEntity<String> reallocateServicePort(@ApiParam(value = "", required = true, name = "service_port_id") @PathVariable(value = "service_port_id") String servicePortId,
                                                        @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId,
                                                        @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
    {
        try
        {

            servicePortService.reallocateServicePort(servicePortId);
            return ResponseEntity.ok(null);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("reallocate service port error. {}", e.getMessage());
            throw throwWebException(e);
        }
    }

    @GetMapping(value = "/service-ports/{service_port_id}/test", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "test service port",notes = DescriptionConfig.REALLOCATE_SERVICE_PORT_MSG)
    public ResponseEntity<List<TargetServiceTestResult>> testServicePort(@ApiParam(value = "", required = true, name = "service_port_id") @PathVariable(value = "service_port_id")String servicePortId,
                                                                         @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId,
                                                                         @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
    {
        try
        {
            return ResponseEntity.ok(servicePortService.testServicePort(servicePortId));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("reallocate service port error. {}", e.getMessage());
            throw throwWebException(e);
        }
    }

    @GetMapping(value = "/service-ports/{service_port_id}/target-services/{target_service_id}/test", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "test service port",notes = DescriptionConfig.REALLOCATE_SERVICE_PORT_MSG)
    public ResponseEntity<List<TargetServiceTestResult>> testTargetServicePort(@ApiParam(value = "", required = true, name = "service_port_id") @PathVariable(value = "service_port_id")String servicePortId,
                                                                               @ApiParam(value = "", required = true, name = "target_service_id") @PathVariable(value = "target_service_id")String targetServiceId,
                                                                               @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId,
                                                                               @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
    {
        try
        {
            return ResponseEntity.ok(servicePortService.testTargetServicePort(servicePortId, targetServiceId));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("reallocate service port error. {}", e.getMessage());
            throw throwWebException(e);
        }
    }

    @GetMapping(value = "/service-ports/{service_port_id}/target-services/{target_service_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get target service",notes = DescriptionConfig.TARGET_SERVICE_DETAIL_MSG)
    public ResponseEntity<GetServicePortRsp> getTargetService(@ApiParam(value = "", required = true, name = "service_port_id") @PathVariable(value = "service_port_id")String servicePortId,
                                                              @ApiParam(value = "", required = true, name = "target_service_id") @PathVariable(value = "target_service_id")String targetServiceId,
                                                              @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId,
                                                              @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
                                                              @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities,
                                                              @RequestHeader(name = UserHeadInfo.USERKIND,    required = false) String userKind)
    {

        try
        {
            return ResponseEntity.ok().body(servicePortService.getTargetService(servicePortId, targetServiceId));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("get service port error. {}", e.getMessage());
            throw throwWebException(e);
        }
    }
}
