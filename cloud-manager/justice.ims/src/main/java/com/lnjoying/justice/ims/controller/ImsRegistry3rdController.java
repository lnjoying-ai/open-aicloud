package com.lnjoying.justice.ims.controller;

import com.lnjoying.justice.commonweb.handler.aspect.annotation.LNJoyAuditLog;
import com.lnjoying.justice.commonweb.handler.aspect.annotation.ResourceIdExtractConfiguration;
import com.lnjoying.justice.commonweb.handler.aspect.enums.ResourceIdLocationType;
import com.lnjoying.justice.ims.common.ImsResources;
import com.lnjoying.justice.ims.domain.dto.req.AddRegistry3rdReq;
import com.lnjoying.justice.ims.domain.dto.req.PreDownloadReq;
import com.lnjoying.justice.ims.domain.dto.req.UpdateRegistry3rdReq;
import com.lnjoying.justice.ims.domain.dto.rsp.Registries3rdRsp;
import com.lnjoying.justice.ims.domain.dto.rsp.Registry3rdNodesRsp;
import com.lnjoying.justice.ims.domain.model.ImsRegistry3rd;
import com.lnjoying.justice.ims.facade.ImsRegistry3rdFacade;
import com.lnjoying.justice.ims.handler.actiondescription.Registry3rdActionDescriptionTemplate;
import com.lnjoying.justice.schema.constant.ResourceOperationTypeConstants;
import com.lnjoying.justice.schema.constant.UserHeadInfo;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Extension;
import io.swagger.annotations.ExtensionProperty;
import lombok.extern.slf4j.Slf4j;
import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

import static com.lnjoying.justice.commonweb.common.SwaggerConstants.*;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.ResponseEntity.status;

/**
 * third party registry handler
 *
 * @author merak
 **/

@RestSchema(schemaId = "ImsRegistry3rdController")
@RequestMapping(path = "/ims/v1/docker/3rd-registries")
@Slf4j
@ApiOperation(value = "", extensions = {@Extension(name = SWAGGER_X_RESOURCE,
        properties = {@ExtensionProperty(name = SWAGGER_RESOURCE_SINGULAR_NAME, value = "3rd-registry"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_PLURAL_NAME, value = "3rd-registries"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_DESCRIPTION, value = "第三方仓库"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_MODEL, value = "TblImsRegistry3rd")})})
