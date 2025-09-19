package com.lnjoying.justice.ecrm.domain.dto.model;

import lombok.Data;

@Data
public class Location
{
	String         nation;
	String        address;
	String         adcode;
	float       longitude;
	float		 latitude;
	float       elevation;
	String detail_address;
}
