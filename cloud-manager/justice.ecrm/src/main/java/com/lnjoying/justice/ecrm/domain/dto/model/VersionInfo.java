package com.lnjoying.justice.ecrm.domain.dto.model;

import lombok.Data;

import java.util.Map;

@Data
public class VersionInfo
{
	String version;
	String image_name;
	Map<String, String> image_hash;
}
