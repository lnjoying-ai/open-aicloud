package com.lnjoying.justice.cmp.controller.nextstack.repo;

import com.lnjoying.justice.cmp.common.CmpResources;
import com.lnjoying.justice.cmp.db.model.TblCmpVolumeSnapKey;
import com.lnjoying.justice.cmp.domain.dto.request.nextstack.baremetal.CommonReq;
import com.lnjoying.justice.cmp.domain.dto.request.nextstack.repo.VolumeSnapCreateReq;
import com.lnjoying.justice.cmp.entity.search.nextstack.repo.VolumeSnapSearchCritical;
import com.lnjoying.justice.cmp.handler.actiondescription.i18n.zh_cn.CmpNextStackActionDescriptionTemplate;
import com.lnjoying.justice.cmp.service.nextstack.repo.VolumeSnapServiceBiz;
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
import javax.validation.constraints.Pattern;

import static com.lnjoying.justice.commonweb.common.SwaggerConstants.*;
import static com.lnjoying.justice.commonweb.util.ServiceCombRequestUtils.isAdmin;

@RestSchema(schemaId = "volumeSnap")
@Api(value = "VolumeSnap Controller", tags = {"VolumeSnap Controller"})
@RequestMapping("/cmp/v1/{vendor}/clouds/{cloud_id}/api/repo/v1")
@ApiOperation(value = "", extensions = {@Extension(name = SWAGGER_X_RESOURCE,
        properties = {@ExtensionProperty(name = SWAGGER_RESOURCE_SINGULAR_NAME, value = "ns_volume_snap"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_PLURAL_NAME, value = "ns_volume_snaps"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_DESCRIPTION, value = "云盘快照"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_MODEL, value = "TblCmpVolumeSnap")})})
public class VolumeSnapController extends RestWebController
{
    private static Logger LOGGER = LogManager.getLogger();

    private static final String REG_UUID = "[0-9a-f]{32}";

    @Autowired
    private VolumeSnapServiceBiz volumeSnapServiceBiz;

    @Autowired
    private CloudService cloudService;

    @GetMapping(value = "/volume_snaps", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get volumeSnaps", response =  Object.class)
    public Object getVolumeSnaps(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                 @ApiParam(name = "name") @RequestParam(required = false, value = "name") String volumeSnapName,
                                 @ApiParam(name = "volume_id") @RequestParam(required = false,value = "volume_id") String volumeId,
                                 @ApiParam(name = "page_size") @RequestParam(required = false, value = "page_size")  Integer pageSize,
                                 @ApiParam(name = "page_num") @RequestParam(required = false, value = "page_num")  Integer pageNum,
                                 @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try {
            LOGGER.debug("get volumeSnap list");

            String filterUserId = userId;
            if (isAdmin() || cloudService.isOwner(cloudId, userId))
            {
                filterUserId = null;
            }
            else
            {
                cloudService.checkCloudStatus(cloudId, true);
            }

            VolumeSnapSearchCritical pageSearchCritical = new VolumeSnapSearchCritical();
            pageSearchCritical.setVolumeSnapName(volumeSnapName);
            pageSearchCritical.setVolumeId(volumeId);

            if (pageNum != null) pageSearchCritical.setPageNum(pageNum);
            if (pageSize != null) pageSearchCritical.setPageSize(pageSize);

            return volumeSnapServiceBiz.getVolumeSnaps(cloudId, pageSearchCritical, filterUserId);
        }
        catch (Exception e)
        {
            LOGGER.error("get volumeSnap list error: ",e);
            throw throwWebException(e);
        }
    }

    @GetMapping(value = "/volume_snaps/{volume_snap_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get volumeSnap detail info", response =  Object.class)
    public Object getVolumeSnap(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                @ApiParam(value = "volume_snap_id", required = true, name = "volume_snap_id") @PathVariable("volume_snap_id") @Pattern(regexp = REG_UUID) String volumeSnapId,
                                @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {
            LOGGER.info("get volumeSnap detail info, volumeSnapId: {}", volumeSnapId);

            String operUserId = userId;
            if (isAdmin() || cloudService.isOwner(cloudId, userId))
            {
                operUserId = null;
            }
            else
            {
                cloudService.checkCloudStatus(cloudId, true);
            }

            return volumeSnapServiceBiz.getVolumeSnap(cloudId, volumeSnapId, operUserId);
        }
        catch (Exception e)
        {
            LOGGER.error("get volumeSnap detail info error: ",e);
            throw throwWebException(e);

        }
    }

    @PostMapping(value = "/volume_snaps", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "create new volumeSnap",response =  Object.class)
    @LNJoyAuditLog(value = "create new volumeSnap",
            resourceMapperId = CmpResources.NS_VOLUME_SNAP,
            actionDescriptionTemplate = CmpNextStackActionDescriptionTemplate.Descriptions.ADD_VOLUME_SNAP,
            associatedResourceCnName = "云盘快照",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.METHOD_RETURN_VALUE,
                    resourceIdParseSPEL = "#obj?.body?.get('volumeSnapId')?.toString()", resourcePrimaryKeyClass = TblCmpVolumeSnapKey.class,
                    convertToResourcePrimaryKeyTypeSpEl = "new com.lnjoying.justice.cmp.db.model.TblCmpVolumeSnapKey(#RESOURCE_POOL_ID,#RESOURCE_INSTANCE_ID)"))
    public ResponseEntity<Object> createVolumeSnap(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                   @ApiParam(value = "volumeSnapCreateReq", required = true, name = "volumeSnapCreateReq")@RequestBody @Valid VolumeSnapCreateReq volumeSnapCreateReq,
                                   @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
                                   @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {
            LOGGER.info("create volumeSnap info: {}",volumeSnapCreateReq);

            if (! isAdmin() && ! cloudService.isOwner(cloudId, userId))
            {
                cloudService.checkCloudStatus(cloudId, false);
            }

            return volumeSnapServiceBiz.createVolumeSnap(cloudId, volumeSnapCreateReq, bpId, userId);
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e){
            LOGGER.error("create volumeSnap info error: ",e);
            throw throwWebException(e);
        }
    }

