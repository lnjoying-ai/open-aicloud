package com.lnjoying.justice.cmp.controller.openstack.compute;

import com.lnjoying.justice.cmp.common.CmpResources;
import com.lnjoying.justice.cmp.db.model.TblCmpOsInstancesKey;
import com.lnjoying.justice.cmp.domain.dto.request.openstack.nova.OSExtActionServerReq;
import com.lnjoying.justice.cmp.domain.dto.response.openstack.glance.OSExtImagesWithDetailsRsp;
import com.lnjoying.justice.cmp.domain.dto.response.openstack.nova.*;
import com.lnjoying.justice.cmp.entity.search.openstack.OSServerSearchCritical;
import com.lnjoying.justice.cmp.handler.actiondescription.i18n.zh_cn.CmpOpenstackActionDescriptionTemplate;
import com.lnjoying.justice.cmp.service.cloud.CloudService;
import com.lnjoying.justice.cmp.service.openstack.ServerService;
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

import static com.lnjoying.justice.commonweb.common.SwaggerConstants.*;
import static com.lnjoying.justice.commonweb.util.ServiceCombRequestUtils.isAdmin;

@RestSchema(schemaId = "osextensionserver")
@RequestMapping("/cmp/v1/{vendor}/clouds/{cloud_id}/extension/v2.1/servers")
@Api(value = "OpenStack Server Controller",tags = "OpenStack Server Controller")
@ApiOperation(value = "", extensions = {@Extension(name = SWAGGER_X_RESOURCE,
        properties = {@ExtensionProperty(name = SWAGGER_RESOURCE_SINGULAR_NAME, value = "os_ext_server"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_PLURAL_NAME, value = "os_ext_servers"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_DESCRIPTION, value = "OpenStack云主机"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_MODEL, value = "TblCmpOsInstances")})})
public class OSExtServerController extends RestWebController
{
    private static Logger LOGGER = LogManager.getLogger();

    @Autowired
    private ServerService serverService;

    @Autowired
    private CloudService cloudService;

