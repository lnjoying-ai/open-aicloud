package com.lnjoying.justice.cmp.controller.nextstack.vm;

import com.lnjoying.justice.cmp.common.CmpResources;
import com.lnjoying.justice.cmp.db.model.TblCmpInstanceGroupKey;
import com.lnjoying.justice.cmp.domain.dto.request.nextstack.vm.InstanceGroupCreateReq;
import com.lnjoying.justice.cmp.domain.dto.response.nextstack.vm.InstanceGroup;
import com.lnjoying.justice.cmp.domain.dto.response.nextstack.vm.InstanceGroupsBaseRsp;
import com.lnjoying.justice.cmp.entity.search.nextstack.vm.InstanceGroupSearchCritical;
import com.lnjoying.justice.cmp.handler.actiondescription.i18n.zh_cn.CmpNextStackActionDescriptionTemplate;
import com.lnjoying.justice.cmp.service.cloud.CloudService;
import com.lnjoying.justice.cmp.service.nextstack.vm.InstanceGroupServiceBiz;
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

import javax.validation.Valid;

import static com.lnjoying.justice.commonweb.common.SwaggerConstants.*;
import static com.lnjoying.justice.commonweb.util.ServiceCombRequestUtils.isAdmin;

@RestSchema(schemaId = "instanceGroup")
@RequestMapping("/cmp/v1/{vendor}/clouds/{cloud_id}/api/vm/v1")
@Api(value = "Virtual Machine Group Controller",tags = "Virtual Machine Group Controller")
@ApiOperation(value = "", extensions = {@Extension(name = SWAGGER_X_RESOURCE,
        properties = {@ExtensionProperty(name = SWAGGER_RESOURCE_SINGULAR_NAME, value = "ns_instance_group"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_PLURAL_NAME, value = "ns_instance_groups"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_DESCRIPTION, value = "虚拟机组"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_MODEL, value = "TblCmpInstanceGroup")})})
public class InstanceGroupController extends RestWebController
{
    private static Logger LOGGER = LogManager.getLogger();

    @Autowired
    private CloudService cloudService;

    @Autowired
    private InstanceGroupServiceBiz instanceGroupService;

