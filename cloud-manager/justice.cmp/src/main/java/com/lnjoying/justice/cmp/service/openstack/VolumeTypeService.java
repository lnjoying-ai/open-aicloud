package com.lnjoying.justice.cmp.service.openstack;

import com.lnjoying.justice.cmp.domain.dto.response.openstack.cinder.OSExtVolumeTypesWithDetailsRsp;
import com.lnjoying.justice.cmp.domain.dto.response.openstack.cinder.OSVolumeTypesWithDetailsRsp;
import com.lnjoying.justice.cmp.entity.search.openstack.OSVolumeTypeSearchCritical;

public interface VolumeTypeService
{
    OSVolumeTypesWithDetailsRsp getVolumeTypesWithDetails(String cloudId, OSVolumeTypeSearchCritical volumeTypeSearchCritical, String userId);

    OSExtVolumeTypesWithDetailsRsp getVolumeTypesWithDetailsPage(String cloudId, OSVolumeTypeSearchCritical volumeTypeSearchCritical, String userId);
}
