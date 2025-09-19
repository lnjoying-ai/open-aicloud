package com.lnjoying.justice.cmp.controller.openstack.blockstorage;

import com.lnjoying.justice.cmp.common.CmpResources;
import com.lnjoying.justice.cmp.db.model.TblCmpOsBackupsKey;
import com.lnjoying.justice.cmp.domain.dto.request.openstack.cinder.*;
import com.lnjoying.justice.cmp.domain.dto.response.openstack.cinder.*;
import com.lnjoying.justice.cmp.entity.search.openstack.OSVolumeBackupSearchCritical;
import com.lnjoying.justice.cmp.handler.actiondescription.i18n.zh_cn.CmpOpenstackActionDescriptionTemplate;
import com.lnjoying.justice.cmp.service.cloud.CloudService;
import com.lnjoying.justice.cmp.service.openstack.BackupService;
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

@RestSchema(schemaId = "osvolumebackup")
@RequestMapping("/cmp/v1/{vendor}/clouds/{cloud_id}/v3")
@Api(value = "OpenStack Volume BackUp Controller",tags = "OpenStack Volume BackUp Controller")
@ApiOperation(value = "", extensions = {@Extension(name = SWAGGER_X_RESOURCE,
        properties = {@ExtensionProperty(name = SWAGGER_RESOURCE_SINGULAR_NAME, value = "os_volume_backup"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_PLURAL_NAME, value = "os_volume_backups"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_DESCRIPTION, value = "OpenStack卷备份"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_MODEL, value = "TblCmpOsBackups")})})
public class OSVolumeBackUpController extends RestWebController
{
    private static Logger LOGGER = LogManager.getLogger();

    @Autowired
    private BackupService backupService;

    @Autowired
    private CloudService cloudService;

    @GetMapping(value = "/{project_id}/backups/detail", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "List backups with detail", response = Object.class)
    public ResponseEntity<OSVolumeBackupsWithDetailsRsp> getBackupsWithDetails(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                                               @ApiParam(value = "", required = true, name = "project_id") @PathVariable(value = "project_id") String projectId,
                                                                               @ApiParam(name = "all_tenants") @RequestParam(required = false, value = "all_tenants") String allTenants,
                                                                               @ApiParam(name = "sort") @RequestParam(required = false, value = "sort") String sort,
                                                                               @ApiParam(name = "limit") @RequestParam(required = false, value = "limit") Integer limit,
                                                                               @ApiParam(name = "offset") @RequestParam(required = false, value = "offset") Integer offset,
                                                                               @ApiParam(name = "marker") @RequestParam(required = false, value = "marker") String marker,
                                                                               @ApiParam(name = "with_count") @RequestParam(required = false, value = "with_count") Boolean withCount,
                                                                               @ApiParam(name = "volume_id") @RequestParam(required = false, value = "volume_id") String volumeId,
                                                                               @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {
            LOGGER.info("get backups. cloud:{}", cloudId);

            String filterUserId = userId;
            if (isAdmin() || cloudService.isOwner(cloudId, userId))
            {
                filterUserId = null;
            }
            else
            {
                cloudService.checkCloudStatus(cloudId, true);
            }

            OSVolumeBackupSearchCritical osVolumeBackupSearchCritical = new OSVolumeBackupSearchCritical(projectId, allTenants,
                    sort, limit, offset, marker, withCount, volumeId, null, null, null);

            OSVolumeBackupsWithDetailsRsp backupsWithDetails = backupService.getBackupsWithDetails(cloudId, osVolumeBackupSearchCritical, filterUserId);

            return ResponseEntity.ok(backupsWithDetails);
        }
        catch (Exception e)
        {
            LOGGER.error("get backups failed: {}", e.getMessage());
            e.printStackTrace();
            throw throwWebException(e);
        }
    }

    @GetMapping(value = "/{project_id}/backups/{backup_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Show backup detail", response = Object.class)
    public ResponseEntity<OSBackupWithDetailsRsp> getBackupDetails(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                                   @ApiParam(value = "", required = true, name = "project_id") @PathVariable(value = "project_id") String projectId,
                                                                   @ApiParam(value = "backup_id", required = true, name = "backup_id") @PathVariable("backup_id") String backupId,
                                                                   @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {
            LOGGER.info("get backup details. cloud:{}, backup:{}", cloudId, backupId);

            String operUserId = userId;
            if (isAdmin() || cloudService.isOwner(cloudId, userId))
            {
                operUserId = null;
            }
            else
            {
                cloudService.checkCloudStatus(cloudId, true);
            }

            OSBackupWithDetailsRsp backupWithDetails = backupService.getBackupDetails(cloudId, backupId, operUserId);

            return ResponseEntity.ok(backupWithDetails);
        }
        catch (Exception e)
        {
            LOGGER.error("get backup details failed: {}", e.getMessage());
            e.printStackTrace();
            throw throwWebException(e);
        }
    }

