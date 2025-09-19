package com.lnjoying.justice.cmp.service.openstack;

import com.lnjoying.justice.cmp.domain.dto.request.openstack.neutron.OSExtRouterUpdateReq;
import com.lnjoying.justice.cmp.domain.dto.request.openstack.neutron.OSRouterCreateReq;
import com.lnjoying.justice.cmp.domain.dto.request.openstack.neutron.OSRouterUpdateReq;
import com.lnjoying.justice.cmp.domain.dto.response.openstack.neutron.OSExtRouterWithDetailsRsp;
import com.lnjoying.justice.cmp.domain.dto.response.openstack.neutron.OSExtRoutersWithDetailsRsp;
import com.lnjoying.justice.cmp.domain.dto.response.openstack.neutron.OSRouterWithDetailsRsp;
import com.lnjoying.justice.cmp.domain.dto.response.openstack.neutron.OSRoutersWithDetailsRsp;
import com.lnjoying.justice.cmp.entity.search.openstack.OSRouterSearchCritical;
import org.springframework.http.ResponseEntity;

public interface RouterService
{
    OSRoutersWithDetailsRsp getRouters(String cloudId, OSRouterSearchCritical routerSearchCritical, String userId);

    ResponseEntity<Object> addRouter(String cloudId, OSRouterCreateReq addRouterReq, String bpId, String userId);

    OSRouterWithDetailsRsp getRouterDetails(String cloudId, String routerId, String fields, String userId);

    ResponseEntity<Object> updateRouter(String cloudId, String routerId, OSRouterUpdateReq request, String bpId, String userId);

    ResponseEntity<Object> removeRouter(String cloudId, String routerId, String userId);

    OSExtRoutersWithDetailsRsp getRoutersPage(String cloudId, OSRouterSearchCritical routerSearchCritical, String userId);

    ResponseEntity<Object> updateExtRouter(String cloudId, String routerId, OSExtRouterUpdateReq request, String bpId, String userId);

    OSExtRouterWithDetailsRsp getExtRouterDetails(String cloudId, String routerId, String fields, String userId);

    void checkRouterUser(String cloudId, String routerId, String userId);
}
