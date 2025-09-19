package com.lnjoying.justice.aos.common;

import com.lnjoying.justice.schema.entity.search.PageSearchCritical;
import lombok.Data;

@Data
public class StackSearchCritical extends PageSearchCritical
{
	String      name;
	Integer   status;
	String    userId;
	String      bpId;
	String  regionId;
	String    siteId;
	String    nodeId;
	String    specId;
	String    stackType;
}
