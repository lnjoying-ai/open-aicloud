package com.lnjoying.justice.cmp.controller.nextstack.repo;

import com.lnjoying.justice.cmp.common.CmpResources;
import com.lnjoying.justice.cmp.common.DataSyncLevel;
import com.lnjoying.justice.cmp.common.SyncMsg;
import com.lnjoying.justice.cmp.common.nextstack.PhaseStatus;
import com.lnjoying.justice.cmp.db.model.TblCmpVolumeKey;
import com.lnjoying.justice.cmp.domain.dto.request.nextstack.baremetal.CommonReq;
import com.lnjoying.justice.cmp.domain.dto.request.nextstack.repo.VolumeAttachReq;
import com.lnjoying.justice.cmp.domain.dto.request.nextstack.repo.VolumeCreateReq;
import com.lnjoying.justice.cmp.domain.dto.request.nextstack.repo.VolumeExportReq;
import com.lnjoying.justice.cmp.domain.dto.response.nextstack.repo.RootVolumesRsp;
import com.lnjoying.justice.cmp.domain.dto.response.nextstack.repo.SnapsTreeRsp;
import com.lnjoying.justice.cmp.domain.dto.response.nextstack.repo.VolumeDetailInfoRsp;
import com.lnjoying.justice.cmp.domain.dto.response.nextstack.repo.VolumesRsp;
import com.lnjoying.justice.cmp.entity.search.nextstack.repo.VolumeSearchCritical;
import com.lnjoying.justice.cmp.handler.actiondescription.i18n.zh_cn.CmpNextStackActionDescriptionTemplate;
import com.lnjoying.justice.cmp.service.nextstack.repo.VolumeServiceBiz;
import com.lnjoying.justice.cmp.service.cloud.CloudService;
import com.lnjoying.justice.cmp.service.nextstack.repo.VolumeSnapServiceBiz;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;

import java.util.List;

import static com.lnjoying.justice.commonweb.common.SwaggerConstants.*;
import static com.lnjoying.justice.commonweb.util.ServiceCombRequestUtils.isAdmin;

@RestSchema(schemaId = "volume")
@Api(value = "Volume Controller", tags = {"Volume Controller"})
@RequestMapping("/cmp/v1/{vendor}/clouds/{cloud_id}/api/repo/v1")
@ApiOperation(value = "", extensions = {@Extension(name = SWAGGER_X_RESOURCE,
        properties = {@ExtensionProperty(name = SWAGGER_RESOURCE_SINGULAR_NAME, value = "ns_volume"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_PLURAL_NAME, value = "ns_volumes"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_DESCRIPTION, value = "云盘"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_MODEL, value = "TblCmpVolume")})})
public class VolumeController extends RestWebController
{
    private static Logger LOGGER = LogManager.getLogger();

    private static final String REG_UUID = "(EE_)?[0-9a-f]{32}";

    @Autowired
    private VolumeServiceBiz volumeServiceBiz;

    @Autowired
    private CloudService cloudService;

    @Autowired
    private VolumeSnapServiceBiz volumeSnapServiceBiz;