    @GetMapping(value = "/detail", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "List Servers Detailed", response = Object.class)
    public ResponseEntity<OSExtServersWithDetailsRsp> getServersDetailed(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                                      @ApiParam(name = "access_ip_v4") @RequestParam(required = false, value = "access_ip_v4") String accessIpV4,
                                                                      @ApiParam(name = "access_ip_v6") @RequestParam(required = false, value = "access_ip_v6") String accessIpV6,
                                                                      @ApiParam(name = "all_tenants") @RequestParam(required = false, value = "all_tenants") Boolean allTenants,
                                                                      @ApiParam(name = "auto_disk_config") @RequestParam(required = false, value = "auto_disk_config") String autoDiskConfig,
                                                                      @ApiParam(name = "availability_zone") @RequestParam(required = false, value = "availability_zone") String availabilityZone,
                                                                      @ApiParam(name = "changes-since") @RequestParam(required = false, value = "changes-since") String changesSince,
                                                                      @ApiParam(name = "config_drive") @RequestParam(required = false, value = "config_drive") String configDrive,
                                                                      @ApiParam(name = "created_at") @RequestParam(required = false, value = "created_at") String createdAt,
                                                                      @ApiParam(name = "deleted") @RequestParam(required = false, value = "deleted") Boolean deleted,
                                                                      @ApiParam(name = "description") @RequestParam(required = false, value = "description") String description,
                                                                      @ApiParam(name = "flavor") @RequestParam(required = false, value = "flavor") String flavor,
                                                                      @ApiParam(name = "host") @RequestParam(required = false, value = "host") String host,
                                                                      @ApiParam(name = "hostname") @RequestParam(required = false, value = "hostname") String hostname,
                                                                      @ApiParam(name = "image") @RequestParam(required = false, value = "image") String image,
                                                                      @ApiParam(name = "ip") @RequestParam(required = false, value = "ip") String ip,
                                                                      @ApiParam(name = "ip6") @RequestParam(required = false, value = "ip6") String ip6,
                                                                      @ApiParam(name = "kernel_id") @RequestParam(required = false, value = "kernel_id") String kernelId,
                                                                      @ApiParam(name = "key_name") @RequestParam(required = false, value = "key_name") String keyName,
                                                                      @ApiParam(name = "launch_index") @RequestParam(required = false, value = "launch_index") Integer launchIndex,
                                                                      @ApiParam(name = "launched_at") @RequestParam(required = false, value = "launched_at") String launchedAt,
                                                                      @ApiParam(name = "limit") @RequestParam(required = false, value = "limit") Integer limit,
                                                                      @ApiParam(name = "locked_by") @RequestParam(required = false, value = "locked_by") String lockedBy,
                                                                      @ApiParam(name = "marker") @RequestParam(required = false, value = "marker") String marker,
                                                                      @ApiParam(name = "name") @RequestParam(required = false, value = "name") String name,
                                                                      @ApiParam(name = "node") @RequestParam(required = false, value = "node") String node,
                                                                      @ApiParam(name = "power_state") @RequestParam(required = false, value = "power_state") Integer powerState,
                                                                      @ApiParam(name = "progress") @RequestParam(required = false, value = "progress") Integer progress,
                                                                      @ApiParam(name = "project_id") @RequestParam(required = false, value = "project_id") String projectId,
                                                                      @ApiParam(name = "ramdisk_id") @RequestParam(required = false, value = "ramdisk_id") String ramdiskId,
                                                                      @ApiParam(name = "reservation_id") @RequestParam(required = false, value = "reservation_id") String reservationId,
                                                                      @ApiParam(name = "root_device_name") @RequestParam(required = false, value = "root_device_name") String rootDeviceName,
                                                                      @ApiParam(name = "soft_deleted") @RequestParam(required = false, value = "soft_deleted") Boolean softDeleted,
                                                                      @ApiParam(name = "sort_dir") @RequestParam(required = false, value = "sort_dir") String sortDir,
                                                                      @ApiParam(name = "sort_key") @RequestParam(required = false, value = "sort_key") String sortKey,
                                                                      @ApiParam(name = "status") @RequestParam(required = false, value = "status") String status,
                                                                      @ApiParam(name = "task_state") @RequestParam(required = false, value = "task_state") String taskState,
                                                                      @ApiParam(name = "terminated_at") @RequestParam(required = false, value = "terminated_at") String terminatedAt,
                                                                      @ApiParam(name = "user_id") @RequestParam(required = false, value = "user_id") String userId,
                                                                      @ApiParam(name = "uuid") @RequestParam(required = false, value = "uuid") String uuid,
                                                                      @ApiParam(name = "vm_state") @RequestParam(required = false, value = "vm_state") String vmState,
                                                                      @ApiParam(name = "not-tags") @RequestParam(required = false, value = "not-tags") String notTags,
                                                                      @ApiParam(name = "not-tags-any") @RequestParam(required = false, value = "not-tags-any") String notTagsAny,
                                                                      @ApiParam(name = "tags") @RequestParam(required = false, value = "tags") String tags,
                                                                      @ApiParam(name = "tags-any") @RequestParam(required = false, value = "tags-any") String tagsAny,
                                                                      @ApiParam(name = "changes-before") @RequestParam(required = false, value = "changes-before") String changesBefore,
                                                                      @ApiParam(name = "locked") @RequestParam(required = false, value = "locked") Boolean locked,
                                                                      @ApiParam(name = "page_size") @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                                      @ApiParam(name = "page_num") @RequestParam(value = "pageNum", required = false) Integer pageNum,
                                                                      @RequestHeader(name = UserHeadInfo.USERID, required = false) String operUserId)
    {
        try
        {
            LOGGER.info("get servers detailed. cloud:{}", cloudId);

            String filterUserId = operUserId;
            if (isAdmin() || cloudService.isOwner(cloudId, operUserId))
            {
                filterUserId = null;
            }
            else
            {
                cloudService.checkCloudStatus(cloudId, true);
            }

            OSServerSearchCritical serverSearchCritical = new OSServerSearchCritical(accessIpV4, accessIpV6, allTenants,
                    autoDiskConfig, availabilityZone, changesSince, configDrive, createdAt, deleted, description, flavor,
                    host, hostname, image, ip, ip6, kernelId, keyName, launchIndex, launchedAt, limit, lockedBy, marker,
                    name, node, powerState, progress, projectId, ramdiskId, reservationId, rootDeviceName, softDeleted,
                    sortDir, sortKey, status, taskState, terminatedAt, userId, uuid, vmState, notTags, notTagsAny, tags,
                    tagsAny, changesBefore, locked, pageNum, pageSize);

            OSExtServersWithDetailsRsp serversRsp = serverService.getServersDetailedPage(cloudId, serverSearchCritical, filterUserId);

            return ResponseEntity.ok(serversRsp);
        }
        catch (Exception e)
        {
            LOGGER.error("get servers detailed failed: {}", e.getMessage());
            e.printStackTrace();
            throw throwWebException(e);
        }
    }

