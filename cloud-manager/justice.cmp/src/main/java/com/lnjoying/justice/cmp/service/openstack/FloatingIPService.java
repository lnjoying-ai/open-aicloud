package com.lnjoying.justice.cmp.service.openstack;

import com.lnjoying.justice.cmp.domain.dto.request.openstack.neutron.OSFloatingIPCreateReq;
import com.lnjoying.justice.cmp.domain.dto.request.openstack.neutron.OSFloatingIPUpdateReq;
import com.lnjoying.justice.cmp.domain.dto.request.openstack.neutron.OSPortForwardingCreateReq;
import com.lnjoying.justice.cmp.domain.dto.request.openstack.neutron.OSPortForwardingUpdateReq;
import com.lnjoying.justice.cmp.domain.dto.response.openstack.neutron.*;
import com.lnjoying.justice.cmp.entity.search.openstack.OSFloatingIPSearchCritical;
import com.lnjoying.justice.cmp.entity.search.openstack.OSPortForwardingSearchCritical;
import org.springframework.http.ResponseEntity;

public interface FloatingIPService
{
    OSFloatingIpsWithDetailsRsp getFloatingIPs(String cloudId, OSFloatingIPSearchCritical floatingIPSearchCritical, String userId);

    ResponseEntity<Object> addFloatingIP(String cloudId, OSFloatingIPCreateReq addFloatingIPReq, String bpId, String userId);

    OSFloatingIpWithDetailsRsp getFloatingIPDetails(String cloudId, String floatingipId, String userId);

    ResponseEntity<Object> updateFloatingIP(String cloudId, String floatingIpId, OSFloatingIPUpdateReq floatingIPUpdateReq, String bpId, String userId);

    ResponseEntity<Object> removeFloatingIP(String cloudId, String floatingipId, String userId);

    OSPortForwardingWithDetailsRsp getPortForwarding(String cloudId, String floatingIpId, String portForwardingId, String fields, String userId);

    ResponseEntity<Object> updatePortForwarding(String cloudId, String floatingipId, String portForwardingId, OSPortForwardingUpdateReq request, String userId);

    ResponseEntity<Object> removePortForwarding(String cloudId, String floatingipId, String portForwardingId, String userId);

    OSPortForwardingsWithDetailsRsp getPortForwardings(String cloudId, OSPortForwardingSearchCritical portForwardingSearchCritical, String userId);

    ResponseEntity<Object> addPortForwarding(String cloudId, String floatingipId, OSPortForwardingCreateReq addPortForwardingReq, String bpId, String userId);

    OSExtFloatingIpsWithDetailsRsp getFloatingIPsPage(String cloudId, OSFloatingIPSearchCritical floatingIPSearchCritical, String userId);

    OSExtFloatingIpWithDetailsRsp getExtFloatingIPDetails(String cloudId, String floatingipId, String userId);
}
