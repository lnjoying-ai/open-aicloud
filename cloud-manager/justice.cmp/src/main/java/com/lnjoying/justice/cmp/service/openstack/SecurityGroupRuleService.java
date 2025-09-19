package com.lnjoying.justice.cmp.service.openstack;

import com.lnjoying.justice.cmp.domain.dto.request.openstack.neutron.OSSecurityGroupRuleCreateReq;
import com.lnjoying.justice.cmp.domain.dto.response.openstack.neutron.OSExtSecurityGroupRulesWithDetailsRsp;
import com.lnjoying.justice.cmp.domain.dto.response.openstack.neutron.OSSecurityGroupRuleWithDetailsRsp;
import com.lnjoying.justice.cmp.domain.dto.response.openstack.neutron.OSSecurityGroupRulesWithDetailsRsp;
import com.lnjoying.justice.cmp.entity.search.openstack.OSSecurityGroupRuleSearchCritical;
import org.springframework.http.ResponseEntity;

public interface SecurityGroupRuleService
{
    OSSecurityGroupRulesWithDetailsRsp getSecurityGroupRules(String cloudId, OSSecurityGroupRuleSearchCritical securityGroupRuleSearchCritical, String userId);

    ResponseEntity<Object> addSecurityGroupRule(String cloudId, OSSecurityGroupRuleCreateReq securityGroupRuleCreateReq, String bpId, String userId);

    OSSecurityGroupRuleWithDetailsRsp getSecurityGroupRuleDetails(String cloudId, String securityGroupRuleId, String fields, String userId);

    ResponseEntity<Object> removeSecurityGroupRule(String cloudId, String securityGroupRuleId, String userId);

    OSExtSecurityGroupRulesWithDetailsRsp getSecurityGroupRulesPage(String cloudId, OSSecurityGroupRuleSearchCritical securityGroupRuleSearchCritical, String userId);
}
