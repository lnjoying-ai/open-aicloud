package com.lnjoying.justice.iam.domain.dto.request.bp;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class BpContactsInfo
{
	String name;
	String email;
	String phone;
	String address;
}