    @GetMapping(value = "/{server_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Show Server Details", response = Object.class)
    public ResponseEntity<OSExtServerWithDetailsRsp> getServerDetails(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                                   @ApiParam(value = "server_id", required = true, name = "server_id") @PathVariable("server_id") String serverId,
                                                                      @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {
            LOGGER.info("get server details. cloud:{}, server_id:{}", cloudId, serverId);

            String operUserId = userId;
            if (isAdmin() || cloudService.isOwner(cloudId, userId))
            {
                operUserId = null;
            }
            else
            {
                cloudService.checkCloudStatus(cloudId, true);
            }

            OSExtServerWithDetailsRsp serverWithDetails = serverService.getExtServerDetails(cloudId, serverId, operUserId);

            return ResponseEntity.ok(serverWithDetails);
        }
        catch (Exception e)
        {
            LOGGER.error("get server details failed: {}", e.getMessage());
            e.printStackTrace();
            throw throwWebException(e);
        }
    }

    @GetMapping(value = "/{server_id}/os-volume_attachments", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "List volume attachments for an instance", response = Object.class)
    public ResponseEntity<OSExtServerVolumeAttachmentsRsp> getServerVolumeAttachments(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                                                   @ApiParam(value = "server_id", required = true, name = "server_id") @PathVariable("server_id")String serverId,
                                                                                   @ApiParam(name = "limit") @RequestParam(required = false, value = "limit") Integer limit,
                                                                                   @ApiParam(name = "offset") @RequestParam(required = false, value = "offset") Integer offset,
                                                                                   @ApiParam(name = "page_size") @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                                                   @ApiParam(name = "page_num") @RequestParam(value = "pageNum", required = false) Integer pageNum,
                                                                                   @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {
            LOGGER.info("get server volume attachments. cloud:{}", cloudId);

            String operUserId = userId;
            if (isAdmin() || cloudService.isOwner(cloudId, userId))
            {
                operUserId = null;
            }
            else
            {
                cloudService.checkCloudStatus(cloudId, true);
            }

            OSExtServerVolumeAttachmentsRsp serverVolumeAttachmentsRsp = serverService.getServerVolumeAttachmentsPage(cloudId, serverId, limit, offset, operUserId, pageSize, pageNum);

            return ResponseEntity.ok(serverVolumeAttachmentsRsp);
        }
        catch (Exception e)
        {
            LOGGER.error("get server volume attachments failed: {}", e.getMessage());
            e.printStackTrace();
            throw throwWebException(e);
        }
    }

