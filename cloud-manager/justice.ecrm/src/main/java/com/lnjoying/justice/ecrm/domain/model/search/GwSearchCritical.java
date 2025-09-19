package com.lnjoying.justice.ecrm.domain.model.search;

import com.lnjoying.justice.schema.entity.search.PageSearchCritical;
import lombok.Data;

@Data
public class GwSearchCritical extends PageSearchCritical
{
	String         region;
	Integer active_status;
	Integer online_status;
}