    @DeleteMapping(value = "/{project_id}/backups/{backup_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Delete a backup", response = Object.class)
    @LNJoyAuditLog(value = "Delete a backup",
            resourceMapperId = CmpResources.OS_VOLUME_BACKUP,
            actionDescriptionTemplate = CmpOpenstackActionDescriptionTemplate.Descriptions.DELETE_BACKUP,
            associatedResourceCnName = "备份",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                    bindParameterName = "backupId", resourcePrimaryKeyClass = TblCmpOsBackupsKey.class,
                    convertToResourcePrimaryKeyTypeSpEl = "new com.lnjoying.justice.cmp.db.model.TblCmpOsBackupsKey(#RESOURCE_POOL_ID,#RESOURCE_INSTANCE_ID)"))
    public ResponseEntity<Object> removeBackup(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                               @ApiParam(value = "", required = true, name = "project_id") @PathVariable(value = "project_id") String projectId,
                                               @ApiParam(value = "backup_id", required = true, name = "backup_id") @PathVariable("backup_id")String backupId,
                                               @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {
            LOGGER.info("remove backup, cloud:{}, backup_id:{}", cloudId, backupId);

            String operUserId = userId;
            if (isAdmin() || cloudService.isOwner(cloudId, userId))
            {
                operUserId = null;
            }
            else
            {
                cloudService.checkCloudStatus(cloudId, false);
            }

            return backupService.removeBackup(cloudId, backupId, operUserId);
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("remove backup failed: {}", e.getMessage());
            e.printStackTrace();
            throw throwWebException(e);
        }
    }

    @PostMapping(value = "/{project_id}/backups/{backup_id}/restore", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Restore a backup", response = Object.class)
    @LNJoyAuditLog(value = "Restore a backup",
            resourceMapperId = CmpResources.OS_VOLUME_BACKUP,
            actionDescriptionTemplate = CmpOpenstackActionDescriptionTemplate.Descriptions.RESTORE_BACKUP,
            associatedResourceCnName = "备份",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                    bindParameterName = "backupId", resourcePrimaryKeyClass = TblCmpOsBackupsKey.class,
                    convertToResourcePrimaryKeyTypeSpEl = "new com.lnjoying.justice.cmp.db.model.TblCmpOsBackupsKey(#RESOURCE_POOL_ID,#RESOURCE_INSTANCE_ID)")
    )
    public ResponseEntity<Object> restoreBackup(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                @ApiParam(value = "", required = true, name = "backup_id") @PathVariable(value = "backup_id") String backupId,
                                                @ApiParam(value = "restore", required = true, name = "restore") @RequestBody OSBackupRestoreReq request,
                                                @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
                                                @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {
            LOGGER.info("add backup, cloud:{}, request:{}", cloudId, request);

            String operUserId = userId;
            if (isAdmin() || cloudService.isOwner(cloudId, userId))
            {
                operUserId = null;
            }
            else
            {
                cloudService.checkCloudStatus(cloudId, false);
            }

            return backupService.restoreBackup(cloudId, backupId, request, bpId, operUserId);
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("add backup failed: {}", e.getMessage());
            e.printStackTrace();
            throw throwWebException(e);
        }
    }

    @PostMapping(value = "/{project_id}/backups", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Create a backup", response = Object.class)
    @LNJoyAuditLog(value = "Create a backup",
            resourceMapperId = CmpResources.OS_VOLUME_BACKUP,
            actionDescriptionTemplate = CmpOpenstackActionDescriptionTemplate.Descriptions.CREATE_BACKUP,
            associatedResourceCnName = "备份",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.METHOD_RETURN_VALUE,
                    resourceIdParseSPEL = "#obj?.body.get('backup')?.get('id')?.toString()", resourcePrimaryKeyClass = TblCmpOsBackupsKey.class,
                    convertToResourcePrimaryKeyTypeSpEl = "new com.lnjoying.justice.cmp.db.model.TblCmpOsBackupsKey(#RESOURCE_POOL_ID,#RESOURCE_INSTANCE_ID)"))
    public ResponseEntity<Object> addBackup(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                            @ApiParam(value = "", required = true, name = "project_id") @PathVariable(value = "project_id") String projectId,
                                            @ApiParam(value = "backup", required = true, name = "backup") @RequestBody OSBackupCreateReq request,
                                            @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
                                            @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {
            LOGGER.info("add backup, cloud:{}, request:{}", cloudId, request);

            if (! isAdmin() && ! cloudService.isOwner(cloudId, userId))
            {
                cloudService.checkCloudStatus(cloudId, false);
            }

            return backupService.addBackup(cloudId, request, bpId, userId);
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("add backup failed: {}", e.getMessage());
            e.printStackTrace();
            throw throwWebException(e);
        }
    }

    @PutMapping(value = "/{project_id}/backups/{backup_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Update a backup", response = Object.class)
    @LNJoyAuditLog(value = "Update a backup",
            resourceMapperId = CmpResources.OS_VOLUME_BACKUP,
            actionDescriptionTemplate = CmpOpenstackActionDescriptionTemplate.Descriptions.UPDATE_BACKUP,
            associatedResourceCnName = "卷备份",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                    bindParameterName = "backupId", resourcePrimaryKeyClass = TblCmpOsBackupsKey.class,
                    convertToResourcePrimaryKeyTypeSpEl = "new com.lnjoying.justice.cmp.db.model.TblCmpOsBackupsKey(#RESOURCE_POOL_ID,#RESOURCE_INSTANCE_ID)"))
    public ResponseEntity<Object> updateBackup(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                               @ApiParam(value = "", required = true, name = "project_id") @PathVariable(value = "project_id") String projectId,
                                               @ApiParam(value = "", required = true, name = "backup_id") @PathVariable(value = "backup_id") String backupId,
                                               @ApiParam(value = "backup", required = true, name = "backup") @RequestBody OSBackupUpdateReq request,
                                               @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
                                               @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {
            LOGGER.info("update backup, cloud:{}, request:{}", cloudId, request);

            String operUserId = userId;
            if (isAdmin() || cloudService.isOwner(cloudId, userId))
            {
                operUserId = null;
            }
            else
            {
                cloudService.checkCloudStatus(cloudId, false);
            }

            return backupService.updateBackup(cloudId, backupId, request, bpId, operUserId);
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("update backup failed: {}", e.getMessage());
            e.printStackTrace();
            throw throwWebException(e);
        }
    }

    @GetMapping(value = "/{project_id}/backups", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "List backups for project", response = Object.class)
    public ResponseEntity<OSBackupsRsp> getBackups(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                   @ApiParam(value = "", required = true, name = "project_id") @PathVariable(value = "project_id") String projectId,
                                                   @ApiParam(name = "all_tenants") @RequestParam(required = false, value = "all_tenants") String allTenants,
                                                   @ApiParam(name = "sort") @RequestParam(required = false, value = "sort") String sort,
                                                   @ApiParam(name = "limit") @RequestParam(required = false, value = "limit") Integer limit,
                                                   @ApiParam(name = "offset") @RequestParam(required = false, value = "offset") Integer offset,
                                                   @ApiParam(name = "marker") @RequestParam(required = false, value = "marker") String marker,
                                                   @ApiParam(name = "with_count") @RequestParam(required = false, value = "with_count") Boolean withCount,
                                                   @ApiParam(name = "volume_id") @RequestParam(required = false, value = "volume_id") String volumeId,
                                                   @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {
            LOGGER.info("get backups. cloud:{}", cloudId);

            String filterUserId = userId;
            if (isAdmin() || cloudService.isOwner(cloudId, userId))
            {
                filterUserId = null;
            }
            else
            {
                cloudService.checkCloudStatus(cloudId, true);
            }

            OSVolumeBackupSearchCritical osVolumeBackupSearchCritical = new OSVolumeBackupSearchCritical(projectId, allTenants,
                    sort, limit, offset, marker, withCount, volumeId, null, null, null);

            OSBackupsRsp backupsRsp = backupService.getBackups(cloudId, osVolumeBackupSearchCritical, filterUserId);

            return ResponseEntity.ok(backupsRsp);
        }
        catch (Exception e)
        {
            LOGGER.error("get backups failed: {}", e.getMessage());
            e.printStackTrace();
            throw throwWebException(e);
        }
    }
}
