package com.lnjoying.justice.cmp.controller;

import com.lnjoying.justice.cmp.common.CmpResources;
import com.lnjoying.justice.cmp.config.DescriptionConfig;
import com.lnjoying.justice.cmp.domain.dto.request.AddCloudReq;
import com.lnjoying.justice.cmp.domain.dto.request.UpdateCloudReq;
import com.lnjoying.justice.cmp.domain.dto.request.ActionCloudReq;
import com.lnjoying.justice.cmp.domain.dto.response.CloudBasicInfoListRsp;
import com.lnjoying.justice.cmp.domain.dto.response.CloudInfoListRsp;
import com.lnjoying.justice.cmp.domain.model.CloudSearchCritical;
import com.lnjoying.justice.cmp.handler.actiondescription.i18n.zh_cn.CmpCloudActionDescriptionTemplate;
import com.lnjoying.justice.cmp.service.cloud.CloudService;
import com.lnjoying.justice.commonweb.controller.RestWebController;
import com.lnjoying.justice.commonweb.exception.WebSystemException;
import com.lnjoying.justice.commonweb.handler.aspect.annotation.LNJoyAuditLog;
import com.lnjoying.justice.commonweb.handler.aspect.annotation.ResourceIdExtractConfiguration;
import com.lnjoying.justice.commonweb.handler.aspect.enums.ResourceIdLocationType;
import com.lnjoying.justice.schema.constant.ResourceOperationTypeConstants;
import com.lnjoying.justice.schema.constant.UserHeadInfo;
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
import java.util.HashMap;
import java.util.Map;

import static com.lnjoying.justice.commonweb.common.SwaggerConstants.*;
import static com.lnjoying.justice.commonweb.util.ServiceCombRequestUtils.isAdmin;

@RestSchema(schemaId = "cloud")
@RequestMapping("/cmp/v1")
@Api(value = "Cloud Controller",tags = "Cloud Controller")
@ApiOperation(value = "", extensions = {@Extension(name = SWAGGER_X_RESOURCE,
        properties = {@ExtensionProperty(name = SWAGGER_RESOURCE_SINGULAR_NAME, value = "cloud"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_PLURAL_NAME, value = "clouds"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_DESCRIPTION, value = "云"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_MODEL, value = "TblCloudInfo")})})
public class CloudController extends RestWebController
{
    private static Logger LOGGER = LogManager.getLogger();

    @Autowired
    private CloudService cloudService;

