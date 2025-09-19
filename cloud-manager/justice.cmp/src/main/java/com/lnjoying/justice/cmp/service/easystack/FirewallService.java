package com.lnjoying.justice.cmp.service.easystack;

import com.lnjoying.justice.cmp.domain.dto.request.easystack.neutron.ESFirewallCreateReq;
import com.lnjoying.justice.cmp.domain.dto.request.easystack.neutron.ESFirewallUpdateReq;
import com.lnjoying.justice.cmp.domain.dto.response.easystack.neturon.ESExtFirewallsRsp;
import com.lnjoying.justice.cmp.domain.dto.response.easystack.neturon.ESFirewallRsp;
import com.lnjoying.justice.cmp.domain.dto.response.easystack.neturon.ESFirewallsRsp;
import com.lnjoying.justice.cmp.entity.search.easystack.ESFirewallSearchCritical;
import org.springframework.http.ResponseEntity;

public interface FirewallService
{
    ESFirewallsRsp getFirewalls(String cloudId, ESFirewallSearchCritical firewallSearchCritical, String userId);

    ResponseEntity addFirewall(String cloudId, ESFirewallCreateReq firewallCreateReq, String bpId, String userId);

    ESFirewallRsp getFirewall(String cloudId, String firewallId, String userId);

    ResponseEntity updateFirewall(String cloudId, String firewallId, ESFirewallUpdateReq request, String userId);

    ResponseEntity removeFirewall(String cloudId, String firewallId, String userId);

    ESExtFirewallsRsp getFirewallsPage(String cloudId, ESFirewallSearchCritical firewallSearchCritical, String userId);
}
