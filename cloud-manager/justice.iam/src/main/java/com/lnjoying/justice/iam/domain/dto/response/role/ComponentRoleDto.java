package com.lnjoying.justice.iam.domain.dto.response.role;

import lombok.Data;

import java.util.List;

@Data
public class ComponentRoleDto
{
	String platform;
	List<String> roles;
}
