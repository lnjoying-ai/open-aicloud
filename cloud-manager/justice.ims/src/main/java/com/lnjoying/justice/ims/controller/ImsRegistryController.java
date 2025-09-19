package com.lnjoying.justice.ims.controller;

import com.lnjoying.justice.commonweb.handler.aspect.annotation.LNJoyAuditLog;
import com.lnjoying.justice.commonweb.handler.aspect.annotation.ResourceIdExtractConfiguration;
import com.lnjoying.justice.commonweb.handler.aspect.enums.ResourceIdLocationType;
import com.lnjoying.justice.ims.common.ImsResources;
import com.lnjoying.justice.ims.domain.dto.req.*;
import com.lnjoying.justice.ims.domain.dto.rsp.*;
import com.lnjoying.justice.ims.domain.model.ImsRegistry;
import com.lnjoying.justice.ims.domain.model.ImsRegistryTenant;
import com.lnjoying.justice.ims.domain.model.RegistryRegion;
import com.lnjoying.justice.ims.facade.ImsRegistryFacade;
import com.lnjoying.justice.ims.handler.actiondescription.RegistryActionDescriptionTemplate;
import com.lnjoying.justice.schema.constant.ResourceOperationTypeConstants;
import com.lnjoying.justice.schema.constant.UserHeadInfo;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Extension;
import io.swagger.annotations.ExtensionProperty;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.util.*;
import java.util.stream.Collectors;

import static com.lnjoying.justice.commonweb.common.SwaggerConstants.*;
import static com.lnjoying.justice.commonweb.util.ServiceCombRequestUtils.*;
import static com.lnjoying.justice.ims.db.model.TblImsRegistry.RegistryStatus.ENABLE;
import static com.lnjoying.justice.ims.db.model.TblImsRegistry.RegistryStatus.otherStatus;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.ResponseEntity.status;

/**
 * registry handler
 *
 * @author merak
 **/

@RestSchema(schemaId = "imsRegistryController")
@RequestMapping(path = "/ims/v1/docker/registries")
@Slf4j
@ApiOperation(value = "", extensions = {@Extension(name = SWAGGER_X_RESOURCE,
        properties = {@ExtensionProperty(name = SWAGGER_RESOURCE_SINGULAR_NAME, value = "registry"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_PLURAL_NAME, value = "registries"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_DESCRIPTION, value = "仓库"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_MODEL, value = "TblImsRegistry")})})
public class ImsRegistryController extends ImsWebController
{

