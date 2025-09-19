package com.lnjoying.justice.aos.domain.dto.req;

import lombok.Data;

import java.util.List;

@Data
public class AddTemplateReq
{
	String name;
	String version;
	String description;
	String logo_url;
	String vendor;
	String aos_type;
	String content_type;
	String stack_compose;
	String justice_compose;
	List<String> labels;
}
