package com.lnjoying.justice.cmp.controller.nextstack.vm;

import com.lnjoying.justice.cmp.common.CmpResources;
import com.lnjoying.justice.cmp.db.model.TblCmpVmSnapKey;
import com.lnjoying.justice.cmp.domain.dto.request.nextstack.baremetal.CommonReq;
import com.lnjoying.justice.cmp.domain.dto.response.nextstack.vm.VmSnapDetailInfoRsp;
import com.lnjoying.justice.cmp.domain.dto.response.nextstack.vm.VmSnapsRsp;
import com.lnjoying.justice.cmp.entity.search.nextstack.vm.VmSnapSearchCritical;
import com.lnjoying.justice.cmp.domain.dto.request.nextstack.vm.SnapCreateReq;
import com.lnjoying.justice.cmp.handler.actiondescription.i18n.zh_cn.CmpNextStackActionDescriptionTemplate;
import com.lnjoying.justice.cmp.service.nextstack.vm.VmSnapServiceBiz;
import com.lnjoying.justice.cmp.service.cloud.CloudService;
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

@RestSchema(schemaId = "snap")
@RequestMapping("/cmp/v1/{vendor}/clouds/{cloud_id}/api/vm/v1")
@Api(value = "Snap Controller",tags = "Snap Controller")
@ApiOperation(value = "", extensions = {@Extension(name = SWAGGER_X_RESOURCE,
                properties = {@ExtensionProperty(name = SWAGGER_RESOURCE_SINGULAR_NAME, value = "ns_vm_snap"),
                        @ExtensionProperty(name = SWAGGER_RESOURCE_PLURAL_NAME, value = "ns_vm_snaps"),
                        @ExtensionProperty(name = SWAGGER_RESOURCE_DESCRIPTION, value = "快照"),
                        @ExtensionProperty(name = SWAGGER_RESOURCE_MODEL, value = "TblCmpVmSnap")})})
public class VmSnapController extends RestWebController
{
    private static Logger LOGGER = LogManager.getLogger();

    @Autowired
    private VmSnapServiceBiz vmComputeService;

    @Autowired
    private CloudService cloudService;