    @Autowired
    private ImsRegistryFacade imsRegistryFacade;

    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "add registry")
    @LNJoyAuditLog(value = "add registry",
            tags = ResourceOperationTypeConstants.RESOURCE_CREATE,
            actionDescriptionTemplateClass = RegistryActionDescriptionTemplate.class,
            isResourceInstanceNameFromArgs = true,
            resourceInstanceNameFromArgsParseSpEl = "#obj?.get('registryName')?.toString()",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.METHOD_RETURN_VALUE,
                            resourceIdParseSPEL = "#obj?.body?.registryId?.toString()"))
    public ResponseEntity<RegistryInstallInfoRsp> addRegistry(@ApiParam(required = true, name = "registryReq") @RequestBody @Valid AddRegistryReq registryReq,
                                                              @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId,
                                                              @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
                                                              @RequestHeader(name = UserHeadInfo.USERNAME, required = false) String userName,
                                                              @RequestHeader(name = UserHeadInfo.BpName, required = false) String bpName,
                                                              @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
    {
        log.info("add registry. req: {}, userId: {}", registryReq, userId);

        registryReq.setBpId(bpId);
        registryReq.setUserId(userId);
        registryReq.setUserName(userName);
        registryReq.setBpName(bpName);
        return status(CREATED).body(imsRegistryFacade.addRegistry(registryReq));
    }


    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get registry list")
    public ResponseEntity<Object> getRegistryList(@ApiParam(name = "registry_name") @RequestParam(value = "registry_name", required = false) String registryName,
                                                  @ApiParam(name = "region_id") @RequestParam(value = "region_id", required = false) String regionId,
                                                  @ApiParam(name = "region_name") @RequestParam(value = "region_name", required = false) String regionName,
                                                  @ApiParam(name = "page_size") @RequestParam(value = "page_size", required = false, defaultValue = Integer.MAX_VALUE + "") Integer pageSize,
                                                  @ApiParam(name = "page_num") @RequestParam(value = "page_num", required = false, defaultValue = "1") Integer pageNum,
                                                  @ApiParam(name = "user_id") @RequestParam(value = "user_id", required = false) String ownerUserId,
                                                  @ApiParam(name = "status") @RequestParam(value = "status", required = false) Integer status,
                                                  @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId,
                                                  @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
    {

        Collection<Integer> statusCollection = validStatus(status);

        RegistriesRsp registryList = imsRegistryFacade.getRegistryList(registryName, regionId, regionName, ownerUserId, statusCollection, pageSize, pageNum);

        if (isAdmin())
        {
            return status(OK).body(registryList);
        }

        List<ImsRegistryTenant> tenantList = registryList.getRegistries().stream().map(ImsRegistryTenant::of).collect(Collectors.toList());
        RegistriesTenantRsp tenantRsp = RegistriesTenantRsp.builder().totalNum(registryList.getTotalNum()).registries(tenantList).build();
        return status(OK).body(tenantRsp);
    }

    @GetMapping(value = "/{registry_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get registry info")
    @LNJoyAuditLog(value = "get registry info",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                            bindParameterName = "registryId"))
    public ResponseEntity<ImsRegistry> getRegistry(@ApiParam(required = true, name = "registry_id") @PathVariable String registryId,
                                                   @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId,
                                                   @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
    {

        checkRoleImsAdmin(authorities);
        return status(OK).body(imsRegistryFacade.getRegistry(registryId, null));

    }

    @PutMapping(value = "/{registry_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "update registry")
    @LNJoyAuditLog(value = "update registry",
            resourceMapperId = ImsResources.REGISTRY,
            actionDescriptionTemplateClass = RegistryActionDescriptionTemplate.class,
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                            bindParameterName = "registryId"))
    public ResponseEntity<Map<String, String>> updateRegistry(@ApiParam(required = true, name = "registry_id") @PathVariable String registryId,
                                                              @ApiParam(required = true, name = "req") @RequestBody @Valid UpdateRegistryReq req,
                                                              @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId,
                                                              @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
                                                              @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
    {

        log.info("update registry. req: {}, userId: {}", req, userId);

        // update userId and bpId
        checkRoleImsAdmin(authorities);
        req.setUserId(userId);
        req.setBpId(bpId);
        imsRegistryFacade.updateRegistry(registryId, req);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @DeleteMapping(value = "/{registry_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "delete registry")
    @LNJoyAuditLog(value = "delete registry",
            resourceMapperId = ImsResources.REGISTRY,
            actionDescriptionTemplateClass = RegistryActionDescriptionTemplate.class,
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                            bindParameterName = "registryId"))
    public ResponseEntity<String> deleteRegistry(@ApiParam(required = true, name = "registry_id") @PathVariable String registryId,
                                                 @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId,
                                                 @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
                                                 @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
    {

        log.info("delete registry: {}, userId: {}", registryId, userId);
        checkRoleImsAdmin(authorities);
        imsRegistryFacade.deleteRegistry(registryId, userId, bpId);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @PutMapping(value = "/{registry_id}/password", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "change password")
    @LNJoyAuditLog(value = "change password",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                    bindParameterName = "registryId"))
    public ResponseEntity<String> changePassword(@ApiParam(required = true, name = "registry_id") @PathVariable String registryId,
                                                 @ApiParam(required = true, name = "req") @RequestBody RegistryPasswordReq req,
                                                 @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId,
                                                 @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
                                                 @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
    {

        checkRoleImsAdmin(authorities);

        req.setUserId(userId);
        req.setBpId(bpId);
        req.setRegistryId(registryId);
        imsRegistryFacade.changePassword(req);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @PostMapping(value = "/{registry_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "enable registry or disable registry")
    @LNJoyAuditLog(value = "enable registry or disable registry",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                            bindParameterName = "registryId"))
    public ResponseEntity<String> action(@ApiParam(required = true, name = "registry_id") @PathVariable String registryId,
                                         @ApiParam(required = true, name = "action") @RequestParam(required = true) @Valid @Pattern(regexp = "(?i)deactive|(?i)active") String action,
                                         @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId,
                                         @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
                                         @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
    {

        checkRoleImsAdmin(authorities);

        imsRegistryFacade.action(registryId, userId, bpId, action);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }


    @GetMapping(value = "/{registry_id}/repos", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get all repos")
    @LNJoyAuditLog(value = "get all repos",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                            bindParameterName = "registryId"))
    public ResponseEntity<RegistryImagesRsp> getAllRegistryRepos(@ApiParam(required = true, name = "registry_id") @PathVariable("registry_id") String registryId,
                                                                 @ApiParam(required = false, name = "project_id") @RequestParam(required = false, value = "project_id") String projectId,
                                                                 @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId,
                                                                 @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
                                                                 @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
    {

        checkRoleImsAdmin(authorities);
        if (StringUtils.isNotBlank(projectId))
        {
            return ResponseEntity.status(HttpStatus.OK).body(imsRegistryFacade.getAllRegistryReposByProjectId(registryId, projectId));
        }
        return ResponseEntity.status(HttpStatus.OK).body(imsRegistryFacade.getAllRegistryRepos(registryId));
    }

    @GetMapping(value = "/{registry_id}/nodes", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get all nodes")
    @LNJoyAuditLog(value = "get all nodes",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                            bindParameterName = "registryId"))
    public ResponseEntity<RegistryNodesRsp> getAllRegistryNodes(@ApiParam(required = true, name = "registry_id") @PathVariable("registry_id") String registryId,
                                                                @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId,
                                                                @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
                                                                @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
    {

        checkRoleImsAdmin(authorities);
        return ResponseEntity.status(HttpStatus.OK).body(imsRegistryFacade.getAllRegistryNodes(registryId));
    }

    @PostMapping(value = "/{registry_id}/pre-download", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "pre download")
    @LNJoyAuditLog(value = "pre download",
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
        imsRegistryFacade.preDownload(req);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @PostMapping(value = "/command", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "command")
    public ResponseEntity<String> command(@ApiParam(required = true, name = "req") @RequestBody TenantCommandReq req,
                                          @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId,
                                          @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
    {

        req.setUserId(userId);
        String command = imsRegistryFacade.command(req);
        return ResponseEntity.status(HttpStatus.OK).body(command);
    }

    @GetMapping(value = "/repos", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "search repos")
    public ResponseEntity<RegistryReposRsp> searchRepos(@ApiParam(required = true, name = "repo_name") @RequestParam(value="repo_name") String repoName,
                                                        @ApiParam(name = "page_size") @RequestParam(value = "page_size", required = false, defaultValue = Integer.MAX_VALUE + "") Integer pageSize,
                                                        @ApiParam(name = "page_num") @RequestParam(value = "page_num", required = false, defaultValue = "1") Integer pageNum,
                                                        @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId,
                                                        @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
                                                        @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
    {

        RegistryReposRsp rsp = RegistryReposRsp.builder().build();
        if (isRoleImsAdmin(authorities))
        {
            rsp = imsRegistryFacade.searchRepos(repoName, pageNum, pageSize, null, null);
        }
        else if (isRoleImsTenantAdmin(authorities))
        {
            rsp = imsRegistryFacade.searchRepos(repoName, pageNum, pageSize, bpId, userId);
        }
        else if (isRoleImsTenant(authorities))
        {
            rsp = imsRegistryFacade.searchRepos(repoName, pageNum, pageSize, bpId, userId);
        }
        return ResponseEntity.status(HttpStatus.OK).body(rsp);
    }

    @GetMapping(value = "/regions", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get regions of all registries mappings")
    public ResponseEntity<List<RegistryRegion>> getRegistryRegions(@RequestHeader(name = UserHeadInfo.USERID, required = false) String userId,
                                                                   @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
    {

        return ResponseEntity.status(HttpStatus.OK).body(imsRegistryFacade.getRegistryRegions());
    }

    @GetMapping(value = "/pre-downloads", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "pre-downloads")
    public ResponseEntity<ImageDownloadInfoRsp> preDownloads(@ApiParam(name = "registry_id") @RequestParam(value = "registry_id", required = false) String registryId,
                                                             @ApiParam(name = "image_name") @RequestParam(value = "image_name", required = false) String imageName,
                                                             @ApiParam(name = "node_id") @RequestParam(value = "node_id", required = false) String nodeId,
                                                             @ApiParam(name = "page_size") @RequestParam(value = "page_size", required = false, defaultValue = Integer.MAX_VALUE + "") Integer pageSize,
                                                             @ApiParam(name = "page_num") @RequestParam(value = "page_num", required = false, defaultValue = "1") Integer pageNum,
                                                             @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId,
                                                             @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
    {

        checkRoleImsAdmin(authorities);
        return ResponseEntity.status(HttpStatus.OK).body(imsRegistryFacade.getPreDownloads(registryId, imageName, nodeId, queryBpId(), queryUserId(), pageNum, pageSize));
    }

    @DeleteMapping(value = "/pre-downloads", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "delete pre-downloads")
    public ResponseEntity<ImageDownloadInfoRsp> deletePreDownloads(@ApiParam(name = "job_ids") @RequestParam(value = "job_ids", required = true) String jobIds,
                                                             @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId,
                                                             @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
    {

        checkRoleImsAdmin(authorities);
        imsRegistryFacade.deletePreDownloads(jobIds);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @DeleteMapping(value = "/pre-downloads/jobs", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "delete pre-downloads")
    public ResponseEntity<ImageDownloadInfoRsp> deletePreDownloadsJobs(@ApiParam(name = "ids") @RequestParam(value = "ids", required = true) String ids,
                                                                   @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId,
                                                                   @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
    {

        log.info("delete pre-downloads ids: {}, userId:{}", ids, userId);
        checkRoleImsAdmin(authorities);
        imsRegistryFacade.deletePreDownloadsJobs(ids);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    private Collection<Integer> validStatus(Integer status)
    {
       List<Integer> statusCollection = new ArrayList<>();
        if (Objects.nonNull(status))
        {
            switch (status.intValue())
            {
                // status 1 ---> enable
                case 1:
                case 6:
                    statusCollection.add(ENABLE.value());
                    break;
                // status 0 ---> States other than enable deleted
                case 0:
                    statusCollection.addAll(otherStatus());
                    break;
                //  status 8 ---> all status exclude deleted
                case 8:
                default:    
                    break;
            }

        }
        
        return statusCollection;
    }
}
