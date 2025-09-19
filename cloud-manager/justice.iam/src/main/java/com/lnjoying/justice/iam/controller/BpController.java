package com.lnjoying.justice.iam.controller;

import com.lnjoying.justice.commonweb.controller.RestWebController;
import com.lnjoying.justice.commonweb.exception.WebSystemException;
import com.lnjoying.justice.commonweb.handler.aspect.annotation.LNJoyAuditLog;
import com.lnjoying.justice.commonweb.handler.aspect.annotation.ResourceIdExtractConfiguration;
import com.lnjoying.justice.commonweb.handler.aspect.enums.ResourceIdLocationType;
import com.lnjoying.justice.iam.common.constant.IamResources;
import com.lnjoying.justice.iam.handler.actiondescription.i18n.zh_cn.BpActionDescriptionTemplate;
import com.lnjoying.justice.schema.common.ErrorLevel;
import com.lnjoying.justice.iam.config.DescriptionConfig;
import com.lnjoying.justice.iam.domain.dto.request.bp.BpRawReq;
import com.lnjoying.justice.iam.domain.dto.request.bp.BpUniqueReq;
import com.lnjoying.justice.iam.domain.model.search.BpSearchCritical;
import com.lnjoying.justice.iam.service.BpManagerService;
import com.lnjoying.justice.schema.constant.ResourceOperationTypeConstants;
import io.swagger.annotations.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import static com.lnjoying.justice.commonweb.common.SwaggerConstants.*;
import static com.lnjoying.justice.schema.common.ErrorCode.User_Not_Grant;
import static com.lnjoying.justice.iam.utils.ServiceCombRequestUtils.*;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.ResponseEntity.status;

@RestSchema(schemaId = "bps-manager")
@RequestMapping("/api/iam/v1/bps")
@Controller
@Api(value = "BP Controller",tags = {"BP Controller"})
@ApiOperation(value = "", extensions = {@Extension(name = SWAGGER_X_RESOURCE,
        properties = {@ExtensionProperty(name = SWAGGER_RESOURCE_SINGULAR_NAME, value = "bp"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_PLURAL_NAME, value = "bps"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_DESCRIPTION, value = "组织"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_MODEL, value = "tbl_bp_info")})})
public class BpController extends RestWebController
{

    private static final String REG_UUID = "[0-9a-f]{32}";
    private static Logger LOGGER = LogManager.getLogger();

