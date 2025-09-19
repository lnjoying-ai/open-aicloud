package com.lnjoying.justice.cmp.service.openstack;

import com.lnjoying.justice.cmp.domain.dto.request.openstack.neutron.OSSubnetCreateReq;
import com.lnjoying.justice.cmp.domain.dto.request.openstack.neutron.OSSubnetUpdateReq;
import com.lnjoying.justice.cmp.domain.dto.response.openstack.neutron.OSExtSubnetsWithDetailsRsp;
import com.lnjoying.justice.cmp.domain.dto.response.openstack.neutron.OSSubnetWithDetailsRsp;
import com.lnjoying.justice.cmp.domain.dto.response.openstack.neutron.OSSubnetsWithDetailsRsp;
import com.lnjoying.justice.cmp.entity.search.openstack.OSSubnetSearchCritical;
import org.springframework.http.ResponseEntity;

public interface SubnetService
{
    OSSubnetsWithDetailsRsp getSubnets(String cloudId, OSSubnetSearchCritical subnetSearchCritical, String userId);

    ResponseEntity<Object> addSubnet(String cloudId, OSSubnetCreateReq addSubnetReq, String bpId, String userId);

    OSSubnetWithDetailsRsp getSubnetDetails(String cloudId, String subnetId, String userId);

    ResponseEntity<Object> updateSubnet(String cloudId, String subnetId, OSSubnetUpdateReq subnetUpdate, String bpId, String userId);

    ResponseEntity<Object> removeSubnet(String cloudId, String subnetId, String userId);

    OSExtSubnetsWithDetailsRsp getSubnetsPage(String cloudId, OSSubnetSearchCritical subnetSearchCritical, String userId);
}
