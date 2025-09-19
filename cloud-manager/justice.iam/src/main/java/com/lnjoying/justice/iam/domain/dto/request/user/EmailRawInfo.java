package com.lnjoying.justice.iam.domain.dto.request.user;

import com.lnjoying.justice.commonweb.handler.aspect.annotation.SensitiveField;
import com.lnjoying.justice.commonweb.handler.aspect.enums.SensitiveType;
import lombok.Data;

@Data
public class EmailRawInfo
{
	@SensitiveField(SensitiveType.EMAIL)
	String email;

	@SensitiveField(SensitiveType.PASSWORD)
	String password;
	String verification_code;
}
