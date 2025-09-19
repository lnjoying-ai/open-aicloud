package com.lnjoying.justice.aos.controller;

import com.lnjoying.justice.aos.common.ServiceSearchCritical;
import com.lnjoying.justice.aos.config.DescriptionConfig;
import com.lnjoying.justice.aos.domain.dto.req.AddServiceReq;
import com.lnjoying.justice.aos.domain.dto.req.UpdateServiceReq;
import com.lnjoying.justice.aos.domain.dto.rsp.GetContainerListRsp;
import com.lnjoying.justice.aos.domain.dto.rsp.GetServiceListRsp;
import com.lnjoying.justice.aos.domain.model.ServiceInfo;
import com.lnjoying.justice.aos.facade.StackServiceFacade;
import com.lnjoying.justice.commonweb.controller.RestWebController;
import com.lnjoying.justice.commonweb.exception.WebSystemException;
import com.lnjoying.justice.commonweb.handler.aspect.annotation.LNJoyAuditLog;
import com.lnjoying.justice.commonweb.handler.aspect.annotation.ResourceIdExtractConfiguration;
import com.lnjoying.justice.commonweb.handler.aspect.enums.ResourceIdLocationType;
import com.lnjoying.justice.schema.common.ErrorCode;
import com.lnjoying.justice.schema.common.ErrorLevel;
import com.lnjoying.justice.schema.constant.ResourceOperationTypeConstants;
import com.lnjoying.justice.schema.constant.UserHeadInfo;
import io.swagger.annotations.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.lnjoying.justice.commonweb.common.SwaggerConstants.*;
import static com.lnjoying.justice.commonweb.util.ServiceCombRequestUtils.*;

/**
 * stack service
 */

@RestSchema(schemaId = "serviceController")
@RequestMapping(path = "/aos/v1/docker/services")
@Api(value = "Service Controller",tags = {"Service Controller"})
@ApiOperation(value = "", extensions = {@Extension(name = SWAGGER_X_RESOURCE,
		properties = {@ExtensionProperty(name = SWAGGER_RESOURCE_SINGULAR_NAME, value = "stack_service"),
				@ExtensionProperty(name = SWAGGER_RESOURCE_PLURAL_NAME, value = "stack_services"),
				@ExtensionProperty(name = SWAGGER_RESOURCE_DESCRIPTION, value = "应用服务"),
				@ExtensionProperty(name = SWAGGER_RESOURCE_MODEL, value = "tbl_stack_service_info")})})
public class ServiceController extends RestWebController
{
	private static Logger LOGGER = LogManager.getLogger();

	@Autowired
	StackServiceFacade stackFacade;

