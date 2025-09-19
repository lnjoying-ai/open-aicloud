package com.lnjoying.justice.cmp.service.easystack;

import com.lnjoying.justice.cmp.domain.dto.response.openstack.neutron.*;
import com.lnjoying.justice.cmp.entity.search.openstack.OSFloatingIPSearchCritical;

public interface ESFloatingIPService
{
    OSExtFloatingIpsWithDetailsRsp getFloatingIPsPage(String cloudId, OSFloatingIPSearchCritical floatingIPSearchCritical, String userId);
}