public class ImsRegistry3rdController extends ImsWebController
{
    @Autowired
    private ImsRegistry3rdFacade imsRegistry3rdFacade;

    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "add third party registry")
    @LNJoyAuditLog(value = "add third party registry",
            tags = ResourceOperationTypeConstants.RESOURCE_CREATE,
            actionDescriptionTemplateClass = Registry3rdActionDescriptionTemplate.class,
            isResourceInstanceNameFromArgs = true,
            resourceInstanceNameFromArgsParseSpEl = "#obj?.get('registryName')?.toString()",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.METHOD_RETURN_VALUE,
                            resourceIdParseSPEL = "#obj?.body?.toString()"))
    public ResponseEntity<String> addRegistry3rd(@ApiParam(required = true, name = "registry3rdReq") @RequestBody @Valid AddRegistry3rdReq registry3rdReq,
                                                 @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId,
                                                 @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
                                                 @RequestHeader(name = UserHeadInfo.USERNAME, required = false) String userName,
                                                 @RequestHeader(name = UserHeadInfo.BpName, required = false) String bpName,
                                                 @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
    {
        log.info("add third party registry. req: {}, userId: {}", registry3rdReq, userId);

        setBaseReq(registry3rdReq, userId, bpId, userName, bpName);

        return status(CREATED).body(imsRegistry3rdFacade.addRegistry3rd(registry3rdReq));
    }

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get third party registry project list")
    public ResponseEntity<Registries3rdRsp> getRegistry3rdList(@ApiParam(name = "registry_name") @RequestParam(value = "registry_name", required = false) String registryName,
                                                               @ApiParam(name = "page_size") @RequestParam(value = "page_size", required = false, defaultValue = Integer.MAX_VALUE + "") Integer pageSize,
                                                               @ApiParam(name = "page_num") @RequestParam(value = "page_num", required = false, defaultValue = "1") Integer pageNum,
                                                               @ApiParam(name = "user_id") @RequestParam(value = "user_id", required = false) String ownerUserId,
                                                               @ApiParam(name = "bp_id") @RequestParam(value = "bp_id", required = false) String ownerBpId,
                                                               @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId,
                                                               @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
                                                               @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
    {
        
        Registries3rdRsp res = Registries3rdRsp.builder().build();

        if (isRoleImsAdmin(authorities))
        {
            res = imsRegistry3rdFacade.getRegistry3rdList(registryName, ownerBpId, ownerUserId, pageNum, pageSize);
        }
        else if (isRoleImsTenantAdmin(authorities))
        {
            res = imsRegistry3rdFacade.getRegistry3rdList(registryName, bpId, ownerUserId, pageNum, pageSize);
        }
        else if (isRoleImsTenant(authorities))
        {
            res = imsRegistry3rdFacade.getRegistry3rdList(registryName, bpId, userId, pageNum, pageSize);
        }

        return status(OK).body(res);
    }


    @GetMapping(value = "/{registry_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get third party registry info")
    @LNJoyAuditLog(value = "get third party registry info",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                            bindParameterName = "registryId"))
    public ResponseEntity<ImsRegistry3rd> getRegistry3rd(@ApiParam(required = true, name = "registry_id") @PathVariable String registryId,
                                                         @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId,
                                                         @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
                                                         @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
    {
        
        ImsRegistry3rd res = null;

        if (isRoleImsAdmin(authorities))
        {
            res = imsRegistry3rdFacade.getRegistry3rd(registryId, null, null);
        }
        else if (isRoleImsTenantAdmin(authorities))
        {
            res = imsRegistry3rdFacade.getRegistry3rd(registryId, bpId, null);
        }
        else if (isRoleImsTenant(authorities))
        {
            res = imsRegistry3rdFacade.getRegistry3rd(registryId, bpId, userId);
        }

        return status(OK).body(res);
    }

    @PutMapping(value = "/{registry_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "update third party registry")
    @LNJoyAuditLog(value = "update third party registry",
            actionDescriptionTemplateClass = Registry3rdActionDescriptionTemplate.class,
            isResourceInstanceNameFromArgs = true,
            resourceInstanceNameFromArgsParseSpEl = "#obj?.get('registryName')?.toString()",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                            bindParameterName = "registryId"))
    public ResponseEntity<Map<String, String>> updateRegistry3rd(@ApiParam(required = true, name = "registry_id") @PathVariable("registry_id") String registryId,
                                                                 @ApiParam(required = true, name = "req") @RequestBody UpdateRegistry3rdReq req,
                                                                 @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId,
                                                                 @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
                                                                 @RequestHeader(name = UserHeadInfo.USERNAME, required = false) String userName,
                                                                 @RequestHeader(name = UserHeadInfo.BpName, required = false) String bpName,
                                                                 @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
    {
        
        setBaseReq(req, userId, bpId, userName, bpName);
        req.setRegistryId(registryId);
        imsRegistry3rdFacade.updateRegistry3rd(req);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @DeleteMapping(value = "/{registry_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "delete third party registry")
    @LNJoyAuditLog(value = "delete third party registry",
            resourceMapperId = ImsResources.THIRD_PARTY_REGISTRY,
            actionDescriptionTemplateClass = Registry3rdActionDescriptionTemplate.class,
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                            bindParameterName = "registryId"))
    public ResponseEntity<String> deleteRegistry3rd(@ApiParam(required = true, name = "registry_id") @PathVariable String registryId,
                                                    @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId,
                                                    @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
                                                    @RequestHeader(name = UserHeadInfo.USERNAME, required = false) String userName,
                                                    @RequestHeader(name = UserHeadInfo.BpName, required = false) String bpName,
                                                    @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
    {
        
        log.info("delete registry: {}, userId: {}", registryId, userId);
        imsRegistry3rdFacade.deleteRegistry3rd(registryId, userId, bpId, userName, bpName);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @PostMapping(value = "/{registry_id}/pre-download", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "pre download 3rd")
    @LNJoyAuditLog(value = "pre download 3rd",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                            bindParameterName = "registryId"))
    public ResponseEntity<String> preDownload(@ApiParam(required = true, name = "registry_id") @PathVariable("registry_id") String registryId,
                                              @ApiParam(required = true, name = "req") @Valid @RequestBody PreDownloadReq req,
                                              @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId,
                                              @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
                                              @RequestHeader(name = UserHeadInfo.USERNAME, required = false) String userName,
                                              @RequestHeader(name = UserHeadInfo.BpName, required = false) String bpName,
                                              @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
    {
        
        checkRoleImsAdmin(authorities);
        req.setRegistryId(registryId);
        setBaseReq(req, userId, bpId, userName, bpName);
        imsRegistry3rdFacade.preDownload(req);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @GetMapping(value = "/nodes", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get all nodes")
    public ResponseEntity<Registry3rdNodesRsp> getAllRegistryNodes(@ApiParam(required = false, name = "region_id") @RequestParam(value = "region_id", required = false) String regionId,
                                                                   @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId,
                                                                   @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
                                                                   @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
    {
        
        checkRoleImsAdmin(authorities);
        return ResponseEntity.status(HttpStatus.OK).body(imsRegistry3rdFacade.getAllRegistryNodes(regionId));
    }
}
