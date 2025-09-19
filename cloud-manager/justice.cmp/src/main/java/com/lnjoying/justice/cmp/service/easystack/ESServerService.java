package com.lnjoying.justice.cmp.service.easystack;

import com.lnjoying.justice.cmp.domain.dto.response.openstack.nova.*;
import com.lnjoying.justice.cmp.entity.search.openstack.OSServerSearchCritical;
import org.springframework.http.ResponseEntity;

public interface ESServerService
{
    OSExtServersWithDetailsRsp getServersDetailedPage(String cloudId, OSServerSearchCritical serverSearchCritical, String userId);

    ResponseEntity<Object> removeServer(String cloudId, String serverId, String userId);
}
