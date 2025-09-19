package com.lnjoying.justice.ecrm.domain.model.search;

import com.lnjoying.justice.schema.entity.search.PageSearchCritical;
import lombok.Data;

@Data
public class ServiceAgentSearchCritical extends PageSearchCritical
{
	private String regionId;
	private String siteId;
	private String nodeId;
	private String agentId;
}
