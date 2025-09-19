package com.lnjoying.justice.cmp.service.openstack;

import com.lnjoying.justice.cmp.domain.dto.request.openstack.neutron.OSNetworkCreateReq;
import com.lnjoying.justice.cmp.domain.dto.request.openstack.neutron.OSNetworkUpdateReq;
import com.lnjoying.justice.cmp.domain.dto.response.openstack.neutron.OSExtNetworksWithDetailsRsp;
import com.lnjoying.justice.cmp.domain.dto.response.openstack.neutron.OSNetworkWithDetailsRsp;
import com.lnjoying.justice.cmp.domain.dto.response.openstack.neutron.OSNetworksWithDetailsRsp;
import com.lnjoying.justice.cmp.entity.search.openstack.OSNetworkSearchCritical;
import org.springframework.http.ResponseEntity;

public interface NetworkService
{
    OSNetworkWithDetailsRsp getNetworkDetails(String cloudId, String networkId, String fields, String userId);

    ResponseEntity<Object> updateNetwork(String cloudId, String networkId, OSNetworkUpdateReq osServerUpdateReq, String bpId, String userId);

    ResponseEntity<Object> removeNetwork(String cloudId, String networkId, String userId);

    OSNetworksWithDetailsRsp getNetworks(String cloudId, OSNetworkSearchCritical networkSearchCritical, String userId);

    ResponseEntity<Object> addNetwork(String cloudId, OSNetworkCreateReq addNetworkReq, String bpId, String userId);

    OSExtNetworksWithDetailsRsp getNetworksPage(String cloudId, OSNetworkSearchCritical networkSearchCritical, String userId);
}
