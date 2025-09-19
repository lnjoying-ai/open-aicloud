package com.lnjoying.justice.servicemanager.entity.search;

import com.lnjoying.justice.schema.entity.search.PageSearchCritical;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceGatewaySearchCritical extends PageSearchCritical
{
	private String serviceGatewayId;
	private String endpoint;
}