	@PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "add service",notes = DescriptionConfig.ADD_SERVICE_MSG,
			tags = ResourceOperationTypeConstants.RESOURCE_CREATE)
	public ResponseEntity<Map<String,String>> addService(@ApiParam(value = "", required = true, name = "addServiceReq")@RequestBody AddServiceReq addServiceReq,
													   	 @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId,
													   	 @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
														 @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
	{
		try
		{
			return ResponseEntity.status(HttpStatus.CREATED).body(stackFacade.addService(addServiceReq));
		}
		catch (Exception e)
		{
			e.printStackTrace();
			LOGGER.error("add service error: {}", e.getMessage());
			throw throwWebException(e);
		}
	}

	@PutMapping(value = "/{service_id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "update service",notes = DescriptionConfig.UPDATE_SERVICE_MSG)
	@LNJoyAuditLog(value = "update service",
	        resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
	                        bindParameterName = "serviceId"))
	public ResponseEntity<Map<String,String>> updateServie(@ApiParam(value = "", required = true, name = "service_id")@PathVariable(value = "serviceId") String serviceId,
														   @ApiParam(value = "", required = true, name = "updateTemplateReq")@RequestBody UpdateServiceReq updateServiceReq,
														   @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId,
														   @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
	{
		try
		{
			return ResponseEntity.status(HttpStatus.OK).body(null);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			LOGGER.error("update service error: {}", e.getMessage());
			throw throwWebException(e);
		}
	}

	@DeleteMapping(value = "/{service_id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "delete service",notes = DescriptionConfig.DELETE_SERVICE_MSG)
	@LNJoyAuditLog(value = "delete service",
	        resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
	                        bindParameterName = "serviceId"))
	public ResponseEntity<String>  deleteService(@ApiParam(value = "", required = true, name = "service_id")@PathVariable(value = "serviceId") String serviceId,
												 @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId,
												 @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
												 @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
	{
		try
		{
			checkAuthorityToOperateService(authorities, userId, bpId, serviceId);

			String queryUserId = null;

			stackFacade.deleteService(serviceId, queryUserId);

			return ResponseEntity.status(HttpStatus.OK).body(null);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			LOGGER.error("delete service error: {}", e.getMessage());
			throw throwWebException(e);
		}
	}

	@PostMapping(value = "/{service_id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "action service",notes = DescriptionConfig.ACTION_SERVICE_MSG)
	@LNJoyAuditLog(value = "action service",
			resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
					bindParameterName = "serviceId"),
			actionDescriptionField = "action",
			actionDescriptionValueSpEl = "#obj",
			actionDescriptionFormatString = "service action: %s")
	public ResponseEntity<String>  actionService(@ApiParam(value = "", required = true, name = "service_id")@PathVariable(value = "serviceId") String serviceId,
											     @ApiParam(value = "", required = true, name = "action")@RequestParam String action,
												 @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId,
												 @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
												 @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
	{
		try
		{
			checkAuthorityToOperateService(authorities, userId, bpId, serviceId);

			String operUserId = null;

			stackFacade.actionService(serviceId, action, operUserId);
			return ResponseEntity.status(HttpStatus.OK).body(null);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			LOGGER.error("action stack error: {}", e.getMessage());
			throw throwWebException(e);
		}
	}

	@GetMapping(value = "/{service_id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "get service info",notes = DescriptionConfig.SERVICE_INFO_MSG)
	@LNJoyAuditLog(value = "get service info",
	        resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
	                        bindParameterName = "serviceId"))
	public ResponseEntity<ServiceInfo>  getService(@ApiParam(value = "", required = true, name = "service_id")@PathVariable(value = "serviceId") String serviceId,
												   @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId,
												   @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
												   @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
	{
		try
		{
			
			checkAuthorityToOperateService(authorities, userId, bpId, serviceId);

			String queryUserId = null;

			return ResponseEntity.status(HttpStatus.OK).body(stackFacade.getService(serviceId, queryUserId));
		}
		catch (Exception e)
		{
			e.printStackTrace();
			LOGGER.error("get service error: {}", e.getMessage());
			throw throwWebException(e);
		}
	}




	@GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "service list",notes = DescriptionConfig.SERVICE_LIST_MSG)
	public ResponseEntity<GetServiceListRsp>  getServiceList(@ApiParam(name = "stack_name")  @RequestParam(value = "stackName", required = false) String stackName,
															 @ApiParam(name = "service_name")@RequestParam(value = "serviceName", required = false) String serviceName,
															 @ApiParam(name = "status")@RequestParam(value = "status", required = false) Integer status,
															 @ApiParam(name = "region")@RequestParam(value = "region", required = false) String regionId,
															 @ApiParam(name = "site")@RequestParam(value = "site", required = false) String siteId,
															 @ApiParam(name = "node_id")@RequestParam(value = "node_id", required = false) String nodeId,
															 @ApiParam(name = "user_id")@RequestParam(value = "user_id", required = false) String ownerUserId,
															 @ApiParam(name = "page_size") @RequestParam(value = "pageSize", required = false) Integer pageSize,
															 @ApiParam(name = "page_num") @RequestParam(value = "pageNum", required = false)  Integer pageNum,
															 @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId,
															 @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
															 @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
	{
		try
		{
			
			ServiceSearchCritical critical = new ServiceSearchCritical();

			if (StringUtils.isNotBlank(stackName))
			{
				critical.setStackName(stackName);
			}

			if (StringUtils.isNotBlank(serviceName))
			{
				critical.setServiceName(serviceName);
			}

			if (isBpAdmin() || isBpUser())
			{
				critical.setUserId(userId);
			}

			if (isAdmin())
			{
				if (StringUtils.isNotBlank(ownerUserId))
				{
					critical.setUserId(ownerUserId);
				}
			}
			if (StringUtils.isNotBlank(regionId)) critical.setRegionId(regionId);
			if (StringUtils.isNotBlank(siteId )) critical.setSiteId(siteId);
			if (StringUtils.isNotBlank(nodeId)) critical.setNodeId(nodeId);
			if (status != null) critical.setStatus(status);
			if (pageNum != null) critical.setPageNum(pageNum);
			if (pageSize != null) critical.setPageSize(pageSize);
			return ResponseEntity.status(HttpStatus.OK).body(stackFacade.getServiceList(critical));
		}
		catch (Exception e)
		{
			e.printStackTrace();
			LOGGER.error("get service list error: {}", e.getMessage());
			throw throwWebException(e);
		}
	}

	@GetMapping(value = "/{service_id}/instances", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "get inst list by service id",notes = DescriptionConfig.SERVICE_INST_LIST_MSG)
	@LNJoyAuditLog(value = "get inst list by service id",
	        resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
	                        bindParameterName = "serviceId"))
	public ResponseEntity<GetContainerListRsp>  getServiceInstList(@ApiParam(value = "", required = true, name = "service_id")@PathVariable(value = "service_id") String serviceId,
																   @ApiParam(value = "page size", required = false, name = "page_size") @RequestParam(value= "pageSize", required = false,  defaultValue = Integer.MAX_VALUE + "") Integer pageSize,
																   @ApiParam(value = "page_num", required = false, name = "page_num") @RequestParam(value= "pageNum", required = false, defaultValue = "1") Integer pageNum,
																   @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId,
																   @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
																   @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
	{
		try
		{
			
			LOGGER.info("get service inst for {} .", serviceId);
			checkAuthorityToOperateService(authorities, userId, bpId, serviceId);

			String queryUserId = null;

			return ResponseEntity.status(HttpStatus.OK).body(stackFacade.getStackInst(serviceId, queryUserId, pageNum, pageSize));
		}
		catch (Exception e)
		{
			e.printStackTrace();
			LOGGER.error("get service list error: {}", e.getMessage());
			throw throwWebException(e);
		}
	}

	private void checkAuthorityToOperateService(String authorities, String userId, String bpId, String serviceId)
	{
		if (StringUtils.isBlank(getKind()))
		{
			throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
		}
		else if (isBpAdmin())
		{
			if (!stackFacade.isOwnerOfService(serviceId, null, userId) && !stackFacade.isOwnerOfService(serviceId, bpId, null))
			{
				throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
			}
		}
		else if (isBpUser())
		{
			if (!stackFacade.isOwnerOfService(serviceId, bpId, userId))
			{
				throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
			}
		}
		else if(! isAdmin())
		{
			throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
		}
	}
}
