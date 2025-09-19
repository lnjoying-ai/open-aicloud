package com.lnjoying.justice.aos.controller;

import com.lnjoying.justice.aos.common.TemplateSearchCritical;
import com.lnjoying.justice.aos.config.DescriptionConfig;
import com.lnjoying.justice.aos.domain.dto.req.AddTemplateReq;
import com.lnjoying.justice.aos.domain.dto.req.UpdateTemplateReq;
import com.lnjoying.justice.aos.domain.dto.rsp.GetTemplateListRsp;
import com.lnjoying.justice.aos.domain.model.TemplateInfo;
import com.lnjoying.justice.aos.domain.model.TemplateVerbose;
import com.lnjoying.justice.aos.facade.TemplateServiceFacade;
import com.lnjoying.justice.commonweb.controller.RestWebController;
import com.lnjoying.justice.commonweb.exception.WebSystemException;
import com.lnjoying.justice.commonweb.handler.aspect.annotation.LNJoyAuditLog;
import com.lnjoying.justice.commonweb.handler.aspect.annotation.ResourceIdExtractConfiguration;
import com.lnjoying.justice.commonweb.handler.aspect.enums.ResourceIdLocationType;
import com.lnjoying.justice.schema.common.ErrorCode;
import com.lnjoying.justice.schema.common.ErrorLevel;
import com.lnjoying.justice.schema.constant.ResourceOperationTypeConstants;
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
import java.util.Map;

import static com.lnjoying.justice.commonweb.common.SwaggerConstants.*;
import static com.lnjoying.justice.commonweb.util.ServiceCombRequestUtils.*;

/**
 * Application orchestration template administration controller
 */

@RestSchema(schemaId = "templateController")
@RequestMapping(path = "/aos/v1/templates")
@Api(value = "Template Controller",tags = "Template Controller")
@ApiOperation(value = "", extensions = {@Extension(name = SWAGGER_X_RESOURCE,
		properties = {@ExtensionProperty(name = SWAGGER_RESOURCE_SINGULAR_NAME, value = "template"),
				@ExtensionProperty(name = SWAGGER_RESOURCE_PLURAL_NAME, value = "templates"),
				@ExtensionProperty(name = SWAGGER_RESOURCE_DESCRIPTION, value = "应用模板"),
				@ExtensionProperty(name = SWAGGER_RESOURCE_MODEL, value = "TblStackTemplateBaseInfo,TblStackTemplateInfo")})})
public class TemplateController extends RestWebController
{
	private static Logger LOGGER = LogManager.getLogger();
	
	@Autowired
	TemplateServiceFacade templateFacade;

