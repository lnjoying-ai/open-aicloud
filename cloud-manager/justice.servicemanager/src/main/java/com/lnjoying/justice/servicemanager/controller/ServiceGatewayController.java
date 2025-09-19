package com.lnjoying.justice.servicemanager.controller;

import com.lnjoying.justice.commonweb.handler.aspect.annotation.LNJoyAuditLog;
import com.lnjoying.justice.commonweb.handler.aspect.annotation.ResourceIdExtractConfiguration;
import com.lnjoying.justice.commonweb.handler.aspect.enums.ResourceIdLocationType;
import com.lnjoying.justice.schema.entity.servicemanager.PortStatus;
import com.lnjoying.justice.servicemanager.config.DescriptionConfig;
import com.lnjoying.justice.servicemanager.domain.dto.request.UpdateServiceGatewayReq;
import com.lnjoying.justice.servicemanager.domain.dto.response.GetServiceGatewaysRsp;
import com.lnjoying.justice.servicemanager.domain.model.ServiceGateway;
import com.lnjoying.justice.servicemanager.entity.search.ServiceGatewaySearchCritical;
import com.lnjoying.justice.servicemanager.service.ServiceGatewayService;
import com.lnjoying.justice.commonweb.controller.RestWebController;
import com.lnjoying.justice.schema.constant.UserHeadInfo;
import io.swagger.annotations.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.lnjoying.justice.commonweb.common.SwaggerConstants.*;

@RestSchema(schemaId = "servicegwteway")
@RequestMapping("/service-manager/v1")
@Api(value = "Service Gateway Controller",tags = "Service Gateway Controller")
@ApiOperation(value = "", extensions = {@Extension(name = SWAGGER_X_RESOURCE,
        properties = {@ExtensionProperty(name = SWAGGER_RESOURCE_SINGULAR_NAME, value = "servicegateway"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_PLURAL_NAME, value = "servicegateways"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_DESCRIPTION, value = "服务网关"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_MODEL, value = "TblServiceGatewayInfo")})})
public class ServiceGatewayController extends RestWebController
{
    private static Logger LOGGER = LogManager.getLogger();

    @Autowired
    ServiceGatewayService serviceGatewayService;

