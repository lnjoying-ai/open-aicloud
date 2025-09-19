package com.lnjoying.justice.cmp.service.openstack;

import com.lnjoying.justice.cmp.domain.dto.request.openstack.neutron.OSSecurityGroupCreateReq;
import com.lnjoying.justice.cmp.domain.dto.request.openstack.neutron.OSSecurityGroupUpdateReq;
import com.lnjoying.justice.cmp.domain.dto.response.openstack.neutron.OSExtSecurityGroupsWithDetailsRsp;
import com.lnjoying.justice.cmp.domain.dto.response.openstack.neutron.OSSecurityGroupWithDetailsRsp;
import com.lnjoying.justice.cmp.domain.dto.response.openstack.neutron.OSSecurityGroupsWithDetailsRsp;
import com.lnjoying.justice.cmp.entity.search.openstack.OSSecurityGroupSearchCritical;
import org.springframework.http.ResponseEntity;

public interface SecurityGroupService
{
    OSSecurityGroupsWithDetailsRsp getSecurityGroups(String cloudId, OSSecurityGroupSearchCritical securityGroupSearchCritical, String userId);

    ResponseEntity<Object> addSecurityGroup(String cloudId, OSSecurityGroupCreateReq securityGroupCreateReq, String bpId, String userId);

    OSSecurityGroupWithDetailsRsp getSecurityGroupDetails(String cloudId, String securityGroupId, String fields, String userId);

    ResponseEntity<Object> updateSecurityGroup(String cloudId, String securityGroupId, OSSecurityGroupUpdateReq securityGroupUpdateReq, String userId);

    ResponseEntity<Object> removeSecurityGroup(String cloudId, String securityGroupId, String userId);

    OSExtSecurityGroupsWithDetailsRsp getSecurityGroupsPage(String cloudId, OSSecurityGroupSearchCritical securityGroupSearchCritical, String userId);
}
