package com.lnjoying.justice.aos.common;

import com.lnjoying.justice.schema.entity.search.PageSearchCritical;
import lombok.Data;

@Data
public class TemplateSearchCritical extends PageSearchCritical
{
	String rootId;
	String name;
	String bpId;
	String userId;
	int startRow;
	String labels;

	/**
	 * The public can be queried, but the public cannot be modified
	 */
	boolean containsPublic = false;
}
