package com.lnjoying.justice.cmp.service.openstack;

import com.lnjoying.justice.cmp.domain.dto.request.openstack.cinder.OSVolumeSnapshotCreateReq;
import com.lnjoying.justice.cmp.domain.dto.request.openstack.cinder.OSVolumeSnapshotUpdateReq;
import com.lnjoying.justice.cmp.domain.dto.response.openstack.cinder.*;
import com.lnjoying.justice.cmp.entity.search.openstack.OSVolumeSnapshotSearchCritical;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface VolumeSnapshotService
{
    OSVolumeSnapshotsWithDetailsRsp getSnapshotsWithDetails(String cloudId, OSVolumeSnapshotSearchCritical volumeSnapshotSearchCritical, String userId);

    ResponseEntity addSnapshot(String cloudId, OSVolumeSnapshotCreateReq addVolumeSnapshotReq, String bpId, String userId);

    OSVolumeSnapshotsRsp getSnapshots(String cloudId, OSVolumeSnapshotSearchCritical volumeSnapshotSearchCritical, String userId);

    OSVolumeSnapshotMetadata getSnapshotMetadata(String cloudId, String snapshotId, String userId);

    ResponseEntity addSnapshotMetadata(String cloudId, String snapshotId, Map<String, String> metadata, String bpId, String userId);

    ResponseEntity updateSnapshotMetadata(String cloudId, String snapshotId, Map<String, String> metadata, String bpId, String userId);

    OSVolumeSnapshotWithDetailsRsp getSnapshotDetails(String cloudId, String snapshotId, String userId);

    ResponseEntity updateSnapshot(String cloudId, String snapshotId, OSVolumeSnapshotUpdateReq request, String bpId, String userId);

    ResponseEntity removeSnapshot(String cloudId, String snapshotId, String userId);

    OSExtVolumeSnapshotsWithDetailsRsp getSnapshotsWithDetailsPage(String cloudId, OSVolumeSnapshotSearchCritical volumeSnapshotSearchCritical, String userId);

    OSExtVolumeSnapshotWithDetailsRsp getExtSnapshotDetails(String cloudId, String snapshotId, String userId);
}
