package com.lnjoying.justice.cmp.service.openstack;

import com.lnjoying.justice.cmp.domain.dto.request.openstack.nova.OSFlavorCreateReq;

import com.lnjoying.justice.cmp.domain.dto.request.openstack.nova.OSFlavorUpdateReq;
import com.lnjoying.justice.cmp.domain.dto.response.openstack.nova.OSExtFlavorsWithDetailsRsp;
import com.lnjoying.justice.cmp.domain.dto.response.openstack.nova.OSFlavorWithDetailsRsp;
import com.lnjoying.justice.cmp.domain.dto.response.openstack.nova.OSFlavorsRsp;
import com.lnjoying.justice.cmp.domain.dto.response.openstack.nova.OSFlavorsWithDetailsRsp;
import com.lnjoying.justice.cmp.entity.search.openstack.OSFlavorSearchCritical;
import org.springframework.http.ResponseEntity;

public interface FlavorService
{
    OSFlavorsRsp getFlavors(String cloudId, OSFlavorSearchCritical flavorSearchCritical, String userId);

    ResponseEntity<Object> addFlavor(String cloudId, OSFlavorCreateReq addFlavorReq, String bpId, String userId);

    OSFlavorsWithDetailsRsp getFlavorsWithDetails(String cloudId, OSFlavorSearchCritical flavorSearchCritical, String userId);

    OSFlavorWithDetailsRsp getFlavor(String cloudId, String flavorId);

    ResponseEntity<Object> updateFlavor(String cloudId, String flavorId, OSFlavorUpdateReq osFlavorUpdateReq);

    ResponseEntity<Object> removeFlavor(String cloudId, String flavorId, String userId);

    OSExtFlavorsWithDetailsRsp getFlavorsWithDetailsPage(String cloudId, OSFlavorSearchCritical flavorSearchCritical, String userId);
}