    @PostMapping(value = "/clouds", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "add cloud",notes = DescriptionConfig.ADD_CLOUD_MSG)
    @LNJoyAuditLog(value = "add cloud",
            tags = ResourceOperationTypeConstants.RESOURCE_CREATE,
            resourceMapperId = CmpResources.CLOUD,
            actionDescriptionTemplate = CmpCloudActionDescriptionTemplate.Descriptions.ADD_CLOUD,
            associatedResourceCnName = "云",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.METHOD_RETURN_VALUE,
                            resourceIdParseSPEL = "#obj?.body?.get('id')?.toString()"))
    public ResponseEntity<Map<String,String>> addCloud(@ApiParam(value = "", required = true, name = "edgeInput")@RequestBody @Valid AddCloudReq addCloudReq,
                                                        @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId,
                                                        @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
                                                        @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
    {
        try
        {

            String cloudId = cloudService.addCloud(addCloudReq, bpId, userId);
            Map<String,String> retMap = new HashMap<>();
            retMap.put("id",cloudId);

            return ResponseEntity.status(HttpStatus.CREATED).body(retMap);
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("add cloud error. {}", e.getMessage());
            throw throwWebException(e);
        }
    }

    @PutMapping(value = "/clouds/{cloud_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "update cloud",notes = DescriptionConfig.UPDATE_CLOUD_MSG)
    @LNJoyAuditLog(value = "update cloud",
            resourceMapperId = CmpResources.CLOUD,
            actionDescriptionTemplate = CmpCloudActionDescriptionTemplate.Descriptions.UPDATE_CLOUD,
            associatedResourceCnName = "云",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                            bindParameterName = "cloudId"))
    public ResponseEntity<String>  updateCloud(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                              @ApiParam(value = "", required = true, name = "updateCloudReq") @RequestBody UpdateCloudReq updateCloudReq,
                                              @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId,
                                              @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
    {
        try
        {

            if (isAdmin() || cloudService.isOwner(cloudId, userId))
            {
                cloudService.updateCloud(cloudId, updateCloudReq, null);
            }else
            {
                cloudService.updateCloud(cloudId, updateCloudReq, userId);
            }

            return ResponseEntity.ok(null);
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("update cloud error. {}", e.getMessage());
            throw throwWebException(e);
        }
    }

    @PostMapping(value = "/clouds/{cloud_id}/action", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "action cloud",notes = DescriptionConfig.UPDATE_CLOUD_MSG)
    @LNJoyAuditLog(value = "action cloud",
            resourceMapperId = CmpResources.CLOUD,
            actionDescriptionTemplate = CmpCloudActionDescriptionTemplate.Descriptions.ACTION_CLOUD,
            associatedResourceCnName = "云",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                    bindParameterName = "cloudId"))
    public ResponseEntity<String> actionCloud(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                              @ApiParam(value = "", required = true, name = "actionCloudReq") @RequestBody ActionCloudReq actionCloudReq,
                                              @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId,
                                              @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
    {
        try
        {

            if (isAdmin() || cloudService.isOwner(cloudId, userId))
            {
                cloudService.actionCloud(cloudId, actionCloudReq, null);
            }
            else
            {
                cloudService.actionCloud(cloudId, actionCloudReq, userId);
            }

            return ResponseEntity.ok(null);
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("action cloud error. {}", e.getMessage());
            throw throwWebException(e);
        }
    }

    @DeleteMapping(value = "/clouds/{cloud_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "delete cloud",notes = DescriptionConfig.DELETE_CLOUD_MSG)
    @LNJoyAuditLog(value = "delete cloud",
            resourceMapperId = CmpResources.CLOUD,
            actionDescriptionTemplate = CmpCloudActionDescriptionTemplate.Descriptions.DELETE_CLOUD,
            associatedResourceCnName = "云",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                            bindParameterName = "cloudId"))
    public ResponseEntity<String> deleteCloud(@ApiParam(value = "", required = true, name = "cloud_id")@PathVariable(value = "cloud_id") String cloudId,
                                              @ApiParam(name = "force") @RequestParam(value = "force", required = false, defaultValue = "false") boolean force,
                                              @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId,
                                              @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
    {
        try
        {

            if (isAdmin() || cloudService.isOwner(cloudId, userId))
            {
                cloudService.deleteCloud(cloudId, force, null);
            }else
            {
                cloudService.deleteCloud(cloudId, force, userId);
            }


            return ResponseEntity.ok(null);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("delete cloud error. {}", e.getMessage());
            throw throwWebException(e);
        }
    }

    @GetMapping(value = "/clouds", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get cloud list",notes = DescriptionConfig.CLOUD_LIST_MSG)
    public ResponseEntity<CloudInfoListRsp> getClouds(@ApiParam(name = "name") @RequestParam(value = "name", required = false) String name,
                                                      @ApiParam(name = "status") @RequestParam(value = "status", required = false) Integer status,
                                                      @ApiParam(name = "region") @RequestParam(value = "region", required = false) String region,
                                                      @ApiParam(name = "site") @RequestParam(value = "site", required = false) String site,
                                                      @ApiParam(name = "product") @RequestParam(value = "product", required = false) String product,
                                                      @ApiParam(name = "page_size") @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                      @ApiParam(name = "page_num") @RequestParam(value = "pageNum", required = false) Integer pageNum,
                                                      @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId,
                                                      @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
                                                      @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities,
                                                      @RequestHeader(name = UserHeadInfo.USERKIND,    required = false) String userKind)
    {

        try
        {
            CloudSearchCritical cloudSearchCritical = new CloudSearchCritical();
            cloudSearchCritical.setName(name);
            cloudSearchCritical.setStatus(status);
            cloudSearchCritical.setRegion(region);
            cloudSearchCritical.setSite(site);
            cloudSearchCritical.setProduct(product);

            if (pageNum != null) cloudSearchCritical.setPageNum(pageNum);
            if (pageSize != null) cloudSearchCritical.setPageSize(pageSize);

            return ResponseEntity.ok().body(cloudService.getClouds(cloudSearchCritical));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("get clouds error. {}", e.getMessage());
            throw throwWebException(e);
        }
    }

    @GetMapping(value = "/clouds/basic-info", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get cloud basic info list",notes = DescriptionConfig.CLOUD_BASIC_INFO_MSG)
    public ResponseEntity<CloudBasicInfoListRsp> getCloudsBasicInfo(@RequestHeader(name = UserHeadInfo.USERID, required = false) String userId,
                                                                    @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
    {

        try
        {
            return ResponseEntity.ok().body(cloudService.getCloudsBasicInfo(userId));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("get clouds error. {}", e.getMessage());
            throw throwWebException(e);
        }
    }

    @GetMapping(value = "/clouds/ping", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "ping cloud url",notes = DescriptionConfig.PING_CLOUD_URL_MSG)
    public ResponseEntity<Map<String,Object>> pingCloudUrl(@ApiParam(name = "url") @RequestParam(value = "url", required = false) String url,
                                                           @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId,
                                                           @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
    {

        try
        {
            return ResponseEntity.ok().body(cloudService.pingCloudUrl(url));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("ping cloud error. {}", e.getMessage());
            throw throwWebException(e);
        }
    }

    @PostMapping(value = "/clouds/{cloud_id}/sync-data", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "sync cloud data",notes = DescriptionConfig.SYNC_CLOUD_DATA_MSG)
    @LNJoyAuditLog(value = "sync cloud data",
            resourceMapperId = CmpResources.CLOUD,
            actionDescriptionTemplate = CmpCloudActionDescriptionTemplate.Descriptions.SYNC_CLOUD,
            associatedResourceCnName = "云",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                            bindParameterName = "cloudId"))
    public ResponseEntity<Map<String,String>> syncCloudData(@ApiParam(value = "", required = true, name = "cloud_id")@PathVariable(value = "cloud_id") String cloudId,
                                                     @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId,
                                                     @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
    {

        try
        {
            return ResponseEntity.ok().body(cloudService.syncCloudData(cloudId));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("sync cloud data error. {}", e.getMessage());
            throw throwWebException(e);
        }
    }

}
