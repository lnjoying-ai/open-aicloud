package com.lnjoying.justice.cmp.service.openstack;

import com.lnjoying.justice.cmp.domain.dto.request.openstack.cinder.OSVolumeCreateReq;
import com.lnjoying.justice.cmp.domain.dto.request.openstack.cinder.OSVolumeUpdateReq;
import com.lnjoying.justice.cmp.domain.dto.response.openstack.cinder.*;
import com.lnjoying.justice.cmp.entity.search.openstack.OSVolumeSearchCritical;
import org.springframework.http.ResponseEntity;

public interface VolumeService
{
    OSVolumesWithDetailsRsp getVolumesWithDetails(String cloudId, OSVolumeSearchCritical volumeSearchCritical, String userId);

    ResponseEntity addVolume(String cloudId, OSVolumeCreateReq addFlavorReq, String bpId, String userId);

    OSVolumesRsp getVolumes(String cloudId, OSVolumeSearchCritical volumeSearchCritical, String userId);

    OSVolumeWithDetailsRsp getVolumeDetails(String cloudId, String volumeId, String userId);

    ResponseEntity updateVolume(String cloudId, String volumeId, OSVolumeUpdateReq request, String bpId, String userId);

    ResponseEntity removeVolume(String cloudId, String volumeId, String userId);

    ResponseEntity actionVolume(String cloudId, String volumeId, Object request, String bpId, String userId);

    OSExtVolumesWithDetailsRsp getVolumesWithDetailsPage(String cloudId, OSVolumeSearchCritical volumeSearchCritical, String userId);

    OSExtVolumeWithDetailsRsp getExtVolumeDetails(String cloudId, String volumeId, String userId);
}