	@PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "add template",notes = DescriptionConfig.ADD_TEMPLATE_MSG)
	@LNJoyAuditLog(value = "add template",
			tags = ResourceOperationTypeConstants.RESOURCE_CREATE,
			resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.METHOD_RETURN_VALUE,
					resourceIdParseSPEL = "#obj?.body?.get('id')?.toString()"))
	public ResponseEntity<Map<String,String>> addTemplate(@ApiParam(value = "", required = true, name = "addTemplateReq")@RequestBody AddTemplateReq addTemplateReq,
														  @RequestHeader(name = UserHeadInfo.USERID,      required = false) String userId,
														  @RequestHeader(name = UserHeadInfo.BPID,        required = false) String bpId,
														  @RequestHeader(name = UserHeadInfo.BpName,      required = false) String bpName,
														  @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
	{
		try
		{
			LOGGER.info("add template info: {}", addTemplateReq);

			return ResponseEntity.status(HttpStatus.CREATED).body(templateFacade.addTemplate(addTemplateReq, bpId, bpName, userId));
		}
		catch (Exception e)
		{
			e.printStackTrace();
			LOGGER.error("add template error: {}", e.getMessage());
			throw throwWebException(e);
		}
	}

	@PutMapping(value = "/{template_id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "update template",notes = DescriptionConfig.UPDATE_TEMPLATE_MSG)
	@LNJoyAuditLog(value = "update template",
			resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH, bindParameterName = "templateId"))
	public ResponseEntity<Map<String,String>> updateTemplate(@ApiParam(value = "", required = true, name = "template_id")@PathVariable(value = "templateId") String templateId,
															 @ApiParam(value = "", required = true, name = "updateTemplateReq")@RequestBody UpdateTemplateReq updateTemplateReq,
															 @RequestHeader(name = UserHeadInfo.USERID,      required = false) String userId,
															 @RequestHeader(name = UserHeadInfo.BPID,        required = false) String bpId,
															 @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
	{
		try
		{
			LOGGER.info("update template {} info: {}", templateId, updateTemplateReq);
			checkAuthorityToOperateTemplate(authorities, userId, bpId, templateId);

			templateFacade.updateTemplate(templateId, updateTemplateReq);
			return ResponseEntity.status(HttpStatus.OK).body(null);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			LOGGER.error("update template error: {}", e.getMessage());
			throw throwWebException(e);
		}
	}

	@DeleteMapping(value = "/{template_id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "delete template by id",notes = DescriptionConfig.DELETE_TEMPLATE_BY_ID_MSG)
	@LNJoyAuditLog(value = "delete template by id",
			resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH, bindParameterName = "templateId"))
	public ResponseEntity<String>  deleteTemplate(@ApiParam(value = "", required = true, name = "template_id")@PathVariable(value = "templateId") String templateId,
												  @RequestHeader(name = UserHeadInfo.USERID,      required = false) String userId,
												  @RequestHeader(name = UserHeadInfo.BPID,        required = false) String bpId,
												  @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
	{
		try
		{
			LOGGER.info("delete template: {}", templateId);
			checkAuthorityToOperateTemplate(authorities, userId, bpId, templateId);

			templateFacade.deleteTemplate(templateId);
			return ResponseEntity.status(HttpStatus.OK).body(null);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			LOGGER.error("delete template error: {}", e.getMessage());
			throw throwWebException(e);
		}
	}

	@DeleteMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "delete template by id and name",notes = DescriptionConfig.DELETE_TEMPLATE_BY_ID_NAME_MSG)
	public ResponseEntity<String>  deleteTemplateByName(@ApiParam(value = "", required = false, name = "id")@RequestParam(required = false) String id,
														@ApiParam(value = "", required = false, name = "name")@RequestParam(required = false) String name,
												  		@RequestHeader(name = UserHeadInfo.USERID,      required = false) String userId,
														@RequestHeader(name = UserHeadInfo.BPID,        required = false) String bpId,
												  		@RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
	{
		try
		{
			LOGGER.info("delete template id: {} name: {}", id, name);
			checkAuthorityToOperateTemplate(authorities, userId, bpId, id);

			String operUserId = null;
			if (isBpUser() || isBpAdmin())
			{
				operUserId = userId;
			}

			if (StringUtils.isEmpty(id) && StringUtils.isEmpty(name))
			{
				throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO);
			}

			if (StringUtils.isEmpty(id))
			{
				templateFacade.deleteTemplateByName(name, operUserId);
			}
			else
			{
				templateFacade.deleteTemplate(id, name, operUserId);
			}

			return ResponseEntity.status(HttpStatus.OK).body(null);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			LOGGER.error("delete template error: {}", e.getMessage());
			throw throwWebException(e);
		}
	}

	@GetMapping(value = "/{template_id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "get template info",notes = DescriptionConfig.TEMPLATE_INFO_MSG)
	@LNJoyAuditLog(value = "get template info",
			resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH, bindParameterName = "templateId"))
	public ResponseEntity<TemplateInfo>  getTemplate(@ApiParam(value = "", required = true, name = "template_id")@PathVariable(value = "templateId") String templateId,
													 @RequestHeader(name = UserHeadInfo.USERID,      required = false) String userId,
													 @RequestHeader(name = UserHeadInfo.BPID,        required = false) String bpId,
													 @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
	{
		try
		{
			checkAuthorityToOperateTemplate(authorities, userId, bpId, templateId);

			String operUserId = null;

			return ResponseEntity.status(HttpStatus.OK).body(templateFacade.getTemplate(templateId, operUserId));
		}
		catch (Exception e)
		{
			e.printStackTrace();
			LOGGER.error("get template error: {}", e.getMessage());
			throw throwWebException(e);
		}
	}

	@GetMapping(value = "/{template_id}/archives", produces = MediaType.TEXT_PLAIN_VALUE)
	@ApiOperation(value = "download template",notes = DescriptionConfig.DOWNLOAD_TEMPLATE_MSG)
	@LNJoyAuditLog(value = "download template",
			resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH, bindParameterName = "templateId"))
	public ResponseEntity<String>  downTemplate(@ApiParam(value = "", required = true, name = "template_id")@PathVariable(value = "templateId") String templateId,
												@RequestHeader(name = UserHeadInfo.USERID,      required = false) String userId,
												@RequestHeader(name = UserHeadInfo.BPID,        required = false) String bpId,
												@RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
	{
		try
		{
			checkAuthorityToOperateTemplate(authorities, userId, bpId, templateId);

			String operUserId = null;

			ByteArrayOutputStream bos = templateFacade.downTemplate(templateId, operUserId);
			HttpHeaders headers = new HttpHeaders();
			headers.add("Content-Type", "application/zip");
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
			LOGGER.error("download template error: {}", e.getMessage());
			throw throwWebException(e);
		}
	}


	@GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "get template list",notes = DescriptionConfig.TEMPLATE_LIST_MSG)
	public ResponseEntity<GetTemplateListRsp>  getTemplateList(@ApiParam(name = "name")@RequestParam(value = "name", required = false) String name,
															   @ApiParam(name = "page_size") @RequestParam(value = "pageSize", required = false) Integer pageSize,
															   @ApiParam(name = "page_num") @RequestParam(value = "pageNum", required = false)  Integer pageNum,
															   @ApiParam(name = "labels") @RequestParam(value = "labels", required = false)  String labels,
	                                                           @RequestHeader(name = UserHeadInfo.USERID,      required = false) String userId,
															   @RequestHeader(name = UserHeadInfo.BPID,        required = false) String bpId,
															   @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
	{
		try
		{

			TemplateSearchCritical searchCritical = new TemplateSearchCritical();
			if (!isAdmin())
			{
				searchCritical.setBpId(queryBpId());
			}

			searchCritical.setName(name);
			if (pageNum != null)  searchCritical.setPageNum(pageNum);
			if (pageSize != null) searchCritical.setPageSize(pageSize);
			if (StringUtils.isNotBlank(labels) && StringUtils.isNotEmpty(labels)) searchCritical.setLabels(labels);
			return ResponseEntity.status(HttpStatus.OK).body(templateFacade.getTemplateList(searchCritical));
		}
		catch (Exception e)
		{
			e.printStackTrace();
			LOGGER.error("get template list error: {}", e.getMessage());
			throw throwWebException(e);
		}
	}

	@GetMapping(value = "versions/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "get template info",notes = DescriptionConfig.TEMPLATE_INFO_MSG)
	@LNJoyAuditLog(value = "get template info",
			resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH, bindParameterName = "rootId"))
	public ResponseEntity<TemplateVerbose>  getTemplateVersionList(@ApiParam(value = "", required = true, name = "id")@PathVariable(value = "rootId") String rootId,
																   @RequestHeader(name = UserHeadInfo.USERID,      required = false) String userId,
																   @RequestHeader(name = UserHeadInfo.BPID,        required = false) String bpId,
																   @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
	{
		try
		{

			TemplateSearchCritical searchCritical = new TemplateSearchCritical();
			searchCritical.setRootId(rootId);
			if (!isAdmin())
			{
				searchCritical.setBpId(queryBpId());
			}

			return ResponseEntity.status(HttpStatus.OK).body(templateFacade.getTemplateVersionList(searchCritical));
		}
		catch (Exception e)
		{
			e.printStackTrace();
			LOGGER.error("get template versions list error: {}", e.getMessage());
			throw throwWebException(e);
		}
	}

	private void checkAuthorityToOperateTemplate(String authorities, String userId, String bpId, String templateId)
	{
		if (StringUtils.isBlank(authorities))
		{
			throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
		}
		else if (isBpAdmin())
		{
			if (!templateFacade.isOwnerOfTemplate(templateId, null, userId) && !templateFacade.isOwnerOfTemplate(templateId, bpId, null))
			{
				throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
			}
		}
		else if (isBpUser())
		{
			if (!templateFacade.isOwnerOfTemplate(templateId, bpId, userId))
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
