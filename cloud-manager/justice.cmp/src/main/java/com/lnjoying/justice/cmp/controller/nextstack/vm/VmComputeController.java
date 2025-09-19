package com.lnjoying.justice.cmp.controller.nextstack.vm;

import com.lnjoying.justice.cmp.common.CmpResources;
import com.lnjoying.justice.cmp.common.DataSyncLevel;
import com.lnjoying.justice.cmp.common.SyncMsg;
import com.lnjoying.justice.cmp.db.model.TblCmpVmInstanceKey;
import com.lnjoying.justice.cmp.domain.dto.request.nextstack.vm.*;
import com.lnjoying.justice.cmp.domain.dto.response.nextstack.vm.SnapsTreeRsp;
import com.lnjoying.justice.cmp.entity.search.nextstack.vm.VmInstanceSearchCritical;
import com.lnjoying.justice.cmp.domain.dto.response.nextstack.vm.VmInstanceDetailInfoRsp;
import com.lnjoying.justice.cmp.domain.dto.response.nextstack.vm.VmInstancesRsp;
import com.lnjoying.justice.cmp.handler.actiondescription.i18n.zh_cn.CmpNextStackActionDescriptionTemplate;
import com.lnjoying.justice.cmp.service.nextstack.vm.VmInstanceServiceBiz;
import com.lnjoying.justice.cmp.service.cloud.CloudService;
import com.lnjoying.justice.cmp.service.nextstack.vm.VmSnapServiceBiz;
import com.lnjoying.justice.commonweb.controller.RestWebController;
import com.lnjoying.justice.commonweb.exception.WebSystemException;
import com.lnjoying.justice.commonweb.handler.aspect.annotation.LNJoyAuditLog;
import com.lnjoying.justice.commonweb.handler.aspect.annotation.ResourceIdExtractConfiguration;
import com.lnjoying.justice.commonweb.handler.aspect.enums.ResourceIdLocationType;
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

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

import static com.lnjoying.justice.commonweb.common.SwaggerConstants.*;
import static com.lnjoying.justice.commonweb.util.ServiceCombRequestUtils.isAdmin;

@RestSchema(schemaId = "vminstance")
@RequestMapping("/cmp/v1/{vendor}/clouds/{cloud_id}/api/vm/v1")
@Api(value = "Virtual Instance Controller",tags = "Virtual Instance Controller")
@ApiOperation(value = "", extensions = {@Extension(name = SWAGGER_X_RESOURCE,
        properties = {@ExtensionProperty(name = SWAGGER_RESOURCE_SINGULAR_NAME, value = "ns_vm_instance"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_PLURAL_NAME, value = "ns_vm_instances"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_DESCRIPTION, value = "虚拟机"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_MODEL, value = "TblCmpVmInstance")})})
public class VmComputeController extends RestWebController
{
    private static Logger LOGGER = LogManager.getLogger();

    @Autowired
    private VmInstanceServiceBiz vmComputeService;

    @Autowired
    private CloudService cloudService;

    @Autowired
    private VmSnapServiceBiz vmSnapService;

