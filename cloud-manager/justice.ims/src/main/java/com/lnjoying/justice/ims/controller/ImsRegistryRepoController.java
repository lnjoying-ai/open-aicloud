package com.lnjoying.justice.ims.controller;

import com.lnjoying.justice.commonweb.handler.aspect.annotation.LNJoyAuditLog;
import com.lnjoying.justice.commonweb.handler.aspect.annotation.ResourceIdExtractConfiguration;
import com.lnjoying.justice.commonweb.handler.aspect.enums.ResourceIdLocationType;
import com.lnjoying.justice.ims.domain.dto.req.UpdateRegistryRepoReq;
import com.lnjoying.justice.ims.domain.dto.rsp.RegistryRepoBuildHistoryRsp;
import com.lnjoying.justice.ims.domain.dto.rsp.RegistryRepoTagsRsp;
import com.lnjoying.justice.ims.domain.dto.rsp.RegistryReposRsp;
import com.lnjoying.justice.ims.domain.model.ImsRegistryRepo;
import com.lnjoying.justice.ims.facade.ImsRegistryRepoFacade;
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

import static com.lnjoying.justice.commonweb.common.SwaggerConstants.*;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.ResponseEntity.status;

/**
 * repo handler
 *
 * @author merak
 **/

@RestSchema(schemaId = "ImsRegistryRepoController")
@RequestMapping(path = "/ims/v1/docker/registries/{registry_id}/projects/{project_id}/repositories")
@Slf4j
@ApiOperation(value = "", extensions = {@Extension(name = SWAGGER_X_RESOURCE,
        properties = {@ExtensionProperty(name = SWAGGER_RESOURCE_SINGULAR_NAME, value = "repository"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_PLURAL_NAME, value = "repositories"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_DESCRIPTION, value = "镜像仓库"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_MODEL, value = "ImsRegistryRepo")})})
public class ImsRegistryRepoController extends ImsWebController
{
    @Autowired
    private ImsRegistryRepoFacade imsRegistryRepoFacade;

