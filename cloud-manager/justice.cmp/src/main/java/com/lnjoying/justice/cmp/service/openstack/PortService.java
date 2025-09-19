package com.lnjoying.justice.cmp.service.openstack;

import com.lnjoying.justice.cmp.domain.dto.request.openstack.neutron.OSPortCreateReq;
import com.lnjoying.justice.cmp.domain.dto.request.openstack.neutron.OSPortUpdateReq;
import com.lnjoying.justice.cmp.domain.dto.response.openstack.neutron.OSExtPortWithDetailsRsp;
import com.lnjoying.justice.cmp.domain.dto.response.openstack.neutron.OSExtPortsWithDetailsRsp;
import com.lnjoying.justice.cmp.domain.dto.response.openstack.neutron.OSPortWithDetailsRsp;
import com.lnjoying.justice.cmp.domain.dto.response.openstack.neutron.OSPortsWithDetailsRsp;
import com.lnjoying.justice.cmp.entity.search.openstack.OSPortSearchCritical;
import org.springframework.http.ResponseEntity;

public interface PortService
{
    OSPortWithDetailsRsp getPortDetails(String cloudId, String portId, String fields, String userId);

    ResponseEntity<Object> updatePort(String cloudId, String portId, OSPortUpdateReq portUpdateReq, String bpId, String userId);

    ResponseEntity<Object> removePort(String cloudId, String portId, String userId);

    OSPortsWithDetailsRsp getPorts(String cloudId, OSPortSearchCritical portSearchCritical, String userId);

    ResponseEntity<Object> addPort(String cloudId, OSPortCreateReq addPortReq, String bpId, String userId);

    OSExtPortsWithDetailsRsp getPortsPage(String cloudId, OSPortSearchCritical portSearchCritical, String userId);

    OSExtPortWithDetailsRsp getExtPortDetails(String cloudId, String portId, String fields, String userId);
}
