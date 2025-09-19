package com.lnjoying.justice.cmp.service.easystack;

import com.lnjoying.justice.cmp.domain.dto.request.easystack.neutron.ESFirewallPolicyCreateReq;
import com.lnjoying.justice.cmp.domain.dto.request.easystack.neutron.ESFirewallPolicyUpdateReq;
import com.lnjoying.justice.cmp.domain.dto.request.easystack.neutron.ESPolicyAddRuleReq;
import com.lnjoying.justice.cmp.domain.dto.request.easystack.neutron.ESPolicyRemoveRuleReq;
import com.lnjoying.justice.cmp.domain.dto.response.easystack.neturon.ESExtFirewallPoliciesRsp;
import com.lnjoying.justice.cmp.domain.dto.response.easystack.neturon.ESFirewallPoliciesRsp;
import com.lnjoying.justice.cmp.domain.dto.response.easystack.neturon.ESFirewallPolicyRsp;
import com.lnjoying.justice.cmp.entity.search.easystack.ESFirewallPoliciesSearchCritical;
import org.springframework.http.ResponseEntity;

public interface FirewallPolicyService
{
    ESFirewallPoliciesRsp getFirewallPolicies(String cloudId, ESFirewallPoliciesSearchCritical firewallPoliciesSearchCritical, String userId);

    ResponseEntity addFirewallPolicy(String cloudId, ESFirewallPolicyCreateReq firewallPolicyCreateReq, String bpId, String userId);

    ESFirewallPolicyRsp getFirewallPolicy(String cloudId, String firewallPolicyId, String userId);

    ResponseEntity updateFirewallPolicy(String cloudId, String firewallPolicyId, ESFirewallPolicyUpdateReq request, String userId);

    ResponseEntity removeFirewallPolicy(String cloudId, String firewallPolicyId, String userId);

    ResponseEntity addFirewallRuleToPolicy(String cloudId, String firewallPolicyId, ESPolicyAddRuleReq request, String userId);

    ResponseEntity removeFirewallRuleFromPolicy(String cloudId, String firewallPolicyId, ESPolicyRemoveRuleReq request, String userId);

    ESExtFirewallPoliciesRsp getFirewallPoliciesPage(String cloudId, ESFirewallPoliciesSearchCritical firewallPoliciesSearchCritical, String userId);
}
