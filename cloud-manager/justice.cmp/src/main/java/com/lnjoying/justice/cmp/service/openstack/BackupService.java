package com.lnjoying.justice.cmp.service.openstack;

import com.lnjoying.justice.cmp.domain.dto.request.openstack.cinder.*;
import com.lnjoying.justice.cmp.domain.dto.response.openstack.cinder.*;
import com.lnjoying.justice.cmp.entity.search.openstack.OSVolumeBackupSearchCritical;
import org.springframework.http.ResponseEntity;

public interface BackupService
{
    OSVolumeBackupsWithDetailsRsp getBackupsWithDetails(String cloudId, OSVolumeBackupSearchCritical osVolumeBackupSearchCritical, String userId);

    OSBackupWithDetailsRsp getBackupDetails(String cloudId, String backupId, String userId);

    ResponseEntity removeBackup(String cloudId, String backupId, String userId);

    ResponseEntity restoreBackup(String cloudId, String backupId, OSBackupRestoreReq backupRestoreReq, String bpId, String userId);

    ResponseEntity addBackup(String cloudId, OSBackupCreateReq backupCreateReq, String bpId, String userId);

    ResponseEntity updateBackup(String cloudId, String backupId, OSBackupUpdateReq request, String bpId, String userId);

    OSBackupsRsp getBackups(String cloudId, OSVolumeBackupSearchCritical osVolumeBackupSearchCritical, String userId);

    OSExtVolumeBackupsWithDetailsRsp getBackupsWithDetailsPage(String cloudId, OSVolumeBackupSearchCritical osVolumeBackupSearchCritical, String userId);

    OSExtBackupWithDetailsRsp getExtBackupDetails(String cloudId, String backupId, String userId);
}
