package com.lnjoying.justice.iam.domain.model.search;

import com.lnjoying.justice.schema.entity.search.PageSearchCritical;
import lombok.Data;

@Data
public class UserSearchCritical extends PageSearchCritical
{
	String name;
	String userId;
	String bpId;
	String queryBpId;
}
