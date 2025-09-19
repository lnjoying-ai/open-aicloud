package com.lnjoying.justice.aos.controller;

import com.lnjoying.justice.aos.config.DescriptionConfig;
import com.lnjoying.justice.aos.db.model.TblHelmStackInfo;
import com.lnjoying.justice.aos.domain.dto.req.AddHelmRepoReq;
import com.lnjoying.justice.aos.domain.dto.req.ImportChartReq;
import com.lnjoying.justice.aos.domain.dto.req.InstallStackReq;
import com.lnjoying.justice.aos.domain.dto.rsp.ChartRsp;
import com.lnjoying.justice.aos.domain.dto.rsp.ChartVersionFilesRsp;
import com.lnjoying.justice.aos.domain.dto.rsp.RepoRsp;
import com.lnjoying.justice.aos.domain.dto.rsp.StackRsp;
import com.lnjoying.justice.aos.domain.model.helm.*;
import com.lnjoying.justice.aos.facade.HelmFacade;
import com.lnjoying.justice.commonweb.handler.aspect.annotation.FunctionRegisterConfiguration;
import com.lnjoying.justice.commonweb.handler.aspect.annotation.LNJoyAuditLog;
import com.lnjoying.justice.commonweb.handler.aspect.annotation.ResourceIdExtractConfiguration;
import com.lnjoying.justice.commonweb.handler.aspect.enums.ResourceIdLocationType;
import com.lnjoying.justice.schema.constant.ResourceOperationTypeConstants;
import com.lnjoying.justice.schema.constant.UserHeadInfo;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.List;

import static com.lnjoying.justice.commonweb.common.SwaggerConstants.*;
import static com.lnjoying.justice.commonweb.util.ServiceCombRequestUtils.*;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.ResponseEntity.status;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2022/8/5 11:07
 */

@Controller
@RestSchema(schemaId = "HelmController")
@RequestMapping("/aos/v1/helm")
@Api(value = "helm Controller",tags = {"helm Controller"})
@Slf4j
@ApiOperation(value = "", extensions = {@Extension(name = SWAGGER_X_RESOURCE,
        properties = {@ExtensionProperty(name = SWAGGER_RESOURCE_SINGULAR_NAME, value = "helm"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_PLURAL_NAME, value = "helms"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_DESCRIPTION, value = "helm应用市场"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_MODEL, value = "tbl_repo_info")})})
public class HelmController
{
    @Autowired
    private HelmFacade helmFacade;