    @PostMapping(value = "/{server_id}/action", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Ext Action Server", response = Object.class)
    @LNJoyAuditLog(value = "Ext Action Server",
            resourceMapperId = CmpResources.OS_SERVER,
            actionDescriptionTemplate = CmpOpenstackActionDescriptionTemplate.Descriptions.EXT_ACTION_INSTANCE,
            associatedResourceCnName = "云主机",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                            bindParameterName = "serverId", resourcePrimaryKeyClass = TblCmpOsInstancesKey.class,
                    convertToResourcePrimaryKeyTypeSpEl = "new com.lnjoying.justice.cmp.db.model.TblCmpOsInstancesKey(#RESOURCE_POOL_ID,#RESOURCE_INSTANCE_ID)"),
            actionDescriptionField = "request",
            actionDescriptionValueSpEl = "#obj?.confirmResize == true ? 'confirmResize' : #obj?.revertResize == true ? 'revertResize' : 'unknown'",
            actionDescriptionFormatString = "OSExtServer action: %s")
    public ResponseEntity<Object> actionServer(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                               @ApiParam(value = "server_id", required = true, name = "server_id") @PathVariable("server_id")String serverId,
                                               @ApiParam(value = "action", required = true, name = "action") @RequestBody OSExtActionServerReq request,
                                               @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
                                               @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {
            LOGGER.info("action server, cloud:{}, server_id:{}", cloudId, serverId);

            String operUserId = userId;
            if (isAdmin() || cloudService.isOwner(cloudId, userId))
            {
                operUserId = null;
            }
            else
            {
                cloudService.checkCloudStatus(cloudId, false);
            }

            return serverService.extEctionServer(cloudId, serverId, request, bpId, operUserId);
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("action server failed: {}", e.getMessage());
            e.printStackTrace();
            throw throwWebException(e);
        }
    }

    @GetMapping(value = "/{server_id}/os-interface", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "List Port Interfaces", response = Object.class)
    public ResponseEntity<OSExtServerOsInterfacesRsp> getServerOsInterfaces(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                                              @ApiParam(value = "server_id", required = true, name = "server_id") @PathVariable("server_id")String serverId,
                                                                              @ApiParam(name = "page_size") @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                                              @ApiParam(name = "page_num") @RequestParam(value = "pageNum", required = false) Integer pageNum,
                                                                              @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {
            LOGGER.info("get server os interfaces. cloud:{}", cloudId);

            String operUserId = userId;
            if (isAdmin() || cloudService.isOwner(cloudId, userId))
            {
                operUserId = null;
            }
            else
            {
                cloudService.checkCloudStatus(cloudId, true);
            }

            OSExtServerOsInterfacesRsp serverOsInterfacesRsp = serverService.getServerOsInterfacesPage(cloudId, serverId, operUserId, pageSize, pageNum);

            return ResponseEntity.ok(serverOsInterfacesRsp);
        }
        catch (Exception e)
        {
            LOGGER.error("get server os interfaces failed: {}", e.getMessage());
            e.printStackTrace();
            throw throwWebException(e);
        }
    }

    @GetMapping(value = "/{server_id}/images", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get Rebuild Server images", response = Object.class)
    public ResponseEntity<OSExtImagesWithDetailsRsp> getRebuildServerImages(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                                            @ApiParam(value = "server_id", required = true, name = "server_id") @PathVariable("server_id")String serverId,
                                                                            @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {
            LOGGER.info("get server os interfaces. cloud:{}", cloudId);

            String operUserId = userId;
            if (isAdmin() || cloudService.isOwner(cloudId, userId))
            {
                operUserId = null;
            }
            else
            {
                cloudService.checkCloudStatus(cloudId, true);
            }

            OSExtImagesWithDetailsRsp imagesWithDetailsRsp = serverService.getRebuildServerImages(cloudId, serverId, operUserId);

            return ResponseEntity.ok(imagesWithDetailsRsp);
        }
        catch (Exception e)
        {
            LOGGER.error("get server os interfaces failed: {}", e.getMessage());
            e.printStackTrace();
            throw throwWebException(e);
        }
    }
}