    @GetMapping(value = "/volumes", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get volumes")
    public VolumesRsp getVolumes(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                 @ApiParam(name = "name") @RequestParam(required = false,value = "name") String volumeName,
                                 @ApiParam(name = "storage_pool_id") @RequestParam(required = false,value = "storage_pool_id") String storagePoolId,
                                 @ApiParam(name = "detached" )@RequestParam(required = false,value = "detached") Boolean isDetached,
                                 @ApiParam(name = "page_size") @RequestParam(required = false,value = "page_size")  Integer pageSize,
                                 @ApiParam(name = "page_num") @RequestParam(required = false,value = "page_num")  Integer pageNum,
                                 @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        LOGGER.debug("get volume list");

        String filterUserId = userId;
        if (isAdmin() || cloudService.isOwner(cloudId, userId))
        {
            filterUserId = null;
        }
        else
        {
            cloudService.checkCloudStatus(cloudId, true);
        }

        VolumeSearchCritical pageSearchCritical = new VolumeSearchCritical();
        pageSearchCritical.setVolumeName(volumeName);
        pageSearchCritical.setPoolId(storagePoolId);
        pageSearchCritical.setIsRoot(false);

        if (pageNum != null) pageSearchCritical.setPageNum(pageNum);
        if (pageSize != null) pageSearchCritical.setPageSize(pageSize);
        if (isDetached != null && isDetached)
        {
            pageSearchCritical.setPhaseStatus(PhaseStatus.DETACHED);
            pageSearchCritical.setIsEqPhaseStatus(true);
        }
        else if (isDetached != null)
        {
            pageSearchCritical.setPhaseStatus(PhaseStatus.DETACHED);
            pageSearchCritical.setIsEqPhaseStatus(false);
        }

        try
        {
            return volumeServiceBiz.getVolumes(cloudId, pageSearchCritical, filterUserId);
        }
        catch (Exception e)
        {
            LOGGER.error("get volumes error {}", e.getMessage());
            throw throwWebException(e);
        }

    }

    @GetMapping(value = "/volumes/recycle", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get volumes")
    public RootVolumesRsp getRecycleVolumes(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                            @ApiParam(name = "name") @RequestParam(required = false,value = "name") String volumeName,
                                            @ApiParam(name = "storage_pool_id") @RequestParam(required = false,value = "storage_pool_id") String storagePoolId,
                                            @ApiParam(name = "page_size") @RequestParam(required = false,value = "page_size")  Integer pageSize,
                                            @ApiParam(name = "page_num") @RequestParam(required = false)  Integer pageNum,
                                            @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {

        LOGGER.debug("get recycle volume list");

        String filterUserId = userId;
        if (isAdmin() || cloudService.isOwner(cloudId, userId))
        {
            filterUserId = null;
        }
        else
        {
            cloudService.checkCloudStatus(cloudId, true);
        }

        VolumeSearchCritical pageSearchCritical = new VolumeSearchCritical();
        pageSearchCritical.setVolumeName(volumeName);
        pageSearchCritical.setPoolId(storagePoolId);
        pageSearchCritical.setIsRoot(true);
        pageSearchCritical.setPhaseStatus(PhaseStatus.DETACHED);
        pageSearchCritical.setIsEqPhaseStatus(true);

        if (pageNum != null) pageSearchCritical.setPageNum(pageNum);
        if (pageSize != null) pageSearchCritical.setPageSize(pageSize);
        try
        {
            return volumeServiceBiz.getRootVolumes(cloudId, pageSearchCritical, filterUserId);
        }
        catch (Exception e)
        {
            LOGGER.error("get volumes error {}", e.getMessage());
            throw throwWebException(e);
        }

    }

    @GetMapping(value = "/volumes/{volume_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get volume detail info")
    public VolumeDetailInfoRsp getVolume(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                         @ApiParam(value = "volume_id", required = true, name = "volume_id") @PathVariable("volume_id") @Pattern(regexp = REG_UUID) String volumeId,
                                         @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {

        LOGGER.info("get volume detail info, volumeId: {}", volumeId);
        try
        {
            String operUserId = userId;
            if (isAdmin() || cloudService.isOwner(cloudId, userId))
            {
                operUserId = null;
            }
            else
            {
                cloudService.checkCloudStatus(cloudId, true);
            }

            return volumeServiceBiz.getVolume(cloudId, volumeId, operUserId);
        }
        catch (Exception e)
        {
            LOGGER.error("get volume detail info error {}", e.getMessage());
            throw throwWebException(e);
        }

    }

    @PostMapping(value = "/volumes", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "create new volume")
    @LNJoyAuditLog(value = "create new volume",
            resourceMapperId = CmpResources.NS_VOLUME,
            actionDescriptionTemplate = CmpNextStackActionDescriptionTemplate.Descriptions.ADD_VOLUME,
            associatedResourceCnName = "云盘",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.METHOD_RETURN_VALUE,
                    resourceIdParseSPEL = "#obj?.body?.get('volumeId')?.toString()", resourcePrimaryKeyClass = TblCmpVolumeKey.class,
                    convertToResourcePrimaryKeyTypeSpEl = "new com.lnjoying.justice.cmp.db.model.TblCmpVolumeKey(#RESOURCE_POOL_ID,#RESOURCE_INSTANCE_ID)"))
    public ResponseEntity<Object> createVolume(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                               @ApiParam(value = "volumeCreateReq", required = true, name = "volumeCreateReq")@RequestBody @Valid VolumeCreateReq volumeCreateReq,
                                               @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
                                               @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {

        LOGGER.info("create volume info: {}",volumeCreateReq);
        try
        {

            if (! isAdmin() && ! cloudService.isOwner(cloudId, userId))
            {
                cloudService.checkCloudStatus(cloudId, false);
            }

            return volumeServiceBiz.createVolume(cloudId, volumeCreateReq, bpId, userId, false, null, null, null);
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("create volume error {}", e.getMessage());
            throw throwWebException(e);
        }
    }

    @DeleteMapping(value = "/volumes/{volume_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "remove volume")
    @LNJoyAuditLog(value = "remove volume",
            resourceMapperId = CmpResources.NS_VOLUME,
            actionDescriptionTemplate = CmpNextStackActionDescriptionTemplate.Descriptions.DELETE_VOLUME,
            associatedResourceCnName = "云盘",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                    bindParameterName = "volumeId", resourcePrimaryKeyClass = TblCmpVolumeKey.class,
                    convertToResourcePrimaryKeyTypeSpEl = "new com.lnjoying.justice.cmp.db.model.TblCmpVolumeKey(#RESOURCE_POOL_ID,#RESOURCE_INSTANCE_ID)"))
    public ResponseEntity<Object> removeVolume(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                               @ApiParam(value = "volume_id", required = true, name = "volume_id")@PathVariable("volume_id") @Pattern(regexp = REG_UUID) String volumeId,
                                               @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {

        LOGGER.info("remove volume, volumeId: {}", volumeId);
        try
        {

            String operUserId = userId;
            if (isAdmin() || cloudService.isOwner(cloudId, userId))
            {
                operUserId = null;
            }
            else
            {
                cloudService.checkCloudStatus(cloudId, false);
            }

            return volumeServiceBiz.removeVolume(cloudId, volumeId, operUserId);
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("remove volume error {}", e.getMessage());
            throw throwWebException(e);
        }
    }

    @PutMapping(value = "/volumes/{volume_id}/attach", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "attach volume")
    @LNJoyAuditLog(value = "attach volume",
            resourceMapperId = CmpResources.NS_VOLUME,
            actionDescriptionTemplate = CmpNextStackActionDescriptionTemplate.Descriptions.ATTACH_VOLUME,
            associatedResourceCnName = "云盘",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                    bindParameterName = "volumeId", resourcePrimaryKeyClass = TblCmpVolumeKey.class,
                    convertToResourcePrimaryKeyTypeSpEl = "new com.lnjoying.justice.cmp.db.model.TblCmpVolumeKey(#RESOURCE_POOL_ID,#RESOURCE_INSTANCE_ID)"))
    public Object attachVolume(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                               @ApiParam(value = "volume_id", required = true, name = "volume_id") @PathVariable("volume_id")String volumeId,
                               @ApiParam(value = "volumeAttachReq", required = true, name = "volumeAttachReq")@RequestBody @Valid VolumeAttachReq volumeAttachReq,
                               @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {

        LOGGER.info("attach volume: volumeId:{} vmId:{}", volumeId, volumeAttachReq.getVmId());
        try
        {

            if (! isAdmin() && ! cloudService.isOwner(cloudId, userId))
            {
                cloudService.checkCloudStatus(cloudId, false);
                volumeServiceBiz.checkVolumeUser(cloudId, volumeId, userId);
            }

            ResponseEntity response = cloudService.sendHttpRequestToCloud(cloudId);
            if (response.getStatusCode() == HttpStatus.OK)
            {
                cloudService.syncSingleData(cloudId, volumeId, SyncMsg.NS_VOLUME, DataSyncLevel.LEVEL_6);
                cloudService.syncSingleData(cloudId, volumeAttachReq.getVmId(), SyncMsg.NS_VM_INSTANCE, DataSyncLevel.LEVEL_6);
            }
            return response;
        }
        catch (Exception e)
        {
            LOGGER.error("attach volume error {}", e.getMessage());
            throw throwWebException(e);
        }
    }

    @PutMapping(value = "/volumes/{volume_id}/detach", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "detach volume")
    @LNJoyAuditLog(value = "detach volume",
            resourceMapperId = CmpResources.NS_VOLUME,
            actionDescriptionTemplate = CmpNextStackActionDescriptionTemplate.Descriptions.DETACH_VOLUME,
            associatedResourceCnName = "云盘",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                    bindParameterName = "volumeId", resourcePrimaryKeyClass = TblCmpVolumeKey.class,
                    convertToResourcePrimaryKeyTypeSpEl = "new com.lnjoying.justice.cmp.db.model.TblCmpVolumeKey(#RESOURCE_POOL_ID,#RESOURCE_INSTANCE_ID)"))
    public ResponseEntity<Object> detachVolume(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                               @ApiParam(value = "volume_id", required = true, name = "volume_id") @PathVariable("volume_id")String volumeId,
                                               @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {

        LOGGER.info("detach volume: volumeId:{} ",volumeId);
        try
        {

            if (! isAdmin() && ! cloudService.isOwner(cloudId, userId))
            {
                cloudService.checkCloudStatus(cloudId, false);
                volumeServiceBiz.checkVolumeUser(cloudId, volumeId, userId);
            }

            ResponseEntity response = cloudService.sendHttpRequestToCloud(cloudId);
            if (response.getStatusCode() == HttpStatus.OK)
            {
                cloudService.syncSingleData(cloudId, volumeId, SyncMsg.NS_VOLUME, DataSyncLevel.LEVEL_6);
            }
            return response;
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("detach volume error {}", e.getMessage());
            throw throwWebException(e);
        }
    }

    @PutMapping(value = "/volumes/{volume_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "update volume")
    @LNJoyAuditLog(value = "update volume",
            resourceMapperId = CmpResources.NS_VOLUME,
            actionDescriptionTemplate = CmpNextStackActionDescriptionTemplate.Descriptions.UPDATE_VOLUME,
            associatedResourceCnName = "云盘",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                    bindParameterName = "volumeId", resourcePrimaryKeyClass = TblCmpVolumeKey.class,
                    convertToResourcePrimaryKeyTypeSpEl = "new com.lnjoying.justice.cmp.db.model.TblCmpVolumeKey(#RESOURCE_POOL_ID,#RESOURCE_INSTANCE_ID)"))
    public ResponseEntity<Object> updateVolume(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                               @ApiParam(value = "volume_id", required = true, name = "volume_id") @PathVariable("volume_id")String volumeId,
                                               @ApiParam(value = "commonReq", required = true, name = "commonReq") @RequestBody @Valid CommonReq request,
                                               @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        LOGGER.info("update volume: volumeId:{} name:{}",volumeId, request.getName());
        try
        {

            if (! isAdmin() && ! cloudService.isOwner(cloudId, userId))
            {
                cloudService.checkCloudStatus(cloudId, false);
                volumeServiceBiz.checkVolumeUser(cloudId, volumeId, userId);
            }

            return cloudService.sendHttpRequestToCloud(cloudId);
        }
        catch (Exception e)
        {
            LOGGER.error("update volume error {}", e.getMessage());
            throw throwWebException(e);
        }
    }

    @PutMapping(value = "/volumes/{volume_id}/export", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "export volume")
    @LNJoyAuditLog(value = "export volume",
            resourceMapperId = CmpResources.NS_VOLUME,
            actionDescriptionTemplate = CmpNextStackActionDescriptionTemplate.Descriptions.EXPORT_VOLUME,
            associatedResourceCnName = "云盘",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                    bindParameterName = "volumeId", resourcePrimaryKeyClass = TblCmpVolumeKey.class,
                    convertToResourcePrimaryKeyTypeSpEl = "new com.lnjoying.justice.cmp.db.model.TblCmpVolumeKey(#RESOURCE_POOL_ID,#RESOURCE_INSTANCE_ID)"))
    public ResponseEntity<Object> exportVolume(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                               @ApiParam(value = "volume_id", required = true, name = "volume_id") @PathVariable("volume_id")String volumeId,
                                               @ApiParam(value = "volumeExportReq", required = true, name = "volumeExportReq") @RequestBody @Valid VolumeExportReq volumeExportReq,
                                               @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
                                               @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        LOGGER.info("export volume: volumeId:{} exportName:{}", volumeId, volumeExportReq.getImageName());
        try
        {

            if (! isAdmin() && ! cloudService.isOwner(cloudId, userId))
            {
                cloudService.checkCloudStatus(cloudId, false);
                volumeServiceBiz.checkVolumeUser(cloudId, volumeId, userId);
            }

            return volumeServiceBiz.exportVolume(cloudId, volumeExportReq, bpId, userId);
        }
        catch (Exception e)
        {
            LOGGER.error("export volume error {}", e.getMessage());
            throw throwWebException(e);
        }
    }

    @GetMapping(value = "/volumes/{volumeId}/snaps", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get snap list", response = Object.class)
    public ResponseEntity<List<SnapsTreeRsp>> getSnapsTree(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                           @ApiParam(value = "volumeId", required = true, name = "volumeId") @PathVariable("volumeId") String volumeId,
                                                           @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId) throws WebSystemException
    {
        try
        {
            LOGGER.info("getSnapsTree,  volumeId: {}", volumeId);

            String operUserId = userId;
            if (isAdmin() || cloudService.isOwner(cloudId, userId))
            {
                operUserId = null;
            }
            else
            {
                cloudService.checkCloudStatus(cloudId, true);
            }

            return ResponseEntity.ok(volumeSnapServiceBiz.getSnapsTree(cloudId, volumeId, operUserId));
        }
        catch (Exception e)
        {
            LOGGER.error("getSnapsTree info error: {}", e.getMessage());
            throw throwWebException(e);
        }
    }
}