    @PostMapping(value = "/instance-groups", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "create instance group", response = Object.class)
    @LNJoyAuditLog(value = "add hypervisor node",
            resourceMapperId = CmpResources.NS_INSTANCE_GROUP,
            actionDescriptionTemplate = CmpNextStackActionDescriptionTemplate.Descriptions.ADD_INSTANCE_GROUP,
            associatedResourceCnName = "虚拟机组",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.METHOD_RETURN_VALUE,
                    resourceIdParseSPEL = "#obj?.body?.get('instanceGroupId')?.toString()", resourcePrimaryKeyClass = TblCmpInstanceGroupKey.class,
                    convertToResourcePrimaryKeyTypeSpEl = "new com.lnjoying.justice.cmp.db.model.TblCmpInstanceGroupKey(#RESOURCE_POOL_ID,#RESOURCE_INSTANCE_ID)"))
    public ResponseEntity<Object> createInstanceGroup(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                      @ApiParam(value = "InstanceGroupCreateReq", required = true, name = "InstanceGroupCreateReq") @RequestBody @Valid InstanceGroupCreateReq request,
                                                      @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
                                                      @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId) throws WebSystemException
    {
        try
        {
            LOGGER.info("create instance group , request:{}, userId:{}", request, userId);

            if (! isAdmin() && ! cloudService.isOwner(cloudId, userId))
            {
                cloudService.checkCloudStatus(cloudId, false);
            }

            return instanceGroupService.addInstanceGroup(cloudId, request, userId, bpId);
        }
        catch (Exception e)
        {
            LOGGER.error("create instance group error: {}", e.getMessage());

            throw throwWebException(e);
        }
    }

    @GetMapping(value = "/instance-groups", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get instance groups", response = Object.class)
    public ResponseEntity<InstanceGroupsBaseRsp> getGroups(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                           @ApiParam(name = "name") @RequestParam(required = false) String name,
                                                           @ApiParam(name = "page_size") @RequestParam(required = false,value = "page_size") Integer pageSize,
                                                           @ApiParam(name = "page_num") @RequestParam(required = false,value = "page_num") Integer pageNum,
                                                           @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId) throws WebSystemException
    {
        try
        {
            LOGGER.debug("get instance group list");

            String filterUserId = userId;
            if (isAdmin() || cloudService.isOwner(cloudId, userId))
            {
                filterUserId = null;
            }
            else
            {
                cloudService.checkCloudStatus(cloudId, true);
            }

            InstanceGroupSearchCritical pageSearchCritical = new InstanceGroupSearchCritical();
            if (null != name) pageSearchCritical.setName(name);
            if (null != pageNum) pageSearchCritical.setPageNum(pageNum);
            if (null != pageSize) pageSearchCritical.setPageSize(pageSize);

            InstanceGroupsBaseRsp instanceGroupsBaseRsp = instanceGroupService.getInstanceGroups(cloudId, pageSearchCritical, filterUserId);
            return ResponseEntity.ok(instanceGroupsBaseRsp);
        }
        catch (Exception e)
        {
            LOGGER.error("get instance groups error: {}",e.getMessage());
            throw throwWebException(e);
        }
    }

    @GetMapping(value = "/instance-groups/{groupId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get instance group detail info", response = Object.class)
    public ResponseEntity<InstanceGroup> getVmGroupDetailInfo(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                              @ApiParam(value = "groupId", required = true, name = "groupId") @PathVariable("groupId") String groupId,
                                                              @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId) throws WebSystemException
    {
        try
        {
            LOGGER.info("get instance group, request:{}, userId:{}", groupId, userId);

            String operUserId = userId;
            if (isAdmin() || cloudService.isOwner(cloudId, userId))
            {
                operUserId = null;
            }
            else
            {
                cloudService.checkCloudStatus(cloudId, true);
            }

            InstanceGroup tblInstanceGroup = instanceGroupService.getInstanceGroup(cloudId, groupId, operUserId);
            return ResponseEntity.ok(tblInstanceGroup);
        }
        catch (Exception e)
        {
            LOGGER.error("get instance group error: {}, vmGroupId: {}",e.getMessage(), groupId);
            throw  throwWebException(e);
        }
    }

    @PutMapping(value = "/instance-groups/{groupId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "update instance group", response = Object.class)
    @LNJoyAuditLog(value = "update instance group",
            resourceMapperId = CmpResources.NS_INSTANCE_GROUP,
            actionDescriptionTemplate = CmpNextStackActionDescriptionTemplate.Descriptions.UPDATE_INSTANCE_GROUP,
            associatedResourceCnName = "虚拟机组",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                    bindParameterName = "groupId", resourcePrimaryKeyClass = TblCmpInstanceGroupKey.class,
                    convertToResourcePrimaryKeyTypeSpEl = "new com.lnjoying.justice.cmp.db.model.TblCmpInstanceGroupKey(#RESOURCE_POOL_ID,#RESOURCE_INSTANCE_ID)"))
    public ResponseEntity<Object> switchVmGroup(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                @ApiParam(value = "groupId", required = true, name = "groupId") @PathVariable("groupId") String groupId,
                                                @ApiParam(value = "InstanceGroupCreateReq", required = true, name = "InstanceGroupCreateReq") @RequestBody @Valid InstanceGroupCreateReq request,
                                                @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId) throws WebSystemException
    {
        try
        {
            LOGGER.info("switch instance group, request:{}, userId:{}", groupId, userId);

            if (! isAdmin() && ! cloudService.isOwner(cloudId, userId))
            {
                cloudService.checkCloudStatus(cloudId, false);
                instanceGroupService.checkInstanceGroupUser(cloudId, groupId, userId);
            }

            return cloudService.sendHttpRequestToCloud(cloudId);
        }
        catch (Exception e)
        {
            LOGGER.error("switch instance group error: {}, vmGroupId: {}", e.getMessage(), groupId);
            throw  throwWebException(e);
        }
    }

    @DeleteMapping(value = "/instance-groups/{groupId}/{vmInstanceId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "delete instance group", response = Object.class)
    @LNJoyAuditLog(value = "delete instance from instance group",
            resourceMapperId = CmpResources.NS_INSTANCE_GROUP,
            actionDescriptionTemplate = CmpNextStackActionDescriptionTemplate.Descriptions.DELETE_INSTANCE_FROM_GROUP,
            associatedResourceCnName = "虚拟机组",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                    bindParameterName = "groupId", resourcePrimaryKeyClass = TblCmpInstanceGroupKey.class,
                    convertToResourcePrimaryKeyTypeSpEl = "new com.lnjoying.justice.cmp.db.model.TblCmpInstanceGroupKey(#RESOURCE_POOL_ID,#RESOURCE_INSTANCE_ID)"))
    public ResponseEntity<Object> removeVmInstanceIdFromGroup(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                              @ApiParam(value = "groupId", required = true, name = "groupId") @PathVariable("groupId") String groupId,
                                                              @ApiParam(value = "vmInstanceId", required = true, name = "vmInstanceId") @PathVariable("vmInstanceId") String vmInstanceId,
                                                              @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId) throws WebSystemException
    {
        try
        {
            LOGGER.debug("delete vmInstanceId {} from instanceGroupId: {}, userId: {}", vmInstanceId, groupId, userId);

            if (! isAdmin() && ! cloudService.isOwner(cloudId, userId))
            {
                cloudService.checkCloudStatus(cloudId, false);
                instanceGroupService.checkInstanceGroupUser(cloudId, groupId, userId);
            }

            return cloudService.sendHttpRequestToCloud(cloudId);
        }
        catch (Exception e)
        {
            LOGGER.error("delete instance group error: {}, vmGroupId: {}", e.getMessage(), groupId);
            throw  throwWebException(e);
        }
    }

    @DeleteMapping(value = "/instance-groups/{groupId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "delete instance group", response = Object.class)
    @LNJoyAuditLog(value = "delete instance group",
            resourceMapperId = CmpResources.NS_INSTANCE_GROUP,
            actionDescriptionTemplate = CmpNextStackActionDescriptionTemplate.Descriptions.DELETE_INSTANCE_GROUP,
            associatedResourceCnName = "虚拟机组",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                    bindParameterName = "groupId", resourcePrimaryKeyClass = TblCmpInstanceGroupKey.class,
                    convertToResourcePrimaryKeyTypeSpEl = "new com.lnjoying.justice.cmp.db.model.TblCmpInstanceGroupKey(#RESOURCE_POOL_ID,#RESOURCE_INSTANCE_ID)"))
    public ResponseEntity<Object> removeGroup(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                              @ApiParam(value = "groupId", required = true, name = "groupId") @PathVariable("groupId") String groupId,
                                              @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId) throws WebSystemException
    {
        try
        {
            LOGGER.debug("delete instanceGroupId: {}, userId: {}", groupId, userId);

            String operUserId = userId;
            if (isAdmin() || cloudService.isOwner(cloudId, userId))
            {
                operUserId = null;
            }
            else
            {
                cloudService.checkCloudStatus(cloudId, false);
            }

            return instanceGroupService.removeInstanceGroup(cloudId, groupId, operUserId);
        }
        catch (Exception e)
        {
            LOGGER.error("delete instance group error: {}, vmGroupId: {}", e.getMessage(), groupId);
            throw  throwWebException(e);
        }
    }
}