    @DeleteMapping(value = "/volume_snaps/{volume_snap_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "remove volumeSnap",response =  Object.class)
    @LNJoyAuditLog(value = "remove volumeSnap",
            resourceMapperId = CmpResources.NS_VOLUME_SNAP,
            actionDescriptionTemplate = CmpNextStackActionDescriptionTemplate.Descriptions.DELETE_VOLUME_SNAP,
            associatedResourceCnName = "云盘快照",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                    bindParameterName = "volumeSnapId", resourcePrimaryKeyClass = TblCmpVolumeSnapKey.class,
                    convertToResourcePrimaryKeyTypeSpEl = "new com.lnjoying.justice.cmp.db.model.TblCmpVolumeSnapKey(#RESOURCE_POOL_ID,#RESOURCE_INSTANCE_ID)"))
    public Object removeVolumeSnap(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                   @ApiParam(value = "volume_snap_id", required = true, name = "volume_snap_id")@PathVariable("volume_snap_id") @Pattern(regexp = REG_UUID) String volumeSnapId,
                                   @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {
            LOGGER.info("remove volumeSnap, volumeSnapId: {}", volumeSnapId);

            String operUserId = userId;
            if (isAdmin() || cloudService.isOwner(cloudId, userId))
            {
                operUserId = null;
            }
            else
            {
                cloudService.checkCloudStatus(cloudId, false);
            }

            return volumeSnapServiceBiz.removeVolumeSnap(cloudId, volumeSnapId, operUserId);
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("remove volumeSnap failed, volumeSnapId: {}", volumeSnapId, e);
            throw throwWebException(e);
        }
    }

    @PutMapping(value = "/volume_snaps/{volume_snap_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "update volumeSnap", response =  Object.class)
    @LNJoyAuditLog(value = "update volumeSnap",
            resourceMapperId = CmpResources.NS_VOLUME_SNAP,
            actionDescriptionTemplate = CmpNextStackActionDescriptionTemplate.Descriptions.UPDATE_VOLUME_SNAP,
            associatedResourceCnName = "云盘快照",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                    bindParameterName = "volumeSnapId", resourcePrimaryKeyClass = TblCmpVolumeSnapKey.class,
                    convertToResourcePrimaryKeyTypeSpEl = "new com.lnjoying.justice.cmp.db.model.TblCmpVolumeSnapKey(#RESOURCE_POOL_ID,#RESOURCE_INSTANCE_ID)"))
    public Object updateVolume(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                               @ApiParam(value = "volume_snap_id", required = true, name = "volume_snap_id") @PathVariable("volume_snap_id")String volumeSnapId,
                               @ApiParam(value = "commonReq", required = true, name = "commonReq") @RequestBody @Valid CommonReq request,
                               @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {
            LOGGER.info("update volume: volumeSnapId:{} name:{}", volumeSnapId, request.getName());

            if (! isAdmin() && ! cloudService.isOwner(cloudId, userId))
            {
                cloudService.checkCloudStatus(cloudId, false);
                volumeSnapServiceBiz.checkVolumeSnapUser(cloudId, volumeSnapId, userId);
            }

            return cloudService.sendHttpRequestToCloud(cloudId);
        }
        catch (Exception e)
        {
            LOGGER.error("update volumeSnap failed: ", e);
            throw throwWebException(e);
        }
    }

    @PutMapping(value = "/volume_snaps/{volume_snap_id}/switch", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "switch volumeSnap", response =  Object.class)
    @LNJoyAuditLog(value = "switch volumeSnap",
            resourceMapperId = CmpResources.NS_VOLUME_SNAP,
            actionDescriptionTemplate = CmpNextStackActionDescriptionTemplate.Descriptions.SWITCH_VOLUME_SNAP,
            associatedResourceCnName = "云盘快照",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                    bindParameterName = "volumeSnapId", resourcePrimaryKeyClass = TblCmpVolumeSnapKey.class,
                    convertToResourcePrimaryKeyTypeSpEl = "new com.lnjoying.justice.cmp.db.model.TblCmpVolumeSnapKey(#RESOURCE_POOL_ID,#RESOURCE_INSTANCE_ID)"))
    public ResponseEntity<Object> switchVolume(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                               @ApiParam(value = "volume_snap_id", required = true, name = "volume_snap_id") @PathVariable("volume_snap_id")String volumeSnapId,
                                               @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {
            LOGGER.info("switch volume: volumeSnapId:{} ",volumeSnapId);

            if (! isAdmin() && ! cloudService.isOwner(cloudId, userId))
            {
                cloudService.checkCloudStatus(cloudId, false);
                volumeSnapServiceBiz.checkVolumeSnapUser(cloudId, volumeSnapId, userId);
            }

            return cloudService.sendHttpRequestToCloud(cloudId);
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("switch  volumeSnap failed: ", e);
            throw throwWebException(e);
        }
    }
}
