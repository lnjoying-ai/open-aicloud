package com.lnjoying.justice.ecrm.domain.dto.model;

import lombok.Data;

@Data
public class NationInfo
{
	String       nation;
	String      cn_name;
	String      en_name;
	float min_longitude;
	float max_longitude;
	float  min_latitude;
	float  max_latitude;
}
