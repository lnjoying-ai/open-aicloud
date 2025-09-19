package com.lnjoying.justice.iam.domain.dto.request.bp;

import lombok.Data;

@Data
public class BpRawReq
{
	int	status = 0;
	String name;
	String description;
	String website;
	String license_id;
	String master_user;
	BpContactsInfo contact_info;
}
