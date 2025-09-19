package com.lnjoying.justice.cmp.controller.nextstack.baremetal;

import com.lnjoying.justice.cmp.common.CmpResources;
import com.lnjoying.justice.cmp.config.DescriptionConfig;
import com.lnjoying.justice.cmp.db.model.TblCmpBaremetalInstanceKey;
import com.lnjoying.justice.cmp.domain.dto.request.nextstack.baremetal.CommonReq;
import com.lnjoying.justice.cmp.domain.dto.request.nextstack.baremetal.ResetBaremetalInstanceReq;
import com.lnjoying.justice.cmp.entity.search.nextstack.baremetal.BaremetalInstanceSearchCritical;
import com.lnjoying.justice.cmp.domain.dto.request.nextstack.baremetal.BaremetalInstanceCreateReq;
import com.lnjoying.justice.cmp.domain.dto.response.nextstack.baremetal.BaremetalInstanceDetailInfoRsp;
import com.lnjoying.justice.cmp.domain.dto.response.nextstack.baremetal.BaremetalInstancesRsp;
import com.lnjoying.justice.cmp.domain.dto.response.nextstack.baremetal.InstanceStatsRsp;
import com.lnjoying.justice.cmp.handler.actiondescription.i18n.zh_cn.CmpNextStackActionDescriptionTemplate;
import com.lnjoying.justice.cmp.service.cloud.CloudService;
import com.lnjoying.justice.cmp.service.nextstack.baremetal.BaremetalComputeService;
import com.lnjoying.justice.commonweb.controller.RestWebController;
import com.lnjoying.justice.commonweb.exception.WebSystemException;
import com.lnjoying.justice.commonweb.handler.aspect.annotation.LNJoyAuditLog;
import com.lnjoying.justice.commonweb.handler.aspect.annotation.ResourceIdExtractConfiguration;
import com.lnjoying.justice.commonweb.handler.aspect.enums.ResourceIdLocationType;
import com.lnjoying.justice.schema.constant.UserHeadInfo;
import io.swagger.annotations.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

import static com.lnjoying.justice.commonweb.common.SwaggerConstants.*;
import static com.lnjoying.justice.commonweb.util.ServiceCombRequestUtils.isAdmin;

@RestSchema(schemaId = "baremetalinstance")
@RequestMapping("/cmp/v1/{vendor}/clouds/{cloud_id}/api/bm/v1")
@Api(value = "Baremetal Instacne Controller",tags = "Baremetal Instacne Controller")
@ApiOperation(value = "", extensions = {@Extension(name = SWAGGER_X_RESOURCE,
        properties = {@ExtensionProperty(name = SWAGGER_RESOURCE_SINGULAR_NAME, value = "ns_baremetal_instance"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_PLURAL_NAME, value = "ns_baremetal_instances"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_DESCRIPTION, value = "裸金属"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_MODEL, value = "TblCmpBaremetalInstance")})})
public class BaremetalInstanceController extends RestWebController
{
    private static Logger LOGGER = LogManager.getLogger();

    @Autowired
    private BaremetalComputeService baremetalComputeService;

    @Autowired
    private CloudService cloudService;

