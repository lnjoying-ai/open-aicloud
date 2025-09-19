package com.lnjoying.justice.cmp.controller.openstack.blockstorage;

import com.lnjoying.justice.cmp.domain.dto.response.openstack.cinder.OSExtBackupWithDetailsRsp;
import com.lnjoying.justice.cmp.domain.dto.response.openstack.cinder.OSExtVolumeBackupsWithDetailsRsp;
import com.lnjoying.justice.cmp.entity.search.openstack.OSVolumeBackupSearchCritical;
import com.lnjoying.justice.cmp.service.cloud.CloudService;
import com.lnjoying.justice.cmp.service.openstack.BackupService;
import com.lnjoying.justice.commonweb.controller.RestWebController;
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

@RestSchema(schemaId = "osextensionvolumebackup")
@RequestMapping("/cmp/v1/{vendor}/clouds/{cloud_id}/extension/v3")
@Api(value = "OpenStack Volume BackUp Controller",tags = "OpenStack Volume BackUp Controller")
@ApiOperation(value = "", extensions = {@Extension(name = SWAGGER_X_RESOURCE,
        properties = {@ExtensionProperty(name = SWAGGER_RESOURCE_SINGULAR_NAME, value = "os_ext_volume_backup"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_PLURAL_NAME, value = "os_ext_volume_backups"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_DESCRIPTION, value = "OpenStack卷备份"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_MODEL, value = "TblCmpOsBackups")})})
public class OSExtVolumeBackUpController extends RestWebController
{
    private static Logger LOGGER = LogManager.getLogger();

    @Autowired
    private BackupService backupService;

    @Autowired
    private CloudService cloudService;

    @GetMapping(value = "/{project_id}/backups/detail", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "List backups with detail", response = Object.class)
    public ResponseEntity<OSExtVolumeBackupsWithDetailsRsp> getBackupsWithDetails(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                                                  @ApiParam(value = "", required = true, name = "project_id") @PathVariable(value = "project_id") String projectId,
                                                                                  @ApiParam(name = "all_tenants") @RequestParam(required = false, value = "all_tenants") String allTenants,
                                                                                  @ApiParam(name = "sort") @RequestParam(required = false, value = "sort") String sort,
                                                                                  @ApiParam(name = "limit") @RequestParam(required = false, value = "limit") Integer limit,
                                                                                  @ApiParam(name = "offset") @RequestParam(required = false, value = "offset") Integer offset,
                                                                                  @ApiParam(name = "marker") @RequestParam(required = false, value = "marker") String marker,
                                                                                  @ApiParam(name = "with_count") @RequestParam(required = false, value = "with_count") Boolean withCount,
                                                                                  @ApiParam(name = "volume_id") @RequestParam(required = false, value = "volume_id") String volumeId,
                                                                                  @ApiParam(name = "page_size") @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                                                  @ApiParam(name = "page_num") @RequestParam(value = "pageNum", required = false) Integer pageNum,
                                                                                  @ApiParam(name = "name") @RequestParam(required = false, value = "name") String name,
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
                    sort, limit, offset, marker, withCount, volumeId, pageNum, pageSize, name);

            OSExtVolumeBackupsWithDetailsRsp backupsWithDetails = backupService.getBackupsWithDetailsPage(cloudId, osVolumeBackupSearchCritical, filterUserId);

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
    public ResponseEntity<OSExtBackupWithDetailsRsp> getBackupDetails(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
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

            OSExtBackupWithDetailsRsp backupWithDetails = backupService.getExtBackupDetails(cloudId, backupId, operUserId);

            return ResponseEntity.ok(backupWithDetails);
        }
        catch (Exception e)
        {
            LOGGER.error("get backup details failed: {}", e.getMessage());
            e.printStackTrace();
            throw throwWebException(e);
        }
    }
}
