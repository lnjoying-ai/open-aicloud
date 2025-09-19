package com.lnjoying.justice.cmp.service.openstack;

import com.lnjoying.justice.cmp.domain.dto.response.openstack.neutron.OSSegmentsWithDetailsRsp;
import com.lnjoying.justice.cmp.entity.search.openstack.OSSegmentSearchCritical;

public interface SegmentService
{

    OSSegmentsWithDetailsRsp getSegments(String cloudId, OSSegmentSearchCritical segmentSearchCritical, String userId);
}
