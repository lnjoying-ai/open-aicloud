package com.lnjoying.justice.cmp.service.easystack;

import com.lnjoying.justice.cmp.domain.dto.response.easystack.neturon.ESExtRouterWithDetailsRsp;
import com.lnjoying.justice.cmp.domain.dto.response.easystack.neturon.ESExtRoutersWithDetailsRsp;
import com.lnjoying.justice.cmp.entity.search.easystack.ESRouterSearchCritical;

public interface ESRouterService
{
    ESExtRoutersWithDetailsRsp getEsRoutersPage(String cloudId, ESRouterSearchCritical routerSearchCritical, String userId);

    ESExtRouterWithDetailsRsp getEsExtRouterDetails(String cloudId, String routerId, String fields, String userId);
}