    @PostMapping(value = "/instances", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "create vm instance", response = Object.class)
    @LNJoyAuditLog(value = "create vm instance",
            tags = ResourceOperationTypeConstants.RESOURCE_CREATE,
            resourceMapperId = CmpResources.NS_VM_INSTANCE,
            actionDescriptionTemplate = CmpNextStackActionDescriptionTemplate.Descriptions.ADD_VM_INSTANCE,
            associatedResourceCnName = "虚拟机",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.METHOD_RETURN_VALUE,
                    resourceIdParseSPEL = "#obj?.body.get('instanceId')?.toString()", resourcePrimaryKeyClass = TblCmpVmInstanceKey.class,
                    convertToResourcePrimaryKeyTypeSpEl = "new com.lnjoying.justice.cmp.db.model.TblCmpVmInstanceKey(#RESOURCE_POOL_ID,#RESOURCE_INSTANCE_ID)"))
    public ResponseEntity<Object> createVmInstance(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                   @ApiParam(value = "VmInstanceCreateReq", required = true, name = "VmInstanceCreateReq") @RequestBody @Valid VmInstanceCreateReq request,
                                                   @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
                                                   @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)throws WebSystemException
    {
        try
        {
            LOGGER.info("create vm instance, cloud:{}, request:{}, userId:{}", cloudId, request, userId);

            if (! isAdmin() && ! cloudService.isOwner(cloudId, userId))
            {
                cloudService.checkCloudStatus(cloudId, false);
            }

            return vmComputeService.addVmInstance(cloudId, request, bpId, userId);
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("create vm instance error: {}", e.getMessage());

            e.printStackTrace();
            throw throwWebException(e);
        }
    }

    @PostMapping(value = "/instances/counts", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "create vm instances", response = Object.class)
    @LNJoyAuditLog(value = "create vm instance counts",
            tags = ResourceOperationTypeConstants.RESOURCE_CREATE,
            resourceMapperId = CmpResources.NS_VM_INSTANCE,
            actionDescriptionTemplate = CmpNextStackActionDescriptionTemplate.Descriptions.ADD_VM_INSTANCE_COUNT,
            associatedResourceCnName = "虚拟机",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.NONE))
    public ResponseEntity<String> createVmInstances(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                    @ApiParam(value = "VmInstancesCreateReq", required = true, name = "VmInstancesCreateReq") @RequestBody @Valid VmInstancesCreateReq request,
                                                    @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
                                                    @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId) throws WebSystemException
    {
        try
        {
            LOGGER.info("create vm instances, request:{}, userId:{}", request, userId);

            if (! isAdmin() && ! cloudService.isOwner(cloudId, userId))
            {
                cloudService.checkCloudStatus(cloudId, false);
            }

            return vmComputeService.addVmInstances(cloudId, request, bpId, userId);
        }
        catch (Exception e)
        {
            LOGGER.error("create vm instance error: {}", e.getMessage());
            throw throwWebException(e);
        }
    }

    @PostMapping(value = "/instances/renews", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "renew vm instance", response = Object.class)
    @LNJoyAuditLog(value = "create vm instance",
            tags = ResourceOperationTypeConstants.RESOURCE_CREATE,
            resourceMapperId = CmpResources.NS_VM_INSTANCE,
            actionDescriptionTemplate = CmpNextStackActionDescriptionTemplate.Descriptions.RENEW_VM_INSTANCE,
            associatedResourceCnName = "虚拟机",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.METHOD_RETURN_VALUE,
                    resourceIdParseSPEL = "#obj?.body.get('instanceId')?.toString()", resourcePrimaryKeyClass = TblCmpVmInstanceKey.class,
                    convertToResourcePrimaryKeyTypeSpEl = "new com.lnjoying.justice.cmp.db.model.TblCmpVmInstanceKey(#RESOURCE_POOL_ID,#RESOURCE_INSTANCE_ID)"))
    public ResponseEntity<Object> renewVmInstance(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                  @ApiParam(value = "VmInstanceRenewReq", required = true, name = "VmInstanceRenewReq") @RequestBody @Valid VmInstanceRenewReq request,
                                                  @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
                                                  @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId) throws WebSystemException
    {
        try
        {
            LOGGER.info("create vm instance, request:{}, userId:{}", request, userId);

            if (! isAdmin() && ! cloudService.isOwner(cloudId, userId))
            {
                cloudService.checkCloudStatus(cloudId, false);
            }

            return ResponseEntity.status(HttpStatus.CREATED).body(vmComputeService.addVmInstance(cloudId, request, bpId, userId));
        }
        catch (Exception e)
        {
            LOGGER.error("create vm instance error: {}", e.getMessage());
            throw throwWebException(e);
        }
    }

    @PostMapping(value = "/instances/{instanceId}/bound_sgs", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "bound sgs", response = Object.class)
    @LNJoyAuditLog(value = "bound sgs",
            resourceMapperId = CmpResources.NS_VM_INSTANCE,
            actionDescriptionTemplate = CmpNextStackActionDescriptionTemplate.Descriptions.BOUND_SECURITY_GROUP_TO_VM_INSTANCE,
            associatedResourceCnName = "虚拟机",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                    bindParameterName = "vmInstanceId", resourcePrimaryKeyClass = TblCmpVmInstanceKey.class,
                    convertToResourcePrimaryKeyTypeSpEl = "new com.lnjoying.justice.cmp.db.model.TblCmpVmInstanceKey(#RESOURCE_POOL_ID,#RESOURCE_INSTANCE_ID)"))
    public ResponseEntity<Object> boundSgs(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                           @ApiParam(value = "SgsUpdateReq", required = true, name = "SgsUpdateReq") @RequestBody SgsUpdateReq request,
                                           @ApiParam(value = "instanceId", required = true, name = "instanceId") @PathVariable("instanceId") String vmInstanceId,
                                           @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
                                           @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)  throws WebSystemException
    {
        try
        {
            LOGGER.info("bound security groups, cloud:{}, vmInstanceId:{}, userId:{}, sgIds:{}", cloudId, vmInstanceId, userId, request.getSgIds());

            if (! isAdmin() && ! cloudService.isOwner(cloudId, userId))
            {
                cloudService.checkCloudStatus(cloudId, false);
                vmComputeService.checkVmInstanceUser(cloudId, vmInstanceId, userId);
                vmComputeService.checkVmInstanceStatus(cloudId, vmInstanceId);
            }

            return cloudService.sendHttpRequestToCloud(cloudId);
        }
        catch (Exception e)
        {
            LOGGER.error("bound security groups error: {}, vmId:{}, sgIds:{}", e.getMessage(), vmInstanceId, request.getSgIds());

            e.printStackTrace();
            throw throwWebException(e);
        }
    }

    @PutMapping(value = "/instances/{instanceId}/bound_sgs", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "update sgs", response = Object.class)
    @LNJoyAuditLog(value = "update sgs",
            resourceMapperId = CmpResources.NS_VM_INSTANCE,
            actionDescriptionTemplate = CmpNextStackActionDescriptionTemplate.Descriptions.UPDATE_BOUND_SECURITY_GROUP,
            associatedResourceCnName = "虚拟机",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                    bindParameterName = "vmInstanceId", resourcePrimaryKeyClass = TblCmpVmInstanceKey.class,
                    convertToResourcePrimaryKeyTypeSpEl = "new com.lnjoying.justice.cmp.db.model.TblCmpVmInstanceKey(#RESOURCE_POOL_ID,#RESOURCE_INSTANCE_ID)"))
    public ResponseEntity<Object> updateBoundSgs(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                 @ApiParam(value = "UpdateSgsReq", required = true, name = "UpdateSgsReq") @RequestBody @Valid SgsUpdateReq request,
                                                 @ApiParam(value = "instanceId", required = true, name = "instanceId") @PathVariable("instanceId") String vmInstanceId,
                                                 @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId) throws WebSystemException
    {
        try
        {
            LOGGER.info("update security groups, vmInstanceId:{}, userId:{}, sgIds:{}", vmInstanceId, userId, request.getSgIds());

            if (! isAdmin() && ! cloudService.isOwner(cloudId, userId))
            {
                cloudService.checkCloudStatus(cloudId, false);
                vmComputeService.checkVmInstanceUser(cloudId, vmInstanceId, userId);
                vmComputeService.checkVmInstanceStatus(cloudId, vmInstanceId);
            }

            return cloudService.sendHttpRequestToCloud(cloudId);
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("update security groups error: {}, vmId:{}, sgIds:{}", e.getMessage(), vmInstanceId, request.getSgIds());
            throw throwWebException(e);
        }
    }

    @GetMapping(value = "/instances/{instanceId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get vm instance detail info", response = Object.class)
    public ResponseEntity<VmInstanceDetailInfoRsp> getVmInstanceDetailInfo(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                                           @ApiParam(value = "instanceId", required = true, name = "instanceId") @PathVariable("instanceId") String vmInstanceId,
                                                                           @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId) throws WebSystemException
    {
        try
        {
            LOGGER.info("get vm instance, cloud:{}, request:{}, userId:{}",cloudId, vmInstanceId, userId);

            String operUserId = userId;
            if (isAdmin() || cloudService.isOwner(cloudId, userId))
            {
                operUserId = null;
            }
            else
            {
                cloudService.checkCloudStatus(cloudId, true);
            }

            VmInstanceDetailInfoRsp vmDetailInfo = vmComputeService.getVmInstance(cloudId, vmInstanceId, operUserId);
            return ResponseEntity.ok(vmDetailInfo);
        }
        catch (Exception e)
        {
            LOGGER.error("get vm instance error: {}, vmInstanceId: {}",e.getMessage(), vmInstanceId);
            e.printStackTrace();
            throw  throwWebException(e);
        }
    }

    @PutMapping(value = "/instances/{instanceId}/poweroff", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "power off instance by instance id", response = Object.class)
    @LNJoyAuditLog(value = "power off instance by instance id",
            resourceMapperId = CmpResources.NS_VM_INSTANCE,
            actionDescriptionTemplate = CmpNextStackActionDescriptionTemplate.Descriptions.POWEROFF_VM_INSTANCE,
            associatedResourceCnName = "虚拟机",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                    bindParameterName = "instanceId", resourcePrimaryKeyClass = TblCmpVmInstanceKey.class,
                    convertToResourcePrimaryKeyTypeSpEl = "new com.lnjoying.justice.cmp.db.model.TblCmpVmInstanceKey(#RESOURCE_POOL_ID,#RESOURCE_INSTANCE_ID)"))
    public ResponseEntity<Object> poweroffInstance(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                   @ApiParam(value = "instanceId", required = true, name = "instanceId") @PathVariable("instanceId") String instanceId,
                                                   @ApiParam(name = "detachment") @RequestParam(required = false) Boolean detachment,
                                                   @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {
            LOGGER.info("power off vm instance, request:{}, userId:{}",instanceId, userId);

            if (! isAdmin() && ! cloudService.isOwner(cloudId, userId))
            {
                cloudService.checkCloudStatus(cloudId, false);
                vmComputeService.checkVmInstanceUser(cloudId, instanceId, userId);
                vmComputeService.checkVmInstanceStatus(cloudId, instanceId);
            }

            return vmComputeService.poweroffInstance(cloudId, instanceId, detachment);
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("power off vm instance error: {}, vmInstanceId: {}",e.getMessage(), instanceId);
            e.printStackTrace();
            throw  throwWebException(e);
        }
    }

    @PutMapping(value = "/instances/{instanceId}/poweron", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "power on instance by instance id", response = Object.class)
    @LNJoyAuditLog(value = "power on instance by instance id",
            resourceMapperId = CmpResources.NS_VM_INSTANCE,
            actionDescriptionTemplate = CmpNextStackActionDescriptionTemplate.Descriptions.POWERON_VM_INSTANCE,
            associatedResourceCnName = "虚拟机",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                    bindParameterName = "instanceId", resourcePrimaryKeyClass = TblCmpVmInstanceKey.class,
                    convertToResourcePrimaryKeyTypeSpEl = "new com.lnjoying.justice.cmp.db.model.TblCmpVmInstanceKey(#RESOURCE_POOL_ID,#RESOURCE_INSTANCE_ID)"))
    public ResponseEntity<Object> poweronInstance(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                  @ApiParam(value = "instanceId", required = true, name = "instanceId") @PathVariable("instanceId") String instanceId,
                                                  @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {
            LOGGER.info("power on vm instance, request:{}, userId:{}",instanceId, userId);

            if (! isAdmin() && ! cloudService.isOwner(cloudId, userId))
            {
                cloudService.checkCloudStatus(cloudId, false);
                vmComputeService.checkVmInstanceUser(cloudId, instanceId, userId);
                vmComputeService.checkVmInstanceStatus(cloudId, instanceId);
            }

            return vmComputeService.poweronInstance(cloudId, instanceId);
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("power on vm instance error: {}, vmInstanceId: {}",e.getMessage(), instanceId);
            e.printStackTrace();
            throw  throwWebException(e);
        }
    }

    @PutMapping(value = "/instances/{instanceId}/reboot", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "restart instance by instance id", response = Object.class)
    @LNJoyAuditLog(value = "restart instance by instance id",
            resourceMapperId = CmpResources.NS_VM_INSTANCE,
            actionDescriptionTemplate = CmpNextStackActionDescriptionTemplate.Descriptions.REBOOT_VM_INSTANCE,
            associatedResourceCnName = "虚拟机",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                    bindParameterName = "instanceId", resourcePrimaryKeyClass = TblCmpVmInstanceKey.class,
                    convertToResourcePrimaryKeyTypeSpEl = "new com.lnjoying.justice.cmp.db.model.TblCmpVmInstanceKey(#RESOURCE_POOL_ID,#RESOURCE_INSTANCE_ID)"))
    public ResponseEntity<Object> restartInstance(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                  @ApiParam(value = "instanceId", required = true, name = "instanceId") @PathVariable("instanceId") String instanceId,
                                                  @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {
            LOGGER.info("restart vm instance, request:{}, userId:{}", instanceId, userId);

            if (! isAdmin() && ! cloudService.isOwner(cloudId, userId))
            {
                cloudService.checkCloudStatus(cloudId, false);
                vmComputeService.checkVmInstanceUser(cloudId, instanceId, userId);
                vmComputeService.checkVmInstanceStatus(cloudId, instanceId);
            }

            return vmComputeService.restartInstance(cloudId, instanceId);
        }
        catch (Exception e)
        {
            LOGGER.error("restart vm instance error: {}, vmInstanceId: {}", e.getMessage(), instanceId);
            throw throwWebException(e);
        }
    }

    @GetMapping(value = "/instances", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get instances", response = Object.class)
    public ResponseEntity<VmInstancesRsp> getInstances(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                       @ApiParam(name = "page_size") @RequestParam(required = false, value = "page_size") Integer pageSize,
                                                       @ApiParam(name = "page_num") @RequestParam(required = false, value = "page_num") Integer pageNum,
                                                       @ApiParam(name = "name") @RequestParam(required = false) String name,
                                                       @ApiParam(name = "uuid") @RequestParam(required = false) String vmInstanceId,
                                                       @ApiParam(name = "subnet_id") @RequestParam(required = false, value = "subnet_id") String subnetId,
                                                       @ApiParam(name = "node_id") @RequestParam(required = false, value = "node_id") String nodeId,
                                                       @ApiParam(name = "port_id_is_null") @RequestParam(required = false, value = "port_id_is_null") Boolean portIdIsNull,
                                                       @ApiParam(name = "instance_group_id") @RequestParam(required = false, value = "instance_group_id") String instanceGroupId,
                                                       @ApiParam(name = "instance_group_id_is_null") @RequestParam(required = false, value = "instance_group_id_is_null") Boolean instanceGroupIdIsNull,
                                                       @ApiParam(name = "eipMap_is_using") @RequestParam(required = false, value = "eipMap_is_using") Boolean eipMapIsUsing,
                                                       @ApiParam(name = "eip_id_is_null") @RequestParam(required = false, value = "eip_id_is_null") Boolean eipIdIsNull,
                                                       @ApiParam(name = "ignore_failed") @RequestParam(required = false, value = "ignore_failed") Boolean ignoreFailed,
                                                       @ApiParam(name = "eip_id") @RequestParam(required = false, value = "eip_id") String eipId,
                                                       @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId) throws WebSystemException
    {
        try
        {
            LOGGER.info("get vm instance list, cloud:{}", cloudId);

            String filterUserId = userId;
            if (isAdmin() || cloudService.isOwner(cloudId, userId))
            {
                filterUserId = null;
            }
            else
            {
                cloudService.checkCloudStatus(cloudId, true);
            }

            VmInstanceSearchCritical pageSearchCritical = new VmInstanceSearchCritical();
            pageSearchCritical.setName(name);
            if (pageNum != null) pageSearchCritical.setPageNum(pageNum);
            if (pageSize != null) pageSearchCritical.setPageSize(pageSize);
            if (portIdIsNull != null) pageSearchCritical.setPortIdIsNull(portIdIsNull);
            if (eipMapIsUsing != null) pageSearchCritical.setEipMapIsUsing(eipMapIsUsing);
            if (null != instanceGroupId) pageSearchCritical.setInstanceGroupId(instanceGroupId);
            if (null != instanceGroupIdIsNull) pageSearchCritical.setInstanceGroupIdIsNull(instanceGroupIdIsNull);
            if (null != eipIdIsNull) pageSearchCritical.setEipIdIsNull(eipIdIsNull);
            if (!StringUtils.isBlank(eipId)) pageSearchCritical.setEipId(eipId);
            if (!StringUtils.isBlank(subnetId)) pageSearchCritical.setSubnetId(subnetId);
            if (!StringUtils.isBlank(vmInstanceId)) pageSearchCritical.setVmInstanceId(vmInstanceId);
            if (!StringUtils.isBlank(nodeId)) pageSearchCritical.setNodeId(nodeId);
            if (null != ignoreFailed) pageSearchCritical.setIgnoreFailed(ignoreFailed);
            VmInstancesRsp getVmInstancesRsp = vmComputeService.getVmInfos(cloudId, pageSearchCritical, filterUserId);
            return ResponseEntity.ok(getVmInstancesRsp);
        }
        catch (Exception e)
        {
            LOGGER.error("get instances error: {}",e.getMessage());
            e.printStackTrace();
            throw throwWebException(e);
        }
    }

    @GetMapping(value = "/instances/{instanceId}/vnc", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get vnc info", response = Object.class)
    public ResponseEntity<Map<String,String>> getVncInfo(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                         @ApiParam(value = "instanceId", required = true, name = "instanceId") @PathVariable("instanceId") String instanceId,
                                                         @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId) throws WebSystemException
    {
        try
        {
            LOGGER.info("get vnc info, cloud:{}, vm instanceId: {}", cloudId, instanceId);

            String operUserId = userId;
            if (isAdmin() || cloudService.isOwner(cloudId, userId))
            {
                operUserId = null;
            }
            else
            {
                cloudService.checkCloudStatus(cloudId, true);
            }

            return ResponseEntity.ok(vmComputeService.getVncInfo(cloudId, instanceId, operUserId));
        }
        catch (Exception e)
        {
            LOGGER.error("get vnc info error: {}",e.getMessage());
            e.printStackTrace();
            throw throwWebException(e);
        }
    }

    @GetMapping(value = "/instances/{instanceId}/iso", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get vnc info", response = Object.class)
    public ResponseEntity<Map<String, String>> getInstanceIdFromAgent(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                                      @ApiParam(value = "instanceId", required = true, name = "instanceId") @PathVariable("instanceId") String instanceId,
                                                                      @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId) throws WebSystemException
    {
        try
        {
            LOGGER.info("get vm instanceIdFromAgent, vm instanceId: {}", instanceId);

            String operUserId = userId;
            if (isAdmin() || cloudService.isOwner(cloudId, userId))
            {
                operUserId = null;
            }
            else
            {
                cloudService.checkCloudStatus(cloudId, true);
            }

            return ResponseEntity.ok(vmComputeService.getVncInfo(cloudId, instanceId, operUserId));
        }
        catch (Exception e)
        {
            LOGGER.error("get vm instanceIdFromAgent info error: {}", e.getMessage());
            throw throwWebException(e);
        }
    }

    @GetMapping(value = "/instances/{instanceId}/snaps", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get snap info", response = Object.class)
    public ResponseEntity<List<SnapsTreeRsp>> getSnapsTree(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                           @ApiParam(value = "instanceId", required = true, name = "instanceId") @PathVariable("instanceId") String instanceId,
                                                           @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId) throws WebSystemException
    {
        try
        {
            LOGGER.info("getSnapsTree, vm instanceId: {}", instanceId);

            String operUserId = userId;
            if (isAdmin() || cloudService.isOwner(cloudId, userId))
            {
                operUserId = null;
            }
            else
            {
                cloudService.checkCloudStatus(cloudId, true);
            }

            return ResponseEntity.ok(vmSnapService.getSnapsTree(cloudId, instanceId, operUserId));
        }
        catch (Exception e)
        {
            LOGGER.error("getSnapsTree info error: {}", e.getMessage());
            throw throwWebException(e);
        }
    }

    @GetMapping(value = "/instances/{instanceId}/injection", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "is iso injected", response = Object.class)
    public ResponseEntity<Object> isInjected(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                             @ApiParam(value = "instanceId", required = true, name = "instanceId") @PathVariable("instanceId") String instanceId,
                                             @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId) throws WebSystemException
    {
        try
        {
            LOGGER.info("get injection,vm instanceId: {}", instanceId);

            if (! isAdmin() && ! cloudService.isOwner(cloudId, userId))
            {
                cloudService.checkCloudStatus(cloudId, false);
                vmComputeService.checkVmInstanceUser(cloudId, instanceId, userId);
                vmComputeService.checkVmInstanceStatus(cloudId, instanceId);
            }

            return cloudService.sendHttpRequestToCloud(cloudId);
        }
        catch (Exception e)
        {
            LOGGER.error("get injection  error: {}", e.getMessage());
            throw throwWebException(e);
        }
    }


    @PutMapping(value = "/instances/{instanceId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "update instance", response = Object.class)
    @LNJoyAuditLog(value = "update instance",
            resourceMapperId = CmpResources.NS_VM_INSTANCE,
            actionDescriptionTemplate = CmpNextStackActionDescriptionTemplate.Descriptions.UPDATE_VM_INSTANCE,
            associatedResourceCnName = "虚拟机",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                    bindParameterName = "instanceId", resourcePrimaryKeyClass = TblCmpVmInstanceKey.class,
                    convertToResourcePrimaryKeyTypeSpEl = "new com.lnjoying.justice.cmp.db.model.TblCmpVmInstanceKey(#RESOURCE_POOL_ID,#RESOURCE_INSTANCE_ID)"))
    public ResponseEntity<Object> updateInstance(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                 @ApiParam(value = "instanceId", required = true, name = "instanceId") @PathVariable("instanceId") String instanceId,
                                                 @ApiParam(value = "VmInstanceUpdateReq", required = true, name = "VmInstanceUpdateReq") @RequestBody @Valid VmInstanceUpdateReq request,
                                                 @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
                                                 @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId) throws WebSystemException
    {
        try
        {
            LOGGER.debug("put vm instance: {}, userId:{}", request, userId);

            String operUserId = userId;
            if (isAdmin() || cloudService.isOwner(cloudId, userId))
            {
                operUserId = null;
            }
            else
            {
                cloudService.checkCloudStatus(cloudId, false);
            }

            return vmComputeService.updateVmInstance(cloudId, request, instanceId, bpId, operUserId);
        }
        catch (Exception e)
        {
            LOGGER.error("update vm instance error: {}, instanceId: {}", e.getMessage(), instanceId);
            throw throwWebException(e);
        }
    }

    @DeleteMapping(value = "/instances/{instanceId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "delete vm instance", response = Object.class)
    @LNJoyAuditLog(value = "delete vm instance",
            resourceMapperId = CmpResources.NS_VM_INSTANCE,
            actionDescriptionTemplate = CmpNextStackActionDescriptionTemplate.Descriptions.DELETE_VM_INSTANCE,
            associatedResourceCnName = "虚拟机",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                    bindParameterName = "instanceId", resourcePrimaryKeyClass = TblCmpVmInstanceKey.class,
                    convertToResourcePrimaryKeyTypeSpEl = "new com.lnjoying.justice.cmp.db.model.TblCmpVmInstanceKey(#RESOURCE_POOL_ID,#RESOURCE_INSTANCE_ID)"))
    public ResponseEntity<Object> removeInstance(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                 @ApiParam(value = "instanceId", required = true, name = "instanceId") @PathVariable("instanceId") String instanceId,
                                                 @ApiParam(name = "remove_root_disk") @RequestParam(required = false, value = "remove_root_disk") Boolean removeRootDisk,
                                                 @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId) throws WebSystemException
    {
        try
        {
            LOGGER.info("delete instance, cloud:{}, instanceId: {}, userId: {}", cloudId, instanceId, userId);

            String operUserId = userId;
            if (isAdmin() || cloudService.isOwner(cloudId, userId))
            {
                operUserId = null;
            }
            else
            {
                cloudService.checkCloudStatus(cloudId, false);
            }

            if (null == removeRootDisk) {removeRootDisk = true;}

            return vmComputeService.removeVmInstance(cloudId, instanceId, operUserId, removeRootDisk);
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("delete vm instance error: {}, vmInstanceId: {}",e.getMessage(), instanceId);
            e.printStackTrace();
            throw  throwWebException(e);
        }
    }

    @PutMapping(value = "/instances/{instanceId}/migrate", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "migrate vm instance", response = Object.class)
    @LNJoyAuditLog(value = "migrate vm instance",
            resourceMapperId = CmpResources.NS_VM_INSTANCE,
            actionDescriptionTemplate = CmpNextStackActionDescriptionTemplate.Descriptions.MIGRATE_VM_INSTANCE,
            associatedResourceCnName = "虚拟机",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                    bindParameterName = "instanceId", resourcePrimaryKeyClass = TblCmpVmInstanceKey.class,
                    convertToResourcePrimaryKeyTypeSpEl = "new com.lnjoying.justice.cmp.db.model.TblCmpVmInstanceKey(#RESOURCE_POOL_ID,#RESOURCE_INSTANCE_ID)"))
    public ResponseEntity<Object> migrateVmInstance(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                    @ApiParam(value = "instanceId", required = true, name = "instanceId") @PathVariable("instanceId") String instanceId,
                                                    @ApiParam(value = "MigrateVmInstanceReq", required = true, name = "MigrateVmInstanceReq") @RequestBody @Valid VmInstanceMigrateReq request,
                                                    @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {

            if (! isAdmin() && ! cloudService.isOwner(cloudId, userId))
            {
                cloudService.checkCloudStatus(cloudId, false);
                vmComputeService.checkVmInstanceUser(cloudId, instanceId, userId);
                vmComputeService.checkVmInstanceStatus(cloudId, instanceId);
            }

            return cloudService.sendHttpRequestToCloud(cloudId);
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("migrate vm instance failed: {}", e.getMessage());
            throw throwWebException(e);
        }
    }

    @PutMapping(value = "/instances/{instanceId}/volumes", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "attach volumes", response = Object.class)
    @LNJoyAuditLog(value = "attach volumes",
            resourceMapperId = CmpResources.NS_VM_INSTANCE,
            actionDescriptionTemplate = CmpNextStackActionDescriptionTemplate.Descriptions.ATTACH_VOLUME_TO_VM_INSTANCE,
            associatedResourceCnName = "虚拟机",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                    bindParameterName = "vmInstanceId", resourcePrimaryKeyClass = TblCmpVmInstanceKey.class,
                    convertToResourcePrimaryKeyTypeSpEl = "new com.lnjoying.justice.cmp.db.model.TblCmpVmInstanceKey(#RESOURCE_POOL_ID,#RESOURCE_INSTANCE_ID)"))
    public ResponseEntity<Object> attachVolumess(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                 @ApiParam(value = "VmInstanceAttachVolumesReq", required = true, name = "VmInstanceAttachVolumesReq") @RequestBody @Valid VmInstanceAttachVolumesReq request,
                                                 @ApiParam(value = "instanceId", required = true, name = "instanceId") @PathVariable("instanceId") String vmInstanceId,
                                                 @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId) throws WebSystemException
    {
        try
        {
            LOGGER.info("attach volumes, vmInstanceId:{}, userId:{}, volumeIds:{}", vmInstanceId, userId, request.getVolumeIds());

            if (! isAdmin() && ! cloudService.isOwner(cloudId, userId))
            {
                cloudService.checkCloudStatus(cloudId, false);
                vmComputeService.checkVmInstanceUser(cloudId, vmInstanceId, userId);
                vmComputeService.checkVmInstanceStatus(cloudId, vmInstanceId);
            }

            ResponseEntity response = cloudService.sendHttpRequestToCloud(cloudId);
            if (response.getStatusCode() == HttpStatus.OK)
            {
                request.getVolumeIds().forEach(volumeId -> cloudService.syncSingleData(cloudId, volumeId, SyncMsg.NS_VOLUME, DataSyncLevel.LEVEL_6));
            }
            return response;
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("attach volumes error: {}, vmId:{}, volumeIds:{}", e.getMessage(), vmInstanceId, request.getVolumeIds());
            throw throwWebException(e);
        }
    }

    @PutMapping(value = "/instances/{instanceId}/reset", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "reset instance", response = Object.class)
    @LNJoyAuditLog(value = "update instance",
            resourceMapperId = CmpResources.NS_VM_INSTANCE,
            actionDescriptionTemplate = CmpNextStackActionDescriptionTemplate.Descriptions.RESET_VM_INSTANCE,
            associatedResourceCnName = "虚拟机",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                    bindParameterName = "instanceId", resourcePrimaryKeyClass = TblCmpVmInstanceKey.class,
                    convertToResourcePrimaryKeyTypeSpEl = "new com.lnjoying.justice.cmp.db.model.TblCmpVmInstanceKey(#RESOURCE_POOL_ID,#RESOURCE_INSTANCE_ID)"))
    public ResponseEntity<Object> resetInstance(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                @ApiParam(value = "instanceId", required = true, name = "instanceId") @PathVariable("instanceId") String instanceId,
                                                @ApiParam(value = "ResetPasswordHostnameReq", required = true, name = "ResetPasswordHostnameReq") @RequestBody @Valid ResetPasswordHostnameReq request,
                                                @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId) throws WebSystemException
    {
        try
        {
            LOGGER.debug("reset vm instance: {}, userId:{}", request, userId);

            if (! isAdmin() && ! cloudService.isOwner(cloudId, userId))
            {
                cloudService.checkCloudStatus(cloudId, false);
                vmComputeService.checkVmInstanceUser(cloudId, instanceId, userId);
                vmComputeService.checkVmInstanceStatus(cloudId, instanceId);
            }

            return cloudService.sendHttpRequestToCloud(cloudId);
        }
        catch (Exception e)
        {
            LOGGER.error("reset vm instance error: {}, instanceId: {}", e.getMessage(), instanceId);
            throw throwWebException(e);
        }
    }
}
