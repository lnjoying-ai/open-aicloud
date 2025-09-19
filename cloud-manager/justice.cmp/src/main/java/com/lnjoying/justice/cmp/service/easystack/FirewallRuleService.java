package com.lnjoying.justice.cmp.service.easystack;

import com.lnjoying.justice.cmp.domain.dto.request.easystack.neutron.ESFirewallRuleCreateReq;
import com.lnjoying.justice.cmp.domain.dto.request.easystack.neutron.ESFirewallRuleUpdateReq;
import com.lnjoying.justice.cmp.domain.dto.response.easystack.neturon.ESExtFirewallRulesRsp;
import com.lnjoying.justice.cmp.domain.dto.response.easystack.neturon.ESFirewallRuleRsp;
import com.lnjoying.justice.cmp.domain.dto.response.easystack.neturon.ESFirewallRulesRsp;
import com.lnjoying.justice.cmp.entity.search.easystack.ESFirewallRuleSearchCritical;
import org.springframework.http.ResponseEntity;

public interface FirewallRuleService
{
    ESFirewallRulesRsp getFirewallRules(String cloudId, ESFirewallRuleSearchCritical firewallRuleSearchCritical, String userId);

    ResponseEntity addFirewallRule(String cloudId, ESFirewallRuleCreateReq firewallRuleCreateReq, String bpId, String userId);

    ESFirewallRuleRsp getFirewallRule(String cloudId, String firewallRuleId, String userId);

    ResponseEntity updateFirewallRule(String cloudId, String firewallRuleId, ESFirewallRuleUpdateReq request, String userId);

    ResponseEntity removeFirewallRule(String cloudId, String firewallRuleId, String userId);

    ESExtFirewallRulesRsp getFirewallRulesPage(String cloudId, ESFirewallRuleSearchCritical firewallRuleSearchCritical, String userId);
}