    @PostMapping(value = "/baremetal_instances", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "add baremetal instance", response = Object.class, notes = DescriptionConfig.ADD_BAREMETAL_DEVICE)
    @LNJoyAuditLog(value = "add baremetal instance",
            resourceMapperId = CmpResources.NS_BAREMETAL_INSTANCE,
            actionDescriptionTemplate = CmpNextStackActionDescriptionTemplate.Descriptions.ADD_BAREMETAL_INSTANCE,
            associatedResourceCnName = "裸金属",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.METHOD_RETURN_VALUE,
                    resourceIdParseSPEL = "#obj?.body?.get('instanceId')?.toString()", resourcePrimaryKeyClass = TblCmpBaremetalInstanceKey.class,
                    convertToResourcePrimaryKeyTypeSpEl = "new com.lnjoying.justice.cmp.db.model.TblCmpBaremetalInstanceKey(#RESOURCE_POOL_ID,#RESOURCE_INSTANCE_ID)"))
    public ResponseEntity<Object> addBaremetalInstance(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                       @ApiParam(value = "BaremetalInstanceCreateReq", required = true, name = "BaremetalInstanceCreateReq") @RequestBody BaremetalInstanceCreateReq request,
                                                       @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
                                                       @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {
            LOGGER.info("add baremetal instance, cloud:{}, request:{}, userId:{}", cloudId, request, userId);

            if (! isAdmin() && ! cloudService.isOwner(cloudId, userId))
            {
                cloudService.checkCloudStatus(cloudId, false);
            }

            return baremetalComputeService.addBaremetalInstance(cloudId, request, bpId, userId);
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("add baremetal instance failed: {}", e.getMessage());
            e.printStackTrace();
            throw throwWebException(e);
        }
    }

    @GetMapping(value = "/baremetal_instances", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get baremetal instances", response = Object.class, notes = DescriptionConfig.GET_BAREMETAL_DEVICES)
    public ResponseEntity<BaremetalInstancesRsp> getBaremetalInstances(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                                       @ApiParam(value = "name", required = false, name = "name") @RequestParam(required = false) String name,
                                                                       @ApiParam(name = "page_size") @RequestParam(required = false, value = "page_size") Integer pageSize,
                                                                       @ApiParam(name = "port_id_is_null") @RequestParam(required = false, value = "port_id_is_null") Boolean portIdIsNull,
                                                                       @ApiParam(name = "subnet_id") @RequestParam(required = false, value = "subnet_id") String subnetId,
                                                                       @ApiParam(name = "eipMap_is_using") @RequestParam(required = false, value = "eipMap_is_using") Boolean eipMapIsUsing,
                                                                       @ApiParam(name = "page_num") @RequestParam(required = false, value = "page_num") Integer pageNum,
                                                                       @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {
            LOGGER.info("get baremetal instances, cloud:{}", cloudId);

            String filterUserId = userId;
            if (isAdmin() || cloudService.isOwner(cloudId, userId))
            {
                filterUserId = null;
            }
            else
            {
                cloudService.checkCloudStatus(cloudId, true);
            }

            BaremetalInstanceSearchCritical critical = new BaremetalInstanceSearchCritical();
            if (null != name) critical.setName(name);
            if (null != pageSize) critical.setPageSize(pageSize);
            if (null != pageNum) critical.setPageNum(pageNum);
            if (null != portIdIsNull) critical.setPortIdIsNull(portIdIsNull);
            if (null != subnetId) critical.setSubnetId(subnetId);
            if (null != eipMapIsUsing) critical.setEipMapIsUsing(eipMapIsUsing);

            BaremetalInstancesRsp baremetalInstancesRsp = baremetalComputeService.getBaremetalInstances(cloudId, critical, filterUserId);

            return ResponseEntity.ok().body(baremetalInstancesRsp);
        }
        catch (Exception e)
        {
            LOGGER.error("get baremetal instances failed, message:{}", e.getMessage());
            e.printStackTrace();
            throw throwWebException(e);
        }
    }

    @GetMapping(value = "/baremetal_instances/{instanceId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get baremetal instance detail info", response = Object.class, notes = DescriptionConfig.GET_BAREMETAL_DEVICE)
    public ResponseEntity<BaremetalInstanceDetailInfoRsp> getBaremetalInstance(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                                               @ApiParam(value = "instanceId", required = true, name = "instanceId") @PathVariable("instanceId") String instanceId,
                                                                               @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {
            LOGGER.info("get baremetal instance detail info, cloud:{}, instance:{}", cloudId, instanceId);

            String operUserId = userId;
            if (isAdmin() || cloudService.isOwner(cloudId, userId))
            {
                operUserId = null;
            }
            else
            {
                cloudService.checkCloudStatus(cloudId, true);
            }

            BaremetalInstanceDetailInfoRsp baremetalInstanceDetailInfoRsp = baremetalComputeService.getBaremetalInstance(cloudId, instanceId, operUserId);

            return ResponseEntity.ok().body(baremetalInstanceDetailInfoRsp);
        }
        catch (Exception e)
        {
            LOGGER.error("get baremetal instance failed: instance id {}, {}",instanceId, e.getMessage());
            e.printStackTrace();
            throw throwWebException(e);
        }
    }

    @DeleteMapping(value = "/baremetal_instances/{instanceId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "remove instance by instance id", response = Object.class, notes = DescriptionConfig.DEL_BAREMETAL_INSTANCE)
    @LNJoyAuditLog(value = "remove instance by instance id",
            resourceMapperId = CmpResources.NS_BAREMETAL_INSTANCE,
            actionDescriptionTemplate = CmpNextStackActionDescriptionTemplate.Descriptions.DELETE_BAREMETAL_INSTANCE,
            associatedResourceCnName = "裸金属",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                    bindParameterName = "instanceId", resourcePrimaryKeyClass = TblCmpBaremetalInstanceKey.class,
                    convertToResourcePrimaryKeyTypeSpEl = "new com.lnjoying.justice.cmp.db.model.TblCmpBaremetalInstanceKey(#RESOURCE_POOL_ID,#RESOURCE_INSTANCE_ID)"))
    public ResponseEntity<Object> reomveBaremetalInstance(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                          @ApiParam(value = "instanceId", required = true, name = "instanceId") @PathVariable("instanceId") String instanceId,
                                                          @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {
            LOGGER.info("delete baremetal instance, cloud:{}, instance:{}", cloudId, instanceId);

            String operUserId = userId;
            if (isAdmin() || cloudService.isOwner(cloudId, operUserId))
            {
                operUserId = null;
            }
            else
            {
                cloudService.checkCloudStatus(cloudId, false);
            }

            return baremetalComputeService.removeBaremetalInstance(cloudId, instanceId, operUserId);
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("delete baremetal instance failed: instance id {}, {}", instanceId, e.getMessage());
            e.printStackTrace();
            throw throwWebException(e);
        }
    }

    @PostMapping(value = "/baremetal_instances/{instanceId}/poweroff", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "poweroff instance by instance id", response = Object.class)
    @LNJoyAuditLog(value = "poweroff instance by instance id",
            resourceMapperId = CmpResources.NS_BAREMETAL_INSTANCE,
            actionDescriptionTemplate = CmpNextStackActionDescriptionTemplate.Descriptions.POWEROFF_BAREMETAL_INSTANCE,
            associatedResourceCnName = "裸金属",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                    bindParameterName = "instanceId", resourcePrimaryKeyClass = TblCmpBaremetalInstanceKey.class,
                    convertToResourcePrimaryKeyTypeSpEl = "new com.lnjoying.justice.cmp.db.model.TblCmpBaremetalInstanceKey(#RESOURCE_POOL_ID,#RESOURCE_INSTANCE_ID)"))
    public ResponseEntity<Object> poweroffBaremetalInstance(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                            @ApiParam(value = "instanceId", required = true, name = "instanceId") @PathVariable("instanceId") String instanceId,
                                                            @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {
            LOGGER.info("poweroff baremetal instance: instance id {}", instanceId);

            if (! isAdmin() && ! cloudService.isOwner(cloudId, userId))
            {
                cloudService.checkCloudStatus(cloudId, false);
                baremetalComputeService.checkBaremetalInstanceUser(cloudId, instanceId, userId);
            }

            return cloudService.sendHttpRequestToCloud(cloudId);
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("poweroff baremetal instance failed: instance id {}, {}", instanceId, e.getMessage());
            throw throwWebException(e);
        }
    }

    @PostMapping(value = "/baremetal_instances/{instanceId}/poweron", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "poweron instance by instance id", response = Object.class)
    @LNJoyAuditLog(value = "poweron instance by instance id",
            resourceMapperId = CmpResources.NS_BAREMETAL_INSTANCE,
            actionDescriptionTemplate = CmpNextStackActionDescriptionTemplate.Descriptions.POWERON_BAREMETAL_INSTANCE,
            associatedResourceCnName = "裸金属",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                    bindParameterName = "instanceId", resourcePrimaryKeyClass = TblCmpBaremetalInstanceKey.class,
                    convertToResourcePrimaryKeyTypeSpEl = "new com.lnjoying.justice.cmp.db.model.TblCmpBaremetalInstanceKey(#RESOURCE_POOL_ID,#RESOURCE_INSTANCE_ID)"))
    public ResponseEntity<Object> poweronBaremetalInstance(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                           @ApiParam(value = "instanceId", required = true, name = "instanceId") @PathVariable("instanceId") String instanceId,
                                                           @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {
            LOGGER.info("poweron baremetal instance: instance id {}", instanceId);

            if (! isAdmin() && ! cloudService.isOwner(cloudId, userId))
            {
                cloudService.checkCloudStatus(cloudId, false);
                baremetalComputeService.checkBaremetalInstanceUser(cloudId, instanceId, userId);
            }

            return cloudService.sendHttpRequestToCloud(cloudId);
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("poweron baremetal instance failed: instance id {}, {}", instanceId, e.getMessage());
            throw throwWebException(e);
        }
    }

    @PostMapping(value = "/baremetal_instances/{instanceId}/reboot", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "reboot instance by instance id", response = Object.class)
    @LNJoyAuditLog(value = "reboot instance by instance id",
            resourceMapperId = CmpResources.NS_BAREMETAL_INSTANCE,
            actionDescriptionTemplate = CmpNextStackActionDescriptionTemplate.Descriptions.REBOOT_BAREMETAL_INSTANCE,
            associatedResourceCnName = "裸金属",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                    bindParameterName = "instanceId", resourcePrimaryKeyClass = TblCmpBaremetalInstanceKey.class,
                    convertToResourcePrimaryKeyTypeSpEl = "new com.lnjoying.justice.cmp.db.model.TblCmpBaremetalInstanceKey(#RESOURCE_POOL_ID,#RESOURCE_INSTANCE_ID)"))
    public ResponseEntity<Object> rebootBaremetalInstance(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                          @ApiParam(value = "instanceId", required = true, name = "instanceId") @PathVariable("instanceId") String instanceId,
                                                          @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {
            LOGGER.info("reboot baremetal instance: instance id {}", instanceId);

            if (! isAdmin() && ! cloudService.isOwner(cloudId, userId))
            {
                cloudService.checkCloudStatus(cloudId, false);
                baremetalComputeService.checkBaremetalInstanceUser(cloudId, instanceId, userId);
            }

            return cloudService.sendHttpRequestToCloud(cloudId);
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("reboot baremetal instance failed: instance id {}, {}", instanceId, e.getMessage());
            throw throwWebException(e);
        }
    }

    @PutMapping(value = "/baremetal_instances/{instanceId}/reset", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "reset instance by instance id", response = Object.class)
    @LNJoyAuditLog(value = "reset instance by instance id",
            resourceMapperId = CmpResources.NS_BAREMETAL_INSTANCE,
            actionDescriptionTemplate = CmpNextStackActionDescriptionTemplate.Descriptions.RESET_BAREMETAL_INSTANCE,
            associatedResourceCnName = "裸金属",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                    bindParameterName = "instanceId", resourcePrimaryKeyClass = TblCmpBaremetalInstanceKey.class,
                    convertToResourcePrimaryKeyTypeSpEl = "new com.lnjoying.justice.cmp.db.model.TblCmpBaremetalInstanceKey(#RESOURCE_POOL_ID,#RESOURCE_INSTANCE_ID)"))
    public ResponseEntity<Object> resetBaremetalInstance(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                         @ApiParam(value = "instanceId", required = true, name = "instanceId") @PathVariable("instanceId") String instanceId,
                                                         @ApiParam(value = "ResetBaremetalInstanceReq", required = true, name = "ResetBaremetalInstanceReq") @RequestBody ResetBaremetalInstanceReq request,
                                                         @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {
            LOGGER.info("reset baremetal instance: instance id: {} resetRequest: {}", instanceId, request);

            if (! isAdmin() && ! cloudService.isOwner(cloudId, userId))
            {
                cloudService.checkCloudStatus(cloudId, false);
                baremetalComputeService.checkBaremetalInstanceUser(cloudId, instanceId, userId);
            }

            return cloudService.sendHttpRequestToCloud(cloudId);
        }
        catch (Exception e)
        {
            LOGGER.error("reset baremetal instance failed: instance id {}, {}", instanceId, e.getMessage());

            throw throwWebException(e);
        }
    }

    @PutMapping(value = "/baremetal_instances/{instanceId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "update instance", response = Object.class)
    @LNJoyAuditLog(value = "reset instance by instance id",
            resourceMapperId = CmpResources.NS_BAREMETAL_INSTANCE,
            actionDescriptionTemplate = CmpNextStackActionDescriptionTemplate.Descriptions.UPDATE_BAREMETAL_INSTANCE,
            associatedResourceCnName = "裸金属",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                    bindParameterName = "instanceId", resourcePrimaryKeyClass = TblCmpBaremetalInstanceKey.class,
                    convertToResourcePrimaryKeyTypeSpEl = "new com.lnjoying.justice.cmp.db.model.TblCmpBaremetalInstanceKey(#RESOURCE_POOL_ID,#RESOURCE_INSTANCE_ID)"))
    public ResponseEntity<Object> updateBaremetalInstance(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                          @ApiParam(value = "instanceId", required = true, name = "instanceId") @PathVariable("instanceId") String instanceId,
                                                          @ApiParam(value = "CommonReq", required = true, name = "CommonReq") @RequestBody CommonReq request,
                                                          @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId) throws WebSystemException
    {
        try
        {
            LOGGER.debug("put baremetal instance: {}, userId:{}", request, userId);

            if (! isAdmin() && ! cloudService.isOwner(cloudId, userId))
            {
                cloudService.checkCloudStatus(cloudId, false);
                baremetalComputeService.checkBaremetalInstanceUser(cloudId, instanceId, userId);
            }

            return cloudService.sendHttpRequestToCloud(cloudId);
        }
        catch (Exception e)
        {
            LOGGER.error("update baremetal instance error: {}, instanceId: {}",e.getMessage(), instanceId);
            throw  throwWebException(e);
        }
    }

    @GetMapping(value = "/bm_stats", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get user baremetal stats", response = Object.class)
    public ResponseEntity<InstanceStatsRsp> getBmStats(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                       @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId) throws IOException
    {
        try
        {
            LOGGER.info("get user baremetal stats, cloud:{}, userId:{}", cloudId, userId);

            String operUserId = userId;
            if (isAdmin() || cloudService.isOwner(cloudId, operUserId))
            {
                operUserId = null;
            }
            else
            {
                cloudService.checkCloudStatus(cloudId, false);
            }

            return ResponseEntity.ok(baremetalComputeService.getInstanceStats(cloudId, operUserId));
        }
        catch (Exception e)
        {
            LOGGER.error("get all storage size failed: {}", e.getMessage());
            throw throwWebException(e);
        }
    }
}
