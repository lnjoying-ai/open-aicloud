package com.lnjoying.justice.aos.controller;

import com.lnjoying.justice.aos.common.AosResources;
import com.lnjoying.justice.aos.common.StackSearchCritical;
import com.lnjoying.justice.aos.config.DescriptionConfig;
import com.lnjoying.justice.aos.handler.actiondescription.i18n.zh_cn.StackInfoActionDescriptionTemplate;
import com.lnjoying.justice.aos.handler.actiondescription.i18n.zh_cn.StackSpecActionDescriptionTemplate;
import com.lnjoying.justice.schema.constant.ResourceOperationTypeConstants;
import com.lnjoying.justice.schema.entity.aos.AddStackReq;
import com.lnjoying.justice.aos.domain.dto.req.UpdateStackCfgReq;
import com.lnjoying.justice.aos.domain.dto.req.UpdateStackReq;
import com.lnjoying.justice.aos.domain.dto.rsp.GetCfgStackListRsp;
import com.lnjoying.justice.aos.domain.dto.rsp.GetServiceListRsp;
import com.lnjoying.justice.aos.domain.dto.rsp.GetStackListRsp;
import com.lnjoying.justice.aos.domain.model.StackComposeInfo;
import com.lnjoying.justice.aos.domain.model.StackInfo;
import com.lnjoying.justice.aos.facade.StackServiceFacade;
import com.lnjoying.justice.commonweb.controller.RestWebController;
import com.lnjoying.justice.commonweb.exception.WebSystemException;
import com.lnjoying.justice.commonweb.handler.aspect.annotation.LNJoyAuditLog;
import com.lnjoying.justice.commonweb.handler.aspect.annotation.ResourceIdExtractConfiguration;
import com.lnjoying.justice.commonweb.handler.aspect.enums.ResourceIdLocationType;
import com.lnjoying.justice.schema.common.ErrorCode;
import com.lnjoying.justice.schema.common.ErrorLevel;
import com.lnjoying.justice.schema.constant.UserHeadInfo;
import com.micro.core.common.Utils;
import io.swagger.annotations.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import static com.lnjoying.justice.commonweb.common.SwaggerConstants.*;
import static com.lnjoying.justice.commonweb.util.ServiceCombRequestUtils.*;

/**
 * stack controller
 */

@RestSchema(schemaId = "stackController")
@RequestMapping(path = "/aos/v1/docker/stacks")
@Api(value = "Stack Controller",tags = "Stack Controller")
@ApiOperation(value = "", extensions = {@Extension(name = SWAGGER_X_RESOURCE,
		properties = {@ExtensionProperty(name = SWAGGER_RESOURCE_SINGULAR_NAME, value = "stack"),
				@ExtensionProperty(name = SWAGGER_RESOURCE_PLURAL_NAME, value = "stacks"),
				@ExtensionProperty(name = SWAGGER_RESOURCE_DESCRIPTION, value = "应用"),
				@ExtensionProperty(name = SWAGGER_RESOURCE_MODEL, value = "TblStackInfo")})})
public class StackController extends RestWebController
{
	private static Logger LOGGER = LogManager.getLogger();

	@Autowired
	StackServiceFacade stackFacade;

