package com.lnjoying.justice.ecrm.domain.model.search;

import com.lnjoying.justice.schema.entity.search.PageSearchCritical;
import lombok.Data;

@Data
public class EdgeSearchCritical extends PageSearchCritical
{
	private String nation;
	private String adcode;
	private String region;
	private String site;
	private Integer activeStatus;
	private Integer onlineStatus;
	private String orchestration;
	private String gpu;
	private String name;
	private String vendor;
	private String uuid;
}