    @PostMapping(value = "/snaps", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "create vm snap", response = Object.class)
    @LNJoyAuditLog(value = "create vm snap",
            resourceMapperId = CmpResources.NS_VM_SNAP,
            actionDescriptionTemplate = CmpNextStackActionDescriptionTemplate.Descriptions.ADD_VM_SNAP,
            associatedResourceCnName = "虚拟机快照",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.METHOD_RETURN_VALUE,
                    resourceIdParseSPEL = "#obj?.body?.get('snapId')?.toString()", resourcePrimaryKeyClass = TblCmpVmSnapKey.class,
                    convertToResourcePrimaryKeyTypeSpEl = "new com.lnjoying.justice.cmp.db.model.TblCmpVmSnapKey(#RESOURCE_POOL_ID,#RESOURCE_INSTANCE_ID)"))
    public ResponseEntity<Object> createVmSnap(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                               @ApiParam(value = "SnapCreateReq", required = true, name = "SnapCreateReq") @RequestBody SnapCreateReq request,
                                               @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
                                               @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId) throws WebSystemException
    {
        try
        {
            LOGGER.info("create vm snap, cloud:{}, request:{}, userId:{}", cloudId, request, userId);

            if (! isAdmin() && ! cloudService.isOwner(cloudId, userId))
            {
                cloudService.checkCloudStatus(cloudId, false);
            }

            return vmComputeService.addVmSnap(cloudId, request, bpId, userId);
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("create vm snap error: {}", e.getMessage());

            e.printStackTrace();
            throw throwWebException(e);
        }
    }

    @GetMapping(value = "/snaps", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get snaps", response = Object.class)
    public ResponseEntity<VmSnapsRsp> getSnaps(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                               @ApiParam(name = "name") @RequestParam(required = false) String name,
                                               @ApiParam(name = "instance_id") @RequestParam(required = false, value = "instance_id") String instanceId,
                                               @ApiParam(name = "page_size") @RequestParam(required = false, value = "page_size") Integer pageSize,
                                               @ApiParam(name = "page_num") @RequestParam(required = false, value = "page_num") Integer pageNum,
                                               @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId) throws WebSystemException
    {
        try
        {
            LOGGER.debug("get vm snap list, cloud:{}", cloudId);

            String filterUserId = userId;
            if (isAdmin() || cloudService.isOwner(cloudId, userId))
            {
                filterUserId = null;
            }
            else
            {
                cloudService.checkCloudStatus(cloudId, true);
            }

            VmSnapSearchCritical pageSearchCritical = new VmSnapSearchCritical();
            if (null != name) pageSearchCritical.setName(name);
            if (null != instanceId) pageSearchCritical.setVmInstanceId(instanceId);
            if (null != pageNum) pageSearchCritical.setPageNum(pageNum);
            if (null != pageSize) pageSearchCritical.setPageSize(pageSize);

            VmSnapsRsp getSnapsRsp = vmComputeService.getSnaps(cloudId, pageSearchCritical, filterUserId);

            return ResponseEntity.ok(getSnapsRsp);
        }
        catch (Exception e)
        {
            LOGGER.error("get snaps error: {}",e.getMessage());
            e.printStackTrace();
            throw throwWebException(e);
        }
    }

    @GetMapping(value = "/snaps/{snapId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get vm snap detail info", response = Object.class)
    public ResponseEntity<VmSnapDetailInfoRsp> getVmSnapDetailInfo(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                                   @ApiParam(value = "snapId", required = true, name = "snapId") @PathVariable("snapId") String vmSnapId,
                                                                   @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId) throws WebSystemException
    {
        try
        {
            LOGGER.info("get vm snap, cloud:{}, request:{}, userId:{}", cloudId, vmSnapId, userId);

            String operUserId = userId;
            if (isAdmin() || cloudService.isOwner(cloudId, userId))
            {
                operUserId = null;
            }
            else
            {
                cloudService.checkCloudStatus(cloudId, true);
            }

            VmSnapDetailInfoRsp vmSnapInfo = vmComputeService.getSnap(cloudId, vmSnapId, operUserId);

            return ResponseEntity.ok(vmSnapInfo);
        }
        catch (Exception e)
        {
            LOGGER.error("get vm snap error: {}, vmSnapId: {}",e.getMessage(), vmSnapId);
            e.printStackTrace();
            throw  throwWebException(e);
        }
    }

    @PutMapping(value = "/snaps/{snapId}/switch", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "switch snap", response = Object.class)
    @LNJoyAuditLog(value = "switch snap",
            resourceMapperId = CmpResources.NS_VM_SNAP,
            actionDescriptionTemplate = CmpNextStackActionDescriptionTemplate.Descriptions.SWITCH_VM_SNAP,
            associatedResourceCnName = "虚拟机快照",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                    bindParameterName = "vmSnapId", resourcePrimaryKeyClass = TblCmpVmSnapKey.class,
                    convertToResourcePrimaryKeyTypeSpEl = "new com.lnjoying.justice.cmp.db.model.TblCmpVmSnapKey(#RESOURCE_POOL_ID,#RESOURCE_INSTANCE_ID)"))
    public ResponseEntity<Object> switchVmSnap(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                               @ApiParam(value = "snapId", required = true, name = "snapId") @PathVariable("snapId") String vmSnapId,
                                               @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId) throws WebSystemException
    {
        try
        {
            LOGGER.info("switch vm snap, request:{}", vmSnapId);

            if (! isAdmin() && ! cloudService.isOwner(cloudId, userId))
            {
                cloudService.checkCloudStatus(cloudId, false);
                vmComputeService.checkVmSnapUser(cloudId, vmSnapId, userId);
            }

            return cloudService.sendHttpRequestToCloud(cloudId);
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("switch vm snap error: {}, vmSnapId: {}",e.getMessage(), vmSnapId);
            throw  throwWebException(e);
        }
    }

    @PutMapping(value = "/snaps/{snapId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "update snap", response = Object.class)
    @LNJoyAuditLog(value = "switch snap",
            resourceMapperId = CmpResources.NS_VM_SNAP,
            actionDescriptionTemplate = CmpNextStackActionDescriptionTemplate.Descriptions.UPDATE_VM_SNAP,
            associatedResourceCnName = "虚拟机快照",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                    bindParameterName = "snapId", resourcePrimaryKeyClass = TblCmpVmSnapKey.class,
                    convertToResourcePrimaryKeyTypeSpEl = "new com.lnjoying.justice.cmp.db.model.TblCmpVmSnapKey(#RESOURCE_POOL_ID,#RESOURCE_INSTANCE_ID)"))
    public ResponseEntity<Object> updateSnap(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                             @ApiParam(value = "snapId", required = true, name = "snapId") @PathVariable("snapId") String snapId,
                                             @ApiParam(value = "CommonReq", required = true, name = "CommonReq") @RequestBody @Valid CommonReq request,
                                             @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId) throws WebSystemException
    {
        try
        {
            LOGGER.debug("put vm snap: {}, userId:{}", request, userId);

            if (! isAdmin() && ! cloudService.isOwner(cloudId, userId))
            {
                cloudService.checkCloudStatus(cloudId, false);
                vmComputeService.checkVmSnapUser(cloudId, snapId, userId);
            }

            return cloudService.sendHttpRequestToCloud(cloudId);
        }
        catch (Exception e)
        {
            LOGGER.error("update vm snap error: {}, instanceId: {}",e.getMessage(), snapId);
            throw  throwWebException(e);
        }
    }

    @DeleteMapping(value = "/snaps/{snapId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "delete vm snap", response = Object.class)
    @LNJoyAuditLog(value = "delete vm snap",
            resourceMapperId = CmpResources.NS_VM_SNAP,
            actionDescriptionTemplate = CmpNextStackActionDescriptionTemplate.Descriptions.DELETE_VM_SNAP,
            associatedResourceCnName = "虚拟机快照",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                    bindParameterName = "snapId", resourcePrimaryKeyClass = TblCmpVmSnapKey.class,
                    convertToResourcePrimaryKeyTypeSpEl = "new com.lnjoying.justice.cmp.db.model.TblCmpVmSnapKey(#RESOURCE_POOL_ID,#RESOURCE_INSTANCE_ID)"))
    public ResponseEntity<Object> removeSnap(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                              @ApiParam(value = "snapId", required = true, name = "snapId") @PathVariable("snapId") String snapId,
                                              @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)throws WebSystemException
    {
        try
        {
            LOGGER.debug("delete vmSnap, cloud:{}, snapId: {}, userId: {}", cloudId, snapId, userId);

            String operUserId = userId;
            if (isAdmin() || cloudService.isOwner(cloudId, userId))
            {
                operUserId = null;
            }
            else
            {
                cloudService.checkCloudStatus(cloudId, false);
            }

            return vmComputeService.removeSnap(cloudId, snapId, operUserId);
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("delete vm snap error: {}, vmSnapId: {}",e.getMessage(), snapId);
            e.printStackTrace();
            throw  throwWebException(e);
        }
    }
}
