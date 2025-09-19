package com.lnjoying.justice.cis.common.constant;

import com.lnjoying.justice.schema.entity.search.PageSearchCritical;
import lombok.Data;

@Data
public class ContainerSearchCritical extends PageSearchCritical
{
	String    site;
	Integer status;
	String  userId;
	String  region;
	String  nodeId;
	String  specId;
	String    name;
}
