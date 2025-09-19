package com.lnjoying.justice.cmp.service.openstack;

import com.lnjoying.justice.cmp.domain.dto.response.openstack.nova.OSFlavorsRsp;
import com.lnjoying.justice.cmp.entity.search.openstack.OSFlavorSearchCritical;

public interface LimitService
{
    OSFlavorsRsp getLimits(String cloudId, OSFlavorSearchCritical flavorSearchCritical, String userId);
}