	@PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "add stack",notes = DescriptionConfig.ADD_STACK_MSG)
	@LNJoyAuditLog(value = "add stack",
			tags = ResourceOperationTypeConstants.RESOURCE_CREATE,
			resourceMapperId = AosResources.STACK_SPEC,
			actionDescriptionTemplate = StackSpecActionDescriptionTemplate.Descriptions.ADD_SPEC,
	        resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.METHOD_RETURN_VALUE,
	                        resourceIdParseSPEL = "#obj?.body?.get('id')?.toString()"))
	public ResponseEntity<Map<String,String>> addStack(@ApiParam(value = "", required = true, name = "addStackReq")@RequestBody AddStackReq addStackReq,
										 			   @RequestHeader(name = UserHeadInfo.USERID,      required = false) String userId,
													   @RequestHeader(name = UserHeadInfo.BPID,        required = false) String bpId,
													   @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
	{
		try
		{
			LOGGER.info("add stack. req: {}", addStackReq);
			if (StringUtils.isNotBlank(addStackReq.getBp_id()) && StringUtils.isNotBlank(addStackReq.getUser_id()))
			{
				bpId = addStackReq.getBp_id();
				userId = addStackReq.getUser_id();
			}
			
			Map<String, String> ret = new HashMap<>();
			ret.put("id", stackFacade.addStack(addStackReq, bpId, userId));

			return ResponseEntity.status(HttpStatus.CREATED).body(ret);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			LOGGER.error("add stack error: {} ", e.getMessage());
			throw throwWebException(e);
		}
	}

	@PutMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "update stack",notes = DescriptionConfig.UPDATE_STACK_MSG)
	@LNJoyAuditLog(value = "update stack",
			resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH, bindParameterName = "stackId"))
	public ResponseEntity<Map<String,String>> updateStack(@ApiParam(value = "", required = true, name = "stack_id")@PathVariable String stackId,
														  @ApiParam(value = "", required = true, name = "updateTemplateReq")@RequestBody UpdateStackReq updateStackReq,
														  @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId,
														  @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
														  @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
	{
		try
		{
			LOGGER.info("update stack. req: {}",  updateStackReq);
			checkAuthorityToOperateStack(authorities, userId, bpId, stackId);

			stackFacade.updateStack(stackId, updateStackReq);
			return ResponseEntity.status(HttpStatus.OK).body(null);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			LOGGER.error("update stack error: {}", e.getMessage());
			throw throwWebException(e);
		}
	}

	@DeleteMapping(value = "/{stack_id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "delete stack",notes = DescriptionConfig.DELETE_STACK_MSG)
	@LNJoyAuditLog(value = "delete stack",
			resourceMapperId = AosResources.STACK,
			actionDescriptionTemplate = StackInfoActionDescriptionTemplate.Descriptions.DELETE_STACK,
			resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH, bindParameterName = "stackId"))
	public ResponseEntity<String>  deleteStack(@ApiParam(value = "", required = true, name = "stack_id")@PathVariable(value = "stackId") String stackId,
											   @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId,
											   @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
											   @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
	{
		try
		{
			LOGGER.info("delete stack: {}", stackId);

			checkAuthorityToOperateStack(authorities, userId, bpId, stackId);

			String operUserId = null;

			stackFacade.deleteStack(stackId, operUserId);
			return ResponseEntity.status(HttpStatus.OK).body(null);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			LOGGER.error("delete stack error: {}", e.getMessage());
			throw throwWebException(e);
		}
	}

	@PostMapping(value = "/{stack_id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "action stack",notes = DescriptionConfig.ACTION_STACK_MSG)
	@LNJoyAuditLog(value = "action stack",
			resourceMapperId = AosResources.STACK,
			actionDescriptionTemplateClass = StackInfoActionDescriptionTemplate.class,
			actionDescriptionField = "action", actionDescriptionFormatString = "action stack:%s",
			resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH, bindParameterName = "stackId"))
	public ResponseEntity<String>  actionStack(@ApiParam(value = "", required = true, name = "stack_id")@PathVariable(value = "stackId") String stackId,
											   @ApiParam(value = "", required = true, name = "action")@RequestParam String action,
											   @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId,
											   @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
											   @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
	{
		try
		{
			LOGGER.info("{} stack {}", action, stackId);
			checkAuthorityToOperateStack(authorities, userId, bpId, stackId);

			String operUserId = null;

			stackFacade.actionStack(stackId, action, operUserId);
			return ResponseEntity.status(HttpStatus.OK).body(null);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			LOGGER.error("action stack error: {}", e.getMessage());
			throw throwWebException(e);
		}
	}

	@GetMapping(value = "/{stack_id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "get stack info",notes = DescriptionConfig.STACK_INFO_MSG)
	@LNJoyAuditLog(value = "get stack info",
			resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH, bindParameterName = "stackId"))
	public ResponseEntity<StackInfo>  getStack(@ApiParam(value = "", required = true, name = "stack_id")@PathVariable(value = "stackId") String stackId,
											   @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId,
											   @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
											   @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
	{
		try
		{
			
			LOGGER.info("get stack: {}", stackId);
			checkAuthorityToOperateStack(authorities, userId, bpId, stackId);

			String operUserId = null;

			return ResponseEntity.status(HttpStatus.OK).body(stackFacade.getStack(stackId, operUserId, false));
		}
		catch (Exception e)
		{
			e.printStackTrace();
			LOGGER.error("get stack error: {}", e.getMessage());
			throw throwWebException(e);
		}
	}

	@GetMapping(value = "/{stack_id}/compose-config", produces = "application/octet-stream;charset=UTF-8")
	@ApiOperation(value = "get compose by stack id",notes = DescriptionConfig.STACK_COMPOSE_MSG)
	@LNJoyAuditLog(value = "get compose by stack id",
			resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH, bindParameterName = "stackId"))
	public ResponseEntity<StackComposeInfo>  getStackCompose(@ApiParam(value = "", required = true, name = "stack_id")@PathVariable(value = "stackId") String stackId,
															 @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId,
															 @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
															 @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
	{
		try
		{
			
			LOGGER.info("get stack compose info for: {}", stackId);
			checkAuthorityToOperateStack(authorities, userId, bpId, stackId);

			String operUserId = null;

			return ResponseEntity.status(HttpStatus.OK).body(stackFacade.getStackCompose(stackId, operUserId));
		}
		catch (Exception e)
		{
			e.printStackTrace();
			LOGGER.error("get stack compose error: {}", e.getMessage());
			throw throwWebException(e);
		}
	}

	@GetMapping(value = "/{stack_id}/compose-archives", produces = MediaType.TEXT_PLAIN_VALUE)
	@ApiOperation(value = "download stack compose",notes = DescriptionConfig.DOWNLOAD_COMPOSE_MSG)
	@LNJoyAuditLog(value = "download stack compose",
			resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH, bindParameterName = "stackId"))
	public ResponseEntity<String>  getStackArchives(@ApiParam(value = "", required = true, name = "stack_id")@PathVariable(value = "stackId") String stackId,
													@RequestHeader(name = UserHeadInfo.USERID, required = false) String userId,
													@RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
													@RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
	{
		try
		{
			
			LOGGER.info("get stack compose archive info for {}", stackId);
			checkAuthorityToOperateStack(authorities, userId, bpId, stackId);

			String operUserId = null;

			ByteArrayOutputStream bos = stackFacade.getStackArchives(stackId, operUserId);
			HttpHeaders headers = new HttpHeaders();
			headers.add("Content-Encoding", "zip");
			headers.add("Content-Disposition", "attachment; filename=compose.zip");
			headers.add("Cache-Control", "private");
			headers.add("Pragma", "private");
			headers.add("trans", "convert");

			return ResponseEntity.status(HttpStatus.OK).headers(headers).body(Utils.byteToHexString(bos.toByteArray()));
		}
		catch (Exception e)
		{
			e.printStackTrace();
			LOGGER.error("get stack archives error: {}", e.getMessage());
			throw throwWebException(e);
		}
	}


	@GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "get stack list",notes = DescriptionConfig.STACK_LIST_MSG)
	public ResponseEntity<GetStackListRsp>  getStackList(@ApiParam(name = "name")@RequestParam(value = "name", required = false) String name,
														 @ApiParam(name = "status")@RequestParam(value = "status", required = false) Integer status,
														 @ApiParam(name = "region")@RequestParam(value = "region", required = false) String regionId,
														 @ApiParam(name = "site")@RequestParam(value = "site", required = false) String siteId,
														 @ApiParam(name = "node_id")@RequestParam(value = "node_id", required = false) String nodeId,
                                                         @ApiParam(name = "stack_type") @RequestParam(value = "stack_type", required = false)  String stackType,
                                                         @ApiParam(name = "user_id")@RequestParam(value = "user_id", required = false) String ownerUserId,
														 @ApiParam(name = "page_size") @RequestParam(value = "page_size", required = false) Integer pageSize,
														 @ApiParam(name = "page_num") @RequestParam(value = "page_num", required = false)  Integer pageNum,
														 @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId,
														 @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
	{
		try
		{
			
			StackSearchCritical critical = new StackSearchCritical();

			boolean queryOwner = StringUtils.isNotBlank(ownerUserId) && (isAdmin() || isBpAdmin());
			if (queryOwner)
			{
				critical.setUserId(ownerUserId);
			}
			else
			{
				critical.setUserId(queryUserId());
				critical.setBpId(queryBpId());
			}

			if (!StringUtils.isEmpty(name)) critical.setName(name);
			if (status != null) critical.setStatus(status);
			if (pageNum != null) critical.setPageNum(pageNum);
			if (pageSize != null) critical.setPageSize(pageSize);
			if (regionId != null) critical.setRegionId(regionId);
			if (siteId != null) critical.setSiteId(siteId);
			if (nodeId != null) critical.setNodeId(nodeId);
			critical.setStackType(stackType);

			LOGGER.info("get stack list, condition: {}", critical);

			return ResponseEntity.status(HttpStatus.OK).body(stackFacade.getStackList(critical));
		}
		catch (Exception e)
		{
			e.printStackTrace();
			LOGGER.error("get stack list error: {}", e.getMessage());
			throw throwWebException(e);
		}
	}

	@GetMapping(value = "/{stack_id}/services", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "get services by stack id",notes = DescriptionConfig.STACK_SERVICE_LIST_MSG)
	@LNJoyAuditLog(value = "get services by stack id",
			resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH, bindParameterName = "stackId"))
	public ResponseEntity<GetServiceListRsp>  getStackServices(@ApiParam(value = "", required = true, name = "stack_id")@PathVariable(value = "stackId") String stackId,
															   @ApiParam(value = "page size", required = false, name = "page_size") @RequestParam(value= "pageSize", required = false,  defaultValue = Integer.MAX_VALUE + "") Integer pageSize,
															   @ApiParam(value = "page_num", required = false, name = "page_num") @RequestParam(value= "pageNum", required = false, defaultValue = "1") Integer pageNum,
															   @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId,
															   @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
															   @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
	{
		try
		{
			
			LOGGER.info("get services for stack: {}", stackId);
			checkAuthorityToOperateStack(authorities, userId, bpId, stackId);

			String operUserId = null;

			return ResponseEntity.status(HttpStatus.OK).body(stackFacade.getServiceByStackId(stackId, operUserId, pageNum, pageSize));
		}
		catch (Exception e)
		{
			e.printStackTrace();
			LOGGER.error("get stack archives error: {}", e.getMessage());
			throw throwWebException(e);
		}
	}

	private void checkAuthorityToOperateStack(String authorities, String userId, String bpId, String stackId)
	{
		if (StringUtils.isBlank(getKind()))
		{
			throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
		}
		else if (isBpAdmin())
		{
			if (!stackFacade.isOwnerOfStack(stackId, null, userId) && !stackFacade.isOwnerOfStack(stackId, bpId, null))
			{
				throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
			}
		}
		else if (isBpUser())
		{
			if (!stackFacade.isOwnerOfStack(stackId, bpId, userId))
			{
				throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
			}
		}
		else if(! isAdmin())
		{
			throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
		}
	}

	@GetMapping(value = "/cfgs", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "get stacks associated with cfgs",notes = DescriptionConfig.STACK_SERVICE_LIST_MSG)
	public ResponseEntity<GetCfgStackListRsp>  getCfgStacks(@ApiParam(name = "user_id")@RequestParam(value = "user_id", required = false) String operUserId,
																		  @ApiParam(name = "data_id")@RequestParam(value = "data_id", required = false) String dataId,
																		  @ApiParam(name = "group")@RequestParam(value = "group", required = false) String group,
																		  @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId,
																		  @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
																		  @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
	{
		try
		{
			
			LOGGER.info("get stacks associated with cfgs, userId: {}, dateId: {}, group: {}", operUserId, dataId, group);

			if (isAdmin())
			{
				if (operUserId.equals(userId))
				{
					operUserId = null;
				}
			}

			return ResponseEntity.status(HttpStatus.OK).body(stackFacade.getCfgStacks(operUserId, dataId, group));
		}
		catch (Exception e)
		{
			e.printStackTrace();
			LOGGER.error("get stack archives error: {}", e.getMessage());
			throw throwWebException(e);
		}
	}

	@PutMapping(value = "/cfgs", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "update cfg",notes = DescriptionConfig.UPDATE_STACK_MSG)
	public ResponseEntity<Map<String,String>> updateStackCfg(@ApiParam(value = "", required = true, name = "updateStackCfgReq")@RequestBody UpdateStackCfgReq updateStackCfgReq,
															 @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId,
															 @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
															 @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
	{
		try
		{
			LOGGER.info("update stack cfg. req: {}", updateStackCfgReq);
			stackFacade.updateStackCfg(updateStackCfgReq);
			return ResponseEntity.status(HttpStatus.OK).body(null);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			LOGGER.error("update stack error: {}", e.getMessage());
			throw throwWebException(e);
		}
	}
}
