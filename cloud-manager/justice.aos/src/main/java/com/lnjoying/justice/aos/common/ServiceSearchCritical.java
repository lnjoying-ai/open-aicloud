package com.lnjoying.justice.aos.common;

import com.lnjoying.justice.schema.entity.search.PageSearchCritical;
import lombok.Data;

@Data
public class ServiceSearchCritical extends PageSearchCritical
{
	String stackName;
	String serviceName;
	String userId;
	Integer status;
	String  regionId;
	String    siteId;
	String    nodeId;
}