    @PostMapping(value = "/repos", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "add repo", notes = DescriptionConfig.ADD_STACK_MSG)
    @LNJoyAuditLog(value = "add repo",
            tags = ResourceOperationTypeConstants.RESOURCE_CREATE,
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.METHOD_RETURN_VALUE,
                            resourceIdParseSPEL = "#obj?.body?.repoName"))
    public ResponseEntity<Object> addRepo(@ApiParam(required = true, name = "helmRepoReq") @RequestBody @Valid AddHelmRepoReq helmRepoReq,
                                          @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId,
                                          @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
                                          @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
    {
        log.info("add repo: {}, userId: {}", helmRepoReq, userId);

        return status(CREATED).body(helmFacade.addRepo(helmRepoReq));
    }

    @DeleteMapping(value = "/repos/{repo_name}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "delete repo")
    @LNJoyAuditLog(value = "delete repo",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                            bindParameterName = "repoName"))
    public ResponseEntity<String> deleteRepo(@ApiParam(required = true, name = "repo_name") @PathVariable("repo_name") String repoName,
                                                 @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId,
                                                 @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
                                                 @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
    {
        log.info("delete repo: {}, userId: {}", repoName, userId);
        checkRoleTenantAdminOrAdmin();

        helmFacade.deleteRepo(repoName, userId, bpId);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @GetMapping(value = "/repos", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get repo list")
    public ResponseEntity<Object> getRepoList(@ApiParam(name = "repo_name") @RequestParam(value = "repo_name", required = false) String repoName,
                                                  @ApiParam(name = "page_size") @RequestParam(value = "page_size",
                                                          required = false, defaultValue = Integer.MAX_VALUE + "") Integer pageSize,
                                                  @ApiParam(name = "page_num") @RequestParam(value = "page_num", required = false, defaultValue = "1") Integer pageNum,
                                                  @ApiParam(name = "status") @RequestParam(value = "status", required = false) Integer status,
                                                  @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId,
                                                  @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
    {
        
        RepoRsp repoList = helmFacade.getRepoList(repoName, status, pageNum, pageSize);

        return status(OK).body(repoList);
    }

    @GetMapping(value = "/repos/{repo_name}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get repo info")
    @LNJoyAuditLog(value = "get repo info",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                            bindParameterName = "repoName"))
    public ResponseEntity<RepoInfo> getRepo(@ApiParam(required = true, name = "repo_name") @PathVariable("repo_name") String repoName,
                                                @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId,
                                                @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
    {
        
        RepoInfo repo = helmFacade.getRepo(repoName);
        return status(OK).body(repo);
    }

    @GetMapping(value = "/charts", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get chart list")
    public ResponseEntity<Object> getChartList(@ApiParam(name = "repo_name") @RequestParam(value = "repo_name", required = false) String repoName,
                                               @ApiParam(name = "app_name") @RequestParam(value = "app_name", required = false) String appName,
                                               @ApiParam(name = "category") @RequestParam(value = "category", required = false) String category,
                                              @ApiParam(name = "page_size") @RequestParam(value = "page_size",
                                                      required = false, defaultValue = Integer.MAX_VALUE + "") Integer pageSize,
                                              @ApiParam(name = "page_num") @RequestParam(value = "page_num", required = false, defaultValue = "1") Integer pageNum,
                                               @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId,
                                               @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
                                               @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
    {
        
        ChartRsp chartList = helmFacade.getChartList(repoName, appName, category, pageNum, pageSize);

        return status(OK).body(chartList);
    }

    @GetMapping(value = "/charts/{app_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get chart")
    @LNJoyAuditLog(value = "get chart",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                            bindParameterName = "appId"))
    public ResponseEntity<Object> getChart(@ApiParam(name = "app_id") @PathVariable("app_id") String appId,
                                           @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId,
                                           @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
                                           @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
    {
        
        ChartInfo chartInfo = helmFacade.getChart(appId);
        return status(OK).body(chartInfo);
    }

    @DeleteMapping(value = "/charts/{app_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "delete chart")
    @LNJoyAuditLog(value = "delete chart",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                            bindParameterName = "appId"))
    public ResponseEntity<String> deleteChart(@ApiParam(required = true, name = "app_id") @PathVariable("app_id") String appId,
                                             @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId,
                                             @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
                                             @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
    {
        log.info("delete chart: {}, userId: {}", appId, userId);
        checkRoleTenantAdminOrAdmin();

        helmFacade.deleteChart(appId, userId, bpId);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @GetMapping(value = "/charts/stats", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get chart stats")
    public ResponseEntity<Object> getChartStats(@ApiParam(name = "repo_name") @RequestParam(value = "repo_name", required = false) String repoName,
                                               @ApiParam(name = "category") @RequestParam(value = "category", required = false) String category,
                                                @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId,
                                                @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
                                                @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
    {
        
        List<CategoryStatsInfo> statsInfo = helmFacade.getChartStats(repoName, category);

        return status(OK).body(statsInfo);
    }

    @GetMapping(value = "/charts/{app_id}/versions", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get chart versions")
    @LNJoyAuditLog(value = "get chart versions",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                            bindParameterName = "appId"))
    public ResponseEntity<Object> getChartVersions(@ApiParam(name = "app_id") @PathVariable("app_id") String appId,
                                                   @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId,
                                                   @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
                                                   @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
    {
        
        List<ChartVersionInfo> chartVersions = helmFacade.getChartVersions(appId);
        return status(OK).body(chartVersions);
    }

    @GetMapping(value = "/charts/{app_id}/versions/{version}/files", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get chart files")
    @LNJoyAuditLog(value = "get chart files",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                            bindParameterName = "appId"))
    public ResponseEntity<Object> getChartVersionFiles(@ApiParam(name = "app_id") @PathVariable("app_id") String appId,
                                                       @ApiParam(name = "version") @PathVariable("version") String version,
                                                   @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId,
                                                   @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
                                                   @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
    {
        
        ChartVersionFilesRsp chartVersionFiles = helmFacade.getChartVersionFiles(appId, version);
        return status(OK).body(chartVersionFiles);
    }


    @PostMapping(value = "/stacks", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "install stack", notes = DescriptionConfig.ADD_STACK_MSG)
    public ResponseEntity<Object> installStack(@ApiParam(required = true, name = "installStackReq") @RequestBody @Valid InstallStackReq installStackReq,
                                          @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId,
                                          @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
                                          @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
    {
        log.info("install stack req: {}, userId: {}", installStackReq, userId);
        

        if (StringUtils.isBlank(installStackReq.getUserId()))
        {
            if (isAdmin())
            {
                installStackReq.setUserId(getUserId());
                installStackReq.setUserName(getUserName());
            }
            else
            {
                installStackReq.setUserId(getUserId());
                installStackReq.setUserName(getUserName());
                installStackReq.setBpId(getBpId());
                installStackReq.setBpName(getBpName());
            }

        }
        return status(CREATED).body(helmFacade.installStack(installStackReq));
    }

    @GetMapping(value = "/stacks", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get stack list")
    public ResponseEntity<Object> getStackList(@ApiParam(name = "stack_name") @RequestParam(value = "stack_name", required = false) String stackName,
                                               @ApiParam(name = "cluster_id") @RequestParam(value = "cluster_id", required = false) String clusterId,
                                               @ApiParam(name = "category") @RequestParam(value = "category", required = false) String category,
                                               @ApiParam(name = "page_size") @RequestParam(value = "page_size",
                                                       required = false, defaultValue = Integer.MAX_VALUE + "") Integer pageSize,
                                               @ApiParam(name = "page_num") @RequestParam(value = "page_num",
                                                       required = false, defaultValue = "1") Integer pageNum,
                                               @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId,
                                               @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
                                               @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
    {
        
        StackRsp stackRsp = helmFacade.getStackList(stackName, clusterId, category, queryBpId(), queryUserId(),pageNum, pageSize);

        return status(OK).body(stackRsp);
    }


    @GetMapping(value = "/stacks/{stack_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get chart list")
    @LNJoyAuditLog(value = "get chart list",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                            bindParameterName = "stackId"))
    public ResponseEntity<Object> getStack(@ApiParam(name = "stack_id") @PathVariable("stack_id") String stackId,
                                           @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId,
                                           @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
                                           @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
    {
        
        HelmStackInfo stackInfo = helmFacade.getStack(stackId);
        return status(OK).body(stackInfo);
    }

    @DeleteMapping(value = "/stacks/{stack_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "delete stack")
    @LNJoyAuditLog(value = "delete stack",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                            bindParameterName = "stackId"))
    public ResponseEntity<String> deleteStack(@ApiParam(required = true, name = "stack_id") @PathVariable("stack_id") String stackId,
                                             @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId,
                                             @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
                                             @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
    {
        log.info("delete stack: {}, userId: {}", stackId, userId);

        helmFacade.deleteStack(stackId, userId, bpId);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @PostMapping(value = "/stacks/{stack_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "action stack")
    @LNJoyAuditLog(value = "action stack",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                    bindParameterName = "stackId"),
            functionRegisterConfig = @FunctionRegisterConfiguration(functionId = "toActionType",
                    functionClass = TblHelmStackInfo.ActionType.class,
                    functionMethodName = "fromValue", parameterTypes = {int.class}),
            actionDescriptionField = "action",
            actionDescriptionValueSpEl = "#toActionType(#obj)?.name()",
            actionDescriptionFormatString = "stack action: %s")
    public ResponseEntity<String> ctrStack(@ApiParam(required = true, name = "stack_id") @PathVariable("stack_id") String stackId,
                                           @ApiParam(value = "", required = true, name = "action")@RequestParam(value = "action") int action,
                                              @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId,
                                              @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
                                              @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
    {
        log.info("action stack: {}, userId: {}, action: {}", stackId, userId, action);

        helmFacade.ctrStack(stackId, action);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }



    @PostMapping(value = "/charts", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "import charts", notes = DescriptionConfig.ADD_STACK_MSG)
    public ResponseEntity<Object> importCharts(@ApiParam(required = true, name = "helmRepoReq") @RequestBody @Valid ImportChartReq importChartReq,
                                               @ApiParam(name = "repo_name") @RequestParam(value = "repo_name", required = false) String repoName,
                                               @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId,
                                               @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
                                               @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
    {
        log.info("import charts, userId: {}", userId);

        helmFacade.importCharts(repoName, importChartReq);
        return status(CREATED).body(null);
    }
}
