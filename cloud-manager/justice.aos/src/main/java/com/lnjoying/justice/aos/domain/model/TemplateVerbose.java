package com.lnjoying.justice.aos.domain.model;

import lombok.Data;

import java.util.List;

@Data
public class TemplateVerbose
{
	String name;
	String version;
	int num;
	String logo_url;
	String vendor;
	int used_num;
	List<TemplateInfo> versions;

	private String userId;

	private String bpId;

	private String userName;

	private String bpName;

	private String id;
}
