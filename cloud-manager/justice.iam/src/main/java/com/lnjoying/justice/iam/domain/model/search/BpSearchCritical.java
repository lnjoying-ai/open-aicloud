package com.lnjoying.justice.iam.domain.model.search;

import com.lnjoying.justice.schema.entity.search.PageSearchCritical;
import lombok.Data;

@Data
public class BpSearchCritical extends PageSearchCritical
{
	String name;

	String bpId;
}
