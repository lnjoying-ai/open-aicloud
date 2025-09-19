package com.lnjoying.justice.cmp.domain.model;

import com.lnjoying.justice.schema.entity.search.PageSearchCritical;
import lombok.Data;

@Data
public class CloudSearchCritical extends PageSearchCritical
{
	private String name;
	private Integer status;
	private String region;
	private String site;
	private String product;
	private String owner;
	private String bp;
}