    @PutMapping(value = "/{repo_name}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "update registry repo")
    @LNJoyAuditLog(value = "update registry repo",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                            bindParameterName = "repoName"))
    public ResponseEntity<String> updateRegistryRepo(@ApiParam(required = true, name = "registry_id") @PathVariable("registry_id") String registryId,
                                                     @ApiParam(required = true, name = "project_id") @PathVariable("project_id") String projectId,
                                                     @ApiParam(required = true, name = "repo_name") @PathVariable("repo_name") String repoName,
                                                     @ApiParam(required = true, name = "req") @RequestBody UpdateRegistryRepoReq req,
                                                     @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId,
                                                     @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
                                                     @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
    {

        log.info("update registry project. req: {}, userId: {}", req, userId);

        req.setRegistryId(registryId);
        req.setProjectId(projectId);
        req.setRepoName(repoName);
        if (isRoleImsTenant(authorities))
        {
            req.setBpId(bpId);
            req.setUserId(userId);
        }
        else if (isRoleImsTenantAdmin(authorities))
        {
            req.setBpId(bpId);
            req.setUserId(null);
        }
        imsRegistryRepoFacade.updateRegistryRepo(req);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get registry repo list")
    public ResponseEntity<RegistryReposRsp> getRegistryRepoList(@ApiParam(required = false, name = "registry_id") @PathVariable(value = "registry_id") String registryId,
                                                                @ApiParam(required = false, name = "project_id") @PathVariable(value = "project_id") String projectId,
                                                                @ApiParam(required = false, name = "repo_name") @RequestParam(value = "repo_name", required = false) String repoName,
                                                                @ApiParam(name = "page_size") @RequestParam(value = "page_size", required = false, defaultValue = Integer.MAX_VALUE + "") Integer pageSize,
                                                                @ApiParam(name = "page_num") @RequestParam(value = "page_num", required = false, defaultValue = "1") Integer pageNum,
                                                                @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId,
                                                                @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
                                                                @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
    {

        if (isRoleImsTenant(authorities))
        {
            RegistryReposRsp res = imsRegistryRepoFacade.getRepoList(registryId, projectId, repoName, pageNum, pageSize, bpId, userId);
            return status(OK).body(res);
        }
        else if (isRoleImsTenantAdmin(authorities))
        {
            RegistryReposRsp res = imsRegistryRepoFacade.getRepoList(registryId, projectId, repoName, pageNum, pageSize, bpId, null);
            return status(OK).body(res);
        }

        RegistryReposRsp res = imsRegistryRepoFacade.getRepoList(registryId, projectId, repoName, pageNum, pageSize, null, null);
        return status(OK).body(res);
    }

    @GetMapping(value = "/{repo_name}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get registry repo info")
    @LNJoyAuditLog(value = "get registry repo info",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                            bindParameterName = "repoName"))
    public ResponseEntity<ImsRegistryRepo> getRegistryRepo(@ApiParam(required = true, name = "registry_id") @PathVariable("registry_id") String registryId,
                                                           @ApiParam(required = true, name = "project_id") @PathVariable("project_id") String projectId,
                                                           @ApiParam(required = true, name = "repo_name") @PathVariable("repo_name") String repoName,
                                                           @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId,
                                                           @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
                                                           @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
    {

        if (isRoleImsTenant(authorities))
        {
            ImsRegistryRepo res = imsRegistryRepoFacade.getRegistryRepo(registryId, projectId, repoName, bpId, userId);
            return status(OK).body(res);
        }
        else if (isRoleImsTenantAdmin(authorities))
        {
            ImsRegistryRepo res = imsRegistryRepoFacade.getRegistryRepo(registryId, projectId, repoName, bpId, null);
            return status(OK).body(res);
        }

        ImsRegistryRepo res = imsRegistryRepoFacade.getRegistryRepo(registryId, projectId, repoName, null, null);
        return status(OK).body(res);
    }

    @GetMapping(value = "/{repo_name}/build-history", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get registry repo build history")
    @LNJoyAuditLog(value = "get registry repo build history",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                            bindParameterName = "repoName"))
    public ResponseEntity<String> buildHistory(@ApiParam(required = true, name = "registry_id") @PathVariable("registry_id") String registryId,
                                               @ApiParam(required = true, name = "project_id") @PathVariable("project_id") String projectId,
                                               @ApiParam(required = true, name = "repo_name") @PathVariable("repo_name") String repoName,
                                               @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId,
                                               @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
                                               @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
    {

        if (isRoleImsTenant(authorities))
        {
            RegistryRepoBuildHistoryRsp res = imsRegistryRepoFacade.buildHistory(registryId, projectId, repoName, bpId, userId);
            return status(OK).body(res.getBuildHistory());
        }
        else if (isRoleImsTenantAdmin(authorities))
        {
            RegistryRepoBuildHistoryRsp res = imsRegistryRepoFacade.buildHistory(registryId, projectId, repoName, bpId, null);
            return status(OK).body(res.getBuildHistory());
        }

        RegistryRepoBuildHistoryRsp res = imsRegistryRepoFacade.buildHistory(registryId, projectId, repoName, null, null);
        return status(OK).body(res.getBuildHistory());
    }

    @GetMapping(value = "/{repo_name}/tags", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get registry repo tags")
    @LNJoyAuditLog(value = "get registry repo tags",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                            bindParameterName = "repoName"))
    public ResponseEntity<RegistryRepoTagsRsp> tags(@ApiParam(required = true, name = "registry_id") @PathVariable("registry_id") String registryId,
                                                    @ApiParam(required = true, name = "project_id") @PathVariable("project_id") String projectId,
                                                    @ApiParam(required = true, name = "repo_name") @PathVariable("repo_name") String repoName,
                                                    @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId,
                                                    @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
                                                    @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
    {

        if (isRoleImsTenant(authorities))
        {
            RegistryRepoTagsRsp res = imsRegistryRepoFacade.tags(registryId, projectId, repoName, bpId, userId);
            return status(OK).body(res);
        }
        else if (isRoleImsTenantAdmin(authorities))
        {
            RegistryRepoTagsRsp res = imsRegistryRepoFacade.tags(registryId, projectId, repoName, bpId, null);
            return status(OK).body(res);
        }

        RegistryRepoTagsRsp res = imsRegistryRepoFacade.tags(registryId, projectId, repoName, null, null);
        return status(OK).body(res);
    }

    @DeleteMapping(value = "/{repo_name}/tags/{tag_name}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "delete registry repo tag")
    @LNJoyAuditLog(value = "delete registry repo tag",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                            bindParameterName = "repoName"))
    public ResponseEntity<String> deleteTag(@ApiParam(required = true, name = "registry_id") @PathVariable("registry_id") String registryId,
                                            @ApiParam(required = true, name = "project_id") @PathVariable("project_id") String projectId,
                                            @ApiParam(required = true, name = "repo_name") @PathVariable("repo_name") String repoName,
                                            @ApiParam(required = true, name = "tag_name") @PathVariable("tag_name") String tagName,
                                            @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId,
                                            @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
                                            @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
    {

        if (isRoleImsTenant(authorities))
        {
            imsRegistryRepoFacade.deleteTag(registryId, projectId, repoName, tagName, bpId, userId);
            return status(OK).body(null);
        }
        else if (isRoleImsTenantAdmin(authorities))
        {
            imsRegistryRepoFacade.deleteTag(registryId, projectId, repoName, tagName, bpId, null);
            return status(OK).body(null);
        }

        imsRegistryRepoFacade.deleteTag(registryId, projectId, repoName, tagName, null, null);
        return status(OK).body(null);
    }
}