    @Autowired
    private BpManagerService bpManagerService;

    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "add bp", response = Object.class, notes = DescriptionConfig.ADD_BP_MSG)
    @LNJoyAuditLog(value = "add bp",
            tags = ResourceOperationTypeConstants.RESOURCE_CREATE,
            actionDescriptionTemplate = BpActionDescriptionTemplate.Descriptions.ADD_BP,
            isResourceInstanceNameFromArgs = true,
            resourceInstanceNameFromArgsParseSpEl = "#obj?.get('name')?.toString()",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.METHOD_RETURN_VALUE,
                            resourceIdParseSPEL = "#obj?.body?.get('id')?.toString()"))
    @ResponseBody    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> addBP(
        @ApiParam(value = "bp raw info", required = true, name = "bp raw info") @RequestBody BpRawReq request)
    {
        try
        {
            LOGGER.debug("add new bp: {}", request);
            checkRoleAdmin();
            return status(CREATED).body(bpManagerService.addBp(request));
        }
        catch (Exception e)
        {
            throw throwWebException(e);
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ALL_ADMIN')")
    @DeleteMapping(value = "/{bpId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "delete bp by bpId", response = Object.class, notes = DescriptionConfig.DELETE_BP_MSG)
    @LNJoyAuditLog(value = "delete bp by bpId",
            resourceMapperId = IamResources.BP,
            actionDescriptionTemplate = BpActionDescriptionTemplate.Descriptions.DELETE_BP,
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                            bindParameterName = "bpId"))
    @ResponseBody    @ResponseStatus(HttpStatus.OK)
    public void deleteBp(
        @ApiParam(value = "bp id") @PathVariable("bpId") String bpId)
    {
        try
        {
            LOGGER.debug("delete bp: {}", bpId);
            checkRoleAdmin();
            bpManagerService.deleteBp(bpId);
        }
        catch (Exception e)
        {
            throw throwWebException(e);
        }
    }

    @PutMapping(value = "/{bpId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "update bp by bpId", response = Object.class, notes = DescriptionConfig.UPDATE_BP_MSG)
    @LNJoyAuditLog(value = "update bp by bpId",
            actionDescriptionTemplate = BpActionDescriptionTemplate.Descriptions.UPDATE_BP,
            isResourceInstanceNameFromArgs = true,
            resourceInstanceNameFromArgsParseSpEl = "#obj?.get('name')?.toString()",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                            bindParameterName = "bpId"))
    @ResponseBody    @ResponseStatus(HttpStatus.OK)
    public void updateBp(
            @ApiParam(value = "", name = "updateBp") @RequestBody BpRawReq bpInfo,
            @ApiParam(value = "", required = true, name = "bpId")@PathVariable String bpId)
    {
        try
        {
            LOGGER.debug("update bp: {} bpinfo: {}", bpId,  bpInfo);
            checkRoleAdmin();
            bpManagerService.updateBpInfo(bpId, bpInfo);
        }
        catch (Exception e)
        {
            throw throwWebException(e);
        }
    }

    @GetMapping(value = "/{bpId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get bp by bpId", response = Object.class, notes = DescriptionConfig.BP_INFO_MSG)
    @LNJoyAuditLog(value = "get bp by bpId",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                            bindParameterName = "bpId"))
    @ResponseBody    @ResponseStatus(HttpStatus.OK)
    public Object getBp(
            @ApiParam(value = "bpId", required = true, name = "bpId") @PathVariable("bpId") String bpId)
    {
        try
        {
            LOGGER.debug("get bp info, bpId: {}", bpId);

            if (isBpAdmin())
            {
                if (!getBpId().equals(bpId))
                {
                   throw new WebSystemException(User_Not_Grant, ErrorLevel.ERROR);
                }
            }
            return bpManagerService.getBpDtoInfo(bpId);
        }
        catch (Exception e)
        {
            throw throwWebException(e);
        }
    }

    /**
     * get bp list
     */
    @PreAuthorize("hasAnyRole('ROLE_ALL_ADMIN')")
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get bp list", response = Object.class, notes = DescriptionConfig.BP_LIST_MSG)
    @ResponseBody     @ResponseStatus(HttpStatus.OK)
    public Object getBps(@ApiParam(name = "name") @RequestParam(required = false) String name,
                         @ApiParam(name = "page_size") @RequestParam(required = false)  Integer page_size,
                         @ApiParam(name = "page_num") @RequestParam(required = false)  Integer page_num)
    {
        try
        {
            LOGGER.debug("get bp list");

            BpSearchCritical pageSearchCritical = new BpSearchCritical();
            pageSearchCritical.setName(name);
            String queryBpId = "";
            if (!isAdmin())
            {
                queryBpId = getUserAttributes().getLeft();
            }
            pageSearchCritical.setBpId(queryBpId);
            if (page_num != null) pageSearchCritical.setPageNum(page_num);
            if (page_size != null) pageSearchCritical.setPageSize(page_size);
            return bpManagerService.getBpDtoInfos(pageSearchCritical);
        }
        catch (Exception e)
        {
            throw throwWebException(e);
        }
    }

    @PutMapping(value = "/uniqueness", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "check uniqueness", response = Object.class, notes = DescriptionConfig.UNIQUENESS_MSG)
    @ResponseBody    @ResponseStatus(HttpStatus.OK)
    public Object uniqueness(
            @ApiParam(value = "uniquenessRequest", required = true, name = "uniqueness") @RequestBody BpUniqueReq request)
    {
        try
        {
            return bpManagerService.uniqueness(request);
        }
        catch (Exception e)
        {
            throw throwWebException(e);
        }
    }
}