    @GetMapping(value = "/service-gateways", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get service gateway list",notes = DescriptionConfig.SERVICE_GATEWAY_LIST_MSG)
    public ResponseEntity<GetServiceGatewaysRsp> getServiceGateways(@ApiParam(name = "service_gateway_id") @RequestParam(value = "service_gateway_id", required = false) String serviceGatewayId,
                                                                    @ApiParam(name = "endpoint") @RequestParam(value = "endpoint", required = false) String endpoint,
                                                                    @ApiParam(name = "page_size") @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                                    @ApiParam(name = "page_num") @RequestParam(value = "pageNum", required = false) Integer pageNum,
                                                                    @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId,
                                                                    @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
                                                                    @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities,
                                                                    @RequestHeader(name = UserHeadInfo.USERKIND,    required = false) String userKind)
    {

        try
        {
            ServiceGatewaySearchCritical serviceGatewaySearchCritical = new ServiceGatewaySearchCritical();
            serviceGatewaySearchCritical.setServiceGatewayId(serviceGatewayId);
            serviceGatewaySearchCritical.setEndpoint(endpoint);

            if (pageNum != null) serviceGatewaySearchCritical.setPageNum(pageNum);
            if (pageSize != null) serviceGatewaySearchCritical.setPageSize(pageSize);

            return ResponseEntity.ok().body(serviceGatewayService.getServiceGateways(serviceGatewaySearchCritical));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("get service gateways error. {}", e.getMessage());
            throw throwWebException(e);
        }
    }

    @GetMapping(value = "/service-gateways/detail", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get service gateway detail list",notes = DescriptionConfig.SERVICE_GATEWAY_DETAIL_LIST_MSG)
    public ResponseEntity<GetServiceGatewaysRsp> getServiceGatewaysWithDetails(@ApiParam(name = "service_gateway_id") @RequestParam(value = "service_gateway_id", required = false) String serviceGatewayId,
                                                                               @ApiParam(name = "endpoint") @RequestParam(value = "endpoint", required = false) String endpoint,
                                                                               @ApiParam(name = "page_size") @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                                               @ApiParam(name = "page_num") @RequestParam(value = "pageNum", required = false) Integer pageNum,
                                                                               @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId,
                                                                               @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
                                                                               @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities,
                                                                               @RequestHeader(name = UserHeadInfo.USERKIND,    required = false) String userKind)
    {

        try
        {
            ServiceGatewaySearchCritical serviceGatewaySearchCritical = new ServiceGatewaySearchCritical();
            serviceGatewaySearchCritical.setServiceGatewayId(serviceGatewayId);
            serviceGatewaySearchCritical.setEndpoint(endpoint);

            if (pageNum != null) serviceGatewaySearchCritical.setPageNum(pageNum);
            if (pageSize != null) serviceGatewaySearchCritical.setPageSize(pageSize);

            return ResponseEntity.ok().body(serviceGatewayService.getServiceGatewaysWithDetails(serviceGatewaySearchCritical));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("get service gateways details error. {}", e.getMessage());
            throw throwWebException(e);
        }
    }

    @GetMapping(value = "/service-gateways/{service_gateway_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get service gateway detail",notes = DescriptionConfig.SERVICE_GATEWAY_LIST_MSG)
    @LNJoyAuditLog(value = "get service gateway detail",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                            bindParameterName = "serviceGatewayId"))
    public ResponseEntity<ServiceGateway> getServiceGatewayDetail(@ApiParam(value = "", required = true, name = "service_gateway_id") @PathVariable(value = "service_gateway_id") String serviceGatewayId,
                                                                  @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId,
                                                                  @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
                                                                  @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities,
                                                                  @RequestHeader(name = UserHeadInfo.USERKIND,    required = false) String userKind)
    {

        try
        {
            return ResponseEntity.ok().body(serviceGatewayService.getServiceGateway(serviceGatewayId));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("get service gateways error. {}", e.getMessage());
            throw throwWebException(e);
        }
    }

    @PutMapping(value = "/service-gateways/{service_gateway_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "update service gateway",notes = DescriptionConfig.UPDATE_SERVICE_GATEWAY_MSG)
    @LNJoyAuditLog(value = "update service gateway",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                            bindParameterName = "serviceGatewayId"))
    public ResponseEntity<String> updateServiceGateway(@ApiParam(value = "", required = true, name = "service_gateway_id") @PathVariable(value = "service_gateway_id") String serviceGatewayId,
                                                       @ApiParam(value = "", required = true, name = "service_gateway") @RequestBody @Valid UpdateServiceGatewayReq updateServiceGatewayReq,
                                                       @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId,
                                                       @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
    {
        try
        {

            serviceGatewayService.updateServiceGateway(serviceGatewayId, updateServiceGatewayReq);
            return ResponseEntity.ok(null);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("update service gateway error. {}", e.getMessage());
            throw throwWebException(e);
        }
    }

    @GetMapping(value = "/service-gateways/{service_gateway_id}/portstatus", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get service gateway port status list",notes = DescriptionConfig.SERVICE_GATEWAY_LIST_MSG)
    @LNJoyAuditLog(value = "get service gateway port status list",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                            bindParameterName = "serviceGatewayId"))
    public ResponseEntity<List<PortStatus>> getServiceGatewayPortStatus(@ApiParam(value = "", required = true, name = "service_gateway_id") @PathVariable(value = "service_gateway_id") String serviceGatewayId,
                                                                        @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId,
                                                                        @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
                                                                        @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities,
                                                                        @RequestHeader(name = UserHeadInfo.USERKIND,    required = false) String userKind)
    {

        try
        {
            return ResponseEntity.ok().body(serviceGatewayService.portStatus(serviceGatewayId));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("get service gateway port status error. {}", e.getMessage());
            throw throwWebException(e);
        }
    }
}
